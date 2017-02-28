package lbn.util;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import lbn.item.implementation.GalionItem;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JavaUtil {
	public static Set<Class<?>> getInterface(Class<?> clazz) {
		return new InterfaceGetter(clazz).getInterfaceList();
	}

	private static Random rnd = new Random();

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
//		StringBuilder sb = new StringBuilder();
//		sb.append(loc.getWorld().getName());
//		sb.append("(");
//		sb.append(loc.getBlockX());
//		sb.append(", ");
//		sb.append(loc.getBlockY());
//		sb.append(", ");
//		sb.append(loc.getBlockZ());
//		sb.append(")");
//		return sb.toString();
	  return String.format("(%d, %d, %d)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public static int getInt(String data, int other) {
		try {
			return Integer.parseInt(data);
		} catch (Exception e) {
			return other;
		}
	}

	public static float getFloat(String data, float other) {
		try {
			return Float.parseFloat(data);
		} catch (Exception e) {
			return other;
		}
	}

	public static short getShort(String data, short other) {
		try {
			return Short.parseShort(data);
		} catch (Exception e) {
			return other;
		}
	}

	public static boolean getBoolean(String data, boolean other) {
		if ("true".equalsIgnoreCase(data)) {
			return true;
		} else if ("false".equalsIgnoreCase(data)) {
			return false;
		}
		return other;
	}

	public static double getDouble(String deta, double other) {
		try {
			return Double.parseDouble(deta);
		} catch (Exception e) {
			return other;
		}
	}

//	public static double getRelativeDouble(String deta, double other) {
//		try {
//			CommandAbstract.b(paramDouble, paramString, paramInt1, paramInt2, paramBoolean)
//		} catch (Exception e) {
//			return other;
//		}
//	}

	static ParticleData particleData = new ParticleData(ParticleType.fireworksSpark, 100);

	public static void addBonusGold(Player p, Location l) {
		l.getWorld().dropItem(l, GalionItem.getInstance(20).getItem());
		particleData.run(l);
		l.getWorld().playSound(l, Sound.FIREWORK_BLAST, 1, 1);
	}

	static TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");

	public static long getJapanTimeInMillis() {
		 Calendar cal1 = Calendar.getInstance(timeZone);
		 long timeInMillis = cal1.getTimeInMillis();
		 return timeInMillis;
	}

	public static Location getSenderLocation (CommandSender sender){
		Location senderLoc = null;
		if ((sender instanceof BlockCommandSender)) {
			senderLoc = ((BlockCommandSender) sender).getBlock().getLocation();
		} else if (sender instanceof Player) {
			senderLoc = ((Player) sender).getLocation();
		}
		return senderLoc;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getField(Class<?> clazz, String fieldName, Object targetInstance) {
		try {
			Field field = clazz.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(targetInstance);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

class InterfaceGetter {
	private Set<Class<?>> interfaceList = new HashSet<Class<?>>();

	private Class<?> clazz;
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

	public Set<Class<?>> getInterfaceList() {
		search(this.clazz);
		return this.interfaceList;
	}
}
