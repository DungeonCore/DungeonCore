package lbn;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lbn.item.ItemManager;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class Config {

  /**
   * Author's twitter account.
   */
  public static final String DEVELOPER_TWITTER_ID = "@namiken1993";

  private static Set<Material> damageIgnoredBlocks = new HashSet<>();
  private static Set<Material> clickIgnoredBlocks = new HashSet<>();
  private static Set<Material> clickCancelledItems = new HashSet<>();
  private static Set<EntityType> clickCancelledEntityTypes = new HashSet<>();
  private static Set<EntityType> damageCancelledEntityType = new HashSet<>();
  static {
    clickIgnoredBlocks.add(Material.ANVIL);
    clickIgnoredBlocks.add(Material.ENCHANTMENT_TABLE);
    clickIgnoredBlocks.add(Material.PAINTING);
    clickIgnoredBlocks.add(Material.ITEM_FRAME);
    clickIgnoredBlocks.add(Material.FURNACE);
    clickIgnoredBlocks.add(Material.BURNING_FURNACE);
    clickIgnoredBlocks.add(Material.DISPENSER);
    clickIgnoredBlocks.add(Material.DROPPER);
    clickIgnoredBlocks.add(Material.BEACON);
    clickIgnoredBlocks.add(Material.HOPPER);
    clickIgnoredBlocks.add(Material.HOPPER_MINECART);
    clickIgnoredBlocks.add(Material.FIRE);

    damageIgnoredBlocks.add(Material.DIAMOND_ORE);
    damageIgnoredBlocks.add(Material.IRON_ORE);
    damageIgnoredBlocks.add(Material.GOLD_ORE);
    damageIgnoredBlocks.add(Material.REDSTONE_ORE);
    damageIgnoredBlocks.add(Material.LAPIS_ORE);
    damageIgnoredBlocks.add(Material.EMERALD_ORE);
    damageIgnoredBlocks.add(Material.COAL_ORE);

    clickCancelledItems.add(Material.EYE_OF_ENDER);
    clickCancelledItems.add(Material.ENDER_PEARL);
    clickCancelledItems.add(Material.BUCKET);
    clickCancelledItems.add(Material.LAVA_BUCKET);
    clickCancelledItems.add(Material.WATER_BUCKET);
    clickCancelledItems.add(Material.FLINT_AND_STEEL);

    clickCancelledEntityTypes.add(EntityType.ITEM_FRAME);
    damageCancelledEntityType.add(EntityType.ITEM_FRAME);
  }

  public static Set<Material> getClickCancelblocks() {
    return clickIgnoredBlocks;
  }

  public static Set<Material> getClickCancelItems() {
    return clickCancelledItems;
  }

  /**
   * ブロックが殴られるのを許可するブロックの種類を取得
   * @return
   */
  public static Set<Material> getDamageAllowBlock() {
	  return damageIgnoredBlocks;
  }

  public static Set<EntityType> getClickCancelEntityTypes() {
    return clickCancelledEntityTypes;
  }

  public static Set<EntityType> getDamageCancelEntityTypes() {
    return damageCancelledEntityType;
  }

  public static boolean allowCraft(ItemStack result, Collection<ItemStack> materials) {
    if (ItemManager.getCustomItem(result) != null) {
      return true;
    }
    return false;
  }

}
