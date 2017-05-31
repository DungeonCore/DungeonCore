package net.l_bulb.dungeoncore.item.nbttag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.StrBuilder;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.SlotManager;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.item.slot.SlotType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class ItemStackNbttagSetter {
  private ItemStack item;

  public ItemStackNbttagSetter(ItemStack item) {
    this.item = item;
  }

  public void setMaxSlotSize(int maxSlotSize) {
    ItemStackUtil.setNBTTag(item, "thelow_item_max_slot", maxSlotSize);
  }

  public void setSlotList(List<SlotInterface> slotList) {
    StrBuilder sb = new StrBuilder();
    // IDで連結する
    slotList.stream()
        .filter(val -> val != null)
        .peek(val -> {
          sb.appendSeparator(",");
          sb.append(val.getId());
        });
    ItemStackUtil.setNBTTag(item, "thelow_item_slot_list", sb.toString());
  }

  public void setDefaultSlotSize(int defaultSlotSize) {
    ItemStackUtil.setNBTTag(item, "thelow_item_default_slot", defaultSlotSize);
  }

  public void setDamage(double damage) {
    ItemStackUtil.setNBTTag(item, "thelow_item_damage", damage);
  }

  public void setMaxDurability(short maxDurability) {
    ItemStackUtil.setNBTTag(item, "thelow_max_durability", maxDurability);
  }

  public void setNowDurability(short nowDurability) {
    ItemStackUtil.setNBTTag(item, "thelow_now_durability", nowDurability);
  }

  public void setItemId(String itemId) {
    ItemStackUtil.setNBTTag(item, "thelow_item_id", itemId);
  }

  public String getItemId() {
    return ItemStackUtil.getNBTTag(item, "thelow_item_id");
  }

  public static String getItemId(ItemStack item) {
    return ItemStackUtil.getNBTTag(item, "thelow_item_id");
  }

  public static void setItemId(ItemStack item, String itemId) {
    ItemStackUtil.setNBTTag(item, "thelow_item_id", itemId);
  }

  public short getMaxDurability() {
    return ItemStackUtil.getNBTTagShort(item, "thelow_max_durability");
  }

  public short getNowDurability() {
    return ItemStackUtil.getNBTTagShort(item, "thelow_now_durability");
  }

  public double getDamage() {
    return ItemStackUtil.getNBTTagDouble(item, "thelow_item_damage");
  }

  public int getMaxSlotSize() {
    return ItemStackUtil.getNBTTagShort(item, "thelow_item_max_slot");
  }

  public int getDefaultSlotSize() {
    return ItemStackUtil.getNBTTagShort(item, "thelow_item_default_slot");
  }

  /**
   * すべての装着されているSlotを取得
   *
   * @return
   */
  public List<SlotInterface> getGetAllSlotList() {
    String nbtTag = ItemStackUtil.getNBTTag(item, "thelow_item_slot_list");
    // 設定されていないなら空のリストを返す
    if (nbtTag == null) { return new ArrayList<>(); }

    // IDをSlotに変換
    List<SlotInterface> collect = Arrays.stream(nbtTag.split(","))
        .map(id -> SlotManager.getSlotByID(id))
        .filter(slot -> slot != null)
        .collect(Collectors.toList());
    return collect;
  }

  /**
   * 指定された装着されているスロットを取得
   *
   * @param type
   * @return
   */
  public List<SlotInterface> getGetAllSlotList(SlotType type) {
    String nbtTag = ItemStackUtil.getNBTTag(item, "thelow_item_slot_list");
    // 設定されていないなら空のリストを返す
    if (nbtTag == null) { return new ArrayList<>(); }

    // IDをSlotに変換
    List<SlotInterface> collect = Arrays.stream(nbtTag.split(","))
        .map(id -> SlotManager.getSlotByID(id))
        .filter(slot -> slot != null && slot.getSlotType() == type)
        .collect(Collectors.toList());
    return collect;
  }
}
