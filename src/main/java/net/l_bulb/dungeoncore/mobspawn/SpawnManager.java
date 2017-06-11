package net.l_bulb.dungeoncore.mobspawn;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.util.DungeonLogger;
import net.l_bulb.dungeoncore.util.LbnRunnable;

import lombok.Getter;

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

    DungeonLogger.info(ChatColor.GREEN + "==MOB SPAWN SYSTEM START!!==");

    List<SpawnPointGroup> allSpawnPoint = SpawnPointGroupFactory.getAllSpawnPoint();
    new LbnRunnable() {
      @Override
      public void run2() {
        for (int i = SpawnScheduleNumber; i < allSpawnPoint.size(); i += SpawnPointRouteTick) {
          spawnEntity(allSpawnPoint.get(i));
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

  static Counter counter = new Counter();

  /**
   * Entityをスポーンする
   *
   * @param pointGroup
   */
  public static void spawnEntity(SpawnPointGroup pointGroup) {
    counter.clear();

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

    // チャンクからPlayerとスポーンするEntityの数を取得
    pointGroup.consume(e -> {
      // Playerならインクリメント
      if (e.getType() == EntityType.PLAYER) {
        counter.incrementPlayer();
      }
      // TargetのEntityならインクリメント
      if (pointGroup.isSameEntity(e)) {
        counter.incrementTarget();
      }
    });

    // Playerがいないならスポーンさせない
    if (counter.getPlayerCount() == 0) {
      pointGroup.acceptAllResult(result -> result.ofNotExistPlayer());
      return;
    }

    // スポーンできるモンスターの数に応じて順次スポーンさせる
    int canCountCount = pointGroup.getMaxCount() - counter.getTargetCount();
    if (canCountCount > 0) {
      Stream.generate(() -> pointGroup.next()).limit(canCountCount)
          // スポーンできるものだけスポーンさせる
          .filter(val -> val.canSpawn())
          // スポーンさせる
          .peek(val -> val.spawn())
          // スポーン数をインクリメントする
          .forEach(val -> val.getSpawnResult().incrementSpawnMob());
    }

    // スポーン結果を更新する
    pointGroup.acceptAllResult(result -> result.build(pointGroup.getSpawnTargetName()));
  }

  @Getter
  static class Counter {
    int playerCount = 0;
    int targetCount = 0;

    public void clear() {
      playerCount = 0;
      targetCount = 0;
    }

    public void incrementPlayer() {
      playerCount++;
    }

    public void incrementTarget() {
      targetCount++;
    }

  }

}
