package lbn.quest;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lbn.util.Message;

public class QuestInventory {
	public static void openQuestViewer(Player p) {
		QuestViewIcon questViewIcon = new QuestViewIcon(p);

		Inventory view = Bukkit.createInventory(null, 9 * 3, ChatColor.WHITE + "quest_view");
		Set<Quest> doingQuest = QuestManager.getDoingQuest(p);
		//メインクエストを先に表示
		for (Quest quest : doingQuest) {
			if (quest.isMainQuest()) {
				view.addItem(questViewIcon.getItemStack(quest));
			}
		}

		//サブクエストを表示
		for (Quest quest : doingQuest) {
			if (!quest.isMainQuest()) {
				view.addItem(questViewIcon.getItemStack(quest));
			}
		}
		p.openInventory(view);
	}

	/**
	 * クエストを破棄するとき
	 * @param e
	 */
	public static void drop(PlayerDropItemEvent e) {
		ItemStack itemStack = e.getItemDrop().getItemStack();
		if (!QuestViewIcon.isQuestIconItem(itemStack)) {
			return;
		}
		Quest quest = new QuestViewIcon(e.getPlayer()).getQuest(itemStack);
		if (quest == null) {
			Player player = e.getPlayer();
			Message.sendMessage(player, "エラーが発生したためクエストを破棄できませんでした。");
			player.closeInventory();
			return;
		}
		if (quest.canDestory()) {
			QuestManager.removeQuest(quest, e.getPlayer());
			e.getItemDrop().remove();
		} else {
			Player player = e.getPlayer();
			Message.sendMessage(player, "このクエストは破棄できません。");
			e.setCancelled(true);
		}
	}

	public static void onClick(InventoryClickEvent e) {
		if (e.getClickedInventory() == null) {
			return;
		}
		String title = e.getClickedInventory().getTitle();
		String name = e.getView().getTopInventory().getName();
		//開いているのがQuestViewでなければ無視
		if (!name.contains("quest_view")) {
			return;
		}

		//クリックしたのがQuestViewのとき
		if (title.contains("quest_view")) {
			ItemStack itemStack = e.getCurrentItem();
			Quest quest = new QuestViewIcon((Player) e.getView().getPlayer()).getQuest(itemStack);
			if (quest != null && !quest.canDestory()) {
				e.setCancelled(true);
			}
		//クリックしたのがQuestViewでない時はキャンセルする
		} else {
			e.setCancelled(true);
		}
	}

	public static void onDrag(InventoryDragEvent e) {
		if (e.getInventory() == null) {
			return;
		}
		String name = e.getView().getTopInventory().getName();
		if (name.contains("quest_view")) {
			e.setCancelled(true);
			return;
		}
	}
}
