package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import net.l_bulb.dungeoncore.player.ItemType;

public class ProtectionArmor extends WeaponSkillForOneType {

  public ProtectionArmor() {
    super(ItemType.SWORD);
  }

  @Override
  public String getName() {
    return "プロテクションアーマー";
  }

  @Override
  public String getId() {
    return "skill7";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    p.playSound(p.getLocation(), Sound.DONKEY_HIT, 1, (float) 0.1);
    Particles.runCircleParticle(p.getLocation().add(0, 1, 0), ParticleType.portal, 1.5, 10);
    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (20 * getData(0)), 6));
    return true;
  }

}
