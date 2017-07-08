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
  static SpawnPointFactory INSTANCE = new SpawnPointFactory();

  private SpawnPointFactory() {}

  public static SpawnPointFactory getInstance() {
    return INSTANCE;
  }

  private int maxId;

  /**
   * 新しいインスタンスを作成する
   *
   * @param data
   * @return
   */
  public SpawnPoint getNewInstance(SpawnPointSpreadSheetData data) {
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
        if (mob1 != null && mob1.isBoss()) { return new BossMonsterSpawnPoint(data, mob1); }
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
  public int getNextId() {
    maxId++;
    return maxId;
  }

  static HashMultimap<Location, SpawnPoint> spawnPointLocationMap = HashMultimap.create();
  static HashMultimap<Integer, SpawnPoint> spawnPointIdMap = HashMultimap.create();

  /**
   * スポーンポイントを追加する
   *
   * @param point
   */
  void addSpawnPoint(SpawnPoint point) {
    spawnPointLocationMap.put(point.getLocation(), point);
    spawnPointIdMap.put(point.getId(), point);

    if (point.getId() == 513) {
      System.out.println(spawnPointIdMap.get(513));
    }
  }

  void clear() {
    spawnPointLocationMap.clear();
    spawnPointIdMap.clear();
  }

  /**
   * すべてのスポーンポイントを取得する
   *
   * @return
   */
  public Collection<SpawnPoint> getSpawnPointList() {
    return spawnPointIdMap.values();
  }

  /**
   * すべてのスポーンポイントを取得する
   *
   * @return
   */
  public Set<Location> getSpawnPointLocation() {
    return spawnPointLocationMap.keySet();
  }

  /**
   * Locationからスポーンポイントを取得する
   *
   * @param loc
   * @return
   */
  public Set<SpawnPoint> getSpawnPointFromLocation(Location loc) {
    return spawnPointLocationMap.get(loc);
  }

  /**
   * IDからスポーンポイントを取得する
   *
   * @param id
   * @return
   */
  public Set<SpawnPoint> getSpawnPointFromId(int id) {
    return spawnPointIdMap.get(id);
  }

}
