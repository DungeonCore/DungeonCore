package net.l_bulb.dungeoncore.npc.villagerNpc;

public enum VillagerType {
  NORMAL, SHOP, BLACKSMITH, REINC, MAGIC_ORE, REST;

  public static VillagerType getValue(String type) {
    for (VillagerType val : values()) {
      if (val.toString().equals(type)) { return val; }
    }
    return null;
  }
}
