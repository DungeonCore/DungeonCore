package net.l_bulb.dungeoncore.api;

public enum PlayerStatusType {
  ADD_SWORD_ATTACK, ADD_BOW_ATTACK, ADD_MAGIC_ATTACK, MULTIPLY_SWORD_ATTACK(ApplyMethod.MULTIPLICATION), MULTIPLY_BOW_ATTACK(
      ApplyMethod.MULTIPLICATION), MULTIPLY_MAGIC_ATTACK(ApplyMethod.MULTIPLICATION), MAX_MAGIC_POINT, MAX_HP, SET_MAX_HP(ApplyMethod.OVERWRITE);

  private final ApplyMethod method;

  private PlayerStatusType(ApplyMethod method) {
    this.method = method;
  }

  private PlayerStatusType() {
    this(ApplyMethod.ADDITION);
  }

  /**
   * 百分率ではなく割合で表しているならTRUE
   *
   * @return
   */
  public ApplyMethod getApplyMethod() {
    return method;
  }

  public enum ApplyMethod {
    ADDITION, MULTIPLICATION, OVERWRITE;
  }
}
