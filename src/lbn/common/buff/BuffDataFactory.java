package lbn.common.buff;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffectType;

public class BuffDataFactory {

	private static final List<BuffData> buffs = new ArrayList<>();

	private BuffDataFactory() {
	}

	public static BuffData create(String id, PotionEffectType effect, int second, int level) {
		if(id == null || effect == null) {
			return null;
		}

		return new BuffData(id, effect, second, level);
	}

	public static void register(BuffData data) {
		buffs.add(data);
	}

	public static List<BuffData> getBuffs() {
		return buffs;
	}

	public static BuffData getBuffFromId(String id) {
		for(BuffData data : buffs) {
			if(data.getId() == id) {
				return data;
			}
		}

		return null;
	}

	public static void clear() {
		buffs.clear();
	}

}
