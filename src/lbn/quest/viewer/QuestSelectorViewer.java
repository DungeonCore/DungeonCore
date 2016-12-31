package lbn.quest.viewer;

import java.util.ArrayList;

import lbn.common.menu.MenuSelecor;
import lbn.common.menu.SelectRunnable;
import lbn.mob.mob.abstractmob.villager.QuestVillager;
import lbn.quest.Quest;
import lbn.quest.QuestManager;
import lbn.quest.QuestManager.QuestStartStatus;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuestSelectorViewer {
	public static void openSelector(QuestVillager villager, Player p) {
		//実行可能クエスト
		ArrayList<Quest> canStartQuestList = new ArrayList<Quest>();
		for (String id : villager.getHaveQuest()) {
			//クエストが存在しなければ無視する
			Quest questById = QuestManager.getQuestById(id);
			//Questが実行可能か調べる
			QuestStartStatus startQuestStatus = QuestManager.getStartQuestStatus(questById, p);
			if (startQuestStatus.canStart()) {
				canStartQuestList.add(questById);
			}
		}

		//実行可能クエストがないなら何もしない
		if (canStartQuestList.isEmpty()) {
			return;
		}
		QuestMenuSelector questMenuSelector = new QuestMenuSelector(canStartQuestList);
		questMenuSelector.open(p);
	}
}

class QuestMenuSelectorRunnable implements SelectRunnable{
	static {
		MenuSelecor menuSelecor = new MenuSelecor("quest_confirm");
		menuSelecor.regist();
	}

	@Override
	public void run(Player p, ItemStack item) {
		//クエストアイテムでないとき
		if (!QuestSelectViewIcon.isThisItem(item)) {
			Message.sendMessage(p, ChatColor.RED + "エラーが発生しました。このクエストを開始できません。(1)");
			p.closeInventory();
			return;
		}

		//クエストが存在しないとき
		Quest questByItem = QuestSelectViewIcon.getQuestByItem(item);
		if (questByItem == null) {
			Message.sendMessage(p, ChatColor.RED + "エラーが発生しました。このクエストを開始できません。(2)");
			p.closeInventory();
			return;
		}

		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(ChatColor.AQUA + "[QUEST] " + questByItem.getName());
		arrayList.add(ChatColor.YELLOW + "   " + questByItem.getQuestDetail());
		arrayList.add(ChatColor.BLACK + ItemStackUtil.getLoreForIdLine(questByItem.getId()));

		MenuSelecor menuSelecor = new MenuSelecor("quest_confirm");
		menuSelecor.addMenu(ItemStackUtil.getItem("クエストを受注する", Material.WOOL, (byte) 5, arrayList.toArray(new String[0])), 11, new SelectRunnable() {
			@Override
			public void run(Player p, ItemStack item) {
				String id = ItemStackUtil.getId(item);
				Quest questById = QuestManager.getQuestById(id);
				//クエストを開始する
				QuestStartStatus startQuestStatus = QuestManager.getStartQuestStatus(questById, p);
				if (startQuestStatus.canStart()) {
					QuestManager.startQuest(questById, p, false, startQuestStatus);
				}
				//インベントリを閉める
				p.closeInventory();
			}
		});
		menuSelecor.addMenu(ItemStackUtil.getItem("クエストを受注しない", Material.WOOL, (byte) 14, new String[0]), 11, new SelectRunnable() {
			@Override
			public void run(Player p, ItemStack item) {
			}
		});

		menuSelecor.open(p);
	}
}
