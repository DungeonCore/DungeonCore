package lbn.money.shop;

import java.util.Iterator;
import java.util.List;

import lbn.util.ItemStackUtil;
import lbn.util.Message;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class CustomShopItem extends ShopItem{

	String shopItemId;

	/**
	 * @param item 実際に買われる商品
	 * @param price　値段
	 * @param amount　個数
	 * @param shopItemId　ID
	 */
	public CustomShopItem(ItemStack item, int price, int amount, String shopItemId) {
		super(item, price, amount);
		this.shopItemId = shopItemId;
	}

	public static CustomShopItem fromTemplate(ItemStack template) {
		List<String> lore = ItemStackUtil.getLore(template);
		int amount = -1;
		int price = -1;
		String shopItemId = null;
		Iterator<String> iterator = lore.iterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			try {
				if (string.contains("Quantity : ")) {
					amount = Integer.parseInt(ChatColor.stripColor(string.replace("Quantity : ", "")));
					iterator.remove();
				} else if (string.contains("Price : ")) {
					price = Integer.parseInt(ChatColor.stripColor(string.replace("Price : ", "").replace("Galions", "").trim()));
					iterator.remove();
				} else if (string.contains(Message.getMessage("使用可能レベル : {0}", ""))) {
					iterator.remove();
				} else if (string.contains("shopitemid: ")) {
					shopItemId = ChatColor.stripColor(string.replace("shopitemid: ", "").trim());
					iterator.remove();
				}
			} catch (NumberFormatException e) {
				return null;
			}
		}
		if (amount == -1 || price == -1 || shopItemId == null) {
			return null;
		}
		ItemStack clone = template.clone();
		ItemStackUtil.setLore(clone, lore);

		return new CustomShopItem(clone, price, amount, shopItemId);
	}

	public static String getShopItemId(ItemStack item) {
		List<String> lore = ItemStackUtil.getLore(item);
		for (String string : lore) {
			if (string.contains("shopitemid: ")) {
				return ChatColor.stripColor(string).replace("shopitemid: ", "");
			}
		}
		return null;
	}

	/**
	 * テンプレートを取得
	 * @param get_money_item
	 * @param price
	 * @return
	 */
	public ItemStack getCustomShopItemTemplate() {
		//実際の商品を複製する
		ItemStack clone = item.clone();
		//量をセット
		clone.setAmount(1);
		//情報をセットする
		ItemStackUtil.addLore(clone, Message.getMessage("Price : {0} Galions", price),
				Message.getMessage("Quantity : {0}", count),
				Message.getMessage("使用可能レベル : {0}", getAvailableLevel(item)),
				Message.getMessage("shopitemid: {0}", shopItemId)
				);
		return clone;
	}

	@Override
	public ItemStack getShopDispItem() {
		ItemStack shopDispItem = super.getShopDispItem();
		//TODO slotなどを追加
		ItemStackUtil.addLore(shopDispItem, ChatColor.BLACK + Message.getMessage("shopitemid: {0}", shopItemId));
		return shopDispItem;
	}
}
