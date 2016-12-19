package lbn.player.status.magicStatus;

import org.bukkit.Material;

import lbn.player.status.IStatusDetail;

public class MagicStatusDetail implements IStatusDetail{
	@Override
	public String[] getIndexDetail() {
		return new String[]{"魔法で敵を倒すたびに", "レベルが上がっていきます。"};
	}

	@Override
	public String[] getDetailByLevel(int level) {
		return new String[]{};
	}

	@Override
	public String getDisplayName() {
		return "MAGIC LEVEL";
	}

	@Override
	public Material getViewIconMaterial() {
		return Material.BLAZE_ROD;
	}

}
