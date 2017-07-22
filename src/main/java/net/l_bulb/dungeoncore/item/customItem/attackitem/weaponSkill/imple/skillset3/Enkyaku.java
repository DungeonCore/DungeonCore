package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset3;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithThrowItem;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class Enkyaku extends WeaponSkillWithThrowItem {

  @Override
  public String getId() {
    return "wskill10";
  }

  @Override
  public void onGround(Player p, ItemStack item, Entity spawnEntity) {
    // nullはあり得ないはずなので無視
    CombatItemable customItem = ItemManager.getCustomItem(CombatItemable.class, item);
    double attackItemDamage = customItem.getAttackItemDamage(item);

    ArrayList<LivingEntity> nearEnemy = LivingEntityUtil.getNearEnemy(spawnEntity, getData(0), getData(0), getData(0));
    for (LivingEntity e : nearEnemy) {
      // 燃えている周囲の敵にダメージを与える
      if (e.getFireTicks() > 0) {
        new CombatEntityEvent(p, attackItemDamage * getData(2), customItem, item, false, e).callEvent().damageEntity();
      }

      // 燃やす
      e.setFireTicks((int) (getData(1) * 20));
    }
  }

  @Override
  public Material getThrowItemType() {
    return Material.BLAZE_POWDER;
  }

}
