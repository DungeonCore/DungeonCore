package net.l_bulb.dungeoncore.dungeon.contents.item.key;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.AbstractItem;

public abstract class AbstractKeyItem extends AbstractItem implements KeyItemable {
  @Override
  public String getId() {
    return ChatColor.stripColor(getItemName());
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 0;
  }

  @Override
  public String[] getDetail() {
    if (getDungeonLocation() != null) {
      return new String[] { getDungeonName() + "で使用可能", StringUtils.join(new Object[] { "x:", (int) getDungeonLocation().getBlockX(), " y:",
          (int) getDungeonLocation().getBlockY(), " z:", (int) getDungeonLocation().getBlockZ() }) };
    } else {
      return new String[] { getDungeonName() + "で使用可能" };
    }
  }

  abstract protected String getDungeonName();

  abstract protected Location getDungeonLocation();
}
