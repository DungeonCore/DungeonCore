package net.l_bulb.dungeoncore.mob;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.dungeoncore.LbnRuntimeException;

public class SummonPlayerManager {
  static HashMap<LivingEntity, Player> entityOwnerMap = new HashMap<>();
  static HashMap<LivingEntity, ItemStack> entityItemMap = new HashMap<>();

  public static void addSummon(LivingEntity e, Player p, ItemStack item) {
    if (entityOwnerMap.size() > 100000) {
      new LbnRuntimeException("summon map is over 100000").printStackTrace();
    }
    entityOwnerMap.put(e, p);
    entityItemMap.put(e, item);
  }

  public static void removeSummon(LivingEntity e) {
    entityOwnerMap.remove(e);
    entityItemMap.remove(e);
  }

  public static void allRemoveSummon() {
    for (LivingEntity e : entityOwnerMap.keySet()) {
      e.remove();
    }
  }

  public static Player getOwner(Entity damager) {
    return entityOwnerMap.get(damager);
  }

  public static boolean isSummonMob(Entity e) {
    return entityOwnerMap.containsKey(e);
  }

  public static ItemStack getUseItem(Entity attacker) {
    return entityItemMap.get(attacker);
  }
}
