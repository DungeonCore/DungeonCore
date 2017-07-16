package net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem;

import java.util.Collections;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerSetStrengthItemResultEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerStrengthFinishEvent;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AttackDamageValue;
import net.l_bulb.dungeoncore.item.customItem.attackitem.SpreadSheetWeaponData;
import net.l_bulb.dungeoncore.item.customItem.attackitem.specialDamage.SpecialType;
import net.l_bulb.dungeoncore.item.itemInterface.StrengthChangeItemable;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.item.system.lore.LoreLine;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.JavaUtil;

public abstract class SpreadSheetAttackItem extends AbstractAttackItem implements StrengthChangeItemable {
  protected SpreadSheetWeaponData data;

  public SpreadSheetAttackItem(SpreadSheetWeaponData data) {
    this.data = data;
  }

  /**
   * 同じレベルのMobを倒すのにかかる攻撃回数
   *
   * @return
   */
  public double getCombatLoad() {
    return AttackDamageValue.getCombatLoad(getWeaponLevel(), getAttackType());
  }

  @Override
  public String getItemName() {
    return data.getName();
  }

  @Override
  public String getId() {
    return data.getId();
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return Math.max(getWeaponLevel() * 100, 10);
  }

  @Override
  public void onCombatEntity(CombatEntityEvent e) {

  }

  @Override
  public short getMaxDurability(ItemStack e) {
    if (data.getMaxDurability() != -1) {
      return data.getMaxDurability();
    } else {
      return getMaterial().getMaxDurability();
    }
  }

  @Override
  public double getAttackItemDamage(int strengthLevel) {
    // TODO NBT TAGから取得
    // 攻撃力取得
    double combatLoad = getCombatLoad();
    double attackDamageValue = AttackDamageValue.getAttackDamageValue(combatLoad, getAvailableLevel());

    // 倍率をかける
    double attackDamageValue2 = attackDamageValue * data.getDamageParcent();
    // クリティカルなら
    if (JavaUtil.isRandomTrue((int) getCriticalHitRate(strengthLevel))) {
      attackDamageValue2 += attackDamageValue * 0.15;
    }
    return attackDamageValue2;
  }

  /**
   * 強化レベルによって変化するLoreを取得
   */
  @Override
  public void setStrengthDetail(int level, ItemLoreToken loreToken, ItemStackNbttagAccessor nbttagSetter) {
    if (level != 0) {
      loreToken.addLore(LoreLine.getLoreLine("クリティカル率", getCriticalHitRate(level) + "%"));
    }

    double normalDamage = nbttagSetter.getDamage();
    double criticalDamage = normalDamage * 0.15;

    if (criticalDamage > 0 && getCriticalHitRate(level) > 0) {
      loreToken.addLore(LoreLine.getLoreLine("クリティカル時", "+" + JavaUtil.round(criticalDamage, 2)));
    }
  }

  /**
   * クリティカル確率
   *
   * @param level
   * @return
   */
  public double getCriticalHitRate(int level) {
    return 1.5 * level;
  }

  @Override
  public int getAvailableLevel() {
    return data.getAvailableLevel();
  }

  @Override
  public String getWeaponSkillSetId() {
    return data.getWeaponSkillId();
  }

  @Override
  public int getWeaponLevel() {
    return data.getRank();
  }

  @Override
  protected Material getMaterial() {
    return data.getItemStack().getType();
  }

  @Override
  public String[] getDetail() {
    return data.getDetail();
  }

  @Override
  public int getMaxSlotCount() {
    return data.getMaxSlot();
  }

  @Override
  public int getDefaultSlotCount() {
    return data.getDefaultSlot();
  }

  @Override
  abstract public ItemType getAttackType();

  @Override
  public int getMaxStrengthCount() {
    return 10;
  }

  @Override
  protected ItemStack getItemStackBase() {
    return data.getItemStack().clone();
  }

  @Override
  public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {
    if (!event.isSuccess()) {
      int level = event.getNextLevel();
      // +6までの強化の時は失敗しても+0にしないで元にもどす
      if (level <= 6) {
        ItemStack item = event.getItem();
        StrengthOperator.updateLore(item, Math.max(0, level - 1));
        event.setItem(item);
      }
    }
  }

  @Override
  public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {}

  @Override
  public Map<SpecialType, Double> getSpecialDamageTypeMap() {
    return JavaUtil.getNull(data.getSpecialDamageMap(), Collections.emptyMap());
  }
}
