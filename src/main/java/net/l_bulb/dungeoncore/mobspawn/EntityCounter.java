package net.l_bulb.dungeoncore.mobspawn;

import java.text.MessageFormat;
import java.util.function.BiConsumer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.l_bulb.dungeoncore.mobspawn.chunk.ChunkData;
import net.l_bulb.dungeoncore.util.DungeonLogger;

import lombok.Getter;
import lombok.Setter;

@Getter
class EntityCounter {
  int playerCount = 0;
  int targetCount = 0;
  int entityCount = 0;

  // デバックモードならTRUE
  @Setter
  boolean isDebug = false;

  /**
   * スポーン処理開始時の処理
   */
  public void start() {
    playerCount = 0;
    targetCount = 0;
    entityCount = 0;
  }

  /**
   * スポーン処理終了時の処理
   */
  public void end() {
    isDebug = false;
  }

  public void incrementPlayer() {
    playerCount++;
  }

  public void incrementTarget() {
    targetCount++;
  }

  public void incrementEntity() {
    entityCount++;
  }

  public BiConsumer<Entity, ChunkData> getCountUpConsumer(SpawnPointGroup pointGroup) {
    return new BiConsumer<Entity, ChunkData>() {
      @Override
      public void accept(Entity e, ChunkData c) {
        // Entityをインクリメント
        incrementEntity();

        // Playerならインクリメント
        if (e.getType() == EntityType.PLAYER) {
          incrementPlayer();
        }

        // 近くのチャンクでないならカウントしない
        if (!c.isNear()) {
          print("近くのチャンクでないのでカウントしません。 name:{0} , type:{1}", e.getCustomName(), e.getType());
          return;
        }

        // entityのy座標がchunkHightの範囲に収まっていないならカウントしない
        if (Math.abs(e.getLocation().getY() - pointGroup.getY()) > pointGroup.getChunkHight()) {
          print("y座標が異なるのでカウントしません。 name:{0} , type:{1}", e.getCustomName(), e.getType());
          return;
        }

        // 同じ種類のmobでないならカウントしない
        if (!pointGroup.isSameEntity(e)) {
          print("同じ種類のmobでないためカウントしません。 name:{0} , type:{1}", e.getCustomName(), e.getType());
          return;
        }
        print("カウントしました。 name:{0} , type:{1}, count:{2}", e.getCustomName(), e.getType(), targetCount);

        // TargetのEntityならインクリメント
        incrementTarget();
      }
    };
  }

  private void print(String msg, Object... param) {
    if (isDebug) {
      DungeonLogger.debug(MessageFormat.format(msg, param));
    }
  }
}