package lbn.item.customItem.itemAbstract;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import lbn.common.book.BookData;
import lbn.common.book.BookManager;
import lbn.item.customItem.AbstractItem;
import lbn.item.itemInterface.RightClickItemable;

public class BookItem extends AbstractItem implements RightClickItemable {

	BookData data;

	ItemStack itemStack;

	public BookItem(BookData data) {
		this.data = data;

		itemStack = data.getItem();
		if (itemStack == null) {
			itemStack = new ItemStack(Material.WRITTEN_BOOK);
		}
		// アイテムの情報を書きかる
		BookMeta itemMeta = (BookMeta) itemStack.getItemMeta();
		itemMeta.setAuthor(data.getAuther());
		itemMeta.setTitle(data.getTitile());
		itemMeta.setPages(data.getContents());
		itemStack.setItemMeta(itemMeta);
	}

	@Override
	public String getItemName() {
		return data.getTitile();
	}

	@Override
	public String getId() {
		return data.getId();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return 10;
	}

	@Override
	protected Material getMaterial() {
		return itemStack.getType();
	}

	@Override
	public String[] getDetail() {
		return null;
	}

	@Override
	protected ItemStack getItemStackBase() {
		return itemStack;
	}

	@Override
	public void excuteOnRightClick(PlayerInteractEvent e) {
		// もし本でなくても本のGUIを強制的に開かせる
		if (getMaterial() != Material.WRITTEN_BOOK) {
			BookManager.openBook(data.toBookItem(), e.getPlayer());
			e.setCancelled(true);
		}

		// 本を開くのを記録する
	}

	/**
	 * 本のID
	 * 
	 * @return
	 */
	public String getBookId() {
		return data.getId();
	}

	@Override
	public boolean isShowItemList() {
		return false;
	}
}
