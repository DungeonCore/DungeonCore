package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.item.system.craft.TheLowCraftRecipeInterface;

public class PlayerCraftCustomItemEvent extends TheLowPlayerEvent {

  private TheLowCraftRecipeInterface thelowRecipe;
  private ItemStack craftedItem;

  public PlayerCraftCustomItemEvent(TheLowPlayer player, TheLowCraftRecipeInterface thelowRecipe, ItemStack craftedItem) {
    super(player);
    this.thelowRecipe = thelowRecipe;
    this.craftedItem = craftedItem;
  }

  private static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  /**
   * クラフトに使用したレシピを取得
   *
   * @return
   */
  public TheLowCraftRecipeInterface getCraftRecipe() {
    return thelowRecipe;
  }

  /**
   * クラフトして実際に手に入れたアイテム
   *
   * @return
   */
  public ItemStack getCraftedItem() {
    return craftedItem;
  }
}
