package net.l_bulb.dungeoncore.mobspawn;

import java.util.ArrayList;
import java.util.Comparator;
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

  @Getter
  int id;

  // チャンク
  @Getter
  ChunkGroup chunkGroup;

  // 最大カウント数
  int maxCount = 0;

  // スポーン時間の倍率
  @Getter
  double spawnTimeScale = 1;

  @Setter
  @Getter
  int beforeSpawnScheduleNumber;

  // テンプレートのスポーンポイント
  SpawnPoint firstSpawnPoint;

  @Getter
  int chunkHight;

  // 周囲のチャンクを見るならTRUE
  boolean isLookNearChunk = true;

  // スポーンポイントが設定されているチャンク間の最大距離
  private double maxChunkDistinct = 0;

  @Getter
  final EntityCounter counter = new EntityCounter();

  public SpawnPointGroup(SpawnPoint p, int id) {
    this.id = id;
    // スポーンポイント追加
    spawnPointList.add(p);
    // 中心のチャンクを設定
    chunkGroup = p.getChunkGroup();
    // 最大湧き数を設定
    setMaxCount(p.getMaxSpawnCount());
    // 最初のチャンクとして設定
    this.firstSpawnPoint = p;

    // 高さ
    chunkHight = p.getCheckHight();

    // 周囲のチャンクをみるならTRUE
    isLookNearChunk |= p.isCheckNearChunk();
  }

  /**
   * 最大湧き数をセットする
   *
   * @param maxCount
   */
  public void setMaxCount(int maxCount) {
    this.maxCount = Math.max(this.maxCount, maxCount);
  }

  public int getMaxCount() {
    // 2×2chunk分の湧き数なのでチャンク数に応じて最大湧き数を調整する
    if (maxChunkDistinct > 1.5) {
      return (int) (maxCount * ChunkGroup.ONE_SIDE_CHUNK_NUMBER / 2.0 * ChunkGroup.ONE_SIDE_CHUNK_NUMBER / 2.0);
    } else {
      return maxCount;
    }
  }

  /**
   * スポーンポイントを追加する
   */
  public void addSpawnPoint(SpawnPoint point) {
    spawnPointList.add(point);
    // 最大湧き数を更新する
    setMaxCount(point.getMaxSpawnCount());
    // 高さ
    chunkHight = Math.max(point.getCheckHight(), chunkHight);

    // chunk間の最大の距離を取得
    maxChunkDistinct = spawnPointList.stream().map(s -> s.getChunk())
        .mapToDouble(c1 -> spawnPointList.stream()
            // 同じチャンクなら無視する
            .filter(s2 -> c1 != s2.getChunk())
            // チャンク間の距離を取得
            .map(s2 -> ChunkGroup.getDistinct(c1, s2.getChunk())).max(Comparator.naturalOrder()).orElse(0.0))
        .max().orElse(0.0);
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
  public void consumeAllChunk(BiConsumer<Entity, ChunkData> consumer) {
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
