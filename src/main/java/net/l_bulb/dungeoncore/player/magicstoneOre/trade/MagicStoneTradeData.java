package net.l_bulb.dungeoncore.player.magicstoneOre.trade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import net.l_bulb.dungeoncore.dungeon.contents.item.other.strengthBase.StrengthBaseJade;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.SlotManager;
import net.l_bulb.dungeoncore.item.customItem.other.SimpleStone;
import net.l_bulb.dungeoncore.item.slot.AbstractSlot;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.slot.UnUseSlot;
import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneOreType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MagicStoneTradeData {
  static Random random = new Random();
  // 石ころ
  static ItemStack stone = new SimpleStone().getItem();

  /**
   * マジックストーンのタイプからランダムでアイテムを取得する
   * 
   * @param type
   * @return
   */
  public static ItemStack getRandomMagicStoneItem(MagicStoneOreType type) {

    int nextInt = random.nextInt(100);

    switch (type) {
      case DIAOMOD_ORE:
        if (5 > nextInt) {
          return stone;
        } else if (10 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL1);
        } else if (20 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL2);
        } else if (40 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL3);
        } else if (70 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL4);
        } else if (95 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL5);
        } else {
          return new StrengthBaseJade().getItem();
        }
      case REDSTONE_ORE:
        if (10 > nextInt) {
          return stone;
        } else if (30 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL1);
        } else if (60 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL2);
        } else if (85 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL3);
        } else {
          return getRandomSlotItem(SlotLevel.LEVEL4);
        }
      case IRON_ORE:
        if (30 > nextInt) {
          return stone;
        } else if (70 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL1);
        } else if (90 > nextInt) {
          return getRandomSlotItem(SlotLevel.LEVEL2);
        } else if (100 > nextInt) { return getRandomSlotItem(SlotLevel.LEVEL3); }
      case EMERALD_ORE:
        return ItemStackUtil.getItem("未設定", Material.EMERALD, new String[] { "未設定アイテム" });
      case GOLD_ORE:
        return ItemStackUtil.getItem("未設定", Material.GOLD_INGOT, new String[] { "未設定アイテム" });
      default:
        break;
    }
    return stone;
  }

  /** レベルごとにAbstractSlotを保持するキャッシュ */
  static HashMap<SlotLevel, ArrayList<ItemInterface>> magicStoneCache = new HashMap<SlotLevel, ArrayList<ItemInterface>>();

  /**
   * 指定されたSlotLevelからランダムで魔法石アイテムを取得する
   * 
   * @param level
   * @return
   */
  public static ItemStack getRandomSlotItem(SlotLevel level) {
    // キャッシュに含まれていないならキャッシュを作成する
    if (!magicStoneCache.containsKey(level)) {
      Collection<SlotInterface> slotListByLevel = SlotManager.getSlotListByLevel(level);
      if (slotListByLevel.isEmpty()) { return null; }
      // ItemInterfaceを継承しているものはItem化できるので保持する
      ArrayList<ItemInterface> arrayList = new ArrayList<ItemInterface>();
      for (SlotInterface slotInterface : slotListByLevel) {
        // 今使えないSlotは無視する
        if (slotInterface instanceof UnUseSlot) {
          continue;
        }
        if (slotInterface instanceof ItemInterface) {
          arrayList.add((AbstractSlot) slotInterface);
        }
      }
      // もし1つもアイテム化出来るものが存在しなければ何もしない
      if (arrayList.isEmpty()) { return null; }
      // キャッシュに保存する
      magicStoneCache.put(level, arrayList);
    }

    // ランダムで1つ選択する
    ArrayList<ItemInterface> magicStoneItemList = magicStoneCache.get(level);
    int nextInt = random.nextInt(magicStoneItemList.size());
    ItemInterface slot = magicStoneItemList.get(nextInt);
    return slot.getItem();
  }
}
