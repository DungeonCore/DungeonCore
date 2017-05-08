package net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem;

import java.util.Arrays;
import java.util.List;

import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpreadSheetOtherItem extends AbstractItem {
  public SpreadSheetOtherItem(String name, String id, int price,
      String command, String detail) {
    super();
    this.name = name;
    this.id = id;
    this.price = price;
    this.command = command;

    ItemStack itemStackByCommand = ItemStackUtil.getItemStackByCommand(command);
    m = itemStackByCommand.getType();

    if (JavaUtil.getNull(detail, "").equals("")) {
      lore = ItemStackUtil.getLore(itemStackByCommand);
    } else {
      lore = Arrays.asList(detail.split(","));
    }
  }

  String name;
  String id;
  int price;
  Material m;
  List<String> lore;
  String command;

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
    return lore.toArray(new String[0]);
  }
}
