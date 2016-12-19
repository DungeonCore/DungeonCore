package lbn.player.status.swordStatus;

import org.bukkit.Material;

import lbn.player.status.IStatusDetail;

public class SwordStatusDetail implements IStatusDetail{
	@Override
	public String[] getIndexDetail() {
		return new String[]{"近接戦闘で敵を倒すたびに", "レベルが上がっていきます。"};
	}

	@Override
	public String[] getDetailByLevel(int level) {
		return new String[]{};
	}

	@Override
	public String getDisplayName() {
		return "SWORD LEVEL";
	}

	@Override
	public Material getViewIconMaterial() {
		return Material.DIAMOND_SWORD;
	}

}
