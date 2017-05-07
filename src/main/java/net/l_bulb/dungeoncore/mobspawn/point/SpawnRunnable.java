package net.l_bulb.dungeoncore.mobspawn.point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.SystemListener;
import net.l_bulb.dungeoncore.mobspawn.ChunkWrapper;
import net.l_bulb.dungeoncore.mobspawn.SpawnLevel;

public class SpawnRunnable extends BukkitRunnable {
  public SpawnRunnable(boolean isRunManage,
      HashMap<SpawnLevel, SpawnScheduler> schedulerList,
      HashSet<ChunkWrapper> loadedChunk) {
    this.schedulerList = schedulerList;
    this.loadedChunk = loadedChunk;
  }

  private HashMap<SpawnLevel, SpawnScheduler> schedulerList;
  private HashSet<ChunkWrapper> loadedChunk;

  public HashMap<SpawnLevel, String> schedulerDetail = new HashMap<>();

  @Override
  public void run() {
    if (SystemListener.loginPlayer == 0) {
      for (SpawnLevel level : SpawnLevel.values()) {
        schedulerDetail.put(level, "FALSE:not player");
      }
      return;
    }

    // 全てのレベルを走査する
    for (SpawnLevel level : SpawnLevel.values()) {
      SpawnScheduler scheduler = schedulerList.get(level);
      if (scheduler == null) {
        schedulerDetail.put(level, "FALSE:scheduler is not found");
        continue;
      }

      if (scheduler.spawnPointList.isEmpty()) {
        schedulerDetail.put(level, "FALSE:spawnPoint is not found");
        continue;
      }

      int spawnMpbCount = 0;
      int spawnPointCount = 0;

      ArrayList<MobSpawnerPoint> spawnPointList = scheduler.getCurrentSpawnPoint();
      for (MobSpawnerPoint spawnPoint : spawnPointList) {
        if (loadedChunk.contains(new ChunkWrapper(spawnPoint))) {
          spawnPointCount++;
          spawnMpbCount += spawnPoint.spawnMob();
        }
      }
      schedulerDetail.put(level, StringUtils.join(new Object[] { "TRUE:spawnpoint(", spawnPointCount, "), spawnmob(", spawnMpbCount, ")" }));
      Collections.shuffle(spawnPointList);
    }
  }

}
