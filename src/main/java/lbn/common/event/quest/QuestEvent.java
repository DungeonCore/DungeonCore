package lbn.common.event.quest;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import lbn.quest.Quest;

public class QuestEvent extends PlayerEvent {

  protected Quest quest;

  public QuestEvent(Player who, Quest quest) {
    super(who);
    this.quest = quest;
  }

  public Quest getQuest() {
    return quest;
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
