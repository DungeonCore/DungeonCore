package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset8;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithProjectile;
import net.l_bulb.dungeoncore.util.NMSUtils;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

public class Chouhatu extends WeaponSkillWithProjectile {

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
    TheLowExecutor.executeTimer(5, r -> r.isElapsedTick((long) (getData(0) * 20)) || !target.isValid(), r -> {
      Particles.runParticle(target, ParticleType.portal, 30);
      NMSUtils.setTarget(target, owner);
    });
  }

  @Override
  public String getId() {
    return "wskill31";
  }

  @Override
  protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
    return p.launchProjectile(Snowball.class, p.getLocation().getDirection().normalize().multiply(3));
  }

}
