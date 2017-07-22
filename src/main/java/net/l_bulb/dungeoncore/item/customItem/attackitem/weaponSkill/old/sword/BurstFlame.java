package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.sword;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithTimeLimitCombat;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;

public class BurstFlame extends WeaponSkillWithTimeLimitCombat {

  @Override
  public String getId() {
    return "skill11";
  }

  @Override
  protected void runWaitParticleData(Location loc, int i) {}

  @Override
  public void onCombat(Player p, ItemStack item, CombatItemable customItem, LivingEntity livingEntity, CombatEntityEvent event) {
    // スキル発動待機中ならスキルを発動する
    if (isWaitingSkill(p)) {
      livingEntity.setFireTicks((int) (20 * getData(1)));
      Particles.runParticle(livingEntity.getEyeLocation(), ParticleType.flame, 10);
    }
  }

  @Override
  public double getTimeLimit() {
    return getData(0);
  }

  @Override
  protected void onCombat2(Player p, ItemStack item, CombatItemable customItem, LivingEntity livingEntity, CombatEntityEvent e) {}

}
