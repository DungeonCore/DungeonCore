package net.l_bulb.dungeoncore.item.nbttag;

import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.specialDamage.SpecialType;
import net.l_bulb.dungeoncore.item.itemInterface.ArmorItemable;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.itemInterface.EquipItemable;

class NbtTagSetter {
  static void setter(ItemInterface itemInterface, ItemStack item) {
    // NBTTagのデータを取得
    NBTTagBean nbtTagBean = new NBTTagBean(item, itemInterface);
    nbtTagBean.fixValue(itemInterface);

    ItemStackNbttagAccessor itemStackNbttagSetter = new ItemStackNbttagAccessor(item);
    // item id
    itemStackNbttagSetter.setItemId(nbtTagBean.getItemId());

    // 耐久値
    if (ItemManager.isImplemental(EquipItemable.class, itemInterface)) {
      itemStackNbttagSetter.setMaxDurability(nbtTagBean.getMaxDurability());
      itemStackNbttagSetter.setNowDurability((short) 0);
    }

    // 武器ならスロットなどを追加
    if (ItemManager.isImplemental(CombatItemable.class, itemInterface)) {
      CustomWeaponItemStack weaponItemStack = ((CombatItemable) itemInterface).getCombatAttackItemStack(item);
      // 攻撃力
      itemStackNbttagSetter.setDamage(nbtTagBean.getDamage());
      // デフォルトスロット数
      itemStackNbttagSetter.setDefaultSlotSize(nbtTagBean.getDefaultSlot());
      // 最大スロット数
      itemStackNbttagSetter.setMaxSlotSize(nbtTagBean.getMaxSlot());
      // 特殊攻撃
      int index = 1;
      for (Entry<SpecialType, Double> e : nbtTagBean.getSpecialDamageTypeMap().entrySet()) {
        weaponItemStack.setSpecialTypeList(e.getKey(), e.getValue(), index++);
      }
    }

    // 防具なら防御力を追加
    if (ItemManager.isImplemental(ArmorItemable.class, itemInterface)) {
      itemStackNbttagSetter.setNormalArmorPoint(nbtTagBean.getNormalArmorPoint());
      itemStackNbttagSetter.setBossArmorPoint(nbtTagBean.getBossArmorPoint());
      itemStackNbttagSetter.setAddMaxHealth(nbtTagBean.getAddMaxHealth());
    }
  }
}
