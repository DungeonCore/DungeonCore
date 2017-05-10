package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mobspawn.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.boss.SpredSheetSpawnBossGetter;
import net.l_bulb.dungeoncore.mobspawn.gettter.SpawnMobGetterInterface;
import net.l_bulb.dungeoncore.mobspawn.gettter.SpletSheetSpawnMobGetter;
import net.l_bulb.dungeoncore.mobspawn.point.MobSpawnerPoint;
import net.l_bulb.dungeoncore.mobspawn.point.MobSpawnerPointManager;
import net.l_bulb.dungeoncore.util.DungeonLogger;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class SpawnPointSheetRunnable extends AbstractComplexSheetRunable {

  public SpawnPointSheetRunnable(CommandSender p) {
    super(p);
  }

  public void addData(MobSpawnerPoint point, String memo) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("id", point.getId());
    Location location = point.getLocation();
    map.put("world", location.getWorld().getName());
    map.put("x", location.getX());
    map.put("y", location.getY());
    map.put("z", location.getZ());
    map.put("最大湧き数", point.getMaxMobCount());
    map.put("level", point.getLevel());
    SpawnMobGetterInterface spawnMobGetter = point.getSpawnMobGetter();

    int i = 1;
    for (AbstractMob<?> mob : spawnMobGetter.getAllMobList()) {
      String name = mob.getName();
      if (name == null || name.isEmpty()) {
        if (mob.getEntityType() == null) {
          // どうせ召喚できない, 起こり得ない
          DungeonLogger.error("mob's name and EntityType is null");
          continue;
        }
        name = mob.getEntityType().toString().toLowerCase();
      }
      map.put("mob" + i, name);
      i++;
    }
    if (memo != null && !memo.isEmpty()) {
      map.put("memo", memo);
    }
    addData(map);
  }

  @Override
  public String getSheetName() {
    return "spawnpoint";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "world", "x", "y", "z", "最大湧き数", "level", "dungeonhight", "looknearchunk", "mob1",
        "mob2", "mob3", "mob4", "mob5", "mob6", "mob7", "mob8", "mob9", "mob10", "mob11", "mob12", "mob13", "mob14",
        "mob15", "mob16", "mob17", "mob18", "mob19", "mob20" };
  }

  @Override
  protected void excuteOnerow(String[] row) {
    try {
      int id = Integer.parseInt(row[0]);
      World world = Bukkit.getWorld(row[1]);
      double x = Double.parseDouble(row[2]);
      double y = Double.parseDouble(row[3]);
      double z = Double.parseDouble(row[4]);
      int maxCount = Integer.parseInt(row[5]);

      SpawnLevel level = SpawnLevel.getLevel(row[6]);

      if (world == null) {
        sendMessage("world:" + row[1] + "が存在しません。:" + id);
        return;
      }

      SpletSheetSpawnMobGetter spletSheetSpawnMobGetter;
      if (level == SpawnLevel.BOSS) {
        spletSheetSpawnMobGetter = new SpredSheetSpawnBossGetter(id);
      } else {
        spletSheetSpawnMobGetter = new SpletSheetSpawnMobGetter(id);
      }
      for (int i = 9; i < getTag().length; i++) {
        boolean addMob = spletSheetSpawnMobGetter.addMob(row[i]);
        if (!addMob && row[i] != null && !row[i].isEmpty()) {
          sendMessage(row[i] + "は無視されました。");
        }
      }

      MobSpawnerPoint mobSpawnerPoint = spletSheetSpawnMobGetter.getMobSpawnerPoint(new Location(world, x, y, z),
          maxCount, level);
      mobSpawnerPoint.setDungeonHight(JavaUtil.getInt(row[7], 6));
      mobSpawnerPoint.setLookNearChunk(JavaUtil.getBoolean(row[8], true));
      MobSpawnerPointManager.addSpawnPoint(mobSpawnerPoint);
    } catch (Exception e) {
      e.printStackTrace();
      sendMessage("入力されたデータが不正です。(spawn mob 設定):" + Arrays.toString(row));
    }
  }

  @Override
  public void getData(String query) {
    if (query == null || query.isEmpty()) {
      query = "world!=\"\"";
    }
    super.getData(query);
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    MobSpawnerPointManager.clear();

    super.onCallbackFunction(submit);

    for (World world : Bukkit.getWorlds()) {
      // loadされているchunkを設定する
      Chunk[] loadedChunks = world.getLoadedChunks();
      for (Chunk chunk : loadedChunks) {
        MobSpawnerPointManager.loadChunk(chunk);
      }
    }
  }
}
