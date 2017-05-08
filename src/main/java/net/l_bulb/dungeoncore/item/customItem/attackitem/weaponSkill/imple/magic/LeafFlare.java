package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.magic;

import java.util.List;

import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class LeafFlare extends WeaponSkillForOneType {

  public LeafFlare() {
    super(ItemType.MAGIC);
  }

  @Override
  public String getId() {
    return "skill14";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    List<Entity> nearbyEntities = p.getNearbyEntities(getData(0), getData(0), getData(0));

    Particles.runCircleParticle(p, ParticleType.portal, getData(0), 10);
    p.getWorld().playSound(p.getLocation(), Sound.BLAZE_HIT, 1, 1);

    for (Entity entity : nearbyEntities) {
      if (!entity.getType().isAlive()) {
        continue;
      }

      if (LivingEntityUtil.isFriendship(entity)) {
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.SLOW);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.CONFUSION);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.HUNGER);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.SLOW_DIGGING);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.WEAKNESS);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.POISON);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.WITHER);
        ((LivingEntity) entity).removePotionEffect(PotionEffectType.BLINDNESS);
      }
    }
    return true;
  }
}
