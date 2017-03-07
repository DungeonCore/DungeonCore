package lbn.common.book;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import lbn.util.ItemStackUtil;

public class BookData {
	public BookData(String command, String id) {
		this.id = id;

		if (isNull(command)) {
			return;
		}
		item = ItemStackUtil.getItemStackByCommand(command);

		//本のコマンドがあるならそれを追加する
		if (item.getItemMeta() instanceof BookMeta) {
			//本の情報を取得
			BookMeta itemMeta = (BookMeta) item.getItemMeta();
			//タイトルを表示
			title = itemMeta.getTitle();
			//作者
			auther = itemMeta.getAuthor();
			//内容
			contents.addAll(itemMeta.getPages());
		}
	}

	protected boolean isNull(String command) {
		return command == null || command.isEmpty();
	}

	String id;

	String title;

	String auther;

	ItemStack item = null;

	ArrayList<String> contents = new ArrayList<>();

	public String getId() {
		return id;
	}

	public String getTitile() {
		return title;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setTitile(String titile) {
		if (isNull(titile)) {
			return;
		}
		this.title = titile;
	}

	public String getAuther() {
		return auther;
	}

	public void setAuther(String auther) {
		if (isNull(auther)) {
			return;
		}
		this.auther = auther;
	}

	public String[] getContents() {
		return contents.toArray(new String[0]);
	}

	public void setContents(String page, int i) {
		if (isNull(page)) {
			return;
		}
		contents.set(i, page);
	}

	ItemStack cacheBookItem = null;
	/**
	 * 本のアイテムへ変換
	 * @return
	 */
	public ItemStack toBookItem() {
		if (cacheBookItem == null) {
			cacheBookItem = new ItemStack(Material.WRITTEN_BOOK);
			//アイテムの情報を書きかる
			BookMeta itemMeta = (BookMeta) cacheBookItem.getItemMeta();
			itemMeta.setAuthor(getAuther());
			itemMeta.setTitle(getTitile());
			itemMeta.setPages(getContents());
			cacheBookItem.setItemMeta(itemMeta);
		}

		return cacheBookItem;
	}
}
