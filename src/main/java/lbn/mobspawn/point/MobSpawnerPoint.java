package lbn.mobspawn.point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import lbn.mob.AbstractMob;
import lbn.mob.customMob.abstractmob.AbstractCombinationMob;
import lbn.mobspawn.ChunkWrapper;
import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.gettter.SpawnMobGetterInterface;

public class MobSpawnerPoint {
  public SpawnMobGetterInterface mobGetter;
  protected int maxMobCount;
  protected SpawnLevel level;
  protected Location loc;

  protected double locY;
  protected boolean lookNearChunk = true;

  public boolean isLookNearChunk() {
    return lookNearChunk;
  }

  public int getDungeonHight() {
    return dungeonHight;
  }

  public void setLookNearChunk(boolean lookNearChunk) {
    this.lookNearChunk = lookNearChunk;
  }

  ChunkWrapper chunk;

  private List<ChunkWrapper> nearChunkList = new ArrayList<ChunkWrapper>();

  HashSet<String> mobNameList = new HashSet<>();

  protected int id = -1;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getMaxMobCount() {
    return maxMobCount;
  }

  public SpawnLevel getLevel() {
    return level;
  }

  public MobSpawnerPoint(Location loc, SpawnMobGetterInterface mobGetter, int maxMobCount, SpawnLevel level) {
    this.loc = loc.getBlock().getLocation();
    chunk = new ChunkWrapper(loc.getChunk());
    this.locY = loc.getY();

    int xKind = (int) Math.signum(loc.getBlockX() - loc.getChunk().getX() * 16 - 7.5);
    int zKind = (int) Math.signum(loc.getBlockZ() - loc.getChunk().getZ() * 16 - 7.5);

    // 周囲のチャンクを保存する
    nearChunkList.add(new ChunkWrapper(loc.clone().add(8 * xKind, 0, 0).getChunk()));
    nearChunkList.add(new ChunkWrapper(loc.clone().add(8 * xKind, 0, 8 * zKind).getChunk()));
    nearChunkList.add(new ChunkWrapper(loc.clone().add(0, 0, 8 * zKind).getChunk()));

    this.mobGetter = mobGetter;
    this.maxMobCount = maxMobCount;
    this.level = level;

    for (AbstractMob<?> mob : mobGetter.getAllMobList()) {
      // nullmobの時は特別処理
      if (mob.isNullMob()) {
        mobNameList.add("normal:" + mob.getEntityType());
        continue;
      }
      mobNameList.add(mob.getName());
      if (mob instanceof AbstractCombinationMob<?>) {
        for (AbstractMob<?> subMob : ((AbstractCombinationMob<?>) mob).getCombinationMobListForSpawn()) {
          mobNameList.add(subMob.getName());
        }
      }
    }
  }

  public String getName() {
    return mobGetter.getName();
  }

  public SpawnMobGetterInterface getSpawnMobGetter() {
    return mobGetter;
  }

  public Chunk getChunk() {
    return chunk.getChunk();
  }

  public Location getLocation() {
    return loc;
  }

  public SpawnLevel getSpawnLevel() {
    return level;
  }

  Random rnd = new Random();

  public long lastSpawnTime = -1;
  public int lastSpawnCount = -1;
  public String cancelReson = null;

  boolean existNearPlayer = true;

  /**
   * スポーンしたMobの種類
   */
  public int spawnMob() {
    if (mobGetter.getAllMobList().size() == 0) {
      cancelReson = "dont regist mob in this spawn point";
      return 0;
    }

    existNearPlayer = false;

    int count = getNearEntity(getChunk(), true);
    // 周囲のチャンクも調べる
    for (ChunkWrapper chunk : nearChunkList) {
      count += getNearEntity(chunk.getChunk(), lookNearChunk);
    }

    // 残り召喚できるモブの数(maxMobCount - count)だけ召喚する
    for (int i = 0; i < maxMobCount - count; i++) {
      int nextInt = rnd.nextInt(mobGetter.getAllMobList().size());
      AbstractMob<?> abstractMob = mobGetter.getAllMobList().get(nextInt);
      abstractMob.spawn(getLocation().clone().add(0.5, 0, 0.5));
    }

    if (existNearPlayer) {
      cancelReson = "player dont exist!!";
      return 0;
    }

    if (maxMobCount - count > 0) {
      lastSpawnTime = System.currentTimeMillis();
      lastSpawnCount = maxMobCount - count;
      cancelReson = null;
    } else {
      cancelReson = "too many entity in near + (" + count + ")";
    }

    return maxMobCount - count;
  }

  protected int dungeonHight = 6;

  public void setDungeonHight(int dungeonHight) {
    this.dungeonHight = dungeonHight;
  }

  public ArrayList<LivingEntity> nearMob = new ArrayList<LivingEntity>();

  /**
   * このGetterに該当するMobを取得する
   * 
   * @param c
   * @param このチャンクのモンスターを調べるならTRUE
   * @return
   */
  protected int getNearEntity(Chunk c, boolean isSearch) {
    if (!c.isLoaded()) { return 0; }
    nearMob.clear();

    Entity[] entities = c.getEntities();
    for (Entity entity : entities) {
      if (entity.getType().isAlive() && entity.getType() != EntityType.PLAYER) {
        // モブの種類を調べる
        if (isSearch) {
          // モブの名前を調べる
          String name = ((LivingEntity) entity).getCustomName();
          if (name == null || name.isEmpty()) {
            name = "normal:" + entity.getType();
          }
          // mobの名前が一致しなければ別のモブとする
          if (!mobNameList.contains(name)) {
            continue;
          }

          // yがスポーンポイントから１６以上はなれていたら別のGetterのモブとする
          if (Math.abs(entity.getLocation().getY() - locY) >= dungeonHight) {
            continue;
          }
          nearMob.add((LivingEntity) entity);
        }
      } else if (entity.getType() == EntityType.PLAYER) {
        if (Math.abs(entity.getLocation().getY() - locY) <= 30) {
          existNearPlayer = true;
        }
      }
    }
    return nearMob.size();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof MobSpawnerPoint) { return ((MobSpawnerPoint) obj).getId() == getId(); }
    return false;
  }

  @Override
  public int hashCode() {
    return getId();
  }
}
