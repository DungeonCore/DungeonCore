package net.l_bulb.dungeoncore.chest.wireless;

import java.text.MessageFormat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorInterface;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import net.md_5.bungee.api.ChatColor;

public class RepositoryMenu implements MenuSelectorInterface {
  static {
    MenuSelectorManager.regist(new RepositoryMenu());
  }

  private static final String ENDER_CHEST = "ENDER_CHEST";
  private static final String NBT_TAG_REPOSITORY_TYPE = "REPOSITORY_TYPE";

  @Override
  public void open(Player p) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(p);
      return;
    }

    Inventory createInventory = Bukkit.createInventory(null, 9 * 3, getTitle());

    ItemStack enderChest = new ItemStack(Material.ENDER_CHEST);
    ItemStackUtil.setNBTTag(enderChest, NBT_TAG_REPOSITORY_TYPE, ENDER_CHEST);
    createInventory.setItem(0, enderChest);
    createInventory.setItem(2, getButtonItemStack(p, RepositoryType.TYPE_A));
    createInventory.setItem(4, getButtonItemStack(p, RepositoryType.TYPE_B));
    createInventory.setItem(6, getButtonItemStack(p, RepositoryType.TYPE_C));
    createInventory.setItem(8, getButtonItemStack(p, RepositoryType.TYPE_D));
    createInventory.setItem(18, getButtonItemStack(p, RepositoryType.TYPE_E));
    createInventory.setItem(20, getButtonItemStack(p, RepositoryType.TYPE_F));

    p.openInventory(createInventory);
  }

  protected ItemStack getButtonItemStack(Player p, RepositoryType type) {
    WireLessChestManager instance = WireLessChestManager.getInstance();

    ItemStack button = null;
    if (instance.exist(p, type)) {
      button = ItemStackUtil.getItem(MessageFormat.format("{0}{1}{2}(購入済み)", ChatColor.GREEN, type.getType(), ChatColor.WHITE), Material.CHEST,
          ChatColor.WHITE + "クリックで開く");
    } else {
      button = ItemStackUtil.getItem(MessageFormat.format("{0}{1}{2}(未購入)", ChatColor.GREEN, type.getType(), ChatColor.RED), Material.WOOD,
          ChatColor.WHITE + "クリックで購入", ChatColor.WHITE.toString() + type.getPrice() + " Galions");
    }
    // typeを記録
    ItemStackUtil.setNBTTag(button, NBT_TAG_REPOSITORY_TYPE, type.getType());

    return button;
  }

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    if (item == null) { return; }

    // リポジトリでないなら何もしない
    String nbtTag = ItemStackUtil.getNBTTag(item, NBT_TAG_REPOSITORY_TYPE);
    if (nbtTag == null) { return; }

    // エンダーチェストのとき
    if (nbtTag.equals(ENDER_CHEST)) {
      p.openInventory(p.getEnderChest());
    } else {
      // タイプ情報が不正な時は何もしない
      RepositoryType type = RepositoryType.getInstance(nbtTag);
      if (type == null) {
        p.sendMessage("エラーが発生しました。リポジトリが不正です。：" + nbtTag);
        p.closeInventory();
        return;
      }

      // 倉庫購入画面に遷移する
      WireLessChestManager instance = WireLessChestManager.getInstance();
      // 倉庫が存在している時は倉庫を開く
      if (instance.exist(p, type)) {
        Inventory repositoryInventory = instance.getRepositoryInventory(p, type);
        // 倉庫を開く
        if (repositoryInventory != null) {
          p.openInventory(repositoryInventory);
        } else {
          p.sendMessage("倉庫が存在しません。");
          p.closeInventory();
        }
      } else {
        RepositoryBuyMenu.open(p, type);
      }
    }
  }

  @Override
  public String getTitle() {
    return "倉庫リスト";
  }

}
