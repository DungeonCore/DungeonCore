package net.l_bulb.dungeoncore.mobspawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.l_bulb.dungeoncore.mobspawn.chunk.ChunkGroup;

import com.google.common.collect.HashMultimap;

public class SpawnPointGroupFactory {

  static List<SpawnPointGroup> spawnPointGroupList = new ArrayList<>();

  static HashMultimap<ChunkGroup, SpawnPointGroup> spawnPointGroupChunkMap = HashMultimap.create();

  static int SPAWN_GROUP_ID = 0;

  /**
   * スポーンポイントを登録する
   *
   * @param point
   */
  public static void registSpawnPoint(SpawnPoint point) {
    // スポーンポイントを登録
    SpawnPointFactory.addSpawnPoint(point);

    ChunkGroup chunkGroup = point.getChunkGroup();

    // すでにスポーンポイントグループが存在するか確認する
    Optional<SpawnPointGroup> spawnPointGroup = spawnPointGroupChunkMap.get(chunkGroup).stream().filter(s -> s.isSameAs(point)).findFirst();

    if (spawnPointGroup.isPresent()) {
      spawnPointGroup.get().addSpawnPoint(point);
    } else {
      // 空の場合は新しく作成し追加する
      SpawnPointGroup pointGroup = new SpawnPointGroup(point, SPAWN_GROUP_ID);
      SPAWN_GROUP_ID++;

      spawnPointGroupList.add(pointGroup);
      spawnPointGroupChunkMap.put(pointGroup.getChunkGroup(), pointGroup);
    }
  }

  /**
   * 指定したスポーンポイントが所属しているスポーンポイントグループを習得する
   */
  public static SpawnPointGroup getSpawnPointGroup(SpawnPoint point) {
    return spawnPointGroupChunkMap.get(new ChunkGroup(point.getLocation())).stream()
        .filter(s -> s.isSameAs(point)).findFirst().orElse(null);
  }

  /**
   * すべてのスポーンポイントグループを取得する
   *
   * @return
   */
  public static List<SpawnPointGroup> getAllSpawnPoint() {
    return spawnPointGroupList;
  }

  /**
   * すべてのスポーンポイントを削除する
   */
  public static void clear() {
    spawnPointGroupList.clear();
    spawnPointGroupChunkMap.clear();
    SpawnPointFactory.clear();
    SPAWN_GROUP_ID = 0;
  }
}
