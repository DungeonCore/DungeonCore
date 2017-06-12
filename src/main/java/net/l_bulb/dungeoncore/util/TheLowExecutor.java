package net.l_bulb.dungeoncore.util;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.dungeoncore.Main;

public class TheLowExecutor {

  /**
   * 指定された作業を指定Tick後に実行する。もしdelayTickが0の場合は即時実行される
   *
   * @param delayTick
   * @param runnable
   */
  public static void executeLater(long delayTick, Runnable runnable) {
    if (delayTick == 0) {
      runnable.run();
    } else {
      new BukkitRunnable() {
        @Override
        public void run() {
          runnable.run();
        }
      }.runTaskLater(Main.plugin, delayTick);
    }
  }

  /**
   * メソッドを実行し戻り値を取得する。もしExceptionが発生した場合はdefaultValueを返す
   *
   * @param supplier
   * @param defaultValue
   * @return
   */
  public static <T> T executeIgnoreException(Supplier<T> supplier, T defaultValue) {
    try {
      return supplier.get();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * メソッドを実行し戻り値を取得する。もしExceptionが発生した場合はdefaultValueを返す
   *
   * @param supplier
   * @param defaultValue
   * @return
   */
  public static double executeDoubleIgnoreException(DoubleSupplier supplier, double defaultValue) {
    try {
      return supplier.getAsDouble();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * メソッドを実行し戻り値を取得する。もしExceptionが発生した場合はdefaultValueを返す
   *
   * @param supplier
   * @param defaultValue
   * @return
   */
  public static boolean executeBooleanIgnoreException(BooleanSupplier supplier, boolean defaultValue) {
    try {
      return supplier.getAsBoolean();
    } catch (Exception e) {
      return defaultValue;
    }
  }
}