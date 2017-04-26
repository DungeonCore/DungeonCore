package lbn.item.system.strength;

import java.util.ArrayList;
import java.util.List;

import lbn.api.player.TheLowPlayer;
import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.common.trade.TheLowMerchant;
import lbn.common.trade.TheLowMerchantRecipe;
import lbn.item.customItem.other.StrengthScrollArmor;
import lbn.item.customItem.other.StrengthScrollWeapon;
import lbn.money.GalionEditReason;
import lbn.util.ItemStackUtil;
import lbn.util.JavaUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class StrengthMerchant extends TheLowMerchant {
  public StrengthMerchant(Player p, TheLowPlayer thelowPlayer) {
    super(p);
    this.theLowPlayer = thelowPlayer;
  }

  TheLowPlayer theLowPlayer;

  StrengthData strengthData = null;

  // 武器の強化スクロール
  private static final ItemStack WEAPON_SCROLL = new StrengthScrollWeapon().getItem();
  // 防具の強化スクロール
  private static final ItemStack ARMOR_SCROLL = new StrengthScrollArmor().getItem();

  @Override
  protected void onSetItem(InventoryView inv) {
    Inventory topInventory = inv.getTopInventory();

    // 強化データをセットする
    if (strengthData == null) {
      strengthData = new StrengthData(topInventory.getItem(0));
    } else {
      strengthData.setItem1(topInventory.getItem(0));
    }

    // 表示するレシピを取得する
    ItemStack dispItem1 = topInventory.getItem(0) == null ? null : topInventory.getItem(0).clone();
    ItemStack dispItem2 = topInventory.getItem(1) == null ? null : topInventory.getItem(1).clone();
    MerchantRecipeCreator merchantRecipeCreator = new MerchantRecipeCreator(dispItem1, dispItem2, theLowPlayer, strengthData);
    List<TheLowMerchantRecipe> strengthItemRecipe = merchantRecipeCreator.getStrengthItemRecipe();
    if (strengthItemRecipe == null) {
      sendRecipeList(getInitRecipes());
    } else {
      sendRecipeList(strengthItemRecipe);
    }

    p.updateInventory();
  }

  @Override
  public String getName() {
    return "アイテム強化";
  }

  @Override
  public TheLowMerchantRecipe getShowResult(TheLowMerchantRecipe recipe) {
    if (!strengthData.isCanStrength()) { return null; }

    // 念のため、バリアブロックならTRUE
    if (recipe.getResult() != null && recipe.getResult().getType() == Material.BARRIER) { return null; }

    // 成功率
    int successChance = strengthData.getSuccessChance();
    boolean isSuccess = JavaUtil.isRandomTrue(successChance);
    strengthData.setSuccessStrength(isSuccess);

    ItemStack result2 = recipe.getBuy1();
    int nextLevel = StrengthOperator.getLevel(result2) + 1;
    if (isSuccess) {
      // 成功
      StrengthOperator.updateLore(result2, nextLevel);
    } else {
      // 失敗
      StrengthOperator.updateLore(result2, 0);
    }

    PlayerSetStrengthItemResultEvent event = new PlayerSetStrengthItemResultEvent(p, result2, nextLevel, isSuccess);
    Bukkit.getPluginManager().callEvent(event);

    recipe.setResult(event.getItem());

    return recipe;
  }

  @Override
  public List<TheLowMerchantRecipe> getInitRecipes() {
    ArrayList<TheLowMerchantRecipe> recipes = new ArrayList<TheLowMerchantRecipe>();
    recipes.add(new TheLowMerchantRecipe(ItemStackUtil.getItem("強化したい武器", Material.IRON_SWORD, "強化する武器を置いてください"),
        WEAPON_SCROLL, ItemStackUtil.getItem("強化された武器", Material.DIAMOND_SWORD)));
    recipes.add(new TheLowMerchantRecipe(ItemStackUtil.getItem("強化したい防具", Material.IRON_CHESTPLATE, "強化する防具を置いてください"),
        ARMOR_SCROLL, ItemStackUtil.getItem("強化された防具", Material.DIAMOND_CHESTPLATE)));
    return recipes;
  }

  @Override
  public void onFinishTrade(TheLowMerchantRecipe recipe) {
    // お金を消費する
    theLowPlayer.setGalions(theLowPlayer.getGalions() - strengthData.getNeedMoney(), GalionEditReason.consume_strength);
    // Eventを発生させる
    PlayerStrengthFinishEvent playerStrengthFinishEvent = new PlayerStrengthFinishEvent(getPlayer(), strengthData.getNextLevel(), recipe.getResult(),
        strengthData.isSuccessStrength());
    Bukkit.getPluginManager().callEvent(playerStrengthFinishEvent);
  }
}
