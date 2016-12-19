package lbn.money.shop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lbn.dungeoncore.LbnRuntimeException;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.util.ItemStackUtil;

public class Shop {
	public String getName() {
		return "NORMAL";
	}

	/**
	 * ショップ用のインベントリを取得
	 * @return
	 */
	public Inventory getShopInventory() {
		ArrayList<ShopItem> arrayList = getShopItemList();

		Inventory createInventory = Bukkit.createInventory(null, ((int)(arrayList.size() / 9) + 1) * 9, getName() + " shop");

		int i = 0;
		for (ShopItem shopItem : arrayList) {
			createInventory.setItem(i, shopItem.getShopDispItem());
			i++;
		}

		return createInventory;
	}

	static ArrayList<ShopItem> arrayList = null;
	/**
	 * 商品を取得
	 * @return
	 */
	protected ArrayList<ShopItem> getShopItemList() {
		if (arrayList != null) {
			return arrayList;
		}
		arrayList = new ArrayList<ShopItem>();

//		for (ItemInterface item : NormalSwordWrapper.getAllNormalItem()) {
//			arrayList.add(new ShopItem(item.getItem(), Math.max(((NormalSwordWrapper)item).getAvailableLevel() * 200 + 20, 330)));
//		}
//		arrayList.add(ShopItem.getBlank());
//
//		for (ItemInterface item : NormalBowWrapper.getAllNormalItem()) {
//			arrayList.add(new ShopItem(item.getItem(), Math.max(((NormalBowWrapper)item).getAvailableLevel() * 200 + 20, 330)));
//		}
//		arrayList.add(ShopItem.getBlank());
//
//		for (ItemInterface item : NormalMagicItem.getAllItem()) {
//			arrayList.add(new ShopItem(item.getItem(), Math.max(((NormalMagicItem)item).getAvailableLevel() * 200 + 20, 330)));
//		}
//		arrayList.add(ShopItem.getBlank());
//
//		arrayList.add(new ShopItem(LeatherArmor.getLeatherHelmet().getItem(), 80));
//		arrayList.add(new ShopItem(LeatherArmor.getLeatherChestplate().getItem(), 80));
//		arrayList.add(new ShopItem(LeatherArmor.getLeatherLeggings().getItem(), 80));
//		arrayList.add(new ShopItem(LeatherArmor.getLeatherBoots().getItem(), 80));
//		arrayList.add(ShopItem.getBlank());
//		arrayList.add(ShopItem.getBlank());
//		arrayList.add(ShopItem.getBlank());
//		arrayList.add(ShopItem.getBlank());
//		arrayList.add(ShopItem.getBlank());
//
//		arrayList.add(new ShopItem(new ItemStack(Material.MELON), 1));
//		arrayList.add(new ShopItem(new ItemStack(Material.MELON), 1 * 60, 64));
//		arrayList.add(new ShopItem(new Potion(PotionType.INSTANT_HEAL, 1).toItemStack(1), 10));
//		arrayList.add(new ShopItem(new Potion(PotionType.SPEED, 1).toItemStack(1), 10000));
//		arrayList.add(new ShopItem(new ItemStack(Material.ARROW), 2));
//		arrayList.add(new ShopItem(new ItemStack(Material.ARROW), 60 * 2, 64));
		return arrayList;
	}

	public void openShop(Player p) {
		p.openInventory(getShopInventory());
	}

	/**
	 * ShopItemを設定したItemStackからShopItemを取得
	 * @param shopItem
	 * @param error
	 * @return
	 */
	public ShopItem fromShopItem(ItemStack shopItem, boolean error) {
		if (shopItem == null || shopItem.getType() == Material.AIR) {
			return null;
		}

		ItemStack cloneItem = shopItem.clone();
		String id = ItemStackUtil.getId(cloneItem);

		//もしIDがなければアイテムのLoreを取り除きそのまま渡す(通常のマイクラのアイテム)
		if (id == null) {
			ItemStackUtil.setLore(cloneItem, new ArrayList<String>());
		} else {
			//idがある場合はCustomItemとみなし、idから逆引きする
			ItemInterface customItemById = ItemManager.getCustomItemById(id);
			//もしそのidに対応したアイテムがなければ購入できないためnullにしておく
			if (customItemById == null) {
				if (error) {
					new LbnRuntimeException("存在しないアイテムIDです。:" + customItemById).printStackTrace();
				}
				return null;
			} else {
				cloneItem = customItemById.getItem();
			}
		}

		List<String> lore = ItemStackUtil.getLore(shopItem);
		int count = -1;
		int price = -1;
		for (String string : lore) {
			try {
				if (string.contains("Quantity : ")) {
					count = Integer.parseInt(ChatColor.stripColor(string.replace("Quantity : ", "")));
				}
				if (string.contains("Price : ")) {
					price = Integer.parseInt(ChatColor.stripColor(string.replace("Price : ", "").replace("Galions", "").trim()));
				}
			} catch (NumberFormatException e) {
				if (error) {
					new LbnRuntimeException(e).printStackTrace();
				}
				return null;
			}
		}

		if (price == -1 || count == -1) {
			return null;
		}

		return new ShopItem(cloneItem, price, count);
	}
}
