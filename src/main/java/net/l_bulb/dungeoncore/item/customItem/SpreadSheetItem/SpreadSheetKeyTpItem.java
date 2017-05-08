package net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.l_bulb.dungeoncore.item.customItem.key.AbstractTeleportKey;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpreadSheetKeyTpItem extends AbstractTeleportKey {

  public SpreadSheetKeyTpItem(String name, String id, int price,
      String command, String dungeon, Location dungeonLoc, Location data) {
    super(data);
    this.name = name;
    this.id = id;
    this.price = price;
    this.command = command;
    this.dungeon = dungeon;
    this.dungeonLoc = dungeonLoc;

    ItemStack itemStackByCommand = ItemStackUtil.getItemStackByCommand(command);
    m = itemStackByCommand.getType();
    lore = ItemStackUtil.getLore(itemStackByCommand);
  }

  String name;
  String id;
  int price;
  Material m;
  List<String> lore;
  String command;
  String dungeon;
  Location dungeonLoc;

  @Override
  public String getItemName() {
    return name;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  protected String getDungeonName() {
    return dungeon;
  }

  @Override
  protected Location getDungeonLocation() {
    return dungeonLoc;
  }

  @Override
  protected ItemStack getItemStackBase() {
    ItemStack itemStack = ItemStackUtil.getItemStackByCommand(command);
    return itemStack;
  }

  @Override
  protected Material getMaterial() {
    return m;
  }

  @Override
  public String[] getDetail() {
    String[] detail = super.getDetail();
    if (detail == null) {
      return lore.toArray(new String[0]);
    } else {
      ArrayList<String> loreList = new ArrayList<>(lore);
      loreList.addAll(Arrays.asList(detail));
      return loreList.toArray(new String[0]);
    }
  }
}
