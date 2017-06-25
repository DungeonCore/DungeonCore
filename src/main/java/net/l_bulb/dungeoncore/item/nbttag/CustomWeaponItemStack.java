package net.l_bulb.dungeoncore.item.nbttag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.attackitem.specialDamage.SpecialType;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.slot.AbstractSlot;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.item.slot.magicstone.EmptySlot;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreData;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreSlotToken;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import lombok.Getter;
import lombok.Setter;

public class CustomWeaponItemStack {
  /**
   * インスタンスを取得、通常はクラス内からしか呼ばれない
   *
   * @param item
   * @param itemInterface
   */
  private CustomWeaponItemStack(ItemStack item, CombatItemable itemInterface) {
    this.item = item;
    this.itemInterface = itemInterface;
    this.nbtBean = new ItemStackNbttagAccessor(item);

    // TODO 必要なときだけにする
    slotList = nbtBean.getGetAllSlotList();
  }

  @Getter
  @Setter
  ItemStack item;
  @Getter
  @Setter
  CombatItemable itemInterface;

  ItemStackNbttagAccessor nbtBean;

  List<SlotInterface> slotList = new ArrayList<>();

  /**
   * ItemStackから武器情報を取得する
   *
   * @param item
   * @return
   */
  public static CustomWeaponItemStack getInstance(ItemStack item, CombatItemable itemInterface) {
    return new CustomWeaponItemStack(item, itemInterface);
  }

  /**
   * 武器のアイテムタイプを取得
   *
   * @return
   */
  public ItemType getItemType() {
    return itemInterface.getAttackType();
  }

  /**
   * 使用している魔法石を取得
   *
   * @return
   */
  public List<SlotInterface> getUseSlot() {
    return slotList;
  }

  /**
   * 魔法石を取り除く
   *
   * @param slot
   * @return
   */
  public boolean removeSlot(SlotInterface slot) {
    return slotList.remove(slot);
  }

  /**
   * 空のスロットを追加する
   *
   * @return
   */
  public boolean addEmptySlot() {
    return addSlot(new EmptySlot());
  }

  /**
   * 魔法石を追加する
   *
   * @param slot
   * @return
   */
  public boolean addSlot(SlotInterface slot) {
    return slotList.add(slot);
  }

  /**
   * 指定されたスロットが存在していたらTRUE
   *
   * @param slot
   * @return
   */
  public boolean hasSlot(AbstractSlot slot) {
    return slotList.contains(slot);
  }

  int strengthLevel = -1;

  /**
   * 強化レベルをセットする
   *
   * @param level
   */
  public void setStrengthLevel(int level) {
    this.strengthLevel = level;
  }

  /**
   * 特攻効果を取得する
   *
   * @return
   */
  public Map<SpecialType, Double> getSpecialTypeList() {
    HashMap<SpecialType, Double> specialMap = new HashMap<>();

    // 特攻データ１を挿入する
    String specialAttackType1 = nbtBean.getSpecialAttackValue1();
    if (specialAttackType1 != null && !specialAttackType1.isEmpty()) {
      String[] split = specialAttackType1.split(":");
      specialMap.put(SpecialType.valueOf(split[0]), Double.parseDouble(split[1]));
    }
    // 特攻データ2を挿入する
    String specialAttackData2 = nbtBean.getSpecialAttackValue1();
    if (specialAttackData2 != null && !specialAttackData2.isEmpty()) {
      String[] split = specialAttackData2.split(":");
      specialMap.put(SpecialType.valueOf(split[0]), Double.parseDouble(split[1]));
    }

    return specialMap;
  }

  /**
   * 特攻効果をセットする
   *
   * @return
   */
  public void setSpecialTypeList(SpecialType type, double value, int index) {
    // 特攻データ
    String specialData = type.toString() + value;

    switch (index) {
      case 1:
        nbtBean.setSpecialAttackType1(specialData);
        break;
      case 2:
        nbtBean.setSpecialAttackType2(specialData);
        break;
      default:
        throw new RuntimeException("index is invalid:" + index);
    }
  }

  /**
   * 武器情報を取得する
   */
  public void updateItem() {
    // 強化レベルをセットしたなら更新する
    if (strengthLevel != -1) {
      StrengthOperator.updateLore(getItem(), strengthLevel);
    }
    // nbtタグにセットする
    nbtBean.setSlotList(slotList);

    ItemLoreData itemLoreData = new ItemLoreData(item);
    // タイトルのSlotを削除
    itemLoreData.removeLore(ItemLoreToken.TITLE_SLOT);

    // LoreToken作成
    ItemLoreSlotToken itemLoreSlotToken = new ItemLoreSlotToken(getMaxSlotCount());

    for (SlotInterface slot : slotList) {
      itemLoreSlotToken.addLoreAsOriginal(StringUtils.join(slot.getNameColor().toString(), "    ■ ", slot.getSlotName()));
    }
    itemLoreData.addLore(itemLoreSlotToken);
    ItemStackUtil.setLore(item, itemLoreData.getLore());
  }

  public int getMaxSlotCount() {
    return nbtBean.getMaxSlotSize();
  }

  public int getDefaultSlotCount() {
    return nbtBean.getDefaultSlotSize();
  }

  public double getDamage(int strengthLevel) {
    return nbtBean.getDamage();
  }

  /**
   * クリティカル時のダメージを取得
   *
   * @param strengthLevel
   * @return
   */
  public double getCriticalDamage(int strengthLevel) {
    return nbtBean.getDamage() * 0.15;
  }

}
