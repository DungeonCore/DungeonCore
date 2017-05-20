package net.l_bulb.dungeoncore.chest.wireless;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorInterface;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.Message;

public class RepositoryBuyMenu implements MenuSelectorInterface {
  static {
    MenuSelectorManager.regist(new RepositoryBuyMenu());
  }

  private static final String TITLE = "倉庫購入";
  private static final String REPOSITORY_TYPE = "REPOSITORY_TYPE";

  static WireLessChestManager instance = WireLessChestManager.getInstance();

  public static void open(Player player, RepositoryType type) {
    ItemStack button1 = ItemStackUtil.getItem("倉庫を購入する", Material.WOOL, (byte) 5, ChatColor.GREEN + Integer.toString(type.price) + "Galionで倉庫を購入する");
    ItemStackUtil.setNBTTag(button1, REPOSITORY_TYPE, type.getType());

    ItemStack button2 = ItemStackUtil.getItem("倉庫を購入しない", Material.WOOL, (byte) 14, ChatColor.GREEN + "画面を閉じる");

    Inventory createInventory = Bukkit.createInventory(null, 9 * 3, TITLE);
    createInventory.setItem(11, button1);
    createInventory.setItem(15, button2);

    player.openInventory(createInventory);
  }

  @Override
  public void open(Player p) {}

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      Message.sendMessage(p, ChatColor.RED + "現在Playerデータをロードしています。もう暫くお待ち下さい");
      return;
    }

    String nbtTag = ItemStackUtil.getNBTTag(item, REPOSITORY_TYPE);
    RepositoryType type = RepositoryType.getInstance(nbtTag);

    // ボタン以外の時
    if (type == null) { return; }

    p.closeInventory();

    // 購入済みか調べる
    if (instance.exist(p, type)) {
      p.sendMessage("すでにその倉庫は購入しています。");
      return;
    }

    if (theLowPlayer.getGalions() > type.getPrice()) {
      // チェストを作成
      instance.createChest(p, type);
      // お金を引く
      theLowPlayer.addGalions(-type.getPrice(), GalionEditReason.consume_shop);
      Message.sendMessage(p, ChatColor.GREEN + "倉庫({0})を購入しました。", type.getType());
      p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
    } else {
      Message.sendMessage(p, ChatColor.RED + "お金が足りないので購入できません。", type.getType());
    }
  }

  @Override
  public String getTitle() {
    return TITLE;
  }

}
