package net.l_bulb.dungeoncore.mobspawn.chunk;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChunkGroupTest {

  @Mock
  Location loc1;

  @Mock
  Location loc2;

  @Mock
  World w;

  @Mock
  Chunk c;

  private ChunkGroup chunkGroup;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(c.getX()).thenReturn(3);
    when(c.getZ()).thenReturn(4);
    when(c.getWorld()).thenReturn(w);
    when(loc1.getBlockY()).thenReturn(100);
    when(loc2.getBlockY()).thenReturn(101);

    when(loc1.getChunk()).thenReturn(c);
    when(loc2.getChunk()).thenReturn(c);

    chunkGroup = new ChunkGroup(loc1);
  }

  @Test
  public void testChunkGroup() {
    // 検証
    assertThat(chunkGroup.x).isEqualTo(3);
    assertThat(chunkGroup.z).isEqualTo(3);
  }

  @Test
  public void testChunkGroup2() {
    when(c.getX()).thenReturn(-2);
    when(c.getZ()).thenReturn(-1);
    when(loc1.getChunk()).thenReturn(c);
    chunkGroup = new ChunkGroup(loc1);

    // 検証
    assertThat(chunkGroup.x).isEqualTo(-3);
    assertThat(chunkGroup.z).isEqualTo(-3);
  }

  @Test
  public void testHashCode() {
    assertThat(chunkGroup.hashCode()).isEqualTo(new ChunkGroup(loc2).hashCode());
  }

  @Test
  public void testEqualsObject() {
    assertThat(chunkGroup).isEqualTo(new ChunkGroup(loc2));
  }

}
