package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset8;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class Eiyuden extends SpreadSheetWeaponSkill {

  @Override
  public String getId() {
    return "wskill32";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    PotionEffect potionEffect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, getDataAsInt(1), (int) (getData(2) * 20));
    // 味方にポーションエフェクトをつける
    LivingEntityUtil.getNearFrendly(p, getData(0), getData(0), getData(0)).stream()
        .forEach(e -> e.addPotionEffect(potionEffect, true));
    p.addPotionEffect(potionEffect);
    return true;
  }

}
