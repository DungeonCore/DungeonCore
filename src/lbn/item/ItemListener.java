package lbn.item;

import java.util.ArrayList;

import lbn.NbtTagConst;
import lbn.common.event.player.PlayerBreakMagicOreEvent;
import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.event.player.PlayerKillEntityEvent;
import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.common.projectile.ProjectileManager;
import lbn.item.customItem.armoritem.ArmorBase;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.item.customItem.attackitem.weaponSkill.WeaponSkillExecutor;
import lbn.item.itemInterface.BowItemable;
import lbn.item.itemInterface.CombatItemable;
import lbn.item.itemInterface.EntityKillable;
import lbn.item.itemInterface.EquipItemable;
import lbn.item.itemInterface.FoodItemable;
import lbn.item.itemInterface.LeftClickItemable;
import lbn.item.itemInterface.MagicPickaxeable;
import lbn.item.itemInterface.MeleeAttackItemable;
import lbn.item.itemInterface.RightClickItemable;
import lbn.item.itemInterface.StrengthChangeItemable;
import lbn.item.slot.SlotInterface;
import lbn.item.slot.slot.CombatSlot;
import lbn.item.slot.slot.KillSlot;
import lbn.item.slot.table.SlotSetTableOperation;
import lbn.mob.LastDamageManager;
import lbn.mob.LastDamageMethodType;
import lbn.util.ItemStackUtil;
import lbn.util.LivingEntityUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class ItemListener implements Listener{

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Action action = e.getAction();

		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();

		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			RightClickItemable clickItem = ItemManager.getCustomItem(RightClickItemable.class, item);
			if (clickItem != null) {
				clickItem.excuteOnRightClick(e);
			}
		} else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
			LeftClickItemable clickItem = ItemManager.getCustomItem(LeftClickItemable.class, item);
			if (clickItem != null) {
				clickItem.excuteOnLeftClick(e);
			}
		}
	}

	@EventHandler
	public void onShootBow(EntityShootBowEvent event) {
		ItemStack bow = event.getBow();
		BowItemable bowItem = ItemManager.getCustomItem(BowItemable.class, bow);
		if (bowItem != null) {
			bowItem.excuteOnShootBow(event);
		}
	}

	@EventHandler
	public void onLunchProjectile(ProjectileLaunchEvent e) {
		Projectile entity = e.getEntity();

		//打った瞬間、手に持ったものからProjectileIDを取得
		ProjectileSource shooter = entity.getShooter();
		if (!(shooter instanceof LivingEntity)) {
			return;
		}
		ItemStack itemInHand = ((LivingEntity) shooter).getEquipment().getItemInHand();
		//custom itemでないなら何もしない
		BowItemable item = ItemManager.getCustomItem(BowItemable.class, itemInHand);
		if (item == null) {
			return;
		}
		ProjectileManager.onLaunchProjectile(entity, item, itemInHand);
	}

	/**
	 * プレイヤーが敵にダメージを与える
	 * @param e
	 */
	@EventHandler(priority=EventPriority.LOW)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (!e.getEntityType().isAlive()) {
			return;
		}

		//プレイヤーがダメージを受けた場合は無視
		if (e.getEntityType() == EntityType.PLAYER) {
			return;
		}

		Entity damager = e.getDamager();
		//直接の攻撃の時
		if (damager.getType().isAlive() && ((LivingEntity)damager).getEquipment() != null ) {
			//手に持っているアイテムの情報を取得する
			ItemStack itemInHand = ((LivingEntity)damager).getEquipment().getItemInHand();
			MeleeAttackItemable customItem = ItemManager.getCustomItem(MeleeAttackItemable.class, itemInHand);
			if (customItem != null) {
				customItem.excuteOnMeleeAttack(itemInHand, (LivingEntity)e.getDamager(), (LivingEntity)e.getEntity(), e);
			}
		}
	}

	/**
	 * 防具の処理
	 * @param e
	 */
	@EventHandler
	public void onDamageAll(EntityDamageEvent e) {
		EntityType entityType = e.getEntityType();
		if (entityType != EntityType.PLAYER) {
			return;
		}

		//しばらくは両方で処理を行う
		//TODO 後で消す
//		OldArmorBase.onArmor(e);
		ArmorBase.onArmor(e);

//		Player p = (Player) e.getEntity();
//		EntityEquipment equipment = p.getEquipment();
//		//防具を取得
//		for (ItemStack armor : equipment.getArmorContents()) {
//			ArmorItemable customItem = ItemManager.getCustomItem(ArmorItemable.class, armor);
//			if (customItem == null) {
//				continue;
//			}
//			if (e instanceof EntityDamageByEntityEvent) {
//				customItem.excuteOnDefenceEntity(((EntityDamageByEntityEvent) e).getDamager(), p, (EntityDamageByEntityEvent)e, armor);
//			} else {
//				customItem.excuteOnDefenceNotEntity(p, e, armor);
//			}
//		}
	}

	@EventHandler
	public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {
		ItemStack item = event.getItem();
		StrengthChangeItemable customItem = ItemManager.getCustomItem(StrengthChangeItemable.class, item);
		if (customItem != null) {
			customItem.onSetStrengthItemResult(event);
		}
	}

	@EventHandler
	public void onPlayerStrengthItemEvent(PlayerStrengthFinishEvent event) {
		ItemStack item = event.getItem();
		StrengthChangeItemable customItem = ItemManager.getCustomItem(StrengthChangeItemable.class, item);
		if (customItem != null) {
			customItem.onPlayerStrengthFinishEvent(event);
		}
	}

	@EventHandler
	public void closeCraftingTable(InventoryCloseEvent e) {
		SlotSetTableOperation.removeGlass(e);
	}

	@EventHandler
	public void inventoryClick (final InventoryClickEvent e) {
		SlotSetTableOperation.inventoryClick(e);
	}

	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		FoodItemable customItem = ItemManager.getCustomItem(FoodItemable.class, item);
		if (customItem != null) {
			customItem.onPlayerItemConsumeEvent(e);
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		//死んだのが敵でなければ何もしない
		if (!LivingEntityUtil.isEnemy(e.getEntity())) {
			return;
		}

		LivingEntity entity = e.getEntity();
		EntityDamageEvent lastDamageCause = entity.getLastDamageCause();
		//最後に倒したのがEntityでなければ何もしない
		if (!(lastDamageCause instanceof EntityDamageByEntityEvent)) {
			return;
		}

		LastDamageMethodType lastDamageMethod = LastDamageManager.getLastDamageAttackType(entity);
		Player player = LastDamageManager.getLastDamagePlayer(entity);

		//倒すときに使ったアイテム
		ItemStack item = null;

		//最後に攻撃をしたEntityを取得
		Entity damager = ((EntityDamageByEntityEvent)lastDamageCause).getDamager();

		if (lastDamageMethod == LastDamageMethodType.SWORD || lastDamageMethod == LastDamageMethodType.BOW) {
			//攻撃をしたのが生き物なら
			if (damager.getType().isAlive()) {
				//手に持っているアイテムで倒したと判断
				item = player.getItemInHand();
			} else {
				//攻撃したのがProjectileならその情報からItemを取得
				item = ProjectileManager.getItemStack(damager);
			}
		}

		//攻撃につかったアイテムが不明な時は無視する
		if (item == null) {
			return;
		}

		ItemInterface customItem = ItemManager.getCustomItem(item);
		//カスタムアイテムでないなら何もしない
		if (customItem == null) {
			return;
		}

		//もしLastDamageManagerの攻撃手段と、使ったアイテムの攻撃手段が異なるならエラーにする
		if (LastDamageMethodType.fromAttackType(customItem.getAttackType()) != lastDamageMethod) {
			new RuntimeException("Player:" + player.getName() + "がモンスターを倒した際にエラーが発生しました。(manager:" + lastDamageMethod + "と item:" + LastDamageMethodType.fromAttackType(customItem.getAttackType())
			+ "が一致しません").printStackTrace();;
		}

		PlayerKillEntityEvent playerKillEntityEvent = new PlayerKillEntityEvent(player, entity, item);
		playerKillEntityEvent.callEvent();
	}

	@EventHandler
	public void onCombat(PlayerCombatEntityEvent e) {
		ArrayList<SlotInterface> useSlot = e.getAttackItem().getUseSlot();
		for (SlotInterface slot : useSlot) {
			if (slot instanceof CombatSlot) {
				((CombatSlot) slot).onCombat(e);
			}
		}

		//武器スキルを実行
		WeaponSkillExecutor.executeWeaponSkillOnCombat(e);
	}

	@EventHandler
	public void onKill(PlayerKillEntityEvent e) {
		ArrayList<SlotInterface> useSlot = e.getAttackItem().getUseSlot();
		for (SlotInterface slot : useSlot) {
			if (slot instanceof KillSlot) {
				((KillSlot) slot).onKill(e);
			}
		}

		EntityKillable customItem = ItemManager.getCustomItem(EntityKillable.class, e.getAttackItem().getItem());
		if (customItem != null) {
			customItem.onKillEvent(e);
		}
	}

	@EventHandler
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {
		ItemStack item = e.getItem();
		//もし指定したアイテムDamageItemableでないなら無視
		EquipItemable customItem = ItemManager.getCustomItem(EquipItemable.class, item);
		if (customItem == null) {
			return;
		}

		//現在の耐久を取得
		short customDurability = ItemStackUtil.getNBTTagShort(item, NbtTagConst.THELOW_DURABILITY);
		//耐久値を更新
		customDurability += e.getDamage();

		//設定した固有の最大耐久
		int customMaxDurability = customItem.getMaxDurability(e.getItem());
		//実際の素材の最大耐久
		short itemMaxDurability = item.getType().getMaxDurability();

		//耐久をセットする
		ItemStackUtil.setNBTTag(item, NbtTagConst.THELOW_DURABILITY, customDurability);

		short ItemDurability = (short) (itemMaxDurability * customDurability / customMaxDurability);
		//耐久をセットする
		item.setDurability(ItemDurability);

		//アイテムの耐久がまだ残っているならここで耐久をセットしたので何もしない
		if (ItemDurability < itemMaxDurability) {
			//耐久をここでセットしたのでEvent後はセットしない
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onCombatEntity(PlayerCombatEntityEvent e) {
		//もし指定したアイテムDamageItemableでないなら無視
		AbstractAttackItem itemInterface = e.getAttackItem().getItemInterface();
		if (itemInterface == null) {
			return;
		}
		itemInterface.onCombatEntity(e);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		//もし指定したアイテムDamageItemableでないなら無視
		CombatItemable customItem = ItemManager.getCustomItem(CombatItemable.class, e.getItemDrop().getItemStack());
		if (customItem == null) {
			return;
		}
		customItem.onPlayerDropItemEvent(e);
	}

	@EventHandler
	public void onPlayerBreakMagicOreEvent(PlayerBreakMagicOreEvent e) {
		Player player = e.getPlayer();
		//所持しているアイテムを取得
		ItemStack itemInHand = player.getItemInHand();

		//登録されているアイテム以外なら鉱石を掘らせない
		MagicPickaxeable customItem = ItemManager.getCustomItem(MagicPickaxeable.class, itemInHand);
		if (customItem == null) {
			e.setCancelled(true);
		} else {
			customItem.onPlayerBreakMagicOreEvent(e);
		}
	}
}
