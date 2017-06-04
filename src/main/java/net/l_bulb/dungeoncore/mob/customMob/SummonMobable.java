package net.l_bulb.dungeoncore.mob.customMob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.mob.SummonPlayerManager;

public interface SummonMobable {
  int getDeadlineTick();

  void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e);

  default public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
    Player owner = SummonPlayerManager.getOwner(mob);
    ItemStack item = SummonPlayerManager.getUseItem(mob);
    ItemInterface customItem = ItemManager.getCustomItem(item);

    if (owner != null && item != null && customItem != null) {
      CombatEntityEvent callEvent = new CombatEntityEvent(owner, e.getDamage(DamageModifier.BASE), customItem, item, false, target).callEvent();
      if (!callEvent.isCancelled()) {
        e.setDamage(DamageModifier.BASE, callEvent.getDamage());
      } else {
        e.setCancelled(true);
      }

    }
  }
}
