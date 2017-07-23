package net.l_bulb.dungeoncore.common.other;

import net.l_bulb.dungeoncore.dungeoncore.Main;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CitizenBugFixPatch {

  /**
   * Removes abnormal keys from an internal field.
   */
  @SuppressWarnings("unchecked")
  private static void applyPatch() {
    final Logger logger = Logger.getLogger("DungeonCore");
    try {
      Field cField = Class.forName("net.minecraft.server.v1_8_R1.EntityTypes").getDeclaredField("c");
      cField.setAccessible(true);
      Map<String, Class<?>> c = (Map<String, Class<?>>) cField.get(null); // Static Field
      // noinspection SuspiciousMethodCalls (for abnormal types)
      new HashMap<Object, Class<?>>(c).entrySet().stream()
          .filter(e -> e.getKey() instanceof Class<?>)
          .peek(e -> c.remove(e.getKey()))
          .filter(e -> e.getValue() != null)
          .forEach(e -> c.put(((Class<?>) e.getKey()).getSimpleName(), e.getValue()));
    } catch (ClassNotFoundException | NoSuchFieldException exception) {
      logger.warning("このバージョンはサポートされていません。");
      logger.log(Level.FINEST, "For Debug: ", exception);
    } catch (IllegalArgumentException | IllegalAccessException exception) {
      logger.log(Level.SEVERE, "パッチを適用できませんでした。", exception);
    }
  }

  public static void doPatch() {
    Bukkit.getScheduler().runTaskLater(Main.plugin, CitizenBugFixPatch::applyPatch, 20 * 5);
  }
}
