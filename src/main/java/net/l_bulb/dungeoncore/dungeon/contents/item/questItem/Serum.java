package net.l_bulb.dungeoncore.dungeon.contents.item.questItem;

import net.l_bulb.dungeoncore.item.customItem.AbstractItem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class Serum extends AbstractItem {

  @Override
  public String getItemName() {
    return "血清";
  }

  @Override
  public Material getMaterial() {
    return Material.INK_SACK;
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemStack getItem() {
    ItemStack item = super.getItem();
    MaterialData data = item.getData();
    data.setData((byte) 1);
    item.setDurability((short) 1);
    return item;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "モンスターの血清", "調剤師によって回復薬が作られる。" };
  }

  @Override
  public boolean isQuestItem() {
    return true;
  }

  static Serum serum = new Serum();

  static public Serum getInstance() {
    return serum;
  }

  @Override
  public String getId() {
    return "serum";
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 0;
  }

  @Override
  public boolean isShowItemList() {
    return false;
  }
}
