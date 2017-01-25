package lbn.player;

import java.util.Arrays;
import java.util.List;

public enum TheLowLevelType {
	SWORD("剣レベル"), BOW("弓レベル"), MAGIC("魔法レベル"), MAIN("メインレベル");

	String name;

	static private List<String> names = Arrays.asList(SWORD.getName(), BOW.getName(), MAGIC.getName(), MAIN.getName());

	private TheLowLevelType(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}

	/**
	 * 名称のリストを取得
	 * @return
	 */
	public static List<String> getNames() {
		return names;
	}

	/**
	 * 日本語表記からインスタンスを取得
	 * @param jpName
	 * @return
	 */
	public static TheLowLevelType fromJpName(String jpName) {
		for (TheLowLevelType type : values()) {
			if (type.name.equals(jpName)) {
				return type;
			}
		}
		return null;
	}
}
