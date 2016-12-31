package lbn.quest.viewer;

import java.util.ArrayList;

import lbn.common.menu.MenuSelecor;
import lbn.quest.Quest;

import org.bukkit.entity.Player;

public class QuestMenuSelector extends MenuSelecor{
	static {
		new QuestMenuSelector().regist();
	}

	static ArrayList<Quest> questViewItemCache = new ArrayList<Quest>();

	ArrayList<Quest> canStartQuestList;

	public QuestMenuSelector(ArrayList<Quest> canStartQuestList) {
		super("Quest Selector");
		this.canStartQuestList = canStartQuestList;
	}

	private QuestMenuSelector() {
		super("Quest Selector");
	}

	@Override
	public void open(Player p) {
		for (Quest quest : canStartQuestList) {
			createInventory.addItem(QuestSelectViewIcon.getItemStack(quest));
		}
		p.openInventory(createInventory);
	}
}
