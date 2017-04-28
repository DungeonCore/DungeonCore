package net.l_bulb.dungeoncore.api;

public enum PlayerStatusType {
  ADD_SWORD_ATTACK, ADD_BOW_ATTACK, ADD_MAGIC_ATTACK, MULTIPLY_SWORD_ATTACK(true), MULTIPLY_BOW_ATTACK(true), MULTIPLY_MAGIC_ATTACK(
      true), MAX_MAGIC_POINT, MAX_HP;

  boolean isPercent = false;

  private PlayerStatusType(boolean isPercent) {
    this.isPercent = isPercent;
  }

  private PlayerStatusType() {}

  /**
   * 百分率ではなく割合で表しているならTRUE
   * 
   * @return
   */
  public boolean isPercentage() {
    return isPercent;
  }
}
