package net.l_bulb.dungeoncore.mobspawn;

import net.l_bulb.dungeoncore.util.JavaUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpawnResult {
  String message = "まだスポーンされていません。";

  int lastSpawnCount;

  long lastSpawnDate = 0;

  String canSpawnCount = "unknown";

  public void setReslt(int lastSpawnCount, String message) {
    setLastSpawnCount(lastSpawnCount);
    setMessage(message);
  }

  public void ofPointGroupCantSpawn() {
    setReslt(0, "このスポーンポイントグループはスポーンが許可されれていません。");
  }

  public void ofUnloadChunk() {
    setReslt(0, "すべてのチャンクがUnloadされているためスポーンされませんでした。");
  }

  public void ofNotExistPlayer() {
    setReslt(0, "プレイヤーが周囲のチャンクにいないためスポーンされませんでした。");
  }

  public void ofMostEntityCount(int entityCount) {
    setReslt(0, "スポーンポイントの周りにモンスターが多すぎます(" + entityCount + "体)");
  }

  public void ofOtherSpawnPointRun() {
    setReslt(0, "他のスポーンポイントでモンスターがスポーンしたためこのスポーンポイントはスキップされました。");
  }

  int tempLastSpawnCount = -1;

  /**
   * スポーン数を増加させる
   */
  public void incrementSpawnMob() {
    if (tempLastSpawnCount == -1) {
      tempLastSpawnCount = 0;
    }
    tempLastSpawnCount++;
  }

  public void build(String spawTargetName) {
    // 1体でもスポーンしたときはスポーン数を上書きする
    if (tempLastSpawnCount > -1) {
      lastSpawnCount = tempLastSpawnCount;
      setMessage(spawTargetName + "をスポーンしました。");
      // スポーンした時間を取得
      lastSpawnDate = System.currentTimeMillis();
    }
    tempLastSpawnCount = -1;
  }

  /**
   * 何秒前にスポーンしたかを取得。スポーンしてない場合は-1を返す
   *
   * @return
   */
  public double getSpawnSecoundAge() {
    if (lastSpawnDate == 0) { return -1; }
    return JavaUtil.round((System.currentTimeMillis() - lastSpawnDate) / 1000.0, 2);
  }
}
