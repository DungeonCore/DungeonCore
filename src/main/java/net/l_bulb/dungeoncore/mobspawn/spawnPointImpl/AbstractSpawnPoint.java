package net.l_bulb.dungeoncore.mobspawn.spawnPointImpl;

import org.bukkit.Chunk;
import org.bukkit.Location;

import net.l_bulb.dungeoncore.mobspawn.SpawnPoint;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData.TargetType;
import net.l_bulb.dungeoncore.mobspawn.SpawnResult;

public abstract class AbstractSpawnPoint implements SpawnPoint {
  private SpawnPointSpreadSheetData data;

  public AbstractSpawnPoint(SpawnPointSpreadSheetData data) {
    this.data = data;
    this.c = data.getLocation().getChunk();
  }

  @Override
  public int getId() {
    return data.getId();
  }

  @Override
  public Location getLocation() {
    return data.getLocation();
  }

  Chunk c;

  @Override
  public Chunk getChunk() {
    return c;
  }

  @Override
  public int getMaxSpawnCount() {
    return data.getMaxSpawnCount();
  }

  @Override
  public boolean isCheckNearChunk() {
    return data.isLookNearChunk();
  }

  @Override
  public int getCheckHight() {
    return data.getDungeonHight();
  }

  @Override
  public boolean canSpawn() {
    return true;
  }

  SpawnResult spawnResult = new SpawnResult();

  @Override
  public SpawnResult getSpawnResult() {
    return spawnResult;
  }

  /**
   * スポーンのターゲットタイプを取得
   *
   * @return
   */
  @Override
  public TargetType getTargetType() {
    return data.getType();
  }

  @Override
  public boolean isSameAs(SpawnPoint point) {
    return point.getTargetType() == getTargetType() && point.getSpawnTargetName().equals(getSpawnTargetName());
  }
}
