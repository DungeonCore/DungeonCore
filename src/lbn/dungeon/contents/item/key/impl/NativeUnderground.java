package lbn.dungeon.contents.item.key.impl;

import lbn.item.key.CommandBlockExceteKey;

import org.bukkit.Location;
import org.bukkit.Material;

public class NativeUnderground extends CommandBlockExceteKey{

	@Override
	public String getItemName() {
		return "The Natives Key";
	}

	@Override
	protected Material getMaterial() {
		return Material.GHAST_TEAR;
	}

	@Override
	protected String[] getDetail() {
		return new String[]{"Native Undergroundで使用可能", "x:1534 y:38 z:243"};
	}

	@Override
	public String getId() {
		return "The Natives Key";
	}

	@Override
	protected String getDungeonName() {
		return null;
	}

	@Override
	protected Location getDungeonLocation() {
		return null;
	}
}
