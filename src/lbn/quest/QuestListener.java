package lbn.quest;

import java.util.Set;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.common.event.quest.ComplateQuestEvent;
import lbn.common.event.quest.DestructionQuestEvent;
import lbn.common.event.quest.StartQuestEvent;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.mob.AbstractMob;
import lbn.mob.LastDamageManager;
import lbn.mob.MobHolder;
import lbn.quest.quest.KillMobQuest;
import lbn.quest.quest.PickItemQuest;
import lbn.quest.quest.QuestType;
import lbn.quest.quest.StrengthItemQuest;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;

public class QuestListener implements Listener{
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e) {
		Item item = e.getItem();
		if (item == null || !item.isValid()) {
			return;
		}
		ItemStack itemStack = item.getItemStack();
		if (itemStack.getAmount() == 0) {
			return;
		}
		ItemInterface customItem = ItemManager.getCustomItem(itemStack);
		if (customItem == null) {
			return;
		}

		Player player = e.getPlayer();
		PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(player);

		Set<PickItemQuest> quest = PickItemQuest.getQuest(customItem);
		for (PickItemQuest pickItemQuest : quest) {
			pickItemQuest.onPickUp(e, session);
			//終了条件を満たしているなら終了する
			onSatisfyCondition(player, pickItemQuest);
		}
	}

	@EventHandler
	public void onStrength(PlayerStrengthFinishEvent e) {
		Player player = e.getPlayer();
		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(player);
		//今実行中のクエスト中からStrengthItemQuestを探しだす
		Set<Quest> doingQuest = questSession.getDoingQuestListByType(QuestType.STRENGTH_ITEM_QUEST);
		for (Quest quest : doingQuest) {
			((StrengthItemQuest)quest).onStrength(e, questSession);
			//終了条件を満たしているなら終了する
			onSatisfyCondition(player, quest);
		}
	}

	@EventHandler
	public void onKillMob(EntityDeathEvent e) {
		AbstractMob<?> mob = MobHolder.getMob(e);
		//死んだのがnull mobなら何もしない
		if (mob.isNullMob()) {
			return;
		}

		//最後に倒したのがPlayerでないなら何もしない
		Player p = LastDamageManager.getLastDamagePlayer(e.getEntity());
		if (p == null) {
			return;
		}

		//殺したmobがtargetに含まれてないなら何もしない
		if (!KillMobQuest.containsTargetMob(mob)) {
			return;
		}

		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);

		//今実行中のクエスト中からStrengthItemQuestを探しだす
		Set<Quest> doingQuest = questSession.getDoingQuestListByType(QuestType.KILL_MOB_QUEST);
		for (Quest quest : doingQuest) {
			((KillMobQuest)quest).onDeath(e, questSession);
			//終了条件を満たしたなら村人の場所へ帰らせる
			onSatisfyCondition(p, quest);
		}
	}

	public void onSatisfyCondition(Player p, Quest quest) {
		if (quest.canFinish(p)) {
			quest.onSatisfyComplateCondtion(p);
		}
	}

	@EventHandler
	public void onStartQuest(StartQuestEvent e) {
		e.getQuest().onStart(e);
		e.getQuest().playStartSound(e.getPlayer());
	}

	@EventHandler
	public void onComplateQuest(ComplateQuestEvent e) {
		e.getQuest().onComplate(e);
		e.getQuest().playCompleteSound(e.getPlayer());

		//クエストが完了したら次のクエストを開始する
//		QuestGroup questGroup = QuestGroupManager.getQuestGroup(e.getQuest());
//		//もしグループが登録されていなかったら何もしない
//		if (questGroup == null) {
//			return;
//		}
//		Quest next = questGroup.next(e.getPlayer(), e.getQuest());
//		QuestManager.startQuest(next, e.getPlayer(), true);
	}

	@EventHandler
	public void onRemoveQuest(DestructionQuestEvent e) {
		e.getQuest().onDistruction(e);
		e.getQuest().playDistructionSound(e.getPlayer());
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		QuestInventory.drop(e);
	}

	@EventHandler
	public void onClick(InventoryDragEvent e) {
		QuestInventory.onDrag(e);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		QuestInventory.onClick(e);
	}
}
