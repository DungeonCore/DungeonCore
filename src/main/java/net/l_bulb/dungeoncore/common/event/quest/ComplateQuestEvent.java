package net.l_bulb.dungeoncore.common.event.quest;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import net.l_bulb.dungeoncore.quest.Quest;

public class ComplateQuestEvent extends QuestEvent implements Cancellable {

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

  boolean cancel = false;

  @Override
  public boolean isCancelled() {
    return cancel;
  }

  @Override
  public void setCancelled(boolean arg0) {
    cancel = arg0;
  }

}
