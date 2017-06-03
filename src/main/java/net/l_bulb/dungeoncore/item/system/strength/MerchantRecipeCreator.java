package net.l_bulb.dungeoncore.item.system.strength;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.common.trade.TheLowMerchantRecipe;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.Strengthenable;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreData;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class MerchantRecipeCreator {
  private static final ItemStack OTHER = new ItemStack(Material.AIR);

  /**
   * @param item1 Playerが置いたアイテム1
   * @param item2 Playerが置いたアイテム2
   * @param player
   * @param strengthData
   */
  public MerchantRecipeCreator(ItemStack item1, ItemStack item2, TheLowPlayer player, StrengthData strengthData) {
    this.strengthData = strengthData;
    this.player = player;

    if (item1 != null) {
      // 強化後のレベル
      this.item1 = item1.clone();
      nextLevel = StrengthOperator.getLevel(item1) + 1;
    }

    if (item2 != null) {
      this.item2 = item2.clone();
      // 数を設定 もし素材がnullなら1
      this.item2.setAmount(Optional.ofNullable(strengthData.getMaterial()).orElse(OTHER).getAmount());
    }
  }

  /** 置いたアイテム1 */
  ItemStack item1;
  /** 置いたアイテム2 */
  ItemStack item2;
  TheLowPlayer player;

  // 強化の情報
  StrengthData strengthData;

  int nextLevel;

  public List<TheLowMerchantRecipe> getStrengthItemRecipe() {
    // 強化できるアイテムが置かれてないなら何もしない
    Strengthenable customItem = ItemManager.getCustomItem(Strengthenable.class, item1);
    if (customItem == null) {
      strengthData.setCanStrength(false);
      return null;
    }

    // 最大レベルに達しているかどうか
    boolean isNotMaxLevel = customItem.getMaxStrengthCount() >= nextLevel;
    if (!isNotMaxLevel) {
      strengthData.setCanStrength(false);
      return Arrays
          .asList(new TheLowMerchantRecipe(item1, item2, ItemStackUtil.getItem("これ以上強化出来ません", Material.BARRIER, "最大レベルに達してるため", "これ以上強化出来ません")));
    }

    // お金が足りるか確認
    boolean isSufficientMoney = player.getGalions() >= strengthData.getNeedMoney();
    // 素材が足りるか確認
    boolean isSufficientMaterial = isSufficientMaterial(item2, player, nextLevel);

    // 強化できるかどうかセットする
    strengthData.setCanStrength(isSufficientMoney && isSufficientMaterial);

    // 従来の強化のレシピ
    TheLowMerchantRecipe recipe1 = new TheLowMerchantRecipe(item1,
        strengthData.getMaterial(), StrengthOperator.getItem(item1.clone(), nextLevel));

    // 強化できるかどうかの情報をセットする(チェスト)
    ItemStack recipe2Result = getShowResult(isSufficientMoney, isSufficientMaterial);
    TheLowMerchantRecipe recipe2 = new TheLowMerchantRecipe(item1, strengthData.getMaterial(), recipe2Result);

    // 素材が違っている かつ 素材が置かれていないなら本来のレシピを表示
    if (!isSufficientMaterial && ItemStackUtil.isEmpty(item2)) {
      return Arrays.asList(recipe1, recipe2);
    } else {
      return Arrays.asList(recipe2, recipe1);
    }
  }

  private boolean isSufficientMaterial(ItemStack item2, TheLowPlayer player, int nextLevel) {
    ItemStack strengthMaterials = strengthData.getMaterial();
    // 強化素材がいらない かつ 強化素材がセットされていないならTRUE
    if (ItemStackUtil.isEmpty(strengthMaterials) && ItemStackUtil.isEmpty(item2)) { return true; }

    // TODO ID比較にする
    if (strengthMaterials.isSimilar(item2) && strengthMaterials.getAmount() <= item2.getAmount()) { return true; }
    return false;
  }

  /**
   * 実際に結果欄に表示されるアイテムを表示
   *
   * @return
   */
  private ItemStack getShowResult(boolean isSufficientMoney, boolean isSufficientMaterial) {
    boolean isError = !isSufficientMaterial || !isSufficientMoney;

    Material m = isError ? Material.BARRIER : Material.CHEST;

    ItemLoreData itemLoreData = new ItemLoreData();

    // 強化のステータスを表示する
    ItemLoreToken strengthInfo = new ItemLoreToken("強化ステータス");
    if (!isError) {
      strengthInfo.addLore("強化出来ます");
    } else {
      if (!isSufficientMaterial) {
        strengthInfo.addLore("強化素材が足りません", ChatColor.RED);
      }
      if (!isSufficientMoney) {
        strengthInfo.addLore("お金が足りません", ChatColor.RED);
      }
    }
    itemLoreData.addLore(strengthInfo);

    // 強化に必要なお金などを表示する
    ItemLoreToken needItem = new ItemLoreToken("強化情報");
    ItemStack strengthMaterials = strengthData.getMaterial();
    if (!ItemStackUtil.isEmpty(strengthMaterials)) {
      needItem.addLore(ItemStackUtil.getName(strengthMaterials) + "   × " + strengthMaterials.getAmount() + "個");
    }
    needItem.addLore(strengthData.getNeedMoney() + "ガリオン");
    needItem.addLore(strengthData.getSuccessChance() + "%の確率で成功");
    itemLoreData.addLore(needItem);

    // 所持金を表示
    itemLoreData.addAfter(ChatColor.WHITE + "所持金:" + player.getGalions() + "ガリオン");

    ItemStack item = ItemStackUtil.getItem("装備強化", m);
    ItemStackUtil.setLore(item, itemLoreData.getLore());

    return item;
  }

}
