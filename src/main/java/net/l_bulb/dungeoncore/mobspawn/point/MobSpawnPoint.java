package net.l_bulb.dungeoncore.mobspawn.point;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;
import org.bukkit.Chunk;
import org.bukkit.Location;

import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mobspawn.old.SpawnLevel;

public class MobSpawnPoint {
  // 湧くモンスター
  ArrayList<AbstractMob<?>> spawnMobList = new ArrayList<>();

  // スポーンポイントID
  int id;

  // スポーンポイントの場所
  Location location;

  // スポーンレベル
  SpawnLevel level;

  // チャンク
  Chunk chunk;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
    this.chunk = location.getChunk();
  }

  public void addSpawnMob(AbstractMob<?> mob) {
    spawnMobList.add(mob);
  }

  public Chunk getChunk() {
    Validate.isTrue(chunk == location.getChunk());
    return chunk;
  }

  public void setLevel(SpawnLevel level) {
    this.level = level;
  }

  public SpawnLevel getLevel() {
    return level;
  }

}
