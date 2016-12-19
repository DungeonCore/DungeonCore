package main.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import main.item.GalionItem;
import main.util.particle.ParticleData;
import main.util.particle.ParticleType;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class JavaUtil {
	public static Set<Class<?>> getInterface(Class<?> clazz) {
		return new InterfaceGetter(clazz).getInterfaceList();
	}

	static Random rnd = new Random();

	public static boolean isRandomTrue(int percent) {
		if (rnd.nextInt(100) < percent) {
			return true;
		}
		return false;
	}

	public static double round(double val, int digits) {
		double pow = Math.pow(10, digits);
		return Math.round(pow * val) / pow;
	}

	public static String getLocationString(Location loc) {
		StringBuilder sb = new StringBuilder();
		sb.append(loc.getWorld().getName());
		sb.append("(");
		sb.append(loc.getBlockX());
		sb.append(", ");
		sb.append(loc.getBlockY());
		sb.append(", ");
		sb.append(loc.getBlockZ());
		sb.append(")");
		return sb.toString();
	}

	public static int getInt(String data, int nullData) {
		try {
			return Integer.parseInt(data);
		} catch (Exception e) {
			return nullData;
		}
	}

	public static boolean getBoolean(String data, boolean nullData) {
		if ("true".equalsIgnoreCase(data)) {
			return true;
		} else if ("false".equalsIgnoreCase(data)) {
			return false;
		}
		return nullData;
	}

	public static double getDouble(String deta, double nullData) {
		try {
			return Double.parseDouble(deta);
		} catch (Exception e) {
			return nullData;
		}
	}

	static ParticleData particleData = new ParticleData(ParticleType.fireworksSpark, 100);

	public static void addBonusGold(Player p, Location l) {
		l.getWorld().dropItem(l, new GalionItem(20).getItem());
		particleData.run(l);
		l.getWorld().playSound(l, Sound.FIREWORK_BLAST, 1, 1);
	}
}

class InterfaceGetter {
	HashSet<Class<?>> interfaceList = new HashSet<Class<?>>();

	Class<?> clazz;
	public InterfaceGetter(Class<?> clazz) {
		this.clazz = clazz;
	}

	private void search(Class<?> clazz) {
		for (Class<?> inter : clazz.getInterfaces()) {
			//すでに探索済みなら何もしない
			if (interfaceList.contains(inter)) {
				continue;
			}
			interfaceList.add(inter);
			search(inter);
		}
		Class<?> superclass = clazz.getSuperclass();
		if (superclass == null) {
			return;
		}
		search(superclass);
	}

	public HashSet<Class<?>> getInterfaceList() {
		search(clazz);
		return interfaceList;
	}

}
