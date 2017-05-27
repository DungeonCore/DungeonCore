package net.l_bulb.dungeoncore.common.explosion;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;

public class NoPlayerDamageExplotionForAttackType extends NotPlayerDamageExplosion {

  LivingEntity sourceEntity;
  AbstractAttackItem customItem;
  private ItemStack item;

  public NoPlayerDamageExplotionForAttackType(Location l, float f, LivingEntity p, AbstractAttackItem customItem, ItemStack item) {
    super(l, f);
    this.sourceEntity = p;
    this.customItem = customItem;
    this.item = item;
  }

  @Override
  public float getDamage(Entity target, float d10) {
    if (target.getType().isAlive()) {
      CombatEntityEvent callEvent = new CombatEntityEvent(sourceEntity, d10, customItem, item, false, (LivingEntity) target).callEvent();
      return (float) callEvent.getDamage();
    } else {
      return d10;
    }
  }

}
