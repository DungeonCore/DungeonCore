package lbn.util;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationData implements Serializable{
	private static final long serialVersionUID = 6380196832013949147L;
	double x;
	double y;
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public UUID getWolrdID() {
		return wolrdID;
	}

	public void setWolrdID(UUID wolrdID) {
		this.wolrdID = wolrdID;
	}

	double z;
	UUID wolrdID;

	public LocationData(double x, double y, double z, UUID wolrdID) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.wolrdID = wolrdID;
	}

	public LocationData(Location location) {
		x = location.getX();
		y = location.getY();
		z = location.getZ();
		wolrdID = location.getWorld().getUID();
	}

	public  Location getLocation() {
		World world = Bukkit.getWorld(wolrdID);
		if (world == null) {
			return null;
		} else {
			return new Location(world, x, y, z);
		}
	}

	/**
	 * world@x,y,z,id
	 * @return
	 */
	public String getSerializeString() {
		StringBuilder sb = new StringBuilder();
		World world = Bukkit.getWorld(wolrdID);
		if (world == null) {
			sb.append("non world");
		} else {
			sb.append(Bukkit.getWorld(wolrdID).getName());
		}
		sb.append("@");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(", ");
		sb.append(z);
		sb.append(", ");
		sb.append(wolrdID.toString());
		return sb.toString();
	}

	public static LocationData fromSerializeString(String str) {
		String[] split = str.split("@");
		if (split.length != 2) {
			return null;
		}

		String[] split2 = split[1].split(",");

		if (split2.length != 4) {
			return null;
		}

		UUID id = UUID.fromString(split2[3].trim());

		try {
			return new LocationData(Double.parseDouble(split2[0]), Double.parseDouble(split2[1]), Double.parseDouble(split2[2]), id);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Bukkit.getWorld(wolrdID).getName());
		sb.append("[");
		sb.append(x);
		sb.append(",");
		sb.append(y);
		sb.append(",");
		sb.append(z);
		sb.append("]");
		return sb.toString();
	}
}