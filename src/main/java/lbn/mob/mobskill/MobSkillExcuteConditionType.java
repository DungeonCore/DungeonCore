package lbn.mob.mobskill;

public enum MobSkillExcuteConditionType {
  MOB_ATTACK("モンスターが攻撃する時"), MOB_DAMAGED("モンスターが攻撃を受けた時"), TARGET_PLAYER("ターゲットを定めた時"), MOB_DEATH("モンスターが死んだ時"), MOB_SPAWN(
      "モンスターがスポーンされる時"), RUNTINE_10SEC("10秒に一度(ボスのみ)", 10), RUNTINE_30SEC("30秒に一度(ボスのみ)", 30), RUNTINE_60SEC("60秒に一度(ボスのみ)", 60);

  String detail;
  int term = 0;

  private MobSkillExcuteConditionType(String detail) {
    this.detail = detail;
  }

  private MobSkillExcuteConditionType(String detail, int term) {
    this.detail = detail;
    this.term = term;
  }

  public String getDetail() {
    return detail;
  }

  public static MobSkillExcuteConditionType getInstance(String detail) {
    for (MobSkillExcuteConditionType val : values()) {
      if (val.getDetail().equals(detail)) { return val; }
    }
    return null;
  }

  public int getTerm() {
    return term;
  }
}
