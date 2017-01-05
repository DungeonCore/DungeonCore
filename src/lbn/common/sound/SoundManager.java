package lbn.common.sound;

import java.util.HashMap;

public class SoundManager {
	static HashMap<String, SoundData> soundMap = new HashMap<String, SoundData>();

	public static void regist(SoundData data) {
		soundMap.put(data.id, data);
	}

	public static SoundData fromId(String id) {
		return soundMap.get(id);
	}

	public static void clear() {
		soundMap.clear();
	}
}

