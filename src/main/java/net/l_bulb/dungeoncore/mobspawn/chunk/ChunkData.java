package net.l_bulb.dungeoncore.mobspawn.chunk;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChunkData {
  Chunk chunk;

  boolean isNear;

  public boolean isLoaded() {
    return chunk.isLoaded();
  }

  public Entity[] getEntities() {
    return chunk.getEntities();
  }

  public int getX() {
    return chunk.getX();
  }

  public int getZ() {
    return chunk.getZ();
  }
}
