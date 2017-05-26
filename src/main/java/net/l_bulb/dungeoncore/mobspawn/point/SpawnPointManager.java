package net.l_bulb.dungeoncore.mobspawn.point;

import org.bukkit.Chunk;

import com.google.common.collect.HashMultimap;

public class SpawnPointManager {

  static HashMultimap<Chunk, MobSpawnPoint> chunkSpawnPointMap = HashMultimap.create();

  /**
   * スポーンポイントを登録する
   *
   * @param point
   */
  public static void registSpawnPoint(MobSpawnPoint point) {
    chunkSpawnPointMap.put(point.getChunk(), point);
  }

  public static void spawnMob() {
    // // チャンクがロード済みのスポーンポイントだけ取得
    // List<Entry<Chunk, MobSpawnPoint>> collect = chunkSpawnPointMap.entries().stream().filter(e ->
    // e.getKey().isLoaded()).collect(Collectors.toList());
  }

}
