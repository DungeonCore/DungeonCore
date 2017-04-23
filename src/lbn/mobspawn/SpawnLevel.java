package lbn.mobspawn;

import java.util.ArrayList;

public enum SpawnLevel {
	LEVEL1(20 * 30),
	LEVEL2(20 * 35),
	LEVEL3(20 * 40),
	LEVEL4(20 * 45),
	LEVEL5(20 * 50),
	BOSS(20 * 35);

	int num;

	private SpawnLevel(int  num) {
		this.num = num;
	}

	public int getSpawnTick() {
		return num;
	}

	public static SpawnLevel getLevel(String name) {
		name = name.toUpperCase();
		for (SpawnLevel level : SpawnLevel.values()) {
			if (level.toString().equals(name)) {
				return level;
			}
		}
		return null;
	}

	static ArrayList<String> names = null;

	public static ArrayList<String> getNames() {
		if (names != null) {
			return names;
		}

		names = new ArrayList<String>();
		for (SpawnLevel level : values()) {
			names.add(level.toString());
		}

		return names;
	}
}
