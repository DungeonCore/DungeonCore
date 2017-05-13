package net.l_bulb.dungeoncore.player.playerIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.dungeoncore.LbnRuntimeException;
import net.l_bulb.dungeoncore.util.DungeonLogger;
import net.l_bulb.dungeoncore.util.InOutputUtil;

public class PlayerLastSaveType {

  private static HashMap<UUID, String> useType = new HashMap<>();

  public static void regist(Player p, String type) {
    useType.put(p.getUniqueId(), type);
  }

  public static String getType(Player p) {
    return useType.get(p.getUniqueId());
  }

  public static String getType(UUID id) {
    return useType.get(id);
  }

  public static void load() {
    DungeonLogger.development("load last_save_type.txt");

    ArrayList<String> readFile;
    try {
      readFile = InOutputUtil.readFile("last_save_type.txt");
      if (readFile.isEmpty()) {
        useType = new HashMap<>();
      }

      String json = StringUtils.join(readFile.iterator(), "");
      Gson gson = new Gson();

      @SuppressWarnings("unchecked")
      HashMap<String, String> data = gson.fromJson(json, HashMap.class);
      for (Entry<String, String> entry : data.entrySet()) {
        useType.put(UUID.fromString(entry.getKey()), entry.getValue());
      }

      if (useType == null) { throw new LbnRuntimeException("json is invalid: last_save_type.txt"); }
    } catch (Exception e) {
      e.printStackTrace();
      useType = new HashMap<>();
    }
  }

  public static void save() {
    DungeonLogger.development("save last_save_type.txt");

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(useType);

    InOutputUtil.write(json, "last_save_type.txt");
  }
}
