package lbn.mob.mob.abstractmob.villager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.AbstractMob;
import lbn.quest.Quest;
import lbn.quest.QuestManager;
import lbn.quest.QuestProcessingStatus;
import lbn.quest.quest.TouchVillagerQuest;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;
import lbn.util.DungeonLogger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public abstract class AbstractVillager extends AbstractMob<LivingEntity>{
	abstract public Location getLocation();

	@Override
	public void onOtherDamage(EntityDamageEvent e) {
		e.setDamage(0);
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		try {
			Villager spawn = (Villager) e.getEntity();
			if (getVillageData() != null) {
				if (getVillageData().isAdult()) {
					spawn.setAdult();
				} else {
					spawn.setBaby();
				}
			} else {
				spawn.setAdult();
			}
			spawn.setCustomName(getName());
			spawn.setCustomNameVisible(true);
			spawn.setRemoveWhenFarAway(false);
			spawn.setMaxHealth(2000.0);
			spawn.setHealth(2000.0);
		} catch (Exception ex) {
			ex.printStackTrace();
			DungeonLogger.error(e.getEntity() + "(" + getName() + ") ocure error when spawn villager!!");
		}
	}

	protected VillagerData getVillageData() {
		return VillagerData.getInstance(getName());
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.VILLAGER;
	}

	public boolean isExecuteOnDamage = true;

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		e.setCancelled(true);
		if (damager.getType() != EntityType.PLAYER) {
			return;
		}
		Player p = (Player) damager;
		PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);
		Collection<Quest> doingQuestList = session.getDoingQuestList();
		for (Quest quest : doingQuestList) {
			//もし終了の村人がこの村人でないなら無視
			if (!getName().equals(quest.getEndVillagerName())) {
				continue;
			}

			//もし処理を全て終わらせていないなら無視
			if (session.getProcessingStatus(quest) != QuestProcessingStatus.PROCESS_END) {
				continue;
			}

			//完了にする
			QuestManager.complateQuest(quest, p);
			isExecuteOnDamage = false;
		}
	}

	abstract protected List<String> getMessage(Player p, LivingEntity mob);

	@Override
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		e.setCancelled(true);

		Player p = e.getPlayer();
		LivingEntity entity = (LivingEntity) e.getRightClicked();
		//動く村人なら消す
//		if (!LivingEntityUtil.isCustomVillager(entity)) {
//			Entity rightClicked = e.getRightClicked();
//			rightClicked.remove();
//			if (p.getGameMode() != GameMode.CREATIVE) {
//				JavaUtil.addBonusGold(p, rightClicked.getLocation());
//			}
//			return;
//		}

		//この村人をタッチするクエストで進行中のクエストがあったら他の会話は開始しない
		boolean isDoingTouchQuest = false;
		Set<TouchVillagerQuest> questForVillager = TouchVillagerQuest.getQuestByTargetVillager(entity);

		//メッセージを取得
		List<String> message = new ArrayList<String>();

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
			VillagerData villageData = getVillageData();
			if (villageData != null) {
				message = Arrays.asList(villageData.getTexts());
			}
		}
		if (!message.isEmpty()) {
			p.sendMessage("");
			for (String string : message) {
				p.sendMessage(ChatColor.GOLD + string);
			}
		}


	}
}

