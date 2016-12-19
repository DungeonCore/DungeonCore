package lbn.dungeon.contents.item.key;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import lbn.item.AbstractItem;

public abstract class AbstractKeyItem extends AbstractItem implements KeyItemable{
	@Override
	public String getId() {
		return ChatColor.stripColor(getItemName());
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return 0;
	}

	@Override
	protected String[] getDetail() {
		return new String[]{getDungeonName() + "で使用可能", StringUtils.join(new Object[]{"x:", (int)getDungeonLocation().getBlockX(), " y:", (int)getDungeonLocation().getBlockY(), " z:", (int)getDungeonLocation().getBlockZ()})};
	}

	abstract protected String getDungeonName();

	abstract protected Location getDungeonLocation();
}
