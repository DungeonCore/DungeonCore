package net.l_bulb.dungeoncore.mobspawn;

import java.util.List;
import java.util.stream.Stream;

import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.util.DungeonLogger;
import net.l_bulb.dungeoncore.util.LbnRunnable;

public class SpawnManager {
  // スポーンさせる周期Tick
  public static int SpawnPointRouteTick = 35 * 20;

  private static int SpawnScheduleNumber = 0;

  /**
   * スポーンスケジュール処理を開始する
   */
  public static void startSpawnEntity() {
    // スポーンさせない
    if (!Main.isDoSpawn()) { return; }

    DungeonLogger.info("==MOB SPAWN SYSTEM START!!==");

    List<SpawnPointGroup> allSpawnPoint = SpawnPointGroupFactory.getAllSpawnPoint();
    new LbnRunnable() {
      @Override
      public void run2() {
        for (int i = SpawnScheduleNumber; i < allSpawnPoint.size(); i += SpawnPointRouteTick) {
          SpawnPointGroup pointGroup = allSpawnPoint.get(i);
          if (pointGroup != null) {
            // カウンター開始
            pointGroup.getCounter().start();
            // スポーン処理
            spawnEntity(pointGroup);
            // カウンター終了
            pointGroup.getCounter().end();
          }
        }
        SpawnScheduleNumber++;
        SpawnScheduleNumber %= SpawnPointRouteTick;
      }
    }.runTaskTimer(1);
  }

  /**
   * スポーンスケジューラーのカウントを取得する
   *
   * @return
   */
  public static int getSpawnScheduleNumber() {
    return SpawnScheduleNumber;
  }

  /**
   * Entityをスポーンする
   *
   * @param pointGroup
   */
  public static void spawnEntity(SpawnPointGroup pointGroup) {
    EntityCounter counter = pointGroup.getCounter();
    // 近くの敵の数を未知数にしておく
    pointGroup.acceptAllResult(r -> r.setCanSpawnCount("unknown"));

    pointGroup.setBeforeSpawnScheduleNumber(SpawnScheduleNumber);

    // スポーンできないならスポーンさせない
    if (!pointGroup.canSpawn()) {
      pointGroup.acceptAllResult(result -> result.ofPointGroupCantSpawn());
      return;
    }

    // チャンクがLoadされていないならスポーンさせない
    if (!pointGroup.getChunkGroup().isLoaded()) {
      pointGroup.acceptAllResult(result -> result.ofUnloadChunk());
      return;
    }

    // 周囲のチャンクからEntityとPlayer数をカウント
    pointGroup.consumeAllChunk(counter.getCountUpConsumer(pointGroup)::accept);

    // Playerがいないならスポーンさせない
    if (counter.getPlayerCount() == 0) {
      pointGroup.acceptAllResult(result -> result.ofNotExistPlayer());
      return;
    }

    // スポーンできるモンスターの数に応じて順次スポーンさせる
    int canCountCount = pointGroup.getMaxCount() - counter.getTargetCount();
    // モブの数を記録する
    pointGroup.acceptAllResult(r -> r.setCanSpawnCount(canCountCount + "体"));
    if (canCountCount <= 0) {
      pointGroup.acceptAllResult(result -> result.ofMostEntityCount(counter.getTargetCount()));
      return;
    }

    // 一旦全てのスポーンポイントを書き換える
    pointGroup.acceptAllResult(result -> result.ofOtherSpawnPointRun());

    Stream.generate(() -> pointGroup.next()).limit(canCountCount)
        // スポーンできるものだけスポーンさせる
        .filter(val -> val.canSpawn())
        // スポーンさせる
        .peek(val -> val.spawn())
        // スポーン数をインクリメントする
        .forEach(val -> val.getSpawnResult().incrementSpawnMob());

    // スポーン結果を更新する
    pointGroup.acceptAllResult(result -> result.build(pointGroup.getSpawnTargetName()));
  }
}
