package net.l_bulb.dungeoncore.mobspawn.spawnPointImpl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData.TargetType;

public class AbstractSpawnPointTest {

  @Mock
  Location location;

  AbstractSpawnPoint point;

  @Mock
  AbstractSpawnPoint point2;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    point = new AbstractSpawnPointImplementation(location);
  }

  @Test
  public void testIsSameAs() {
    // setup
    when(point2.getTargetType()).thenReturn(TargetType.MONSTER);
    when(point2.getSpawnTargetName()).thenReturn("dummy name");

    // 実行
    boolean actual = point.isSameAs(point2);

    // 検証
    assertThat(actual, is(true));
  }

}

class AbstractSpawnPointImplementation extends AbstractSpawnPoint {

  public AbstractSpawnPointImplementation(Location loc) {
    super(getData(loc));
  }

  @Override
  public Entity spawn() {
    return null;
  }

  @Override
  public boolean equalsEntity(Entity e) {
    return false;
  }

  @Override
  public String getSpawnTargetName() {
    return "dummy name";
  }

  static SpawnPointSpreadSheetData getData(Location loc) {
    SpawnPointSpreadSheetData data = new SpawnPointSpreadSheetData();
    data.setDungeonHight(8);
    data.setId(8);
    data.setLocation(loc);
    data.setType(TargetType.MONSTER);
    return data;
  }

}
