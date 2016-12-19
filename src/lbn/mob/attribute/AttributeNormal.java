package lbn.mob.attribute;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttributeNormal extends Attribute{

	static AttributeNormal instance = new AttributeNormal();

	public static AttributeNormal getInstance() {
		return instance;
	}

	@Override
	public String getName() {
		return "属性無し";
	}

	@Override
	public ChatColor getColor() {
		return null;
	}

	@Override
	public String getTag() {
		return "";
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
	}

	public boolean isNonAttribute() {
		return true;
	}

}
