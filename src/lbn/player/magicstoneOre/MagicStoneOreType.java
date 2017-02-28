package lbn.player.magicstoneOre;

import org.bukkit.Material;

/**
 *	魔法鉱石の種類を管理するためのクラス
 * @author KENSUKE
 *
 */
public enum MagicStoneOreType {
	DIAOMOD_ORE("ダイヤ鉱石", Material.DIAMOND_ORE), AAAA("aaa", Material.APPLE);

	//日本語名
	String jpName;

	//鉱石のブロックの素材
	Material m;

	private MagicStoneOreType(String jpName, Material m) {
		this.jpName = jpName;
		this.m = m;
	}

	/**
	 * ブロックの素材を取得する
	 * @return
	 */
	public Material getMaterial() {
		return m;
	}

	/**
	 * 日本語名を取得
	 * @return
	 */
	public String getJpName() {
		return jpName;
	}

	/**
	 * 日本語名から魔法鉱石を取得する。もし存在しない日本語名の時はnullを返す
	 * @param jpName
	 * @return
	 */
	public static MagicStoneOreType FromJpName(String jpName) {
		for (MagicStoneOreType type : values()) {
			//日本語名が同じならそれを返す
			if (type.getJpName().equals(jpName)) {
				return type;
			}
		}
		return null;
	}
}
