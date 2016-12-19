package main.common.text;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SayText {
	static HashMap<String, String[]> sayTextMap = new HashMap<>();
	static {
		sayTextMap.put("error", new String[]{ChatColor.RED + "messageが存在しません。"});
	}

	/**
	 *
	 * @param id
	 * @param color
	 * @param text jp text
	 */
	public static void registSayText(String id, String colorString, String... text) {
		ChatColor color = null;

		for (ChatColor c : ChatColor.values()) {
			if (c.toString().equalsIgnoreCase(colorString)) {
				color = c;
			}
		}
		if (color == null) {
			color = ChatColor.BLACK;
		}

		for (int i = 0; i < text.length; i++) {
			text[i] = color + text[i];
		}

		sayTextMap.put(id, text);
	}

	public static String getSayText(String id, Player p) {
		if (!sayTextMap.containsKey(id)) {
			//絶対存在する
			id = "error";
		}
		String[] strings = sayTextMap.get(id);

		if (strings.length == 0) {
			return "";
		} else {
			return strings[0];
		}
	}
}
