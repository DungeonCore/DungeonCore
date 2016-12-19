package main.quest;

import java.util.Set;

import main.common.event.player.PlayerStrengthFinishEvent;
import main.common.event.quest.ComplateQuestEvent;
import main.common.event.quest.DestructionQuestEvent;
import main.common.event.quest.StartQuestEvent;
import main.item.ItemInterface;
import main.item.ItemManager;
import main.mob.AbstractMob;
import main.mob.LastDamageManager;
import main.mob.MobHolder;
import main.quest.quest.KillMobQuest;
import main.quest.quest.PickItemQuest;
import main.quest.quest.StrengthItemQuest;

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

		Set<PickItemQuest> quest = PickItemQuest.getQuest(customItem);
		for (PickItemQuest pickItemQuest : quest) {
			pickItemQuest.onPickUp(e);
		}
	}

	@EventHandler
	public void onStrength(PlayerStrengthFinishEvent e) {
		//今実行中のクエスト中からStrengthItemQuestを探しだす
		Set<Quest> doingQuest = QuestManager.getDoingQuest(e.getPlayer());
		for (Quest quest : doingQuest) {
			if (!StrengthItemQuest.isStrengthItemQuest(quest)) {
				continue;
			}
			((StrengthItemQuest)quest).onStrength(e);
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

		//今実行中のクエスト中からStrengthItemQuestを探しだす
		Set<Quest> doingQuest = QuestManager.getDoingQuest(p);
		for (Quest quest : doingQuest) {
			if (!KillMobQuest.isKillMobQuest(quest)) {
				continue;
			}
			((KillMobQuest)quest).onDeath(e);
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
