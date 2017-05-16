package net.l_bulb.dungeoncore.item.system.craft;

import java.util.HashMap;

import net.l_bulb.dungeoncore.item.ItemInterface;

public class CraftItemRecipeFactory {

  // レシピを保持するためのMap(Key＝ItemID、Value＝レシピ)
  static HashMap<String, TheLowCraftRecipeInterface> craftRecipeMap = new HashMap<>();

  /**
   * レシピを追加する
   *
   * @param itemId ITEM ID
   * @param recipe レシピ
   */
  public static void addRecipe(String itemId, TheLowCraftRecipeInterface recipe) {
    craftRecipeMap.put(itemId, recipe);
  }

  /**
   * レシピを取得する
   *
   * @param itemid
   * @return
   */
  public static TheLowCraftRecipeInterface getRecipe(String itemid) {
    return craftRecipeMap.get(itemid);
  }

  /**
   * クラフトできるITEMならTRUE
   *
   * @param item
   * @return
   */
  public static boolean contains(ItemInterface item) {
    return craftRecipeMap.containsKey(item.getId());
  }

  /**
   * レシピを取得する
   *
   * @param itemid
   * @return
   */
  public static TheLowCraftRecipeInterface getRecipe(ItemInterface item) {
    return craftRecipeMap.get(item.getId());
  }

  /**
   * クラフトできるITEMならTRUE
   *
   * @param itemId
   * @return
   */
  public static boolean contains(String itemId) {
    return craftRecipeMap.containsKey(itemId);
  }
}
