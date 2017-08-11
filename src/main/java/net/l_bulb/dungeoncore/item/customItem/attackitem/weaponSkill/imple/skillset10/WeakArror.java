package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset10;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithProjectile;

public class WeakArror extends WeaponSkillWithProjectile {

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
    target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) (getData(0) * 20), getDataAsInt(1)));
    Particles.runParticle(target, ParticleType.magicCrit, 20);
  }

  @Override
  public String getId() {
    return "wskill37";
  }

  @Override
  protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
    return p.launchProjectile(Arrow.class, p.getLocation().getDirection().multiply(3));
  }

}
