package net.l_bulb.dungeoncore.dungeon.contents.item.key.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.l_bulb.dungeoncore.dungeon.contents.item.key.AbstractKeyItem;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class SpreadSheetMultiPushKey extends AbstractKeyItem implements ItemInterface {

  private String name;
  private String id;
  private int price;
  private String command;
  private String dungeon;
  private Location dungeonLoc;
  private int count;
  private Material m;
  private ItemInterface customItemById;
  private String[] detailLineList;

  public SpreadSheetMultiPushKey(String name, String id, int price,
      String command, String dungeon, Location dungeonLoc, String data, String detail) {
    this.name = name;
    this.id = id;
    this.price = price;
    this.command = command;
    this.dungeon = dungeon;
    this.dungeonLoc = dungeonLoc;
    this.count = JavaUtil.getInt(data.split(":")[0], -1);

    customItemById = ItemManager.getCustomItemById(data.split(":")[1]);
    detailLineList = Optional.ofNullable(detail).map(d -> d.split(",")).orElse(new String[0]);

    ItemStack itemStackByCommand = ItemStackUtil.getItemStackByCommand(command);
    m = itemStackByCommand.getType();
  }

  /**
   * 指定したアイテムがnullならエラー。countが-1ならエラー
   *
   * @return
   */
  public boolean isError() {
    return customItemById == null || count == -1;
  }

  @Override
  public String getItemName() {
    return name;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return price;
  }

  @Override
  protected Material getMaterial() {
    return m;
  }

  @Override
  public String[] getDetail() {
    ArrayList<String> detailList = new ArrayList<>();
    if (detailLineList.length != 0) {
      for (String line : detailLineList) {
        detailList.add(ChatColor.AQUA + line);
      }
      detailList.add("");
    }
    detailList.add(MessageFormat.format("{0}{1}つのポイントをクリックせよ", ChatColor.GREEN, count));
    detailList.add("");
    detailList.addAll(Arrays.asList(super.getDetail()));
    return detailList.toArray(new String[detailList.size()]);
  }

  @Override
  protected ItemStack getItemStackBase() {
    return ItemStackUtil.getItemStackByCommand(command);
  }

  /**
   * 鍵を更新する
   *
   * @param item
   * @param signLoc
   * @param player
   */
  protected void updateKey(ItemStack item, Location signLoc, Player player) {
    List<String> loreList = ItemStackUtil.getLore(item);

    String signLogStr = StringUtils
        .join(new Object[] { "@", (int) signLoc.getBlockX(), ",", (int) signLoc.getBlockY(), ",", (int) signLoc.getBlockZ() });

    if (loreList.contains(signLogStr)) {
      player.sendMessage(ChatColor.RED + "すでに登録済みです。");
      return;
    }

    PlayerInventory inventory = player.getInventory();
    // Loreを更新
    item.setAmount(1);
    loreList.add(signLogStr);
    ItemStackUtil.setLore(item, loreList);
    inventory.setItemInHand(item);

    int count = 0;
    for (String lore : loreList) {
      if (lore.contains("@")) {
        count++;
      }
    }

    player.sendMessage(ChatColor.GREEN + "この地点をキーに登録しました。");

    // 最後のキーを渡す
    if (count >= this.count) {
      // それ以外のキーを全て削除する
      ItemStackUtil.removeAll(inventory, getItem());
      inventory.setItemInHand(customItemById.getItem());
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
  protected String getDungeonName() {
    return dungeon;
  }

  @Override
  protected Location getDungeonLocation() {
    return dungeonLoc;
  }
}
