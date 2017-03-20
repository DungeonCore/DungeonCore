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
import net.minecraft.server.v1_8_R1.PacketPlayOutNamedSoundEffect;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftSound;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
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
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(targetInstance);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Privateなメソッドに値を挿入する
	 * @param target_object
	 * @param field_name
	 * @param value
	 * @throws Exception
	 */
	public static void setPrivateField(Object target_object, String field_name, Object value){
		try {
			Class<?> c = target_object.getClass();
			Field fld = c.getDeclaredField(field_name);
			fld.setAccessible(true);
			fld.set(target_object, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
		//http://wiki.vg/Protocol#Sound_Effect
	 * @param center
	 * @param sound
	 * @param volume
	 * @param pitch
	 * @param range
	 */
	public static void sendSound(Location center, Sound sound, float volume, float pitch, double range) {
		//packetを送信
		for (Player p : center.getWorld().getPlayers()) {
			if (!p.isOnline()) {
				continue;
			}
			Location loc = p.getLocation();


			//聞こえるが遠い場所
			if (loc.distance(center) <= range && loc.distance(center) > (range / 2)) {
				volume = volume * 0.7f;
			}

			PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect( CraftSound.getSound(sound), center.getX(), center.getY(), center.getZ(), volume, pitch);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void chunkLoadIfUnload(Chunk c) {
		if (!c.isLoaded()) {
			c.load();
		}
	}

	/**
	 * 指定された座標間の距離の2乗を返す
	 * @param loc
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static double getDistanceSquared(Location loc, double x, double y, double z) {
		return loc.getX() * x + loc.getY() * y + loc.getZ() * z;
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
