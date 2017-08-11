package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset8;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;

public class Kouka extends SpreadSheetWeaponSkill {

  @Override
  public String getId() {
    return "wskill30";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, (int) (getData(0) * 20), getDataAsInt(1)));
    return true;
  }

}
