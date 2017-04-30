package lbn.common.buff;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.potion.PotionEffectType;

public class BuffDataFactory {

	private static final Map<String, BuffData> buffs = new HashMap<>();

	private BuffDataFactory() {
	}

	public static BuffData create(String id, PotionEffectType effect, double second, int level) {
		if(id == null || effect == null) {
			return null;
		}

		return new BuffData(id, effect, second, level);
	}

	public static void register(BuffData data) {
		buffs.put(data.getId(), data);
	}

	public static Collection<BuffData> getBuffs() {
		return buffs.values();
	}

	public static BuffData getBuffFromId(String id) {
		if (id == null || id.isEmpty()) {
			return null;
		} else {
			return buffs.get(id);
		}
	}

	public static void clear() {
		buffs.clear();
	}

}
