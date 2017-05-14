package net.l_bulb.dungeoncore.common.event.quest;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import net.l_bulb.dungeoncore.quest.Quest;

import lombok.Getter;

public class QuestEvent extends PlayerEvent {
  @Getter
  protected Quest quest;

  public QuestEvent(Player who, Quest quest) {
    super(who);
    this.quest = quest;
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
