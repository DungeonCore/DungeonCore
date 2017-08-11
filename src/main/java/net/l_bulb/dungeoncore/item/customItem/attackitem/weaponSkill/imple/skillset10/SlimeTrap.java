package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset10;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.other.BattleTrap;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithTrap;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class SlimeTrap extends WeaponSkillWithTrap {

  @Override
  public String getId() {
    return "wskill39";
  }

  @Override
  public BattleTrap getBattleTrap(Player p, ItemStack item, AbstractAttackItem customItem) {
    // ダメージを与えて上にノックアップする
    BattleTrap battleTrap = new BattleTrap(ParticleType.slime, 4, e -> LivingEntityUtil.isEnemy(e) && e.getType().isAlive());
    battleTrap.setDeadlineSecond(getData(0));
    battleTrap.setFiring(e -> {
      new CombatEntityEvent(p, getNBTTagAccessor(item).getDamage() * getData(3), customItem, item, false, (LivingEntity) e).callEvent()
          .damageEntity();
      e.setVelocity(new Vector(0, 3, 0));
    });
    return battleTrap;
  }

  @Override
  public int getMaxTrapCount() {
    return getDataAsInt(2);
  }

  @Override
  public double getOneTrapCooltime() {
    return getData(3) * 20;
  }

}
