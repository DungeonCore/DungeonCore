package net.l_bulb.dungeoncore.item.customItem.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.MoneyItemable;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public final class GalionItem extends AbstractItem implements MoneyItemable {

  /**
   * Get instance of GalionItem.
   *
   * @param galions
   *          value
   * @return instance
   */
  public static GalionItem getInstance(int galions) {
    return new GalionItem(galions);
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
  public String[] getDetail() {
    return new String[] { "+ " + galions + " galions" };
  }

  public int getGalions() {
    return galions;
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return getGalions();
  }

  /**
   * 金のインゴットを消してお金を加算させる
   * 
   * @param player
   */
  @Override
  public void applyGalionItem(Player player) {
    if (player.getGameMode() == GameMode.CREATIVE) { return; }

    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
    if (theLowPlayer == null) { return; }

    new BukkitRunnable() {
      @Override
      public void run() {
        HashMap<Integer, ? extends ItemStack> all = player.getInventory().all(getMaterial());
        ArrayList<Integer> indexList = new ArrayList<>();
        for (Entry<Integer, ? extends ItemStack> entry : all.entrySet()) {
          if (isThisItem(entry.getValue())) {
            int galions = new GalionItem(entry.getValue()).getGalions();
            indexList.add(entry.getKey());
            theLowPlayer.addGalions(galions * entry.getValue().getAmount(), GalionEditReason.get_money_item);
          }
        }
        for (Integer integer : indexList) {
          player.getInventory().clear(integer);
        }
      }
    }.runTaskLater(Main.plugin, 2);
  }

}
