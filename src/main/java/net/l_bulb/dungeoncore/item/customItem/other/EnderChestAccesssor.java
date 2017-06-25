package net.l_bulb.dungeoncore.item.customItem.other;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EnderChestAccesssor extends RepositoryAccessor {

  public EnderChestAccesssor() {
    super(null);
  }

  @Override
  public String getId() {
    return "enderchest_accessor";
  }

  @Override
  public String[] getDetail() {
    return new String[] { "エンダーチェストを開くことが出来ます。", "このアイテムはスタックできません。" };
  }

  @Override
  protected Inventory getOpenRepository(Player player) {
    return player.getEnderChest();
  }

  @Override
  protected Material getMaterial() {
    return Material.ENDER_CHEST;
  }

  @Override
  protected String getRepositoryName() {
    return "【エンダーチェスト】";
  }

}
