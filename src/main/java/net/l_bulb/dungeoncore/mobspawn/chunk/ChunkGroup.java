package net.l_bulb.dungeoncore.mobspawn.chunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import net.l_bulb.dungeoncore.util.JavaUtil;

import lombok.Data;

/**
 * (3n+2,3m)(3n+2, 3m+1) (3n+2, 3m+2)
 * (3n+1,3m)(3n+1, 3m+1) (3n+1, 3m+2)
 * (3n,3m) (3n, 3m+1) (3n, 3m+2)
 *
 * @author KENSUKE
 *
 */
@Data
public class ChunkGroup {

  transient List<ChunkData> chunkList = null;

  // 一辺のチャンク数
  public static final int ONE_SIDE_CHUNK_NUMBER = 3;

  // 左下のChunkの番号
  int x;
  int z;
  // 中心のy座標
  int y;

  transient String worldName;

  World world;

  public ChunkGroup(Location loc) {
    Chunk c = loc.getChunk();
    x = c.getX();
    z = c.getZ();
    world = c.getWorld();

    int xRemainder = x % ONE_SIDE_CHUNK_NUMBER;
    if (xRemainder < 0) {
      xRemainder += ONE_SIDE_CHUNK_NUMBER;
    }
    x -= xRemainder;

    int zRemainder = z % ONE_SIDE_CHUNK_NUMBER;
    if (zRemainder < 0) {
      zRemainder += ONE_SIDE_CHUNK_NUMBER;
    }
    z -= zRemainder;

    y = (int) (loc.getBlockY() / 16.0) * 16 + 8;
  }

  /**
   * 周囲ののチャンクのEntityに対してconsumerを実行する
   */
  public void consumeChunk(Predicate<Entity> predicate, BiConsumer<Entity, ChunkData> consumer) {
    initChunkData();
    chunkList.stream().filter(c -> c.isLoaded())
        .forEach(c -> Arrays.stream(c.getEntities()).filter(predicate::test).forEach(e -> consumer.accept(e, c)));
  }

  /**
   * チャンクの情報を初期化する
   */
  protected void initChunkData() {
    if (chunkList == null) {
      chunkList = new ArrayList<>();

      IntStream.rangeClosed(-1, ONE_SIDE_CHUNK_NUMBER)
          .forEach(dx -> IntStream.rangeClosed(-1, ONE_SIDE_CHUNK_NUMBER)
              .mapToObj(dz -> world.getChunkAt(x + dx, z + dz))
              .map(c -> new ChunkData(c, JavaUtil.inInt2(c.getX() - x, 0, ONE_SIDE_CHUNK_NUMBER - 1)
                  && JavaUtil.inInt2(c.getZ() - z, 0, ONE_SIDE_CHUNK_NUMBER - 1)))
              .forEach(chunkList::add));
    }
  }

  /**
   * チャンクが1つでもLoadされていたらTRUE
   *
   * @return
   */
  public boolean isLoaded() {
    initChunkData();
    return chunkList.stream().filter(c -> c.isNear()).anyMatch(c -> c.isLoaded());
  }

  /**
   * チャンク間の距離を取得する
   *
   * @param chunk1
   * @param chunk2
   * @return
   */
  public static double getDistinct(Chunk chunk1, Chunk chunk2) {
    int dx = chunk1.getX() - chunk2.getX();
    int dz = chunk1.getZ() - chunk2.getZ();

    return Math.sqrt(dx * dx + dz * dz);
  }
}
