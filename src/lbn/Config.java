package lbn;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemManager;

public class Config {
	private static Set<Material> notClickBlock = new HashSet<Material>();
	private static Set<Material> notClickItem = new HashSet<Material>();
	private static Set<EntityType> notClickEntity = new HashSet<EntityType>();
	private static Set<EntityType> notDamageEntity = new HashSet<EntityType>();
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

	public static Set<Material> getClickCancelblock() {
		return notClickBlock;
	}

	public static Set<Material> getClickCancelItem() {
		return notClickItem;
	}

	public static Set<EntityType> getClickCancelEntityType() {
		return notClickEntity;
	}

	public static Set<EntityType> getDamageCancelEntityType() {
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
