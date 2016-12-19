package lbn.player.status.mainStatus;

import org.bukkit.Material;

import lbn.player.status.IStatusDetail;

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
