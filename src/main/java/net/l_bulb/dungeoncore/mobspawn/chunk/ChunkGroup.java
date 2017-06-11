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
 * (2n+1,2m)(2n+1, 2m+1)
 * (2n,2m)(2n, 2m+1)
 *
 * @author KENSUKE
 *
 */
@Data
public class ChunkGroup {

  transient List<ChunkData> chunkList = null;

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

    if (this.x % 2 != 0) {
      x--;
    }
    if (this.z % 2 != 0) {
      z--;
    }

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

      IntStream.rangeClosed(-1, 2)
          .forEach(dx -> IntStream.rangeClosed(-1, 2)
              .mapToObj(dz -> world.getChunkAt(x + dx, z + dz))
              .map(c -> new ChunkData(c, JavaUtil.inInt2(c.getX() - x, 0, 1) && JavaUtil.inInt2(c.getZ() - z, 0, 1)))
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
}
