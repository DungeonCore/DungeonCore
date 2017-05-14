package net.l_bulb.dungeoncore.item.system.craft;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.NbtTagConst;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.itemInterface.CraftItemable;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreData;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.item.system.lore.LoreLine;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class CraftItemSelectViewerItems {
  private static final String NOT_CRAFT_LINE = ChatColor.RED + "" + ChatColor.BOLD + "素材が足りないためアイテムを作成出来ません";
  private static final String CRAFT_MATERIAL_LORE_TITLE = "クラフト素材";

  public static ItemStack getViewItem(CraftItemable craftItem) {
    ItemStack itemstack = craftItem.getItem();

    TheLowCraftRecipeInterface craftRecipe = craftItem.getCraftRecipe();

    boolean isValid = true;

    // LoreDataを生成
    ItemLoreData itemLoreData = new ItemLoreData(itemstack, new CraftViewerLoreComparator());

    // 素材のLoreを取得
    ItemLoreToken materialLore = getLoreTokenFromRecipe(craftRecipe);
    if (materialLore == null) {
      isValid = false;
    }

    // エラーがあるなら
    if (!isValid) { return ItemStackUtil.getItem(craftItem.getItemName(), Material.BARRIER, "エラーがあるので生成出来ません"); }
    // 材料のLoreを追加する
    itemLoreData.addLore(materialLore);

    // SlotのLoreを削除する
    itemLoreData.removeLore(ItemLoreToken.TITLE_SLOT);

    // Slotを装着できるならSlotの情報をのせる
    if (ItemManager.isImplemental(CombatItemable.class, craftItem)) {
      ItemLoreToken loreToken = itemLoreData.getLoreToken(ItemLoreToken.TITLE_STANDARD);
      loreToken.addLore(LoreLine.getLoreLine("最大スロット数", ((CombatItemable) craftItem).getMaxSlotCount()));
      loreToken.addLore(LoreLine.getLoreLine("初期スロット数", ((CombatItemable) craftItem).getDefaultSlotCount()));

      itemLoreData.addLore(materialLore);
    }

    ItemStackUtil.setLore(itemstack, itemLoreData.getLore());

    // IDをつける
    ItemStackUtil.setNBTTag(itemstack, NbtTagConst.THELOW_ITEM_ID_FOR_CRAFT, craftItem.getId());

    return itemstack;
  }

  /**
   * レシピからLoreTokenを取得。もしエラーがあるならnullを返す
   *
   * @param craftRecipe
   * @return
   */
  public static ItemLoreToken getLoreTokenFromRecipe(TheLowCraftRecipeInterface craftRecipe) {
    ItemLoreToken materialLore = new ItemLoreToken(CRAFT_MATERIAL_LORE_TITLE);
    // メインアイテムがあるならTRUE
    if (craftRecipe.hasMainItem()) {
      // メインアイテムがないならエラーとする
      ItemInterface mainItem = craftRecipe.getMainItem();
      if (mainItem != null) {
        materialLore.addLore(mainItem.getItemName(), ChatColor.LIGHT_PURPLE);
      } else {
        return null;
      }
    }

    // 材料を追加する
    Map<ItemInterface, Integer> materialMap = craftRecipe.getMaterialMap();
    if (materialMap != null) {
      for (Entry<ItemInterface, Integer> entry : materialMap.entrySet()) {
        ItemInterface materialItem = entry.getKey();
        if (materialItem != null) {
          materialLore.addLore(MessageFormat.format("{0}  {1}個", materialItem.getItemName(), entry.getValue()));
        } else {
          return null;
        }
      }
    } else {
      return null;
    }
    return materialLore;
  }

  /**
   * 素材が足りないからクラフトできないという文をLoreに追加する
   *
   * @param item
   */
  public static void addDontHasMaterial(ItemStack item) {
    // まずは削除する
    List<String> lore = removeDontHasMaterialLine(item);
    // 一番最初に追加する
    ArrayList<String> newLore = new ArrayList<>();
    newLore.add(NOT_CRAFT_LINE);
    newLore.addAll(lore);
    ItemStackUtil.setLore(item, newLore);
  }

  /**
   * 素材が足りないからクラフト出来ないという文をLoreから削除する
   *
   * @param item
   * @return
   */
  public static List<String> removeDontHasMaterialLine(ItemStack item) {
    List<String> lore = ItemStackUtil.getLore(item);
    // もしすでに警告文が存在するなら何もしない
    Iterator<String> iterator = lore.iterator();
    while (iterator.hasNext()) {
      String line = iterator.next();
      if (NOT_CRAFT_LINE.equals(line)) {
        iterator.remove();
      } else {
        // もし警告文でなければこれ以上警告文がないと判断する
        return lore;
      }
    }
    return lore;
  }

  static class CraftViewerLoreComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
      int index1 = getIndex(o1);
      int index2 = getIndex(o2);

      if (index1 == index2) { return o1.compareTo(o2); }
      return index1 - index2;
    }

    public int getIndex(String value) {
      if (value.contains(ItemLoreToken.TITLE_STANDARD)) {
        return 2;
      } else if (value.contains(CRAFT_MATERIAL_LORE_TITLE)) {
        return 1;
      } else if (value.contains(ItemLoreToken.TITLE_STRENGTH)) {
        return 3;
      } else if (value.contains(ItemLoreToken.TITLE_SLOT)) { return 20; }
      return 10;
    }
  }
}
