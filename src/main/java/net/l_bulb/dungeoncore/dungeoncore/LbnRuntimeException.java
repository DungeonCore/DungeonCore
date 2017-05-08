package net.l_bulb.dungeoncore.dungeoncore;

public class LbnRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public LbnRuntimeException(String msg) {
    super(msg);
  }

  public LbnRuntimeException(Exception e) {
    super(e);
  }
}
