package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.other.ItemStackData;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillData;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillInterface;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillType;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;

public abstract class SpreadSheetWeaponSkill implements WeaponSkillInterface {

  WeaponSkillData data;

  public void setData(WeaponSkillData data) {
    this.data = data;
  }

  @Override
  public String getName() {
    return data.getName();
  }

  @Override
  public String[] getDetail() {
    return data.getDetail();
  }

  @Override
  public int getCooltime() {
    return data.getCooltime();
  }

  @Override
  public int getNeedMagicPoint() {
    return data.getNeedMp();
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    return false;
  }

  @Override
  public void onCombat(Player p, ItemStack item, CombatItemable customItem, LivingEntity livingEntity, CombatEntityEvent event) {}

  protected String getDataString(int i) {
    return String.valueOf(data.getData(i));
  }

  protected double getData(int i) {
    return data.getData(i);
  }

  protected int getDataAsInt(int i) {
    return (int) data.getData(i);
  }

  @Override
  public ItemStackData getViewItemStackData() {
    return new ItemStackData(data.getMaterial(), data.getMaterialdata());
  }

  @Override
  public WeaponSkillType geWeaponSkillType() {
    return data.getWeaponSkillType();
  }
}
