package main.chest;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ChestLocationManager {
	public static HashMap<String, Location> chestName = new HashMap<>();
	public static HashMap<Location, String> chestLoc = new HashMap<>();

	public static void regist(String name, Location loc) {
		chestLoc.put(loc, name);
		chestName.put(name, loc);
	}

	public static Set<String> getNames() {
		return chestName.keySet();
	}

	public static void regist(String name, double[] point, String worldName) {
		World world = Bukkit.getWorld(worldName);
		if (world == null || point.length != 3) {
			return;
		}

		Location loc = new Location(world, point[0], point[1], point[2]);
		chestLoc.put(loc, name);
		chestName.put(name, loc);
	}

	public static Location getChestLocation(String name) {
		return chestName.get(name);
	}

	public static String getName(Location loc) {
		return chestLoc.get(loc);
	}

	static{
		ChestLocationManager.regist("potion_common", new double[]{110,42,828}, "world");
		ChestLocationManager.regist("potion_uncommon", new double[]{110,42,826}, "world");
		ChestLocationManager.regist("potion_rare", new double[]{110,42,824}, "world");
		ChestLocationManager.regist("potion_epic", new double[]{110,42,822}, "world");
		ChestLocationManager.regist("potion_dungeon", new double[]{110,42,820}, "world");
		ChestLocationManager.regist("potion_dungeon_rare", new double[]{110,42,818}, "world");
		ChestLocationManager.regist("potion_legendary", new double[]{110,42,816}, "world");
		ChestLocationManager.regist("dungeon_epic", new double[]{113,42,820}, "world");
		ChestLocationManager.regist("dungeon_legendary", new double[]{113,42,818}, "world");
		ChestLocationManager.regist("dungeon_godly", new double[]{116,42,818}, "world");
		ChestLocationManager.regist("food", new double[]{113,42,828}, "world");
		ChestLocationManager.regist("food_rare", new double[]{113,42,826}, "world");
		ChestLocationManager.regist("military_common", new double[]{113,42,824}, "world");
		ChestLocationManager.regist("military_uncommon", new double[]{113,42,822}, "world");
		ChestLocationManager.regist("military_rare", new double[]{116,42,828}, "world");
		ChestLocationManager.regist("military_epic", new double[]{116,42,826}, "world");
		ChestLocationManager.regist("military_legendary", new double[]{116,42,824}, "world");
		ChestLocationManager.regist("money_drop", new double[]{113,42,816}, "world");
		ChestLocationManager.regist("magic_stone_common", new double[]{119,42,828}, "world");
		ChestLocationManager.regist("magic_stone_uncommon", new double[]{119,42,826}, "world");
		ChestLocationManager.regist("magic_stone_rare", new double[]{119,42,824}, "world");
		ChestLocationManager.regist("magic_stone_epic", new double[]{119,42,822}, "world");
		ChestLocationManager.regist("magic_stone_legendary", new double[]{119,42,820}, "world");
	}
}

