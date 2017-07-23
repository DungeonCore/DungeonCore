package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset5;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithTimeLimit;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.util.MinecraftUtil;

public class Kyoki extends WeaponSkillWithTimeLimit {

  @Override
  public String getId() {
    return "wskill18";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    // パーティクル
    Particles.runParticle(p.getLocation(), ParticleType.lava);
    // サウンド
    MinecraftUtil.playSoundForAll(p.getLocation(), Sound.EXPLODE, 1, 0.5);

    return super.onClick(p, item, customItem);
  }

  @Override
  public void onCombat(Player p, ItemStack item, CombatItemable customItem, LivingEntity livingEntity, CombatEntityEvent event) {
    if (isActive(p, item)) {
      event.setDamage(event.getDamage() * getData(1));
    }
  }

  @Override
  protected void offActive(Player p, ItemStack item, AbstractAttackItem customItem) {
    // Slowを付与する
    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (getData(2) * 20), getDataAsInt(1) - 1), true);
  }
}