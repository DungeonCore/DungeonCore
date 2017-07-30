package net.l_bulb.dungeoncore.common.cooltime;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.dungeoncore.LbnRuntimeException;

public class Cooltimes {
  private final static CooltimeKeyClass COOLTIME_KEY = new CooltimeKeyClass(null, null);

  /**
   * クールタイムが終了していて利用できるならTRUE
   *
   * @param playerUUID
   * @param cooltime
   * @return
   */
  public static boolean canUse(UUID playerUUID, Cooltimable cooltime) {
    COOLTIME_KEY.setPlayerUUID(playerUUID);
    COOLTIME_KEY.setCooltimeName(cooltime.getId());

    if (CooltimeManager.create.containsKey(COOLTIME_KEY)) {
      long canUseMillisTimes = CooltimeManager.create.get(COOLTIME_KEY);
      boolean canUse = canUseMillisTimes < System.currentTimeMillis();
      if (canUse) {
        CooltimeManager.create.remove(COOLTIME_KEY);
      }
      return canUse;
    }
    return true;
  }

  /**
   * cooltimeを開始する
   */
  public static void setCoolTime(UUID playerUUID, Cooltimable cooltime, ItemStack item) {
    if (cooltime.getCooltimeTick(item) <= 0) { return; }

    if (CooltimeManager.create.size() > 1000000) {
      new LbnRuntimeException("cool time size is too big!!").printStackTrace();
    }

    COOLTIME_KEY.setCooltimeName(cooltime.getId());
    COOLTIME_KEY.setPlayerUUID(playerUUID);
    CooltimeManager.create.put(COOLTIME_KEY, System.currentTimeMillis() + cooltime.getCooltimeTick(item) * 50L);
  }
}
