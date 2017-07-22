package net.l_bulb.dungeoncore.util;

public class TheLowValidates {
  /**
   * IlligalStatementExceptionをthrowする
   *
   * @param msg
   */
  public static void throwIllegalState(String msg) {
    throw new IllegalStateException(msg);
  }
}
