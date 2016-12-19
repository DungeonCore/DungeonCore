package lbn.common.event.quest;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import lbn.quest.Quest;

public class ComplateQuestEvent extends QuestEvent{

	public ComplateQuestEvent(Player who, Quest quest) {
		super(who, quest);
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
