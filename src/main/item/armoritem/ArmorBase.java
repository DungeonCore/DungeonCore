package main.item.armoritem;

import main.item.ItemManager;
import main.item.itemInterface.ArmorItemable;
import main.mob.AbstractMob;
import main.mob.MobHolder;
import main.util.DungeonLog;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class ArmorBase {
	public static void onArmor(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		if (entity.getType() != EntityType.PLAYER) {
			return;
		}

		Player p = (Player) entity;

//		DungeonLog.printDevelopln(e.getCause().toString());
//		for (DamageModifier damageModifier : EntityDamageEvent.DamageModifier.values()) {
//			sendDebug(e, damageModifier);
//		}

		//一旦防具のダメージを全て消す
		if (e.isApplicable(DamageModifier.ARMOR)) {
			e.setDamage(DamageModifier.ARMOR, 0);
		}
		if (e.isApplicable(DamageModifier.MAGIC)) {
		e.setDamage(DamageModifier.MAGIC, 0);
		}

		//ダメージを与えたmob
		LivingEntity mob = null;
		//防具で軽減できるダメージならTRUE
		boolean isArmorCut = false;
		try {
			switch (e.getCause()) {
			case ENTITY_ATTACK:
				isArmorCut = true;
				//攻撃者をEntityとする
				Entity damager = ((EntityDamageByEntityEvent)e).getDamager();
				if (damager.getType().isAlive()) {
					mob = (LivingEntity) damager;
				}
				break;
			case PROJECTILE:
				isArmorCut = true;
				Entity damager2 = ((EntityDamageByEntityEvent)e).getDamager();
				if (((Projectile)damager2).getShooter() instanceof LivingEntity) {
					mob = (LivingEntity) ((Projectile)damager2).getShooter();
				}
				break;
			case ENTITY_EXPLOSION:
			case BLOCK_EXPLOSION:
			case CUSTOM:
				isArmorCut = true;
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		//bossかどうか調べてダメージを軽減する
		boolean isBoss = false;
		double damage = e.getDamage();
		if (mob != null) {
			AbstractMob<?> customMob = MobHolder.getMob(mob);
			isBoss = customMob.isNullMob() || !customMob.isBoss();
		}

		//ダメージのカット率
		double baseDamageCuteParcent = 0;

		double strengthDamageCuteParcent = 1;

		EntityEquipment equipment = p.getEquipment();
		//防具を取得
		for (ItemStack armor : equipment.getArmorContents()) {
			ArmorItemable customItem = ItemManager.getCustomItem(ArmorItemable.class, armor);
			if (customItem == null) {
				continue;
			}
			//防具でカットできるダメージのみ計算を行う
			if (isArmorCut) {
				//ブロック率を計算
				if (isBoss) {
					baseDamageCuteParcent += customItem.getBaseBossDamageCuteParcent(p, e, armor);
				} else {
					baseDamageCuteParcent += customItem.getBaseDamageCuteParcent(p, e, armor);
				}
			}

			//強化によるダメージ軽減
			strengthDamageCuteParcent = strengthDamageCuteParcent * (1 - customItem.getStrengthDamageCuteParcent(p, e, armor, isArmorCut, isBoss, mob));
			//その他の特殊ダメージ軽減
			customItem.extraDamageCut(p, e, armor, isArmorCut, isBoss, mob);
		}
		damage = damage * (1- baseDamageCuteParcent) * strengthDamageCuteParcent;

		e.setDamage(Math.max(damage, 0));
	}

	protected static void sendDebug(EntityDamageEvent e, DamageModifier type) {
		DungeonLog.printDevelopln(type + ":" + e.getDamage(type));
	}
}
