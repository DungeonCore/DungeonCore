package lbn.item.customItem.armoritem.old;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class BeneEffect {
	String id;
	String name;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setDamage(Player me, EntityDamageEvent e,
			ItemStack armor, boolean isArmorCutDamage, boolean isBoss,
			LivingEntity mob) {

	}

}
