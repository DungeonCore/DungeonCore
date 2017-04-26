package lbn.mob;

public enum AIType {
  NO_ATACK("攻撃しない"), NORMAL("通常AI"), SHORT_LONG("近遠両方"), LONG1("遠距離(弱)"), LONG2("遠距離(強)");

  String name;

  private AIType(String name) {
    this.name = name;
  }

  /**
   * 名前からインスタンスを取得
   * 
   * @param name
   * @return
   */
  public static AIType fromName(String name) {
    for (AIType type : values()) {
      if (type.name.equals(name)) { return type; }
    }
    return null;
  }
}
