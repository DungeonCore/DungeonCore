package lbn.item.lore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class ItemLoreToken {
	public static final String TITLE_TAG = ChatColor.BLACK + "title";

	public static final String TITLE_STANDARD = "機能性能";

	public static final String TITLE_STRENGTH = "強化性能";

	public static final String TITLE_SLOT = "SLOT";

	ArrayList<String> lore = new ArrayList<String>();

	String title;

	public ItemLoreToken(String title) {
		this(title, false);
	}

	/**
	 * もしLineがTitleに相当すればTRUEを返す
	 * @param line
	 * @return
	 */
	public static boolean isTitle(String line) {
		if (line == null) {
			return false;
		}
		return line.contains(TITLE_TAG);
	}

	/**
	 * @param title
	 * @param isFormated すでにフォーマットが終わってるならTRUE
	 */
	public ItemLoreToken(String title, boolean isFormated) {
		lore.add("");
		if (!isFormated) {
			title = ChatColor.stripColor(title.replace("[", "").replace("]", "").replace(TITLE_TAG, ""));
			lore.add(MessageFormat.format("{0}[{1}]{2}", ChatColor.GREEN, title, getTag(title)));
		} else {
			lore.add(title + getTag(title));
		}
		this.title = lore.get(1);
	}

	public String getTag(String title) {
		//すでにTagが付いていたら何もしない
		if (title.contains(TITLE_TAG)) {
			return "";
		}
		return TITLE_TAG;
	}

	/**
	 * Loreを追加する
	 * @param value
	 */
	public void addLore(String value) {
		if (value != null && !value.isEmpty()) {
			addLore(value, ChatColor.YELLOW);
		}
	}

	/**
	 * フォーマットを変えないでそのまま追加する
	 * @param value
	 */
	public void addLoreAsOriginal(String value) {
		if (value != null && !value.isEmpty()) {
			lore.add(value);
		}
	}

	public void addAllLore(List<String> values) {
		for (String value : values) {
			addLore(value);
		}
	}

	public void addLore(String value, ChatColor c) {
		lore.add("    " + c + value);
	}

	public int size() {
		return lore.size() - 2;
	}

	/**
	 * 登録したLoreを取得
	 * @return
	 */
	public List<String> getLoreToken() {
		return lore;
	}

	/**
	 * 追加したLoreを取得する(タイトルは含まない)
	 * @return
	 */
	public List<String> getLore() {
			return lore.subList(2, lore.size());
	}

	/**
	 *
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return lore.toString();
	}
}
