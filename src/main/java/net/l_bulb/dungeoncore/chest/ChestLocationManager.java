package net.l_bulb.dungeoncore.chest;

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
    if (world == null || point.length != 3) { return; }

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

  /**
   * アルファベット名から日本語名を取得
   *
   * @param alphabetName
   * @return
   */
  public static String getJpName(String alphabetName) {
    return chestNameMap.get(alphabetName);
  }

  public static HashMap<String, String> chestNameMap = new HashMap<>();

  static {
    ChestLocationManager.regist("mori", new double[] { 23, 77, 69 }, "thelow");
    ChestLocationManager.regist("yuki", new double[] { 23, 77, 72 }, "thelow");
    ChestLocationManager.regist("sabaku", new double[] { 23, 77, 77 }, "thelow");
    ChestLocationManager.regist("umi", new double[] { 23, 77, 80 }, "thelow");
    ChestLocationManager.regist("sogen", new double[] { 23, 77, 84 }, "thelow");
    ChestLocationManager.regist("dokutu", new double[] { 23, 77, 88 }, "thelow");
    ChestLocationManager.regist("mati", new double[] { 23, 77, 92 }, "thelow");

    chestNameMap.put("mori", "森チェスト");
    chestNameMap.put("yuki", "雪チェスト");
    chestNameMap.put("sabaku", "砂漠チェスト");
    chestNameMap.put("umi", "海チェスト");
    chestNameMap.put("sogen", "草原チェスト");
    chestNameMap.put("dokutu", "洞窟チェスト");
    chestNameMap.put("mati", "街チェスト");
  }
}
