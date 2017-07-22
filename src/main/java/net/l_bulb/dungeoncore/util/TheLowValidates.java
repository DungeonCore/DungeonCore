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

  /**
   * IlligalArgumentExceptionをthrowする
   *
   * @param msg
   */
  public static void throwIllegalArgument(String msg) {
    throw new IllegalArgumentException(msg);
  }

  /**
   * NullPointerExceptionをthrowする
   *
   * @param msg
   */
  public static void throwNullPointer(String msg) {
    throw new NullPointerException(msg);
  }
}
