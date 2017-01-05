package lbn.item.itemInterface;

import lbn.item.ItemInterface;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public interface MeleeAttackItemable extends ItemInterface{
	public void excuteOnMeleeAttack(ItemStack itemInHand, LivingEntity owner, LivingEntity target, EntityDamageByEntityEvent e);
}
