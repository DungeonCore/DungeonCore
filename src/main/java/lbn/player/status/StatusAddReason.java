package lbn.player.status;

public enum StatusAddReason {
  monster_drop(true), food_eat(true), quest_reword(true), system(false), commad(true);

  boolean isPrintMessageLog;

  StatusAddReason(boolean isPrintMessageLog) {
    this.isPrintMessageLog = isPrintMessageLog;
  }

  public boolean isPrintMessageLog() {
    return isPrintMessageLog;
  }
}
