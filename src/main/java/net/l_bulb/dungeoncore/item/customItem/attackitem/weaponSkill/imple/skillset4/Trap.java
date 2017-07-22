package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset4;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.other.BattleTrap;
import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithTrap;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class Trap extends WeaponSkillWithTrap {
  @Override
  public String getId() {
    return "wskill14";
  }

  @Override
  public BattleTrap getBattleTrap(Player p, ItemStack item, AbstractAttackItem customItem) {
    BattleTrap battleTrap = new BattleTrap(ParticleType.portal, 3, LivingEntityUtil::isEnemy);
    battleTrap.setDeadlineSecond(getData(0));
    // モンスターが踏んだ時の処理
    battleTrap.setFiring(e -> {
      Stun.addStun((LivingEntity) e, (int) (getData(1) * 20.0));
      Particles.runParticle(e.getLocation(), ParticleType.slime, 100);
      e.getWorld().playSound(e.getLocation(), Sound.SLIME_ATTACK, 2, (float) 0.5);
    });
    return battleTrap;
  }

  @Override
  public double getMaxTrapCount() {
    return getData(2);
  }

  /**
   * 1つあたりのトラップのクールタイムを取得
   *
   * @return
   */
  @Override
  public double getOneTrapCooltime() {
    return getData(4);
  }
}
