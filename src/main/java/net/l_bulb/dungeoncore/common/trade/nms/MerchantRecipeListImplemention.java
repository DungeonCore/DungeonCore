package net.l_bulb.dungeoncore.common.trade.nms;

import net.l_bulb.dungeoncore.common.trade.TheLowMerchant;
import net.l_bulb.dungeoncore.common.trade.TheLowMerchantRecipe;

import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.MerchantRecipe;
import net.minecraft.server.v1_8_R1.MerchantRecipeList;

public class MerchantRecipeListImplemention extends MerchantRecipeList {
  private static final long serialVersionUID = -7387736942727601348L;

  TheLowMerchant merchant;

  public MerchantRecipeListImplemention(TheLowMerchant merchant) {
    this.merchant = merchant;
  }

  /**
   * レシピを追加する
   *
   * @param recipe
   */
  @SuppressWarnings("unchecked")
  public void addTheLowRecipe(TheLowMerchantRecipe recipe) {
    add(recipe.toMerchantRecipe());
  }

  @Override
  public MerchantRecipe a(ItemStack paramItemStack1, ItemStack paramItemStack2, int paramInt) {
    for (int i = 0; i < size(); i++) {
      MerchantRecipe localMerchantRecipe2 = (MerchantRecipe) get(i);
      if (ItemStack.c(paramItemStack1, localMerchantRecipe2.getBuyItem1())
          && paramItemStack1.count >= localMerchantRecipe2.getBuyItem1().count
          && ((!localMerchantRecipe2.hasSecondItem()) && (paramItemStack2 == null) ||
              (localMerchantRecipe2.hasSecondItem() && ItemStack.c(paramItemStack2, localMerchantRecipe2.getBuyItem2())
                  && (paramItemStack2.count >= localMerchantRecipe2.getBuyItem2().count)))) {
        TheLowMerchantRecipe recipe = new TheLowMerchantRecipe(localMerchantRecipe2);
        recipe = merchant.getShowResult(recipe);
        if (recipe != null) {
          return recipe.toMerchantRecipe();
        } else {
          return null;
        }
      }
    }
    return null;
  }
}
