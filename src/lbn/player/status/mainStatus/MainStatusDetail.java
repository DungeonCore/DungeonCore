package lbn.player.status.mainStatus;

import lbn.player.status.IStatusDetail;

import org.bukkit.Material;

public class MainStatusDetail implements IStatusDetail{
	@Override
	public String[] getIndexDetail() {
		return new String[]{"プレイヤー自身のレベルです"};
	}

	@Override
	public String[] getDetailByLevel(int level) {
		return new String[]{};
	}

	@Override
	public String getDisplayName() {
		return "PLAYER LEVEL";
	}

	@Override
	public Material getViewIconMaterial() {
		return Material.SKULL_ITEM;
	}

}
