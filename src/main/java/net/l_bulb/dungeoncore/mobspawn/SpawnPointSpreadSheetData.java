package net.l_bulb.dungeoncore.mobspawn;

import org.bukkit.Location;

import lombok.Data;

@Data
public class SpawnPointSpreadSheetData {

  public SpawnPointSpreadSheetData(SpawnPointSpreadSheetData data, String targetName) {
    setId(data.getId());
    setLocation(data.getLocation());
    setMaxSpawnCount(data.getMaxSpawnCount());
    setDungeonHight(data.getDungeonHight());
    setLookNearChunk(data.isLookNearChunk());
    setType(data.getType());
    setTargetName(targetName);
  }

  public SpawnPointSpreadSheetData() {}

  int id;

  Location location;

  int maxSpawnCount;

  int dungeonHight = 8;

  boolean lookNearChunk = true;

  String targetName;

  TargetType type = TargetType.MONSTER;

  public enum TargetType {
    ITEM(false), MONSTER(true), BOSS(true);

    private boolean isCheckNearChunk;

    private TargetType(boolean isCheckNearChunk) {
      this.isCheckNearChunk = isCheckNearChunk;
    }

    public boolean isCheckNearChunk() {
      return isCheckNearChunk;
    }

    public static TargetType getType(String type) {
      try {
        return valueOf(type.toString());
      } catch (Exception e) {
        return MONSTER;
      }
    }
  }
}
