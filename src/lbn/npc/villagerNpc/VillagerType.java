package lbn.npc.villagerNpc;

public enum VillagerType {
	NORMAL, SHOP, BLACKSMITH, REINC, MAGIC_ORE;

	public static VillagerType getValue(String type) {
		for (VillagerType val : values()) {
			if (val.toString().equals(type)) {
				return val;
			}
		}
		return null;
	}
}
