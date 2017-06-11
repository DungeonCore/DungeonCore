package net.l_bulb.dungeoncore.mobspawn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpawnResult {
  String message = "まだスポーンされていません。";

  int lastSpawnCount;

  long lastSpawnDate = 0;

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
  public long getSpawnSecoundAge() {
    if (lastSpawnDate == -1) { return -1; }

    return (System.currentTimeMillis() - lastSpawnCount) / 1000;
  }
}
