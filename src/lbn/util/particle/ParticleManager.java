package lbn.util.particle;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class ParticleManager {
	static HashMap<String, ParticleData> dataMap = new HashMap<String, ParticleData>();

	public static void regist(String id, ParticleData data) {
		dataMap.put(id, data);
	}

	public static void clear() {
		dataMap.clear();
	}

	public static ParticleData getParticleData(String id) {
		return dataMap.get(id);
	}

	public static boolean playPerticle(String id, Location loc) {
		ParticleData particleData = dataMap.get(id);
		if (particleData == null) {
			return false;
		}
		particleData.run(loc);
		return true;
	}

	public static boolean playPerticle(String id, Entity entity) {
		return playPerticle(id, entity.getLocation());
	}

	public static boolean contains(String id) {
		return dataMap.containsKey(id);
	}
}
