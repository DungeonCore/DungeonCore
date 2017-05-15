package net.l_bulb.dungeoncore.item;

import java.util.List;

import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillInterface;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreSlotToken;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomWeaponItemBean {

  public CustomWeaponItemBean() {}

  short durability;

  List<WeaponSkillInterface> weaponSkillList;

  double damage;

  int maxSlotCount;

  int defaultSlotCount;

  // 装着済みのスロット
  List<SlotInterface> setSlotList;

  /**
   * スロット用のLoreTokenを取得
   *
   * @return
   */
  public ItemLoreSlotToken getSlotLoreToken() {
    return null;
  }

  /**
   * 標準のLoreTokenを取得
   *
   * @return
   */
  public ItemLoreToken getStandardLoreToken() {
    return null;
  }
}
