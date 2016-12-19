package lbn.player.status.bowStatus;

import org.bukkit.Material;

import lbn.player.status.IStatusDetail;

public class BowStatusDetail implements IStatusDetail{
	@Override
	public String[] getIndexDetail() {
		return new String[]{"弓で敵を倒すたびに", "レベルが上がっていきます。"};
	}

	@Override
	public String[] getDetailByLevel(int level) {
		return new String[]{};
	}

	@Override
	public String getDisplayName() {
		return "BOW LEVEL";
	}

	@Override
	public Material getViewIconMaterial() {
		return Material.BOW;
	}

}
