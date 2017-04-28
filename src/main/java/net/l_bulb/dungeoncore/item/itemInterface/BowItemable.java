package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.event.entity.EntityShootBowEvent;

import net.l_bulb.dungeoncore.common.projectile.ProjectileInterface;

public interface BowItemable extends CombatItemable, ProjectileInterface {
  public void excuteOnShootBow(EntityShootBowEvent e);
}
