package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public interface MeleeAttackItemable extends CombatItemable {
  public void excuteOnMeleeAttack(ItemStack itemInHand, LivingEntity owner, LivingEntity target, EntityDamageByEntityEvent e);
}
