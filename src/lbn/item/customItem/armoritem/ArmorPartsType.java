package lbn.item.customItem.armoritem;

import org.bukkit.Material;

public enum ArmorPartsType {
	HELMET, CHEST_PLATE, LEGGINGS, BOOTS;

	/**
	 * 防具の種類を取得
	 * 
	 * @param name
	 * @return
	 */
	public static ArmorPartsType getType(String name) {
		if (name == null) {
			return null;
		}
		try {
			return valueOf(name.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * アイテムの素材から取得
	 * 
	 * @return
	 */
	public static ArmorPartsType getType(Material m) {
		if (m == null) {
			return null;
		}
		switch (m) {
		case DIAMOND_HELMET:
		case IRON_HELMET:
		case GOLD_HELMET:
		case LEATHER_HELMET:
			return HELMET;
		case DIAMOND_CHESTPLATE:
		case IRON_CHESTPLATE:
		case GOLD_CHESTPLATE:
		case LEATHER_CHESTPLATE:
			return CHEST_PLATE;
		case DIAMOND_LEGGINGS:
		case IRON_LEGGINGS:
		case GOLD_LEGGINGS:
		case LEATHER_LEGGINGS:
			return LEGGINGS;
		case DIAMOND_BOOTS:
		case IRON_BOOTS:
		case GOLD_BOOTS:
		case LEATHER_BOOTS:
			return BOOTS;
		default:
			return null;
		}
	}
}
