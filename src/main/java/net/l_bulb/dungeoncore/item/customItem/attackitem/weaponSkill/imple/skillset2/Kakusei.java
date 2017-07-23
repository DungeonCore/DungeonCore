package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset2;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;

public class Kakusei extends SpreadSheetWeaponSkill {

  private static final String NAME = new Kaiho().getName();

  @Override
  public String getId() {
    return "wskill8";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    final PotionEffect REGENERATION = new PotionEffect(PotionEffectType.REGENERATION, (int) (getData(2) * 20), getDataAsInt(5) - 1);
    final PotionEffect ABSORPTION = new PotionEffect(PotionEffectType.ABSORPTION, (int) (getData(2) * 20), getDataAsInt(3) - 1);
    final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, (int) (getData(2) * 20), getDataAsInt(4) - 1);
    final PotionEffect STRENGTH = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (getData(2) * 20), getDataAsInt(1) - 1);

    Long executeSkillMillisTile = Kaiho.getExecuteSkillMillisTile(p);

    // 予兆を実行してから指定時間経っているかしらべる
    if (executeSkillMillisTile == -1 || executeSkillMillisTile + getData(0) * 1000 > System.currentTimeMillis()) {
      p.sendMessage(ChatColor.RED + "このスキルはまだ実行できません。" + NAME + "を実行してから" + getData(0) + "秒以上経過する必要があります。");
      return false;
    }

    // すべてのバフを消す
    KiniNoTikara.removePassiveBuff(p);

    // バフ効果を付与する
    p.addPotionEffect(REGENERATION);
    p.addPotionEffect(ABSORPTION);
    p.addPotionEffect(SPEED);
    p.addPotionEffect(STRENGTH);
    // 覚醒実行を通知する
    KiniNoTikara.onExecuteKakusei(p);
    return true;
  }
}
