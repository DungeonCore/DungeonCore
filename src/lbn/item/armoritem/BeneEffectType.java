package lbn.item.armoritem;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum BeneEffectType {
	PROTECTION_FALL(new BeneEffectProtection(DamageCause.FALL, 0.05), "落下耐性", "1"),
	PROTECTION_FIRE(new BeneEffectProtection(DamageCause.FIRE_TICK, 0.05), "火炎耐性", "2"),
	PROTECTION_EXPLOTION(new BeneEffectProtection(DamageCause.BLOCK_EXPLOSION, 0.06), "爆破耐性", "3"),
	PROTECTION_POISON(new BeneEffectProtection(DamageCause.POISON, 0.06), "毒耐性", "4"),
	PROTECTION_WITHER(new BeneEffectProtection(DamageCause.WITHER, 0.06), "ウィザー耐性", "5"),
	PROTECTION_DROWN(new BeneEffectProtection(DamageCause.DROWNING, 0.07), "窒息耐性", "6"),
	RESISTANCE_ZOMBIE(new BeneEffectResistance(EntityType.ZOMBIE, 0.05), "ゾンビ耐性", "7"),
	RESISTANCE_SKELETON(new BeneEffectResistance(EntityType.SKELETON, 0.05), "スケルトン耐性", "8"),
	RESISTANCE_SPIDER(new BeneEffectResistance(EntityType.SPIDER, 0.05), "クモ耐性", "9"),
	RESISTANCE_GIANT(new BeneEffectResistance(EntityType.GIANT, 0.05), "ジャイアント耐性", "10"),
	RESISTANCE_PIGMAN(new BeneEffectResistance(EntityType.PIG_ZOMBIE, 0.05), "ピッグマン耐性", "11"),
	POTION_SPEED(new BeneEffectPotion(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1), 0.015, false), "ダメージ時確率でスピード付与", "12"),
	POTION_RESISTANC(new BeneEffectPotion(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 2, 0), 0.015, false), "ダメージ時確率で耐性付与", "13"),
	POTION_REGENERATION(new BeneEffectPotion(new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 0), 0.015, false), "ダメージ時確率でリジェネレーション付与", "14"),
	POTION_SLOW(new BeneEffectPotion(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1), 0.03, true), "ダメージ時確率で敵に鈍足付与", "15"),
	POTION_WEAKNESS(new BeneEffectPotion(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 3, 1), 0.02, true), "ダメージ時確率で敵に弱体化付与", "16"),
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

class BeneEffectProtection implements BeneEffectExcutor{
	DamageCause cause;
	double oneLevelProtectionParcent;
	public BeneEffectProtection(DamageCause cause, double oneLevelProtectionParcent) {
		this.cause = cause;
		this.oneLevelProtectionParcent = oneLevelProtectionParcent;
	}

	@Override
	public void execute(Player me, EntityDamageEvent e, ItemStack armor,
			boolean isArmorCutDamage, boolean isBoss, LivingEntity mob, int level) {
		if (e.getCause() == cause) {
			double damage = e.getDamage();
			damage = (1 - oneLevelProtectionParcent * level) * damage;
			e.setDamage(damage);
		}
	}
}

class BeneEffectResistance implements BeneEffectExcutor{
	protected BeneEffectResistance(EntityType type,
			double oneLevelProtectionParcent) {
		this.type = type;
		this.oneLevelProtectionParcent = oneLevelProtectionParcent;
	}

	EntityType type;
	double oneLevelProtectionParcent;

	@Override
	public void execute(Player me, EntityDamageEvent e, ItemStack armor,
			boolean isArmorCutDamage, boolean isBoss, LivingEntity mob,
			int level) {
		if (e.getEntityType() == type) {
			double damage = e.getDamage();
			damage = (1 - oneLevelProtectionParcent * level) * damage;
			e.setDamage(damage);
		}
	}

}

class BeneEffectPotion implements BeneEffectExcutor{
	protected BeneEffectPotion(PotionEffect type, double parcent,
			boolean applyEnemy) {
		this.type = type;
		this.parcent = parcent;
		this.applyEnemy = applyEnemy;
	}

	PotionEffect type;
	boolean applyEnemy;
	double parcent;

	Random rnd = new Random();

	@Override
	public void execute(Player me, EntityDamageEvent e, ItemStack armor,
			boolean isArmorCutDamage, boolean isBoss, LivingEntity mob,
			int level) {
		int nextInt = rnd.nextInt(100);
		if (parcent * 100 * level > nextInt) {
			if (applyEnemy) {
				type.apply(mob);
			} else {
				type.apply(me);
			}
		}

	}
}

class BeneEffectNull implements BeneEffectExcutor {

	@Override
	public void execute(Player me, EntityDamageEvent e, ItemStack armor,
			boolean isArmorCutDamage, boolean isBoss, LivingEntity mob,
			int level) {
		// TODO 自動生成されたメソッド・スタブ

	}

}

