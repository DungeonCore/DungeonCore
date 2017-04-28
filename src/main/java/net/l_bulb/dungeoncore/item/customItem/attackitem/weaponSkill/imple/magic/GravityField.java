package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.magic;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.FallingBlockParticles;
import net.l_bulb.dungeoncore.util.LbnRunnable;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class GravityField extends WeaponSkillForOneType {

  public GravityField() {
    super(ItemType.MAGIC);
  }

  @Override
  public String getId() {
    return "skill15";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    HashSet<LivingEntity> enemy = new HashSet<LivingEntity>();

    for (Entity entity : p.getNearbyEntities(getData(0), getData(0), getData(0))) {
      if (!entity.getType().isAlive()) {
        continue;
      }
      // 敵じゃない時は無視する
      if (!LivingEntityUtil.isEnemy(entity)) {
        continue;
      }
      LivingEntity e = (LivingEntity) entity;

      // 動きを止める
      e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (20 * getData(0)), 100), true);
      // ダメージを与える敵を記録する
      enemy.add(e);
    }

    List<Location> randomCircleUpperBlock = FallingBlockParticles.randomCircleUpperBlock(p.getLocation(), Material.DIRT, (byte) 0, 0.5, 1.1, 50,
        getData(0));
    for (Location location : randomCircleUpperBlock) {
      Particles.runParticle(location, ParticleType.explode);
    }
    p.getWorld().playSound(p.getLocation(), Sound.EXPLODE, 1, (float) 0.5);

    // {1}秒ごとにダメージを与える
    new LbnRunnable() {
      @Override
      public void run2() {
        for (LivingEntity livingEntity : enemy) {
          // ダメージを与える
          livingEntity.damage(customItem.getAttackItemDamage(StrengthOperator.getLevel(item)) * getData(2));
          Particles.runParticle(livingEntity, ParticleType.lava, 100);
        }
      }
    }.runTaskTimer((long) (20 * getData(1)));
    return true;
  }

}
