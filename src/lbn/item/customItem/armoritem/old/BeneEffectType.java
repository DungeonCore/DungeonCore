package lbn.item.customItem.armoritem.old;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public enum BeneEffectType {
	BENE_EFFECT_UNKNOW(new BeneEffectNull(), "????????", "17");

	public static final String BENE_ID = "   beneid:";

	static HashMap<String, BeneEffectType> idMap = new HashMap<String, BeneEffectType>();
	static {
		for (BeneEffectType type : values()) {
			idMap.put(type.id, type);
		}
	}

	BeneEffectExcutor excuter;
	String title;
	String id;
	private BeneEffectType(BeneEffectExcutor excuter, String title, String id) {
		this.excuter = excuter;
		this.title = title;
		this.id = id;
	}

	public void execute(Player me, EntityDamageEvent e,
			ItemStack armor, boolean isArmorCutDamage, boolean isBoss,
			LivingEntity mob, int level) {
		excuter.execute(me, e, armor, isArmorCutDamage, isBoss, mob, level);
	}

	public String getLine(int level) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(title);

		if (this != BENE_EFFECT_UNKNOW) {
			stringBuilder.append("  lv.");
			stringBuilder.append(level);
		}
		stringBuilder.append(ChatColor.BLACK);
		stringBuilder.append(BENE_ID);
		stringBuilder.append(id);
		return stringBuilder.toString();
	}

	public static BeneEffectType getBeneTypeById(String id) {
		return idMap.get(id);
	}
}

interface BeneEffectExcutor {
	void execute(Player me, EntityDamageEvent e,
			ItemStack armor, boolean isArmorCutDamage, boolean isBoss,
			LivingEntity mob, int level);
}

class BeneEffectNull implements BeneEffectExcutor {

	@Override
	public void execute(Player me, EntityDamageEvent e, ItemStack armor,
			boolean isArmorCutDamage, boolean isBoss, LivingEntity mob,
			int level) {
	}

}

