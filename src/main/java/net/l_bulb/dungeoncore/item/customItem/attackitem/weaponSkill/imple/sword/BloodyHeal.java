package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithMultiCombat;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class BloodyHeal extends WeaponSkillWithMultiCombat {

  public BloodyHeal() {
    super(ItemType.SWORD);
  }

  @Override
  public String getId() {
    return "skill2";
  }

  @Override
  protected void runWaitParticleData(Location loc, int count) {
    Particles.runParticle(loc, ParticleType.reddust);
  }

  @Override
  protected int getMaxAttackCount() {
    return 100;
  }

  @Override
  protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e,
      int attackCount) {
    // ダメージを{1}倍する
    e.setDamage(e.getDamage() * getData(1));

    // 体力を回復する
    LivingEntityUtil.addHealth(p, 2 * getData(2));

    Particles.runParticle(p.getLocation(), ParticleType.heart);
  }

  @Override
  public double getTimeLimit() {
    return getData(0);
  }
}
