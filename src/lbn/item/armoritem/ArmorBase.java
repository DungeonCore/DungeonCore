package lbn.item.armoritem;

import java.text.MessageFormat;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.item.ItemManager;
import lbn.item.itemInterface.ArmorItemable;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.player.PlayerChecker;
import lbn.util.DungeonLogger;

import org.bukkit.ChatColor;
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

		//メインレベル
		int mainLevel;
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
		//データが読み込まれていない時, またはクリエのときはとりあえず装備可能にする
		if (theLowPlayer == null || PlayerChecker.isNonNormalPlayer(p)) {
			mainLevel = 1000;
		} else {
			mainLevel = theLowPlayer.getLevel(LevelType.MAIN);
		}

		// 一旦防具のダメージを全て消す
		if (e.isApplicable(DamageModifier.ARMOR)) {
			e.setDamage(DamageModifier.ARMOR, 0);
		}
		if (e.isApplicable(DamageModifier.MAGIC)) {
			e.setDamage(DamageModifier.MAGIC, 0);
		}

		// ダメージを与えたmob
		LivingEntity mob = null;
		// 防具で軽減できるダメージならTRUE
		boolean isArmorCut = false;
		try {
			switch (e.getCause()) {
			case ENTITY_ATTACK:
				isArmorCut = true;
				// 攻撃者をEntityとする
				Entity damager = ((EntityDamageByEntityEvent) e).getDamager();
				if (damager.getType().isAlive()) {
					mob = (LivingEntity) damager;
				}
				break;
			case PROJECTILE:
				isArmorCut = true;
				Entity damager2 = ((EntityDamageByEntityEvent) e).getDamager();
				if (((Projectile) damager2).getShooter() instanceof LivingEntity) {
					mob = (LivingEntity) ((Projectile) damager2).getShooter();
				}
				break;
			case ENTITY_EXPLOSION:
			case BLOCK_EXPLOSION:
			case CUSTOM:
			case LAVA:
				isArmorCut = true;
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// bossかどうか調べてダメージを軽減する
		boolean isBoss = false;
		double damage = e.getDamage();
		if (mob != null) {
			AbstractMob<?> customMob = MobHolder.getMob(mob);
			isBoss = customMob.isNullMob() || !customMob.isBoss();
		}

		//装備を取得
		EntityEquipment equipment = p.getEquipment();

		double totalArmorPoint = 0;
		// 防具を取得
		for (ItemStack armor : equipment.getArmorContents()) {
			ArmorItemable customItem = ItemManager.getCustomItem(ArmorItemable.class, armor);
			if (customItem == null) {
				continue;
			}

			//レベルを調べる
			if (customItem.getAvailableLevel() > mainLevel) {
				p.sendMessage(MessageFormat.format("{0}この防具はメインレベル{1}以上のPlayerのみ効果があります。({2})", ChatColor.RED, mainLevel, customItem.getItemName()));
				continue;
			}

			// 防具でカットできるダメージのみ計算を行う
			if (isArmorCut) {
				// ブロック率を計算
				if (isBoss) {
					totalArmorPoint += customItem.getArmorPointForBossMob();
				} else {
					totalArmorPoint += customItem.getArmorPointForNormalMob();
				}
			}
			//ダメージを修正する
			damage = customItem.getModifiedDamage(damage, p, e, isBoss, mob);
		}
		//防御ポイントを用いてダメージを修正する
		double cutParcent = getDamageCutParcent(totalArmorPoint);
		damage = damage * cutParcent;

		//ダメージをセット
		e.setDamage(Math.max(damage, 0));
	}

	/**
	 * ダメージのカット率を取得
	 * @param totalArmorPoint
	 * @return
	 */
	public static double getDamageCutParcent(double totalArmorPoint) {
		return 50.0 / (50.0 + totalArmorPoint);
	}

	protected static void sendDebug(EntityDamageEvent e, DamageModifier type) {
		DungeonLogger.development(type + ":" + e.getDamage(type));
	}
}