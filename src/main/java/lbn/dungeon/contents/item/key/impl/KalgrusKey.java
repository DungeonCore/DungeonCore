package lbn.dungeon.contents.item.key.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import lbn.dungeon.contents.item.key.AbstractKeyItem;
import lbn.item.itemInterface.GettingItemable;
import lbn.util.ItemStackUtil;

public class KalgrusKey extends AbstractKeyItem implements GettingItemable {

  @Override
  public String getItemName() {
    return "Kalgrus Key";
  }

  @Override
  protected Material getMaterial() {
    return Material.QUARTZ;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "CLICK 7 POINT IN WATER", "水の中にある7つのポイントをクリックせよ" };
  }

  protected void updateKey(ItemStack item, Location signLoc, Player player) {
    List<String> loreList = ItemStackUtil.getLore(item);

    String signLogStr = StringUtils
        .join(new Object[] { "@", (int) signLoc.getBlockX(), ",", (int) signLoc.getBlockY(), ",", (int) signLoc.getBlockZ() });

    if (loreList.contains(signLogStr)) {
      player.sendMessage(ChatColor.RED + "すでに登録済みです。");
      return;
    }

    int count = 0;
    for (String lore : loreList) {
      if (lore.contains("@")) {
        count++;
      }
    }

    player.sendMessage(ChatColor.GREEN + "この地点をキーに登録しました。");

    PlayerInventory inventory = player.getInventory();
    // 最後のキーを渡す
    if (count >= 6) {
      // それ以外のキーを全て削除する
      ItemStackUtil.removeAll(inventory, getItem());
      inventory.setItemInHand(new KalgrusFinishKey().getItem());
      player.updateInventory();
    } else {
      item.setAmount(1);
      loreList.add(signLogStr);
      ItemStackUtil.setLore(item, loreList);
      inventory.setItemInHand(item);
    }
    player.updateInventory();
  }

  @Override
  public void onClick(PlayerInteractEvent e, String[] lines, ItemStack item) {
    updateKey(item, e.getClickedBlock().getLocation(), e.getPlayer());
  }

  @Override
  public String getLastLine(Player p, String[] params) {
    return "";
  }

  @Override
  public void onClickForGetting(PlayerInteractEvent e, String[] lines) {
    Player player = e.getPlayer();
    if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
      ItemStackUtil.removeAll(player.getInventory(), getItem());
      player.setItemInHand(getItem());
      player.updateInventory();
    } else {
      player.sendMessage(ChatColor.RED + "何も持たないでクリックしてください。");
    }
  }

  @Override
  protected String getDungeonName() {
    return null;
  }

  @Override
  protected Location getDungeonLocation() {
    return null;
  }
}
