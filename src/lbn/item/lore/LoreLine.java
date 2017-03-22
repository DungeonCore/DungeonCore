package lbn.item.lore;

import org.bukkit.ChatColor;

public class LoreLine {
	public LoreLine(String subTitle, Object value) {
		this.subTitle = subTitle;
		this.value = value;
	}

	String subTitle;
	Object value;

	ChatColor subTitleColor = ChatColor.YELLOW;
	ChatColor valueColor = ChatColor.GOLD;

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(subTitleColor);
		stringBuilder.append(subTitle);
		stringBuilder.append(" ：");
		stringBuilder.append(valueColor);
		stringBuilder.append(value);
		return stringBuilder.toString();
	}

	/**
	 * subTitleからLoreの１行を取得する
	 * @param subTitle
	 * @param value
	 * @return
	 */
	public static String getLoreLine(String subTitle, Object value) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ChatColor.YELLOW);
		stringBuilder.append(subTitle);
		stringBuilder.append(" ：");
		stringBuilder.append(ChatColor.GOLD);
		stringBuilder.append(value);
		return stringBuilder.toString();
	}
}
