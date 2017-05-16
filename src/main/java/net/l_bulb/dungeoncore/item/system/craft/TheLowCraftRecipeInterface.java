package net.l_bulb.dungeoncore.item.system.craft;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface TheLowCraftRecipeInterface {

  /**
   * レシピIDを取得する
   *
   * @return
   */
  public String getId();

  /**
   * レシピIDをセットする
   *
   * @return
   */
  public void setId(String id);

  /**
   * 材料を追加する
   *
   * @param itemid
   * @param amount
   */
  public abstract void addMaterial(String itemid, int amount);

  /**
   * 必要素材を取得する。もし登録された素材が不正、もしくはクラフトできないならnullを返す
   *
   * @return
   */
  public abstract Map<ItemInterface, Integer> getMaterialMap();

  /**
   * クラフトに使うもとのアイテムを取得する。存在しないならnull
   *
   * @return
   */
  public ItemInterface getMainItem();

  /**
   * メインアイテムが存在するならTRUE
   *
   * @return
   */
  public boolean hasMainItem();

  /**
   * 通常時のアイテムIDを取得
   */
  public ItemInterface getCraftItem();

  /**
   * 成功時のアイテムIDを取得
   */
  public ItemInterface getSuccessItem();

  /**
   * 大成功時のアイテムIDを取得
   */
  public ItemInterface getGreateSuccessItem();

  /**
   * 通常時のアイテムIDをセットする
   */
  public void setCraftItemId(String id);

  /**
   * 成功時のアイテムIDをセットする
   */
  public void setSuccessItemId(String id);

  /**
   * 大成功時のアイテムIDをセットする
   */
  public void setGreateSuccessItemId(String id);

  /**
   * 指定されたPlayerが全てのクラフト素材を持っているか確認する
   *
   * @param p
   * @param withMainItem メインアイテムも含めて考えるならTRUE
   * @return
   */
  public boolean hasAllMaterial(Player p, boolean withMainItem);

  /**
   * インベントリから素材を削除する
   *
   * @param inv
   */
  public void removeMaterial(Inventory inv);

  /**
   * レシピが正常ならTRUE
   */
  public boolean isValidRecipe();
}