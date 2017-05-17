package net.l_bulb.dungeoncore.item.system.craft;

import java.util.Random;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;

public class CraftItemUtil {
  static Random rnd = new Random();

  /**
   * クラフト後のアイテムを取得する
   *
   * @param recipe 不正な値でないレシピ
   * @param mainItemLevel メインアイテムのレベル
   * @return
   */
  public static ItemStack getCraftedItem(TheLowCraftRecipeInterface recipe, int mainItemLevel) {
    int nextInt = rnd.nextInt(100);

    // クラフト後のアイテム
    ItemInterface resultItem = null;

    if (nextInt < GRATE_SUCCESS_RATE) {
      // クラフト大成功
      ItemInterface item = recipe.getGreateSuccessItem();
      if (item != null) {
        resultItem = item;
      }
    } else if (nextInt < GRATE_SUCCESS_RATE + SUCCESS_RATE) {
      // クラフト成功
      ItemInterface item = recipe.getSuccessItem();
      if (item != null) {
        resultItem = item;
      }
    }

    // それ以外の時は通常のクラフトアイテム
    if (resultItem == null) {
      resultItem = recipe.getCraftItem();
    }

    // クラフト後のアイテム
    ItemStack resultItemStack = resultItem.getItem();

    // 強化出来るアイテムなら強化する
    if (mainItemLevel != 0 && resultItem.isStrengthItem()) {
      StrengthOperator.updateLore(resultItemStack, mainItemLevel);
    }

    return resultItemStack;
  }

  /** 成功の確率 */
  public static final int SUCCESS_RATE = 20;
  /** 大成功の確率 */
  public static final int GRATE_SUCCESS_RATE = 10;
}
