package lbn.common.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MenuSelectorInterface {
	/**
	 * メニューを開く
	 * @param p
	 */
	public void open(Player p);

	/**
	 * アイテムを選択する
	 * @param p
	 * @param item
	 */
	public void onSelectItem(Player p, ItemStack item);

	/**
	 * インベントリのタイトルを取得
	 * @return
	 */
	public String getTitle();
}
