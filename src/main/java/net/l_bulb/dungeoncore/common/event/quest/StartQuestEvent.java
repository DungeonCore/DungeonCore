package net.l_bulb.dungeoncore.common.event.quest;

import net.l_bulb.dungeoncore.quest.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class StartQuestEvent extends QuestEvent implements Cancellable {

  public StartQuestEvent(Player who, Quest quest) {
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

  boolean isCancel = false;

  @Override
  public boolean isCancelled() {
    return isCancel;
  }

  @Override
  public void setCancelled(boolean paramBoolean) {
    isCancel = paramBoolean;
  }
}
