package net.l_bulb.dungeoncore.npc.gui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorInterface;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.AbstractSheetRunable;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpc;
import net.l_bulb.dungeoncore.player.PlayerChecker;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import net.md_5.bungee.api.ChatColor;

public class RestVillagerGUI implements MenuSelectorInterface {
  static {
    MenuSelectorManager.regist(new RestVillagerGUI());
  }

  private static final String TITLE = "宿屋";

  private static final int PRICE = 500;

  public static void open(Player p, VillagerNpc npc) {
    // 座標の確認
    Location locationByString = AbstractSheetRunable.getLocationByString(npc.getData());
    if (locationByString == null) {
      if (PlayerChecker.isNonNormalPlayer(p)) {
        p.sendMessage("正しい座標が登録されていないため画面を開けません。");
      }
    }

    Inventory createInventory = Bukkit.createInventory(null, 3 * 9, TITLE);

    ItemStack item1 = ItemStackUtil.getItem(ChatColor.WHITE + "この街をスポーン地点にする", Material.WOOL, ChatColor.GREEN.toString() + (byte) 5, PRICE
        + "ガリオンでこの街を", "スポーン地点にします。");
    ItemStackUtil.setNBTTag(item1, "spawn_location", npc.getData());
    createInventory.setItem(11, item1);

    ItemStack item2 = ItemStackUtil.getItem(ChatColor.WHITE + "画面を閉じる", Material.WOOL, (byte) 14);
    ItemStackUtil.setNBTTag(item2, "spawn_location", "close");
    createInventory.setItem(15, item2);

    // インベントリを開く
    p.openInventory(createInventory);
  }

  @Override
  public void open(Player p) {}

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    // ボタンでないなら何もしない
    String nbtTag = ItemStackUtil.getNBTTag(item, "spawn_location");
    if (nbtTag == null || nbtTag.isEmpty()) { return; }

    // 閉じるボタン
    if (nbtTag.equals("close")) {
      p.closeInventory();
      return;
    }

    // 座標が指定されていないなら
    Location locationByString = AbstractSheetRunable.getLocationByString(nbtTag);
    if (locationByString == null) { return; }

    // Playerデータを取得する
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(p);
      p.closeInventory();
      return;
    }

    // お金を確認し消費する
    if (theLowPlayer.getGalions() > PRICE) {
      theLowPlayer.addGalions(-PRICE, GalionEditReason.consume_shop);
    } else {
      p.sendMessage(ChatColor.RED + "お金が足りないためスポーン地点を変更できません");
      p.closeInventory();
      return;
    }

    // スポーン地点を変更
    p.setBedSpawnLocation(locationByString, true);
    p.sendMessage(ChatColor.GREEN + "あなたのスポーン地点を変更しました");
    p.closeInventory();
  }

  @Override
  public String getTitle() {
    return TITLE;
  }

}
