package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PlayerCustomMobSpawnEvent extends CreatureSpawnEvent {

  String name;

  EntityType type;

  public PlayerCustomMobSpawnEvent(LivingEntity spawnee) {
    super(spawnee, SpawnReason.CUSTOM);
    name = spawnee.getCustomName();
    type = spawnee.getType();
  }

  public String getName() {
    return name;
  }

  public EntityType getType() {
    return type;
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
