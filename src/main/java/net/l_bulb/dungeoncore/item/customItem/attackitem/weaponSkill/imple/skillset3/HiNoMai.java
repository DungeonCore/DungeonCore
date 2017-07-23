package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset3;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithThrowItem;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class HiNoMai extends WeaponSkillWithThrowItem {

  @Override
  public String getId() {
    return "wskill11";
  }

  @Override
  public void onGround(Player p, ItemStack item, Entity spawnEntity) {
    final PotionEffect SLOW = new PotionEffect(PotionEffectType.SLOW, (int) (getData(3) * 20), (int) (getData(2) - 1));

    ArrayList<LivingEntity> nearEnemy = LivingEntityUtil.getNearEnemy(spawnEntity, getData(0), getData(0), getData(0));
    for (LivingEntity e : nearEnemy) {
      // 燃えている周囲のデバフ効果を与える
      if (e.getFireTicks() > 0) {
        e.addPotionEffect(SLOW, true);
      }

      // 燃やす
      e.setFireTicks((int) (getData(1) * 20));
    }
  }

  @Override
  public Material getThrowItemType() {
    return Material.FIREBALL;
  }

}
