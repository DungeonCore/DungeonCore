package lbn.item.itemInterface;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemInterface;

public interface MeleeAttackItemable extends ItemInterface{
	public void excuteOnMeleeAttack(ItemStack itemInHand, LivingEntity owner, LivingEntity target, EntityDamageByEntityEvent e);
}
