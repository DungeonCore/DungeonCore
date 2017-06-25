package net.l_bulb.dungeoncore.item.customItem.other;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.chest.wireless.RepositoryType;
import net.l_bulb.dungeoncore.chest.wireless.WireLessChestManager;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.RightClickItemable;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class RepositoryAccessor extends AbstractItem implements RightClickItemable {

  @Getter
  private RepositoryType type;

  public RepositoryAccessor(RepositoryType type) {
    this.type = type;
  }

  @Override
  public String getItemName() {
    return "倉庫アクセス" + getRepositoryName();
  }

  @Override
  public String getId() {
    return type.getType() + "repositoryaccessor";
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 500;
  }

  @Override
  protected Material getMaterial() {
    return Material.CHEST;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "倉庫を開くことが出来ます。", "このアイテムはスタックできません。" };
  }

  @Override
  public ItemStack getItem() {
    ItemStack item = super.getItem();
    ItemStackUtil.setNBTTag(item, "random_uuid", UUID.randomUUID().toString());
    return item;
  }

  @Override
  public boolean excuteOnRightClick(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    // 倉庫を取得する
    Inventory repositoryInventory = getOpenRepository(player);
    if (repositoryInventory == null) {
      player.sendMessage(ChatColor.RED + "倉庫" + getRepositoryName() + "は購入されていません。村へ行って倉庫を購入してください。");
      return false;
    }
    player.openInventory(repositoryInventory);
    return true;
  }

  /**
   * 倉庫の名前を取得
   *
   * @return
   */
  protected String getRepositoryName() {
    return "【" + type.getType() + "】";
  }

  /**
   * 開くインベントリを取得する
   *
   * @param player
   * @return
   */
  protected Inventory getOpenRepository(Player player) {
    WireLessChestManager instance = WireLessChestManager.getInstance();
    Inventory repositoryInventory = instance.getRepositoryInventory(player, getType());
    return repositoryInventory;
  }

  @Override
  public boolean isConsumeWhenRightClick(PlayerInteractEvent event) {
    return true;
  }

}
