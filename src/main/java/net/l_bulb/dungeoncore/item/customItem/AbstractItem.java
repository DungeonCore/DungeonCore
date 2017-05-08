package net.l_bulb.dungeoncore.item.customItem;

import java.util.List;

import net.l_bulb.dungeoncore.NbtTagConst;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.itemInterface.Strengthenable;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreData;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.Message;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItem implements ItemInterface {
  @Override
  public boolean isThisItem(ItemStack item) {
    if (ItemStackUtil.isEmpty(item)) { return false; }
    return getId().equals(ItemStackUtil.getId(item));
  }

  @Override
  public boolean isShowItemList() {
    return true;
  }

  boolean isStrength = false;
  {
    isStrength = this instanceof Strengthenable;
  }

  @Override
  public boolean isStrengthItem() {
    return isStrength;
  }

  @Override
  public ItemStack getItem() {
    ItemStack itemStack = getItemStackBase();

    // アイテム名
    ItemStackUtil.setDispName(itemStack, ChatColor.RESET + getItemName());

    // アイテムの説明
    List<String> lore = ItemStackUtil.getLore(itemStack);

    // id付与
    lore.add(ChatColor.DARK_GRAY + ItemStackUtil.getLoreForIdLine(getId()));
    ItemStackUtil.setNBTTag(itemStack, NbtTagConst.THELOW_ITEM_ID, getId());

    if (getDetail() != null) {
      for (String string : getDetail()) {
        lore.add(ChatColor.AQUA + string);
      }
    }

    // IDを付与

    // Loreを生成
    ItemLoreData itemLoreData = new ItemLoreData();
    itemLoreData.setBefore(lore);

    // スタンダートLoreTokenを取得
    itemLoreData.addLore(getStandardLoreToken());

    // if (isStrengthItem()) {
    // itemLoreData.addLore(StrengthOperator.getStrengthLoreToken((Strengthenable) this, 0));
    // }

    ItemStackUtil.setLore(itemStack, itemLoreData.getLore());

    StrengthOperator.updateLore(itemStack, 0);
    return itemStack;
  }

  /**
   * 基本性能のLoreTokenを取得する
   * 
   * @return
   */
  public ItemLoreToken getStandardLoreToken() {
    ItemLoreToken loreToken = new ItemLoreToken(ItemLoreToken.TITLE_STANDARD);
    if (this instanceof Strengthenable) {
      // 最大強化
      if (((Strengthenable) this).getMaxStrengthCount() != 0) {
        loreToken.addLore(Message.getMessage("最大強化 ： {1}+{0}", ((Strengthenable) this).getMaxStrengthCount(), ChatColor.GOLD));
      }
    }
    return loreToken;
  }

  protected ItemStack getItemStackBase() {
    ItemStack itemStack = new ItemStack(getMaterial());
    return itemStack;
  }

  abstract protected Material getMaterial();

  public abstract String[] getDetail();

  @Override
  public boolean equals(Object paramObject) {
    if (paramObject instanceof ItemInterface) { return getItemName().equals(((ItemInterface) paramObject).getItemName()); }
    return false;
  }

  @Override
  public int hashCode() {
    return getItemName().hashCode();
  }

  @Override
  public ItemType getAttackType() {
    return ItemType.IGNORE;
  }

  @Override
  public String getSimpleName() {
    return ChatColor.stripColor(getItemName());
  }

  @Override
  public boolean isQuestItem() {
    return false;
  }

  @Override
  public String toString() {
    return getItemName();
  }

}
