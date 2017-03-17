package lbn.npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import lbn.common.menu.MenuSelectorManager;
import lbn.money.BuyerShopSelector;
import lbn.npc.citizens.TheLowIdTrail;
import lbn.quest.Quest;
import lbn.quest.QuestManager;
import lbn.quest.QuestProcessingStatus;
import lbn.quest.abstractQuest.TakeItemQuest;
import lbn.quest.abstractQuest.TouchVillagerQuest;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;
import lbn.quest.viewer.QuestSelectorViewer;
import lbn.util.QuestUtil;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class VillagerNpc {
	VillagerData data;

	public VillagerNpc(VillagerData data) {
		this.data = data;
	}

	public String getData() {
		return data.getData();
	}

	protected List<String> getMessage(Player p, Entity mob) {
		return Arrays.asList(data.getTexts());
	}

	NPC npc;

//	public NPC getNpc() {
//		return npc;
//	}

	public void setNpc(NPC npc) {
		this.data.location = npc.getStoredLocation();
		this.npc = npc;
		this.data.uuid = npc.getUniqueId();
		//NPCを更新する
		updateNpc();
	}

	/**
	 * 右クリック時の処理
	 * @param e
	 */
	public void onNPCRightClickEvent(NPCRightClickEvent e) {
		Player p = e.getClicker();

		Entity entity = e.getNPC().getEntity();
		Set<TouchVillagerQuest> questForVillager = TouchVillagerQuest.getQuestByTargetVillager(entity);

		//メッセージを取得
		List<String> message = new ArrayList<String>();

		boolean isDoingTouchQuest = false;

		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);
		for (TouchVillagerQuest touchVillagerQuest : questForVillager) {
			//クエスト実行中なら処理を行う
			if (questSession.getProcessingStatus(touchVillagerQuest) == QuestProcessingStatus.PROCESSING) {
				//村人にタッチしたときの処理を実行
				touchVillagerQuest.onTouchVillager(p, entity, questSession);
				//データを記録
				questSession.setQuestData(touchVillagerQuest, 1);
				touchVillagerQuest.onSatisfyComplateCondtion(p);
				//メッセージを格納
				message.addAll(Arrays.asList(touchVillagerQuest.talkOnTouch()));
				isDoingTouchQuest = true;
			}
		}

		//もしクエスト実行中でなければ通常の村人のメッセージを出力する
		if (!isDoingTouchQuest) {
			message = getMessage(p, entity);
		}

		//メッセージを出力する
		if (!message.isEmpty()) {
			QuestUtil.sendMessageByVillager(p, message.toArray(new String[0]));
		}
	}

//	/**
//	 * NPCをスポーンさせる
//	 * @param loc
//	 * @return
//	 */
//	public NPC spawn(Location loc) {
//		NPC npc = CitizensAPI.getNPCRegistry().createNPC(getEntityType(), getName());
//		npc.spawn(loc);
//		if (npc != null) {
//			remove();
//		}
//		this.npc = npc;
//		updateNpc();
//		return npc;
//	}

	/**
	 * デスポーンさせる
	 */
	public void despawn() {
		if (npc == null) {
			return;
		}
		if (npc.isSpawned()) {
			npc.despawn(DespawnReason.PLUGIN);
		}
	}

	/**
	 * NPCを削除する
	 */
	public void remove() {
		despawn();
		if (npc != null) {
			npc.destroy();
		}
		npc = null;
	}

	/**
	 * reload時にも呼ばれる。もしすでに同じNPCが存在した場合、それらを削除する
	 * @param e
	 */
	public void onSpawn(NPCSpawnEvent e) {
	}

	public EntityType getEntityType() {
		return data.getEntityType();
	}

	public void updateNpc() {
		if (npc == null || npc.getEntity() == null) {
			return;
		}

		//IDを挿入
		npc.removeTrait(TheLowIdTrail.class);
		npc.addTrait(TheLowIdTrail.fromId(getId()));

		VillagerData villagerData = getVillagerData();
		if (villagerData == null) {
			return;
		}
		if (villagerData.getEntityType() != npc.getEntity().getType()) {
			npc.setBukkitEntityType(villagerData.getEntityType());
		}
		if (getEntityType() == EntityType.PLAYER && villagerData.getSkin() != null) {
			//skinの適用
			npc.data().setPersistent("player-skin-name", villagerData.getSkin());
		}
		//周りを見る処理
		npc.addTrait(new LookClose());

		if (npc.isSpawned()) {
			npc.despawn(DespawnReason.PENDING_RESPAWN);
			npc.spawn(npc.getStoredLocation());
		}
	}

	/**
	 * 左クリック時の処理
	 * @param e
	 */
	public void onNPCLeftClickEvent(NPCLeftClickEvent e) {
		Player p = e.getClicker();

		//クエスト終了の村人ならここでクエストを終了させる
		boolean existTouchQuest = false;
		PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);
		Collection<Quest> doingQuestList = session.getDoingQuestList();
		for (Quest quest : doingQuestList) {
			//もし終了の村人がこの村人でないなら無視
			if (!getId().equals(quest.getEndVillagerId())) {
				continue;
			}

			//アイテムを持ってくるクエストなら処理を行う
			if (TakeItemQuest.isTakeItemQuest(quest)) {
				((TakeItemQuest)quest).onTouchVillager(p, e.getNPC().getEntity(), session);
				existTouchQuest = true;
			}

			//もし処理を全て終わらせているなら完了にする
			if (session.getProcessingStatus(quest) == QuestProcessingStatus.PROCESS_END) {
				QuestManager.complateQuest(quest, p, false);
				existTouchQuest = true;
			}

		}

		//村人タッチのクエストが存在したらここで終了
		if (existTouchQuest) {
			return;
		}

		if (data.getType() == VillagerType.NORMAL) {
			QuestSelectorViewer.openSelector(this, p);
		} else if (data.getType() == VillagerType.SHOP) {
			BuyerShopSelector.onOpen(p, NpcManager.getId(e.getNPC()));
		} if (data.getType() == VillagerType.BLACKSMITH) {
			MenuSelectorManager.open(p, "blacksmith menu");
		}
	}

	public String getName() {
		return data.getName();
	}

	public void onNPCDamageEvent(NPCDamageEvent e) {
//		e.setCancelled(true);
	}

	public Location getLocation() {
		if (npc == null) {
			return data.getLocation();
		}
		return npc.getStoredLocation();
	}

	public VillagerData getVillagerData() {
		return data;
	}

	public String getId() {
		return data.getId();
	}

}
