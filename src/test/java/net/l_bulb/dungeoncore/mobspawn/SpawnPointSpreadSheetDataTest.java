package net.l_bulb.dungeoncore.mobspawn;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData.TargetType;

public class SpawnPointSpreadSheetDataTest {

  @Test
  public void testGetType() {
    // 実行
    TargetType actual = SpawnPointSpreadSheetData.TargetType.getType("BOSS");

    // 検証
    assertThat(actual).isEqualTo(TargetType.BOSS);

  }

}
