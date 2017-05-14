package net.l_bulb.dungeoncore.money.buyer;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.trade.TheLowMerchant;
import net.l_bulb.dungeoncore.common.trade.TheLowMerchantRecipe;
import net.l_bulb.dungeoncore.common.trade.TheLowTrades;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.other.GalionItem;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import net.md_5.bungee.api.ChatColor;

public class Buyer extends TheLowMerchant {

  /**
   * Buyer画面を開く
   *
   * @param p
   */
  public static void open(Player p) {
    Buyer buyer = new Buyer(p);
    TheLowTrades.open(buyer, p);
  }

  public Buyer(Player p) {
    super(p);
  }

  @Override
  protected void onSetItem(InventoryView inv) {
    int galions = 0;

    ItemStack item1 = inv.getItem(0);
    ItemStack item2 = inv.getItem(1);

    if ((item1 == null && item2 == null) || (item1.getType() == Material.AIR && item2.getType() == Material.AIR)) {
      sendRecipeList(new TheLowMerchantRecipe(ItemStackUtil.getItem("売りたいアイテム1", Material.DIAMOND_AXE), ItemStackUtil.getItem("値段",
          Material.GOLD_INGOT)));
      return;
    }

    int price1 = getPrice(item1);

    // Price1が売ることの出来ないアイテムなら何もしない
    if (price1 < 0) {
      sendRecipeList(new TheLowMerchantRecipe(item1, item2, ItemStackUtil.getItem(ChatColor.RED + "このアイテムは売却できません", Material.BARRIER)));
      p.updateInventory();
      return;
    }

    galions = price1;

    TheLowMerchantRecipe newRecipe = null;
    // price2が売ることの出来ないアイテムなら何もしない
    newRecipe = new TheLowMerchantRecipe(item1, GalionItem.getInstance(galions).getItem());
    sendRecipeList(newRecipe);

    p.updateInventory();
  }

  @Override
  public String getName() {
    return "Buyer";
  }

  @Override
  public TheLowMerchantRecipe getShowResult(TheLowMerchantRecipe recipe) {
    if (recipe.getResult() != null && recipe.getResult().getType() == Material.BARRIER) { return null; }
    return recipe;
  }

  /**
   * ItemStackから値段を取得する
   *
   * @param item
   * @return
   */
  public int getPrice(ItemStack item) {
    // nullなら何もしない
    if (item == null) { return -1; }

    ItemInterface customItem = ItemManager.getCustomItem(item);
    if (customItem == null) { return -1; }

    if (customItem.getBuyPrice(item) < 0) {
      return -1;
    } else {
      return customItem.getBuyPrice(item) * item.getAmount();
    }
  }

  @Override
  public List<TheLowMerchantRecipe> getInitRecipes() {
    return Arrays.asList(new TheLowMerchantRecipe(ItemStackUtil.getItem("売りたいアイテム1", Material.DIAMOND_AXE), ItemStackUtil.getItem("値段",
        Material.GOLD_INGOT)));
  }

  @Override
  public void onFinishTrade(TheLowMerchantRecipe recipe) {

  }

}
