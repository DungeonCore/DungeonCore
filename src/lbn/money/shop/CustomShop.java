package lbn.money.shop;

import java.util.ArrayList;

import lbn.dungeoncore.LbnRuntimeException;
import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mob.mob.abstractmob.villager.SpletSheetVillager;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class CustomShop extends Shop{
	public CustomShop(String villagerName) {
		//dataからLocationを取得しショップを作成する
		AbstractMob<?> mob = MobHolder.getMob(villagerName);
		if (mob instanceof SpletSheetVillager) {
			SpletSheetVillager villager = ((SpletSheetVillager)mob);
			String data = villager.getData();
			Location locationByString = AbstractSheetRunable.getLocationByString(data);
			this.loc = locationByString;
		}
		this.name = villagerName;
	}

	@Override
	public String getName() {
		return name;
	}

	String name;
	Location loc = null;

	@Override
	protected ArrayList<ShopItem> getShopItemList() {
		if (loc == null) {
			return super.getShopItemList();
		}

		Block block = loc.getBlock();
		BlockState state = block.getState();

		if (!(state instanceof Chest)) {
			return super.getShopItemList();
		}

		ArrayList<ShopItem> shopitems = new ArrayList<ShopItem>();

		Chest chest = (Chest) state;
		for (ItemStack shopItemTemplate : chest.getBlockInventory().getContents()) {
			ShopItem fromShopItem = CustomShopItem.fromTemplate(shopItemTemplate);
			if (fromShopItem == null) {
				shopitems.add(ShopItem.getBlank());
			} else {
				shopitems.add(fromShopItem);
			}
		}
		return shopitems;
	}

	@Override
	public ShopItem fromShopItem(ItemStack shopItem, boolean error) {
		//shopid 取得
		String shopItemId = CustomShopItem.getShopItemId(shopItem);
		//IDがなけばれ通常のshopと同じ扱いにする
		if (shopItemId == null) {
//			new LbnRuntimeException("invaild shop item id :" + ItemStackUtil.getLore(shopItem) + ", shop name:" + getName()).printStackTrace();
			return super.fromShopItem(shopItem, error);
		}

		ItemStack templateByShopId = getTemplateByShopId(shopItemId);
		if (templateByShopId == null) {
			new LbnRuntimeException("can not found template item. id:" + shopItemId + ", shop name:" + getName()).printStackTrace();
			return super.fromShopItem(shopItem, error);
		}
		return CustomShopItem.fromTemplate(templateByShopId);
	}

	public ItemStack getTemplateByShopId(String shopItemId) {
		if (loc == null) {
			return null;
		}

		Block block = loc.getBlock();
		BlockState state = block.getState();

		if (!(state instanceof Chest)) {
			return null;
		}
		Chest chest = (Chest) state;
		for (ItemStack shopItemTemplate : chest.getBlockInventory().getContents()) {
			String shopItemIdForTemplate = CustomShopItem.getShopItemId(shopItemTemplate);
			if (shopItemIdForTemplate == null) {
				continue;
			}

			if (shopItemIdForTemplate.equals(shopItemId)) {
				return shopItemTemplate;
			}
		}
		return null;
	}
}
