package lbn.util.particle;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class Particles {
	static HashMap<ParticleType, ParticleData> cacheParticle = new HashMap<ParticleType, ParticleData>();

	/**
	 * パーティクルを発生させる
	 * @param loc
	 */
	public static void runParticle(Location loc, ParticleType type) {
		ParticleData particleData = cacheParticle.get(type);
		//キャッシュがなければ作成
		if (particleData == null) {
			particleData = new ParticleData(type, 50);
			cacheParticle.put(type, particleData);
		}
		particleData.run(loc);
	}

	/**
	 * パーティクルを発生させる
	 * @param entity
	 */
	public static void runParticle(Entity entity, ParticleType type, int amount) {
		runParticle(entity.getLocation(), type, amount);
	}

	/**
	 * パーティクルを発生させる
	 * @param loc
	 */
	public static void runParticle(Location loc, ParticleType type, int amount) {
		new ParticleData(type, amount).run(loc);
	}


	/**
	 * 円のパーティクルを発生させる
	 * @param loc
	 * @param type
	 */
	public static void runCircleParticle(Entity entity, ParticleType type, double radius, int amount) {
		runCircleParticle(entity.getLocation(), type, radius, amount);
	}
		/**
		 * 円のパーティクルを発生させる
		 * @param loc
		 * @param type
		 */
		public static void runCircleParticle(Location loc, ParticleType type, double radius, int amount) {
			CircleParticleData circleParticleData = new CircleParticleData(new ParticleData(ParticleType.portal, 2), radius);
			circleParticleData.run(loc);
	}
}
