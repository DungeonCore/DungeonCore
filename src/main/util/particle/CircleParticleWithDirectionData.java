package main.util.particle;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class CircleParticleWithDirectionData extends ParticleData{

	double radius;
	double directionLength;

	public CircleParticleWithDirectionData(ParticleType particle, int amount, double radius, double directionLength) {
		super(particle, amount);
		this.radius = radius;
		this.directionLength = directionLength;
		setDispersion(0, 0, 0);
	}

	public CircleParticleWithDirectionData(ParticleData data, double radius, double directionLength) {
		super(data);
		this.radius = radius;
		this.directionLength = directionLength;
		setDispersion(0, 0, 0);
	}

	public void run(LivingEntity e) {
		ArrayList<Vector> circleVecList = CircleParticleCache.getCircleVecList(radius, true);

		Location location = e.getLocation();
		Vector vector = location.getDirection().normalize().multiply(directionLength);

//		float pitch = -location.getPitch();
		float pitch = 0;
//		float yaw = location.getYaw();
		float yaw = 90;

		double cosTheta = Math.cos(Math.toRadians(pitch));
		double sinTheta = Math.sin(Math.toRadians(pitch));

		double cosPsi = Math.cos(Math.toRadians(yaw));
		double sinPsi = Math.sin(Math.toRadians(yaw));


		double[][] matrix = {{cosTheta, sinTheta * sinPsi, sinTheta * cosPsi},
				{0, cosPsi, -sinPsi},
				{-sinTheta, cosTheta * sinPsi, cosTheta * sinPsi}};

		for (Vector ds : circleVecList) {
			Vector product = product(matrix, ds);
			product.setX(product.getX() + location.getX() + vector.getX());
			product.setY(product.getY() + location.getY() + vector.getBlockY());
			product.setZ(product.getZ() + location.getZ() + vector.getBlockZ());
			run(e.getWorld(), product);
		}
	}

	/**
	 * =Ax
	 * 行列Aとベクトルxの積
	 */
	public static Vector product(double[][] A, Vector ds) { // =Ax
		double[] temp = new double[A.length];

		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[i].length; j++)
				if (j == 0) {
					temp[i] += A[i][j] * ds.getX();
				} else if (j == 1) {
					temp[i] += A[i][j] * ds.getY();
				} else if (j == 2) {
					temp[i] += A[i][j] * ds.getZ();
				}
		}
		return new Vector(temp[0], temp[1], temp[2]);
	}

}
