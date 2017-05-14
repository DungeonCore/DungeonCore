package net.l_bulb.dungeoncore.player.magicstoneOre.trade;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneOreType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import net.minecraft.server.v1_8_R1.ChatMessage;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.IMerchant;
import net.minecraft.server.v1_8_R1.MerchantRecipe;
import net.minecraft.server.v1_8_R1.MerchantRecipeList;

public class MagicStoneMerchant implements IMerchant {
  EntityHuman e;

  /**
   * 取引完了時
   */
  @Override
  public void a(MerchantRecipe paramMerchantRecipe) {}

  @Override
  public void a_(EntityHuman paramEntityHuman) {
    e = paramEntityHuman;
  }

  static ItemStack diamond = ItemStackUtil.getItem("魔法石", Material.DIAMOND, "ランダムで魔法石を1つ取得する");
  static ItemStack redstone = ItemStackUtil.getItem("魔法石", Material.REDSTONE, "ランダムで魔法石を1つ取得する");
  static ItemStack iron = ItemStackUtil.getItem("魔法石", Material.IRON_INGOT, "ランダムで魔法石を1つ取得する");

  @SuppressWarnings("unchecked")
  @Override
  public MerchantRecipeList getOffers(EntityHuman paramEntityHuman) {
    // ダイヤ鉱石
    MerchantRecipe merchantRecipe1 = new MagicStoneMerchantRecipe(MagicStoneOreType.DIAOMOD_ORE, CraftItemStack.asNMSCopy(diamond));
    // レッドストーン鉱石
    MerchantRecipe merchantRecipe2 = new MagicStoneMerchantRecipe(MagicStoneOreType.REDSTONE_ORE, CraftItemStack.asNMSCopy(redstone));
    // 鉄鉱石
    MerchantRecipe merchantRecipe3 = new MagicStoneMerchantRecipe(MagicStoneOreType.IRON_ORE, CraftItemStack.asNMSCopy(iron));
    // エメラルド鉱石
    MerchantRecipe merchantRecipe4 = new MagicStoneMerchantRecipe(MagicStoneOreType.EMERALD_ORE, CraftItemStack.asNMSCopy(new ItemStack(
        Material.ANVIL)));
    // 金鉱石
    MerchantRecipe merchantRecipe5 = new MagicStoneMerchantRecipe(MagicStoneOreType.GOLD_ORE,
        CraftItemStack.asNMSCopy(new ItemStack(Material.ANVIL)));

    MerchantRecipeList merchantRecipeList = new MagicStoneMerchantRecipeList();
    merchantRecipeList.add(merchantRecipe1);
    merchantRecipeList.add(merchantRecipe2);
    merchantRecipeList.add(merchantRecipe3);
    merchantRecipeList.add(merchantRecipe4);
    merchantRecipeList.add(merchantRecipe5);
    return merchantRecipeList;
  }

  @Override
  public IChatBaseComponent getScoreboardDisplayName() {
    return new ChatMessage("Magic Stone Factory", new Object[0]);
  }

  @Override
  public EntityHuman u_() {
    return e;
  }

  @Override
  public void a_(net.minecraft.server.v1_8_R1.ItemStack itemstack) {
    if (itemstack != null) {
      // 取引アイテム表示
    } else {
      // 取引アイテム非表示
    }
  }
}

class MagicStoneMerchantRecipeList extends MerchantRecipeList {
  private static final long serialVersionUID = 2598462579436583041L;

  /**
   * 取引時, 取得するアイテムをランダムで変更する
   */
  @Override
  public MerchantRecipe
      a(net.minecraft.server.v1_8_R1.ItemStack paramItemStack1, net.minecraft.server.v1_8_R1.ItemStack paramItemStack2, int paramInt) {
    // 設置したアイテムからrecipeを取得
    MagicStoneMerchantRecipe recipe = null;
    for (int i = 0; i < size(); i++) {
      MagicStoneMerchantRecipe localMerchantRecipe2 = (MagicStoneMerchantRecipe) get(i);
      if ((net.minecraft.server.v1_8_R1.ItemStack.c(paramItemStack1, localMerchantRecipe2.getBuyItem1()))
          && (paramItemStack1.count >= localMerchantRecipe2.getBuyItem1().count)
          && (((!localMerchantRecipe2.hasSecondItem()) && (paramItemStack2 == null)) || ((localMerchantRecipe2.hasSecondItem())
              && (net.minecraft.server.v1_8_R1.ItemStack.c(paramItemStack2, localMerchantRecipe2.getBuyItem2()))
              && (paramItemStack2.count >= localMerchantRecipe2
                  .getBuyItem2().count)))) {
        recipe = localMerchantRecipe2;
      }
    }

    // 別なものを置いた時はnullを返す
    if (recipe == null) { return null; }
    return new MagicStoneMerchantRecipe(recipe.getType(), CraftItemStack.asNMSCopy(MagicStoneTradeData.getRandomMagicStoneItem(recipe.getType())));
  }
}
