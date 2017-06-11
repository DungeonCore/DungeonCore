package net.l_bulb.dungeoncore.mobspawn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.entity.Entity;

import net.l_bulb.dungeoncore.mobspawn.chunk.ChunkData;
import net.l_bulb.dungeoncore.mobspawn.chunk.ChunkGroup;

import lombok.Getter;
import lombok.Setter;

public class SpawnPointGroup {
  // スポーンポイントのリスト
  ArrayList<SpawnPoint> spawnPointList = new ArrayList<>();

  // チャンク
  @Getter
  ChunkGroup chunkGroup;

  // 最大カウント数
  @Getter
  int maxCount;

  @Setter
  @Getter
  int beforeSpawnScheduleNumber;

  // テンプレートのスポーンポイント
  SpawnPoint firstSpawnPoint;

  @Getter
  int chunkHight;

  // 周囲のチャンクを見るならTRUE
  boolean isLookNearChunk = true;

  public SpawnPointGroup(SpawnPoint p) {
    // スポーンポイント追加
    spawnPointList.add(p);
    // 中心のチャンクを設定
    chunkGroup = p.getChunkGroup();
    // 最大湧き数を設定
    maxCount = p.getMaxSpawnCount();
    // 最初のチャンクとして設定
    this.firstSpawnPoint = p;

    // 高さ
    chunkHight = p.getCheckHight();

    // 周囲のチャンクをみるならTRUE
    isLookNearChunk |= p.isCheckNearChunk();
  }

  /**
   * スポーンポイントを追加する
   */
  public void addSpawnPoint(SpawnPoint point) {
    spawnPointList.add(point);
    // 最大湧き数を更新する
    maxCount = Math.max(maxCount, point.getMaxSpawnCount());
    // 高さ
    chunkHight = Math.max(point.getCheckHight(), chunkHight);
  }

  /**
   * スポーンポイントのリストを取得する
   *
   * @return
   */
  public List<SpawnPoint> getSpawnPointList() {
    return spawnPointList;
  }

  int index = 0;

  /**
   * 次にスポーンさせるスポーンポイントを取得
   *
   * @return
   */
  public SpawnPoint next() {
    if (index >= spawnPointList.size()) {
      index %= spawnPointList.size();
    }
    SpawnPoint spawnPoint = spawnPointList.get(index);
    index++;
    return spawnPoint;
  }

  public boolean canSpawn() {
    return true;
  }

  /**
   * 同じ種類のEntityをスポーンするならTRUE
   *
   * @param point
   * @return
   */
  public boolean isSameAs(SpawnPoint point) {
    return firstSpawnPoint.isSameAs(point);
  }

  /**
   * 指定したEntityがスポーンするEntityと同じ種類ならTRUE
   *
   * @param point
   * @return
   */
  public boolean isSameEntity(Entity e) {
    return firstSpawnPoint.equalsEntity(e);
  }

  /**
   * このチャンクの捜査範囲のEntityに対してConsumerを実行する
   *
   * @param consumer
   */
  public void consumeNearChunk(BiConsumer<Entity, ChunkData> consumer) {
    chunkGroup.consumeChunk(e -> true, consumer);
  }

  /**
   * 指定したConsumerをすべてのスポーンポイントに適応させる
   *
   * @param consumer
   */
  public void acceptAllResult(Consumer<SpawnResult> consumer) {
    spawnPointList.stream().map(s -> s.getSpawnResult()).forEach(consumer::accept);
  }

  /**
   * ターゲット名を取得
   */
  public String getSpawnTargetName() {
    return firstSpawnPoint.getSpawnTargetName();
  }

  /**
   * このスポーンポイントのY座標の中心を取得
   *
   * @return
   */
  public int getY() {
    return chunkGroup.getY();
  }

}
