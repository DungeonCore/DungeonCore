package lbn.player.status.magicStatus;

import lbn.player.status.IStatusDetail;

import org.bukkit.Material;

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
