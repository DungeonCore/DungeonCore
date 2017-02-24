package lbn.item.itemInterface;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public interface BowItemable extends CombatItemable{
	public void excuteOnShootBow(EntityShootBowEvent e);
	public void excuteOnProjectileHit(ProjectileHitEvent e, ItemStack bow);
	public void excuteOnProjectileDamage(EntityDamageByEntityEvent e, ItemStack bow, LivingEntity owner, LivingEntity target);

}
