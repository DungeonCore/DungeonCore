package net.l_bulb.dungeoncore.common.event.quest;

import net.l_bulb.dungeoncore.quest.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class DestructionQuestEvent extends QuestEvent {

  public DestructionQuestEvent(Player who, Quest quest) {
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
