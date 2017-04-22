package lbn.item.itemInterface;

import lbn.common.projectile.ProjectileInterface;

import org.bukkit.event.entity.EntityShootBowEvent;

public interface BowItemable extends CombatItemable, ProjectileInterface {
	public void excuteOnShootBow(EntityShootBowEvent e);
}
