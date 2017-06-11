package net.l_bulb.dungeoncore.mobspawn;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

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
  transient Chunk[] chunkList;

  // 左下のChunkの番号
  int x;
  int z;
  int y;

  String worldName;

  World world;

  public ChunkGroup(Location loc) {
    Chunk c = loc.getChunk();
    x = c.getX();
    z = c.getZ();
    world = c.getWorld();

    if (this.x % 2 == 1) {
      x--;
    }
    if (this.z % 2 == 1) {
      z--;
    }

    y = (loc.getBlockY() % 16 + 1) * 8;
  }

  /**
   * すべてのチャンクのEntityに対してconsumerを実行する
   */
  public void consume(Predicate<Entity> predicate, Consumer<Entity> consumer) {
    if (chunkList == null) {
      chunkList = new Chunk[] { world.getChunkAt(x, z),
          world.getChunkAt(x + 1, z),
          world.getChunkAt(x, z + 1),
          world.getChunkAt(x + 1, z + 1) };
    }
    Arrays.stream(chunkList).filter(c -> c.isLoaded()).forEach(c -> Arrays.stream(c.getEntities()).filter(predicate::test).forEach(consumer::accept));
  }

  /**
   * チャンクが1つでもLoadされていたらTRUE
   *
   * @return
   */
  public boolean isLoaded() {
    if (chunkList == null) {
      chunkList = new Chunk[] { world.getChunkAt(x, z),
          world.getChunkAt(x + 1, z),
          world.getChunkAt(x, z + 1),
          world.getChunkAt(x + 1, z + 1) };
    }
    return Arrays.stream(chunkList).anyMatch(c -> c.isLoaded());
  }
}
