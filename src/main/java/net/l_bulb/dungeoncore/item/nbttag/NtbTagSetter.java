package net.l_bulb.dungeoncore.item.nbttag;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.itemInterface.EquipItemable;

public class NtbTagSetter {
  public static void setter(ItemInterface itemInterface, ItemStack item) {
    ItemStackNbttagSetter itemStackNbttagSetter = new ItemStackNbttagSetter(item);
    // item id
    itemStackNbttagSetter.setItemId(itemInterface.getId());
    // 耐久値
    if (ItemManager.isImplemental(EquipItemable.class, itemInterface)) {
      itemStackNbttagSetter.setMaxDurability(((EquipItemable) itemInterface).getMaxDurability(item));
      itemStackNbttagSetter.setNowDurability(((EquipItemable) itemInterface).getMaxDurability(item));
    }

    // 武器ならスロットなどを追加
    if (ItemManager.isImplemental(CombatItemable.class, itemInterface)) {
      CombatItemable combatItemable = (CombatItemable) itemInterface;
      // 攻撃力
      itemStackNbttagSetter.setDamage(combatItemable.getAttackItemDamage(0));
      // デフォルトスロット数
      itemStackNbttagSetter.setDefaultSlotSize(combatItemable.getDefaultSlotCount());
      // 最大スロット数
      itemStackNbttagSetter.setMaxSlotSize(combatItemable.getMaxSlotCount());
    }
  }

}
