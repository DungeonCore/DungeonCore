package net.l_bulb.dungeoncore.mobspawn;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.l_bulb.dungeoncore.mobspawn.chunk.ChunkGroup;

public class SpawnPointGroupFactoryTest {

  @Mock
  ChunkGroup chunk1;

  @Mock
  SpawnPoint point1;

  @Mock
  SpawnPoint point2;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    when(point1.getChunkGroup()).thenReturn(chunk1);
    when(point2.getChunkGroup()).thenReturn(chunk1);

    when(point1.isSameAs(point2)).thenReturn(true);
  }

  @Test
  public void testRegistSpawnPoint() {
    // 実行
    SpawnPointGroupFactory.registSpawnPoint(point1);
    SpawnPointGroupFactory.registSpawnPoint(point2);

    // 検証
    assertThat(SpawnPointGroupFactory.spawnPointGroupChunkMap.size(), is(1));
  }

}
