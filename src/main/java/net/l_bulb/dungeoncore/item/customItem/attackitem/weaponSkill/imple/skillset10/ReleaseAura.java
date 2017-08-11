package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset10;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class ReleaseAura extends SpreadSheetWeaponSkill {

  @Override
  public String getId() {
    return "wskill40";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    PotionEffect potionEffect = new PotionEffect(PotionEffectType.ABSORPTION, (int) (getData(1) * 20), getDataAsInt(2));

    LivingEntityUtil.getNearFrendly(p, getData(0), getData(0), getData(0)).stream().forEach(e -> {
      // デバフをつける
      LivingEntityUtil.removeDebuff(e);
      // ポーションをつける
      e.addPotionEffect(potionEffect);
    });

    // デバフをつける
    LivingEntityUtil.removeDebuff(p);
    // ポーションをつける
    p.addPotionEffect(potionEffect);

    return true;
  }

}
