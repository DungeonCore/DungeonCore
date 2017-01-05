package lbn.item;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import lbn.util.ItemStackUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class GalionItem extends AbstractItem {

  /**
   * Instance cache
   */
  private static Map<Integer, GalionItem> cache = new WeakHashMap<>();

  /**
   * Get instance of GalionItem.
   *
   * @param galions
   *          value
   * @return instance
   */
  public static GalionItem getInstance(int galions) {
    Integer key = Integer.valueOf(galions);
    if (cache.containsKey(key)) {
      return cache.get(key);
    }
    GalionItem item = new GalionItem(galions);
    cache.put(key, item);
    return item;
  }

  private GalionItem(int galions) {
    this.galions = galions;
  }

  public GalionItem(ItemStack stack) {
    List<String> lore = ItemStackUtil.getLore(stack);
    for (String string : lore) {
      if (string.contains("+") && string.contains("galions")) {
        String replace = string.replace("+", "").replace("galions", "");
        this.galions = Integer.parseInt(ChatColor.stripColor(replace).trim());
      }
    }
  }

  /**
   * Immutable
   */
  private int galions = 0;

  @Override
  public String getItemName() {
    return ChatColor.GOLD + "Money";
  }

  @Override
  public String getId() {
    return "galions";
  }

  @Override
  protected Material getMaterial() {
    return Material.GOLD_INGOT;
  }

  @Override
  protected String[] getDetail() {
    return new String[] { "+ " + galions + " galions" };
  }

  public int getGalions() {
    return galions;
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return getGalions();
  }

}
