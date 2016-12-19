package main;

import java.util.Collection;
import java.util.HashSet;

import main.item.ItemManager;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class Config {
	static HashSet<Material> notClickBlock = new HashSet<Material>();
	static HashSet<Material> notClickItem = new HashSet<Material>();
	static HashSet<EntityType> notClickEntity = new HashSet<EntityType>();
	static HashSet<EntityType> notDamageEntity = new HashSet<EntityType>();
	static {
		notClickBlock.add(Material.ANVIL);
		notClickBlock.add(Material.ENCHANTMENT_TABLE);
		notClickBlock.add(Material.PAINTING);
		notClickBlock.add(Material.ITEM_FRAME);
		notClickBlock.add(Material.FURNACE);
		notClickBlock.add(Material.BURNING_FURNACE);
		notClickBlock.add(Material.DISPENSER);
		notClickBlock.add(Material.DROPPER);
		notClickBlock.add(Material.BEACON);
		notClickBlock.add(Material.HOPPER);
		notClickBlock.add(Material.HOPPER_MINECART);
		notClickBlock.add(Material.FIRE);

		notClickItem.add(Material.EYE_OF_ENDER);
		notClickItem.add(Material.ENDER_PEARL);
		notClickItem.add(Material.BUCKET);
		notClickItem.add(Material.LAVA_BUCKET);
		notClickItem.add(Material.WATER_BUCKET);
		notClickItem.add(Material.FLINT_AND_STEEL);

		notClickEntity.add(EntityType.ITEM_FRAME);
		notDamageEntity.add(EntityType.ITEM_FRAME);
	}

	public static HashSet<Material> getClickCancelblock() {
		return notClickBlock;
	}

	public static HashSet<Material> getClickCancelItem() {
		return notClickItem;
	}

	public static HashSet<EntityType> getClickCancelEntityType() {
		return notClickEntity;
	}

	public static HashSet<EntityType> getDamageCancelEntityType() {
		return notDamageEntity;
	}

	public static boolean allowCraft(ItemStack result, Collection<ItemStack> materials) {
		if (ItemManager.getCustomItem(result) != null) {
			return true;
		}
		return false;
	}

	public static String DEVELOPER_TWITTER_ID = "@namiken1993";

}
