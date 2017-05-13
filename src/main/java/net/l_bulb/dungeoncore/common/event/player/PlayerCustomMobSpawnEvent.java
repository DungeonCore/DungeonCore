package net.l_bulb.dungeoncore.common.event.player;

import lombok.Getter;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;

@Getter
public class PlayerCustomMobSpawnEvent extends CreatureSpawnEvent {

  String name;

  EntityType type;

  public PlayerCustomMobSpawnEvent(LivingEntity spawnee) {
    super(spawnee, SpawnReason.CUSTOM);
    name = spawnee.getCustomName();
    type = spawnee.getType();
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
