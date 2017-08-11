package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset9;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class SenkinoGourei extends SpreadSheetWeaponSkill {

  @Override
  public String getId() {
    return "wskill36";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    List<PotionEffect> potionEffectList = Arrays.asList(
        new PotionEffect(PotionEffectType.ABSORPTION, getDataAsInt(2), (int) (getData(1) * 20)),
        new PotionEffect(PotionEffectType.SPEED, getDataAsInt(3), (int) (getData(1) * 20)),
        new PotionEffect(PotionEffectType.REGENERATION, getDataAsInt(4), (int) (getData(1) * 20)),
        new PotionEffect(PotionEffectType.INCREASE_DAMAGE, getDataAsInt(5), (int) (getData(1) * 20)));
    // 味方にポーションエフェクトをつける
    LivingEntityUtil.getNearFrendly(p, getData(0), getData(0), getData(0)).stream()
        .forEach(e -> e.addPotionEffects(potionEffectList));
    return true;
  }

}
