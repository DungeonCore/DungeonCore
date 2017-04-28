package net.l_bulb.dungeoncore.common.place;

public enum PlaceType {
  DUNGEON("ダンジョン"), DUNGEON_IMMATURE("ダンジョン(未完成)"), TOWN("町"), TEMP("一時的な場所");

  private String name;

  private PlaceType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static PlaceType getInstance(String name) {
    for (PlaceType type : values()) {
      if (type.getName().equals(name)) { return type; }
    }
    return TEMP;
  }
}
