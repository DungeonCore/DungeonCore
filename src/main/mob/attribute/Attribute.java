package main.mob.attribute;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class Attribute {
	public abstract String getName();

	public String getTag() {
		return "[✠] ";
	}

	abstract public ChatColor getColor();

	public String getPrefix() {
		if (getColor() == null) {
			return getTag();
		} else {
			return getColor() + getTag();
		}
	}

	public static String removePrefix(String str) {
		if (str == null) {
			return null;
		}
		return ChatColor.stripColor(str).replace("[✠] ", "");
	}

	/**
	 * ダメージ受けた時
	 * @param mob
	 * @param target
	 * @param e
	 */
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		e.setDamage(e.getDamage() * 0.5);
	}

	/**
	 * ダメージ与えた時
	 * @param mob
	 * @param damager
	 * @param e
	 */
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		e.setDamage(e.getDamage() * 3.0);
	}

	public boolean isSame(Attribute attribute) {
		if (attribute == null) {
			return false;
		}
		return getName().equals(attribute.getName());
	}

	public boolean isNonAttribute() {
		return false;
	}

	public static Attribute getAttribute(String name) {
		if ("木属性".equals(name)) {
			return new AttributeTree();
		}
		return AttributeNormal.getInstance();
	}

}
