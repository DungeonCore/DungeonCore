package lbn.player.magicstoneOre;

import org.bukkit.Material;

/**
 *	魔法鉱石の種類を管理するためのクラス
 * @author KENSUKE
 *
 */
public enum MagicStoneOreType {
	DIAOMOD_ORE("ダイヤ鉱石", Material.DIAMOND_ORE, 180,60),
	REDSTONE_ORE("レッドストーン鉱石", Material.REDSTONE_ORE, 90,40),
	GOLD_ORE("金鉱石", Material.GOLD_ORE, 90,30),
	EMERALD_ORE("エメラルド鉱石", Material.EMERALD_ORE, 15,10),
	IRON_ORE("鉄鉱石", Material.IRON_ORE, 30,10),
	COAL_ORE("石炭鉱石", Material.COAL_ORE, 30,10);
	
	//日本語名
	String jpName;

	//鉱石のブロックの素材
	Material m;
	
	//復活する最大時間
	long maxMin;
	
	//復活する最小時間
	long minMin;

	private MagicStoneOreType(String jpName, Material m, long maxMin,long minMin) {
		this.jpName = jpName;
		this.m = m;
		this.maxMin = maxMin;
		this.minMin = minMin;
		
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
	 * 鉱石の最大復活時間を取得
	 * @return
	 */
	public long getMaxRespawnTick() {
		return maxMin;
	}
	
	/**
	 * 鉱石の最小復活時間を取得
	 * @return
	 */
	public long getMinRespawnTick() {
		return minMin;
	}
	
	/**
	 * マテリアルから復活する時間を取得
	 * @param material
	 * @return
	 */
	public static long getRespawnTickFromMaterial(Material m) {
		for(MagicStoneOreType tick: values()){
			if(tick.getMaterial().equals(m)){
				return tick.getMinRespawnTick();
			}
		}
		return -1;
	}
	/**
	 * マテリアルから魔法鉱石を取得。もし存在しない日本語名の時はnullを返す
	 * @param m
	 * @return
	 */
	public static MagicStoneOreType FromMaterial(Material m) {
		for(MagicStoneOreType type : values()){
			if(type.getMaterial().equals(m)){
				return type;
			}
		}
		return null;
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
