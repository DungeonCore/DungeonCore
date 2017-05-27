package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithMultiCombat;
import net.l_bulb.dungeoncore.player.ItemType;

public class Lump extends WeaponSkillWithMultiCombat {

  public Lump() {
    super(ItemType.SWORD);
  }

  @Override
  public String getId() {
    return "skill9";
  }

  @Override
  protected void runWaitParticleData(Location loc, int i) {
    if (i % 3 == 0) {
      Particles.runParticle(loc, ParticleType.lava, 10);
    }
  }

  @Override
  public double getTimeLimit() {
    return getData(3);
  }

  @Override
  protected int getMaxAttackCount() {
    return 3;
  }

  @Override
  protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, CombatEntityEvent e,
      int attackCount) {
    switch (attackCount) {
      case 1:
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (20 * getData(0)), 0));
        p.playSound(p.getLocation(), Sound.ANVIL_LAND, (float) 0.5, 3);
        break;
      case 2:
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (20 * getData(1)), 0));
        p.playSound(p.getLocation(), Sound.ANVIL_LAND, (float) 0.5, 3);
        break;
      case 3:
        e.setDamage(e.getDamage() * getData(2));
        p.playSound(p.getLocation(), Sound.ANVIL_LAND, (float) 0.5, (float) 0.5);
        break;
      default:
        break;
    }
    p.sendMessage(ChatColor.RED + "[武器スキル]" + ChatColor.GREEN + attackCount + " HIT!!!");
  }

}
