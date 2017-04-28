package net.l_bulb.dungeoncore.item.customItem.attackitem.old;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.menu.MenuSelector;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.common.menu.SelectRunnable;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.AvailableLevelItemable;
import net.l_bulb.dungeoncore.item.itemInterface.LeftClickItemable;
import net.l_bulb.dungeoncore.item.itemInterface.RightClickItemable;
import net.l_bulb.dungeoncore.item.itemInterface.SpecialAttackItemable;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.Message;

public class SpecialAttackItemSelectorOld extends AbstractItem implements RightClickItemable, LeftClickItemable {

  SpecialAttackItemable specialItem;

  public SpecialAttackItemSelectorOld(SpecialAttackItemable specialItem) {
    this.specialItem = specialItem;

    MenuSelector menuSelecor = new MenuSelector(specialItem.getId() + " selector");

    // レベルごとにソートする
    ArrayList<ItemInterface> allItem = new ArrayList<ItemInterface>(specialItem.getAllItem());
    Collections.sort(allItem, new Comparator<ItemInterface>() {
      @Override
      public int compare(ItemInterface o1, ItemInterface o2) {
        if (o1 instanceof AvailableLevelItemable && o2 instanceof AvailableLevelItemable) {
          if (((AvailableLevelItemable) o1).getAvailableLevel() != ((AvailableLevelItemable) o2)
              .getAvailableLevel()) { return ((AvailableLevelItemable) o1).getAvailableLevel() - ((AvailableLevelItemable) o2).getAvailableLevel(); }
        }
        return o1.getId().compareTo(o2.getId());
      }
    });

    int i = 0;
    // アイテムを登録
    for (ItemInterface itemStack : allItem) {
      if (itemStack instanceof AvailableLevelItemable && ((AvailableLevelItemable) itemStack).getAvailableLevel() > 80) {
        continue;
      }
      menuSelecor.addMenu(itemStack.getItem(), i, new SelectRunnable() {
        @Override
        public void run(Player p, ItemStack item) {
          ItemStack itemInHand = p.getItemInHand();
          // 手に持っているのがこのアイテムでないなら何もしない
          if (!isThisItem(itemInHand)) { return; }
          if (itemInHand.getAmount() != 1) {
            Message.sendMessage(p, "スタックしない状態でクリックしてください。");
            return;
          }
          p.setItemInHand(itemStack.getItem());
          p.closeInventory();
        }
      });
      i++;
    }

    menuSelecor.regist();
  }

  @SuppressWarnings("unchecked")
  @Override
  public String getItemName() {
    return StringUtils.join(ChatColor.BOLD, ChatColor.DARK_GREEN, "アイテムセレクター [", ChatColor.DARK_AQUA, specialItem.getSpecialName(),
        ChatColor.DARK_GREEN, "]");
  }

  @Override
  public String getId() {
    return specialItem.getId() + " selector";
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 200 * specialItem.getRank();
  }

  @Override
  protected Material getMaterial() {
    switch (specialItem.getAttackType()) {
      case SWORD:
        return Material.DIAMOND_SWORD;
      case BOW:
        return Material.BOW;
      case MAGIC:
        return Material.BLAZE_ROD;
      default:
        break;
    }
    return Material.BOOK;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "右クリックして欲しいアイテムを選択してください" };
  }

  @Override
  public boolean isShowItemList() {
    return false;
  }

  @Override
  public ItemLoreToken getStandardLoreToken() {
    ItemLoreToken loreToken = super.getStandardLoreToken();
    loreToken.addLore(Message.getMessage("レア度：{0}", getRarityStart()));
    loreToken.addLore(Message.getMessage("タイプ：{0}", specialItem.getAttackType()));
    return loreToken;
  }

  protected String getRarityStart() {
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= specialItem.getRank(); i++) {
      sb.append("★");
    }
    return sb.toString();
  }

  @Override
  public void excuteOnRightClick(PlayerInteractEvent e) {
    MenuSelectorManager.open(e.getPlayer(), specialItem.getId() + " selector");
  }

  @Override
  public ItemType getAttackType() {
    return specialItem.getAttackType();
  }

  @Override
  public void excuteOnLeftClick(PlayerInteractEvent e) {
    MenuSelectorManager.open(e.getPlayer(), specialItem.getId() + " selector");
  }

}
