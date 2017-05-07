package lbn.item.itemInterface;

import org.bukkit.event.entity.EntityShootBowEvent;

import lbn.common.projectile.ProjectileInterface;

public interface BowItemable extends CombatItemable, ProjectileInterface {
  public void excuteOnShootBow(EntityShootBowEvent e);
}
