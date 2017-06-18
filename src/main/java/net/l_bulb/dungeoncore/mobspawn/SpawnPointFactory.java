package net.l_bulb.dungeoncore.mobspawn;

import java.util.Collection;
import java.util.Set;

import org.bukkit.Location;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mobspawn.spawnPointImpl.BossMonsterSpawnPoint;
import net.l_bulb.dungeoncore.mobspawn.spawnPointImpl.ItemSpawnPoint;
import net.l_bulb.dungeoncore.mobspawn.spawnPointImpl.MonsterSpawnPoint;
import net.l_bulb.dungeoncore.util.JavaUtil;

import com.google.common.collect.HashMultimap;

public class SpawnPointFactory {
  private static int maxId;

  /**
   * 新しいインスタンスを作成する
   *
   * @param data
   * @return
   */
  public static SpawnPoint getNewInstance(SpawnPointSpreadSheetData data) {
    maxId = Math.max(data.getId(), maxId);

    String targetName = JavaUtil.getValueOrDefaultWhenThrow(() -> data.getTargetName().replace("_", " "), "");
    switch (data.getType()) {
      case MONSTER:
        AbstractMob<?> mob = MobHolder.getMob(targetName);
        if (mob != null && !mob.isNullMob()) { return new MonsterSpawnPoint(data, mob); }
        break;
      case ITEM:
        ItemInterface customItem = ItemManager.getCustomItemById(targetName);
        if (customItem != null) { return new ItemSpawnPoint(data, customItem); }
        break;
      case BOSS:
        AbstractMob<?> mob1 = MobHolder.getMob(targetName);
        if (mob1 != null) { return new BossMonsterSpawnPoint(data, mob1); }
        break;
      default:
        break;
    }
    return null;
  }

  /**
   * IDを更新し新しいIDを作成する
   *
   * @return
   */
  public static int getNextId() {
    maxId++;
    return maxId;
  }

  static HashMultimap<Location, SpawnPoint> spawnPointMap = HashMultimap.create();
  static HashMultimap<Integer, SpawnPoint> spawnPointIdMap = HashMultimap.create();

  /**
   * スポーンポイントを追加する
   *
   * @param point
   */
  static void addSpawnPoint(SpawnPoint point) {
    spawnPointMap.put(point.getLocation(), point);
    spawnPointIdMap.put(point.getId(), point);
  }

  static void clear() {
    spawnPointMap.clear();
    spawnPointIdMap.clear();
  }

  /**
   * すべてのスポーンポイントを取得する
   *
   * @return
   */
  public static Collection<SpawnPoint> getSpawnPointList() {
    return spawnPointIdMap.values();
  }

  /**
   * すべてのスポーンポイントを取得する
   *
   * @return
   */
  public static Set<Location> getSpawnPointLocation() {
    return spawnPointMap.keySet();
  }

  /**
   * Locationからスポーンポイントを取得する
   *
   * @param loc
   * @return
   */
  public static Set<SpawnPoint> getSpawnPointFromLocation(Location loc) {
    return spawnPointMap.get(loc);
  }

  /**
   * IDからスポーンポイントを取得する
   *
   * @param id
   * @return
   */
  public static Set<SpawnPoint> getSpawnPointFromId(int id) {
    return spawnPointIdMap.get(id);
  }

}
