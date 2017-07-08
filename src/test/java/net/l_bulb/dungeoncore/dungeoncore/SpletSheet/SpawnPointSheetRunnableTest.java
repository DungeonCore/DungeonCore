package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import net.l_bulb.dungeoncore.mobspawn.SpawnPoint;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointFactory;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointGroupFactory;

public class SpawnPointSheetRunnableTest {

  @Spy
  @InjectMocks
  SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(mock(Player.class));

  @Mock
  SpawnPointGroupFactory groupFactory;

  @Mock
  SpawnPointFactory spawnPointFactory;

  @Captor
  ArgumentCaptor<SpawnPoint> captor;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void excuteOnerowTest() {
    // set up
    doReturn(mock(World.class)).when(spawnPointSheetRunnable).getWorld("world");
    when(spawnPointFactory.getNewInstance(any())).thenReturn(mock(SpawnPoint.class)).thenReturn(mock(SpawnPoint.class));
    String[] row = { "513", "world", "0", "0", "0", "1", "MONSTER", null, null, "mobA", "mobB", null, null, null };

    // 実行
    spawnPointSheetRunnable.excuteOnerow(row);

    // 検証
    verify(groupFactory, times(2)).registSpawnPoint(captor.capture());
    List<SpawnPoint> allValues = captor.getAllValues();
    SpawnPoint spawnPoint1 = allValues.get(0);
    SpawnPoint spawnPoint2 = allValues.get(1);

    assertThat(spawnPoint1.hashCode()).isNotEqualTo(spawnPoint2.hashCode());
    assertThat(spawnPoint1).isNotEqualTo(spawnPoint2);
    assertThat(spawnPoint1.isSameAs(spawnPoint2)).isEqualTo(false);
  }

}
