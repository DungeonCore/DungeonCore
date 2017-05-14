package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.bow;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import net.l_bulb.dungeoncore.player.ItemType;

public class BlindEye extends WeaponSkillForOneType {

  public BlindEye() {
    super(ItemType.BOW);
  }

  @Override
  public String getId() {
    return "skill10";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, (int) (20 * getData(0)), 0));
    p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.8f, 0.8f);
    return true;
  }

}
