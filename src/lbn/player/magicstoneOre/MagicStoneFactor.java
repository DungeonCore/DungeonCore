package lbn.player.magicstoneOre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

public class MagicStoneFactor {
	
	public static HashMap<Location, MagicStoneOreType> magicStoneOres = new HashMap<>();
	public static List<Location> locations = new ArrayList<>();
	
	/**
	 * 全ての魔法鉱石の種類と座標を取得する
	 * @return
	 */
	public static HashMap<Location, MagicStoneOreType> getAllMagicStone() {
		
		return magicStoneOres;
	}

	/**
	 * 指定された座標から魔法鉱石のタイプを取得
	 * @param loc
	 * @return
	 */
	public static MagicStoneOreType getMagicStoneByLocation(Location loc) {
		
		return magicStoneOres.get(loc);
	}

	/**
	 * 魔法鉱石と座標を取得する
	 */
	public static void regist(Location loc, MagicStoneOreType type) {
		magicStoneOres.put(loc, type);
	}
	
}
