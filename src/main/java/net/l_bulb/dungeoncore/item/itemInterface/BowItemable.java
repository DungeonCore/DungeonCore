package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.common.projectile.ProjectileInterface;

import org.bukkit.event.entity.EntityShootBowEvent;

public interface BowItemable extends CombatItemable, ProjectileInterface {
  public void excuteOnShootBow(EntityShootBowEvent e);
}
