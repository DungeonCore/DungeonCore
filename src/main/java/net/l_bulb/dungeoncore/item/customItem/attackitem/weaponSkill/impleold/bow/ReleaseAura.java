package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.impleold.bow;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.impleold.WeaponSkillForOneType;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class ReleaseAura extends WeaponSkillForOneType {

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    List<Entity> nearbyEntities = p.getNearbyEntities(getData(0), getData(0), getData(0));

    Particles.runCircleParticle(p, ParticleType.mobSpell, getData(0), 10);
    p.getWorld().playSound(p.getLocation(), Sound.BLAZE_HIT, 1, 1);

    for (Entity entity : nearbyEntities) {
      if (!entity.getType().isAlive()) {
        continue;
      }

      if (LivingEntityUtil.isEnemy(entity)) {
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.ABSORPTION);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.FAST_DIGGING);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.HEALTH_BOOST);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.INVISIBILITY);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.JUMP);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.NIGHT_VISION);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.REGENERATION);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.SATURATION);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.SPEED);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.WATER_BREATHING);
      }
    }
    return true;
  }

  @Override
  public String getId() {
    return "skill16";
  }

}
