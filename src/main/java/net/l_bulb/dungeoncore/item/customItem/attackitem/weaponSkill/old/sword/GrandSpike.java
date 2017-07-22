package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.sword;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.WeaponSkillForOneType;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class GrandSpike extends WeaponSkillForOneType {
  @Override
  public String getId() {
    return "skill12";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    List<Entity> nearbyEntities = p.getNearbyEntities(getData(0), 3, getData(0));
    for (Entity entity : nearbyEntities) {
      if (LivingEntityUtil.isEnemy(entity)) {
        entity.setVelocity(new Vector(0, getData(2), 0));
        Stun.addStun((LivingEntity) entity, (int) (getData(1) * 20));
        Particles.runParticle(p.getLocation(), ParticleType.splash, 20);
      }
    }
    Particles.runCircleParticle(p, ParticleType.smoke, 5, 5);

    p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);

    return true;
  }
}
