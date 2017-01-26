package lbn.item;

import java.util.ArrayList;
import java.util.List;

import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.event.ChangeStrengthLevelItemEvent;
import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.event.player.PlayerKillEntityEvent;
import lbn.common.event.player.PlayerRightShiftClickEvent;
import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.dungeoncore.Main;
import lbn.item.armoritem.ArmorBase;
import lbn.item.attackitem.AttackItemStack;
import lbn.item.itemInterface.BowItemable;
import lbn.item.itemInterface.CombatItemable;
import lbn.item.itemInterface.LeftClickItemable;
import lbn.item.itemInterface.MeleeAttackItemable;
import lbn.item.itemInterface.RightClickItemable;
import lbn.item.itemInterface.StrengthChangeItemable;
import lbn.item.slot.SlotInterface;
import lbn.item.slot.slot.CombatSlot;
import lbn.item.slot.slot.KillSlot;
import lbn.item.slot.slot.ShiftRightClickSlot;
import lbn.item.slot.table.SlotSetTableOperation;
import lbn.item.strength.CraeteStrengthItemResultLater;
import lbn.item.strength.StrengthLaterRunnable;
import lbn.item.strength.StrengthTableOperation;
import lbn.mob.LastDamageManager;
import lbn.player.AttackType;
import lbn.util.LivingEntityUtil;
import lbn.util.Message;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
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
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
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
		//発射されたものに対して弓の情報をつけておく
		if (bowItem != null) {
			bowItem.excuteOnShootBow(event);
			//弓の情報を削除する
			event.getProjectile().removeMetadata("bow_date_lbn_doungeon_customitem", Main.plugin);
			event.getProjectile().removeMetadata("bow_date_lbn_doungeon_itemstack", Main.plugin);
			//弓の情報をつける
			event.getProjectile().setMetadata("bow_date_lbn_doungeon_customitem", new FixedMetadataValue(Main.plugin, bowItem));
			//弓のアイテム情報をつける
			event.getProjectile().setMetadata("bow_date_lbn_doungeon_itemstack", new FixedMetadataValue(Main.plugin, bow));
		}
	}

	@EventHandler
	public void onLunchProjectile(ProjectileLaunchEvent e) {
		Projectile entity = e.getEntity();
		if (entity.getType() == EntityType.ARROW) {
			return;
		}

		ProjectileSource shooter = entity.getShooter();
		if (!(shooter instanceof LivingEntity)) {
			return;
		}
		ItemStack itemInHand = ((LivingEntity) shooter).getEquipment().getItemInHand();
		//custom itemでないなら何もしない
		ItemInterface item = ItemManager.getCustomItem(itemInHand);
		if (item == null) {
			return;
		}
		AttackType attackType = item.getAttackType();
		entity.setMetadata("the_low_launched_projectile_attack_type", new FixedMetadataValue(Main.plugin, attackType));
		entity.setMetadata("the_low_launched_projectile_attack_entity", new FixedMetadataValue(Main.plugin, shooter));
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile entity = event.getEntity();
		List<MetadataValue> metadata = entity.getMetadata("bow_date_lbn_doungeon_customitem");
		List<MetadataValue> metadataBow = entity.getMetadata("bow_date_lbn_doungeon_itemstack");
		//着地したものに弓の情報がある確認する
		if (metadata.size() != 0 || metadataBow.size() != 0) {
			BowItemable bowItem = (BowItemable)metadata.get(0).value();
			ItemStack bow = (ItemStack)metadataBow.get(0).value();
			bowItem.excuteOnProjectileHit(event, bow);
		}
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

		LivingEntity entity = (LivingEntity) e.getEntity();

		Entity damager = e.getDamager();
		if (damager instanceof LivingEntity && ((LivingEntity)damager).getEquipment() != null ) {
			//手に持っているアイテムの情報を取得する
			ItemStack itemInHand = ((LivingEntity)damager).getEquipment().getItemInHand();
			MeleeAttackItemable customItem = ItemManager.getCustomItem(MeleeAttackItemable.class, itemInHand);
			if (customItem != null) {
				customItem.excuteOnMeleeAttack(itemInHand, (LivingEntity)e.getDamager(), (LivingEntity)e.getEntity(), e);
			}
		} else if (damager instanceof Projectile) {
			if (damager.getType() == EntityType.ARROW) {
				List<MetadataValue> metadata = damager.getMetadata("bow_date_lbn_doungeon_customitem");
				List<MetadataValue> metadataBow = damager.getMetadata("bow_date_lbn_doungeon_itemstack");
				//着地したものに弓の情報がある確認する
				if (metadata.size() != 0 || metadataBow.size() != 0) {
					BowItemable bowItem = (BowItemable)metadata.get(0).value();
					ItemStack bow = (ItemStack)metadataBow.get(0).value();
					bowItem.excuteOnProjectileDamage(e, bow, (LivingEntity)((Projectile)damager).getShooter(), entity);
				}
			} else {
				List<MetadataValue> metadata1 = damager.getMetadata("the_low_launched_projectile_attack_type");
				List<MetadataValue> metadata2 = damager.getMetadata("the_low_launched_projectile_attack_entity");
				if (metadata1.size() != 0 || metadata2.size() != 0) {
					AttackType type = (AttackType) metadata1.get(0).value();
					LivingEntity shooter = (LivingEntity) metadata2.get(0).value();
					if (shooter.getType() == EntityType.PLAYER) {
						LastDamageManager.onDamage(entity, (Player) shooter, type);
					}
				}
			}
		}
//		else if (e.getEntity() instanceof LivingEntity) {
//			LivingEntity target = (LivingEntity) e.getEntity();
//			EntityEquipment equipment = target.getEquipment();
//			for (ItemStack armor : equipment.getArmorContents()) {
//				ArmorItemable customItem = ItemManager.getCustomItem(ArmorItemable.class, armor);
//				if (customItem != null) {
//					customItem.excuteOnDefenceEntity(e.getDamager(), (LivingEntity)e.getEntity(), e, armor);
//				}
//			}
//		}
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
	public void onChangeStrengthLevelItemEvente(ChangeStrengthLevelItemEvent event) {
		ItemStack item = event.getBefore();
		StrengthChangeItemable customItem = ItemManager.getCustomItem(StrengthChangeItemable.class, item);
		if (customItem != null) {
			customItem.onChangeStrengthLevelItemEvent(event);
		}
	}


	@EventHandler
	public void closeCraftingTable(InventoryCloseEvent e) {
		StrengthTableOperation.removeGlass(e);
		SlotSetTableOperation.removeGlass(e);
	}

	@EventHandler
	public void inventoryClick (final InventoryClickEvent e) {
		SlotSetTableOperation.inventoryClick(e);

		if (!StrengthTableOperation.isOpenStrengthTable(e.getWhoClicked())) {
			return;
		}

		//絶対に作業台が開いているはず
		if (!(e.getView().getTopInventory() instanceof CraftingInventory)) {
			return;
		}

		//黄色のガラスと赤のガラスはクリックしてもキャンセルする
		ItemStack currentItem = e.getCurrentItem();
		if (StrengthTableOperation.isRedGlass(currentItem) || StrengthTableOperation.isYellowGlass(currentItem)) {
			e.setCancelled(true);
		}

		//Playerデータがロードされていないので何もしない
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer((Player)e.getWhoClicked());
		if (theLowPlayer == null) {
			Message.sendMessage((Player)e.getWhoClicked(), ChatColor.RED + "現在Playerデータをロードしています。もう暫くお待ち下さい");
			return;
		}

		CraftingInventory top = (CraftingInventory) e.getView().getTopInventory();
		//強化を行う
		if (e.getSlotType() != SlotType.RESULT){
			new StrengthLaterRunnable(top, e, theLowPlayer).runTaskLater(Main.plugin, 1);
		} else {
			if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT) {
				if (top.getResult() == null || top.getResult().getType() == Material.AIR) {
					e.setCancelled(true);
					return;
				}
				new CraeteStrengthItemResultLater(e, theLowPlayer).runTaskLater(Main.plugin);
			} else {
				e.setCancelled(true);
			}
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

		AttackType attackType = LastDamageManager.getLastDamageAttackType(entity);
		Player player = LastDamageManager.getLastDamagePlayer(entity);

		//倒すときに使ったアイテム
		ItemStack item = null;

		//最後に倒したのが剣のとき
		if (attackType == AttackType.SWORD) {
			//手に持っているアイテムで倒したと判断
			item = player.getItemInHand();
		} else if (attackType == AttackType.BOW) {
			Entity damager = ((EntityDamageByEntityEvent)lastDamageCause).getDamager();
			//最後に倒したのが矢なら
			if (damager.getType() == EntityType.ARROW) {
				//メタデータを取得
				List<MetadataValue> metadataBow = damager.getMetadata("bow_date_lbn_doungeon_itemstack");
				if (metadataBow.size() != 0) {
					item = (ItemStack)metadataBow.get(0).value();
				}
			}
		} else {
			//魔法に関しては別処理でやっているためここではやらない
		}

		if (item == null) {
			return;
		}

		ItemInterface customItem = ItemManager.getCustomItem(item);
		//カスタムアイテムでないなら何もしない
		if (customItem == null) {
			return;
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
	}

	@EventHandler
	public void onKill(PlayerKillEntityEvent e) {
		ArrayList<SlotInterface> useSlot = e.getAttackItem().getUseSlot();
		for (SlotInterface slot : useSlot) {
			if (slot instanceof KillSlot) {
				((KillSlot) slot).onKill(e);
			}
		}
	}

	@EventHandler
	public void onShiftRightClick(PlayerRightShiftClickEvent e) {
		ArrayList<SlotInterface> useSlot = e.getAttackItem().getUseSlot();
		for (SlotInterface slot : useSlot) {
			if (slot instanceof ShiftRightClickSlot) {
				((ShiftRightClickSlot) slot).onPlayerRightShiftClick(e);
			}
		}
	}

	@EventHandler
	public void PlayerCombatEntityEvent(PlayerCombatEntityEvent e) {
		AttackItemStack attackItem = e.getAttackItem();
		ItemStack item = attackItem.getItem();
		if (item == null) {
			return;
		}

		CombatItemable customItem = ItemManager.getCustomItem(CombatItemable.class, item);
		if (customItem != null) {
			customItem.onCombatEntity(e);
		}
	}

}
