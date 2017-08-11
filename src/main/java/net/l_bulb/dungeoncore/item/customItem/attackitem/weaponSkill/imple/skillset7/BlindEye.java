package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset7;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.WeaponSkillForOneType;

public class BlindEye extends WeaponSkillForOneType {

  @Override
  public String getId() {
    return "wskill27";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, (int) (20 * getData(0)), 0));
    p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.8f, 0.8f);
    return true;
  }

}
