package lbn.npc;

public enum VillagerType {
	NORMAL, SHOP, BLACKSMITH;


	public static VillagerType getValue(String type) {
		for (VillagerType val : values()) {
			if (val.toString().equals(type)) {
				return val;
			}
		}
		return null;
	}
}
