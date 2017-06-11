package net.l_bulb.dungeoncore.mobspawn;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData.TargetType;
import net.l_bulb.dungeoncore.mobspawn.chunk.ChunkGroup;

public interface SpawnPoint {
  /**
   * IDを取得
   *
   * @return
   */
  public int getId();

  public Location getLocation();

  public Chunk getChunk();

  /**
   * チャンクグループを取得
   * 
   * @return
   */
  default public ChunkGroup getChunkGroup() {
    return new ChunkGroup(getLocation());
  }

  /**
   * 最大湧き数
   *
   * @return
   */
  int getMaxSpawnCount();

  /**
   * 周囲のチャンクを調べるならTRUE
   *
   * @return
   */
  boolean isCheckNearChunk();

  /**
   * 調べる高さ
   *
   * @return
   */
  int getCheckHight();

  /**
   * モンスターをスポーンさせることができならTRUE
   *
   * @return
   */
  boolean canSpawn();

  /**
   * モンスターをスポーンさせる
   *
   * @return TODO
   *
   * @return
   */
  Entity spawn();

  /**
   * 同じ種類のスポーンポイントならTRUE
   *
   * @param point
   * @return
   */
  boolean isSameAs(SpawnPoint point);

  /**
   * 指定されたEntityとスポーン予定のモンスターが同じものならTRUE
   *
   * @param e
   * @return
   */
  boolean equalsEntity(Entity e);

  /**
   * スポーン結果を取得する
   *
   * @return
   */
  public SpawnResult getSpawnResult();

  /**
   * スポーンターゲットの名前
   *
   * @return
   */
  public String getSpawnTargetName();

  public TargetType getTargetType();
}
