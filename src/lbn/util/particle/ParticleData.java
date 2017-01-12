package lbn.util.particle;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.server.v1_8_R1.EnumParticle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.util.Vector;

/**
 * パーティクル種類を保持するクラス
 *
 */
public class ParticleData {
	public EnumParticle particleEnum;
	public int amount;

	public double dx = 0.32D;
	public double dy = 0.32D;
	public double dz = 0.32D;

	public ParticleData(ParticleType particle, int amount) {
		this.amount = amount;
		this.particleEnum = particle.getEnumType();
	}

	public ParticleData(EnumParticle particle, int amount) {
		this.amount = amount;
		this.particleEnum = particle;
	}

	public ParticleData(ParticleData data) {
		this.amount = data.amount;
		this.lastArgument = data.lastArgument;
		this.particleEnum = data.particleEnum;
		this.dx = data.dx;
		this.dy = data.dy;
		this.dz = data.dz;
	}

	public ParticleData setParticle(ParticleType particle) {
		this.particleEnum = particle.getEnumType();
		return this;
	}

	/**
	 * パーティクルの量をセットする
	 * @param amount
	 * @return
	 */
	public ParticleData setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * 分散をセットする
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public ParticleData setDispersion(double dx, double dy, double dz) {
		if (dx >= 0) {
			this.dx = dx;
		}
		if (dy >= 0) {
			this.dy = dx;
		}
		if (dz >= 0) {
			this.dz = dx;
		}
		return this;
	}

	public double lastArgument = 0;

	public ParticleData setLastArgument(double value) {
		this.lastArgument = value;
		return this;
	}

	/**
	 * パーティクル実行
	 * @param locList
	 */
	public void run(Location... locList) {
		for (Location loc : locList) {
			run(loc.getWorld(), loc.toVector());
		}
	}

	/**
	 * パーティクル実行
	 * @param locList
	 */
	public void run(Collection<Location> locList) {
		for (Location loc : locList) {
			run(loc.getWorld(), loc.toVector());
		}
	}

	/**
	 * 必ずこのメソッドを通ります
	 * @param w
	 * @param v
	 */
	protected void run(World w, Vector... v) {
		for (Vector vector : v) {
			runParticle(w, vector.getX(), vector.getY(), vector.getZ());
		}
	}

	protected void runParticle(World w, double x, double y, double z) {
		((CraftWorld) w).getHandle().a(particleEnum, x, y, z, amount, dx, dy, dz, lastArgument);
	}

	final protected void run(World w, List<Vector> v) {
		run(w, v.toArray(new Vector[0]));
	}

	/**
	 * パーティクル名を取得
	 * @return パーティクル名(小文字)
	 */
	public String getParticleName() {
		return particleEnum.toString();
	}

	@Override
	public String toString() {
		return "name:" + getParticleName() + ", amount:" + amount + ", dispersion:" + Arrays.asList(dx, dy, dz);
	}
}
