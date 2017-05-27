package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.all;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent_old;
import net.l_bulb.dungeoncore.common.other.ItemStackData;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillInterface;
import net.l_bulb.dungeoncore.player.ItemType;

public class WeaponSkillCancel implements WeaponSkillInterface {

  private static final String SKILL_CANCEL = "skill_cancel";

  @Override
  public int getSkillLevel() {
    return 0;
  }

  @Override
  public String getName() {
    return "スキルを解除する";
  }

  @Override
  public String getId() {
    return SKILL_CANCEL;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "武器スキルを解除する" };
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    return false;
  }

  @Override
  public void onCombat(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent_old event) {}

  @Override
  public int getCooltime() {
    return 0;
  }

  @Override
  public int getNeedMagicPoint() {
    return 0;
  }

  @Override
  public ItemStackData getViewItemStackData() {
    return new ItemStackData(Material.GLASS);
  }

  @Override
  public boolean canUse(ItemType type) {
    return true;
  }

  /**
   * 指定した武器スキルがこのスキルならTRUE
   *
   * @param skill
   * @return
   */
  public static boolean isThisSkill(WeaponSkillInterface skill) {
    if (skill == null) { return false; }

    return skill.getId().equals(SKILL_CANCEL);
  }

}
