package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset9;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class Heiwanotameni extends SpreadSheetWeaponSkill {

  @Override
  public String getId() {
    return "wskill35";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    PotionEffect potionEffect = new PotionEffect(PotionEffectType.REGENERATION, getDataAsInt(1), (int) (getData(2) * 20));
    // 味方にポーションエフェクトをつける
    LivingEntityUtil.getNearFrendly(p, getData(0), getData(0), getData(0)).stream()
        .forEach(e -> e.addPotionEffect(potionEffect, true));
    return true;
  }

}
