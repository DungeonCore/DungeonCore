package net.l_bulb.dungeoncore.mobspawn.point;

import java.util.ArrayList;
import java.util.HashSet;

import net.l_bulb.dungeoncore.mobspawn.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointMonitor;

public class SpawnScheduler {
  int spawnTick;

  ArrayList<MobSpawnerPoint> spawnPointList = new ArrayList<>();

  public SpawnScheduler(SpawnLevel level) {
    spawnTick = level.getSpawnTick();
  }

  public SpawnPointMonitor getMonitor(MobSpawnerPoint point) {
    SpawnPointMonitor spawnPointMonitor = new SpawnPointMonitor(point);
    spawnPointMonitor.setWaitTime(getWaitingTime(point));
    spawnPointMonitor.setWorking(isWorking(point));
    spawnPointMonitor.setWillDeleteList(willDeleteList.contains(point));
    spawnPointMonitor.setDetail(spawnPointList.indexOf(point) + "%" + spawnTick + "-" + count);
    return spawnPointMonitor;
  }

  public boolean isWorking(MobSpawnerPoint point) {
    if (spawnPointList.contains(point) && !willDeleteList.contains(point)) { return true; }
    return false;
  }

  public int getWaitingTime(MobSpawnerPoint point) {
    if (!spawnPointList.contains(point)) { return -1; }

    int oneTarmTick = spawnPointList.indexOf(point) % spawnTick;
    int differenceTick = oneTarmTick - count;

    if (differenceTick < 0) {
      return differenceTick + spawnTick;
    } else {
      return differenceTick;
    }
  }

  /**
   * スケジューラにスポーンポイントをセットする
   *
   * @param spawnPoint
   */
  public void addSpawnPoint(MobSpawnerPoint spawnPoint) {
    if (!spawnPointList.contains(spawnPoint)) {
      spawnPointList.add(spawnPoint);
    }
    willDeleteList.remove(spawnPoint);
  }

  ArrayList<MobSpawnerPoint> currentSpawnPointList = new ArrayList<>();

  // カウント
  int count = 0;

  /**
   * 現在のスポーンポイントを取得(chunkがアンロードされたものも存在する可能性あり)
   *
   * @param count
   */
  public ArrayList<MobSpawnerPoint> getCurrentSpawnPoint() {
    currentSpawnPointList.clear();
    for (int i = count; i < spawnPointList.size(); i += spawnTick) {
      MobSpawnerPoint mobSpawnPointInterface = spawnPointList.get(i);
      currentSpawnPointList.add(mobSpawnPointInterface);
    }
    if (count >= spawnTick) {
      count = 0;
    } else {
      count++;
    }
    return currentSpawnPointList;
  }

  HashSet<MobSpawnerPoint> willDeleteList = new HashSet<>();

  public void removeSpawnPoint(boolean isForce, MobSpawnerPoint... deleteSpawnPointList) {
    for (MobSpawnerPoint mobSpawnPointInterface : deleteSpawnPointList) {
      if (spawnPointList.contains(mobSpawnPointInterface)) {
        willDeleteList.add(mobSpawnPointInterface);
      }
    }

    if (isForce || willDeleteList.size() > 100) {
      spawnPointList.removeAll(willDeleteList);
      willDeleteList.clear();
    }
  }

  public int getSize() {
    return spawnPointList.size() - willDeleteList.size();
  }

  public void clear() {
    spawnPointList.clear();
    currentSpawnPointList.clear();
    willDeleteList.clear();
  }
}