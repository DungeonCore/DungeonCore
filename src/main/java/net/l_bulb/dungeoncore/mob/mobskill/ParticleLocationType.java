package net.l_bulb.dungeoncore.mob.mobskill;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public enum ParticleLocationType {
  MONSTER_UNDER("モンスターの足元", 0, true),
  MONSTER_BODY("モンスターの体", 1, true),
  MONSTER_UPPER("モンスターの上", 2, true),
  PLAYER_UNDER("プレイヤーの足元", 0, false),
  PLAYER_BODY("プレイヤーの体", 1, false),
  PLAYER_UPPER("プレイヤーの上", 2, false);

  private ParticleLocationType(String data, int addY, boolean isMobLoc) {
    this.data = data;
    this.addY = addY;
    this.isMobLoc = isMobLoc;
  }

  String data;
  int addY;
  boolean isMobLoc;

  public static ParticleLocationType getValue(String data) {
    if (data == null) { return null; }
    for (ParticleLocationType val : values()) {
      if (val.data.equals(data.trim())) { return val; }
    }
    return null;
  }

  public Location getLocation(Entity mob, Entity target) {
    Location loc;
    if (isMobLoc) {
      loc = mob.getLocation();
    } else {
      loc = target.getLocation();
    }

    return loc.add(0, addY, 0);
  }
}
