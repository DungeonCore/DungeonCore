package net.l_bulb.dungeoncore.command.util;

public interface UsageCommandable {
  /**
   * コマンドの使い方を取得
   * 
   * @return
   */
  String getUsage();

  /**
   * コマンドの説明を取得
   * 
   * @return
   */
  String getDescription();
}
