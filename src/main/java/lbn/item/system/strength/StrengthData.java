package lbn.item.system.strength;

import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemManager;
import lbn.item.itemInterface.Strengthenable;

import org.bukkit.inventory.ItemStack;

/**
 * 一回の強化に使われる情報
 *
 */
public class StrengthData {
  ItemStack item1;

  ItemStack material;

  int nextLevel;

  int needMoney;

  int successChance = 0;

  boolean canStrength = false;

  public StrengthData(ItemStack item1) {
    setItem1(item1);
  }

  /**
   * 強化するアイテムをセットする
   * 
   * @param item1
   */
  public void setItem1(ItemStack item1) {
    this.item1 = item1;
    if (item1 == null) {
      nextLevel = 0;
      material = null;
      needMoney = 0;
      successChance = 0;
      canStrength = false;
      return;
    }

    Strengthenable customItem = ItemManager.getCustomItem(Strengthenable.class, item1);
    if (customItem == null) {
      nextLevel = 0;
      material = null;
      needMoney = 0;
      successChance = 0;
      canStrength = false;
      return;
    }

    StrengthTemplate strengthTemplate = customItem.getStrengthTemplate();

    // 今のレベル
    int level = StrengthOperator.getLevel(item1);
    nextLevel = level + 1;

    material = strengthTemplate.getStrengthMaterials(nextLevel);

    needMoney = strengthTemplate.getStrengthGalions(nextLevel);

    successChance = strengthTemplate.successChance(nextLevel);
  }

  /**
   * 強化できるかどうかを記録する
   * 
   * @param canStrength
   */
  public void setCanStrength(boolean canStrength) {
    this.canStrength = canStrength;
  }

  /**
   * 強化できるかどうかを取得
   * 
   * @return
   */
  public boolean isCanStrength() {
    return canStrength;
  }

  /**
   * 強化素材を取得
   * 
   * @return
   */
  public ItemStack getMaterial() {
    if (material != null) {
      return material.clone();
    } else {
      return null;
    }
  }

  /**
   * 必要なお金を取得
   * 
   * @return
   */
  public int getNeedMoney() {
    return needMoney;
  }

  /**
   * 強化後のレベルを取得
   * 
   * @return
   */
  public int getNextLevel() {
    return nextLevel;
  }

  /**
   * 成功率を取得
   * 
   * @return
   */
  public int getSuccessChance() {
    return successChance;
  }

  /** 実際の強化に成功したかどうかを記録する */
  boolean successStrength = false;

  public boolean isSuccessStrength() {
    return successStrength;
  }

  public void setSuccessStrength(boolean successStrength) {
    this.successStrength = successStrength;
  }
}
