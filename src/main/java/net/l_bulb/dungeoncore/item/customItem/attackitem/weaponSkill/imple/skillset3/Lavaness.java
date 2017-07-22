package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset3;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithThrowItem;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class Lavaness extends WeaponSkillWithThrowItem {

  @Override
  public String getId() {
    return "wskill12";
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
        Stun.addStun(e, (int) (getData(2) * 20));
      }

      // ダメージを与える
      new CombatEntityEvent(p, attackItemDamage * getData(1), customItem, item, false, e).callEvent().damageEntity();
    }
  }

  @Override
  public Material getThrowItemType() {
    return Material.BLAZE_POWDER;
  }

}
