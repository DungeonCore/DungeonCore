package lbn.player.status.detail;

import lbn.player.TheLowLevelType;
import lbn.player.TheLowPlayer;
import lbn.player.status.IStatusDetail;

import org.bukkit.Material;

public class SwordStatusDetail extends IStatusDetail{
	public SwordStatusDetail(TheLowPlayer p) {
		super(p);
	}

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

	@Override
	public TheLowLevelType getLevelType() {
		return TheLowLevelType.SWORD;
	}

}
