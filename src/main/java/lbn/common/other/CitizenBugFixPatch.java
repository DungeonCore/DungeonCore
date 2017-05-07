package lbn.common.other;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.server.v1_8_R1.EntityTypes;

import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;

public class CitizenBugFixPatch {
  public static void doPatch() {
    new BukkitRunnable() {
      @Override
      public void run() {
        try {
          Field[] declaredFields = EntityTypes.class.getDeclaredFields();
          for (Field field : declaredFields) {
            if (field.toGenericString().contains("EntityTypes.c")) {
              field.setAccessible(true);
              @SuppressWarnings("unchecked")
              // EntityTypes.cを取得
              Map<String, Class<?>> map = (Map<String, Class<?>>) field.get(null);
              // 追加するMap
              Map<String, Class<?>> addMap = new HashMap<String, Class<?>>();

              // １つずつ見て、もしKeyがClassならStringにする
              Iterator<?> iterator = map.entrySet().iterator();
              while (iterator.hasNext()) {
                Entry<?, ?> next = (Entry<?, ?>) iterator.next();
                // Classか確認
                if (next.getKey() instanceof Class<?>) {
                  // 一旦消す
                  iterator.remove();
                  // valueがnullでないなら値を入れる
                  if (next.getValue() != null) {
                    addMap.put(((Class<?>) next.getKey()).getSimpleName(), (Class<?>) next.getValue());
                  }
                }
              }
              map.putAll(addMap);
            }
          }
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }.runTaskLater(Main.plugin, 20 * 5);
  }
}
