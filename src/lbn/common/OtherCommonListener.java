package lbn.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import lbn.command.CommandChest;
import lbn.command.CommandGiveItem;
import lbn.command.util.CommandSpecialSign;
import lbn.command.util.SimplySetSpawnPointCommand;
import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.menu.MenuSelectorManager;
import lbn.common.other.BookshelfCommandRunner;
import lbn.common.other.DungeonList;
import lbn.common.other.EndPortalOperator;
import lbn.common.other.GetItemSign;
import lbn.common.other.InHandItemClickSign;
import lbn.common.other.SoulBound;
import lbn.common.other.Stun;
import lbn.common.projectile.ProjectileInterface;
import lbn.common.projectile.ProjectileManager;
import lbn.item.ItemManager;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.strength.old.StrengthOperator;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.util.ItemStackUtil;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class OtherCommonListener implements Listener{
	@EventHandler
	public void StopDrops(PlayerDropItemEvent event) {
		SoulBound.onDrops(event);
	}

	@EventHandler
	public void onCraftSoulBound(PrepareItemCraftEvent e) {
		SoulBound.onCraftSoulBound(e);
	}

	@EventHandler
	public void onDeathPlayer(PlayerDeathEvent e) {
		for (ItemStack s : new ArrayList<>(e.getDrops())) {
			if (ItemStackUtil.isSoulBound(s)) {
				e.getDrops().remove(s);
			}
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		//取得経験値は常にゼロ
		e.setDroppedExp(0);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void StopClicking(InventoryClickEvent event) {
		//soulboundの処理
		SoulBound.onInventoryClick(event);
	}

	@EventHandler
	public void shootbow(EntityShootBowEvent e) {
		Stun.onShootbow(e);
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		Stun.onDamage(e);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		//stun処理
		Stun.onClick(event);

		//ダンジョン情報
		if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LAPIS_BLOCK) {
			DungeonList.sendDungeonInfo(event.getClickedBlock().getLocation(), event.getPlayer());
		}

		//エンダーアイの処理
		EndPortalOperator.onClick(event);
	}

	static boolean notItemFlg = false;

	@EventHandler
	public void onBlockCanBuildEvent(BlockCanBuildEvent event) {
		if (!event.isBuildable() && event.getMaterial() == Material.GRAVEL
				&& (event.getBlock().getType() == Material.REDSTONE_TORCH_ON || event.getBlock().getType() == Material.REDSTONE_TORCH_OFF)) {
			notItemFlg = true;
		} else {
			notItemFlg = false;
		}
	}

	@EventHandler
	public void onItemSpawnEvent(ItemSpawnEvent e) {
		try {
			if (e.getEntity().getItemStack().getType() == Material.GRAVEL) {
				if (notItemFlg) {
					e.getEntity().remove();
				}
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} finally {
			notItemFlg = false;
		}
	}

	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if (event.getEntity().getType() == EntityType.FALLING_BLOCK
				&& ((FallingBlock) event.getEntity()).getMaterial() == Material.GRAVEL
				&& event.getTo() == Material.AIR) {
			Block block = event.getBlock().getLocation().add(0, 1, 0).getBlock();
			if (block.getType() == Material.STONE_PLATE) {
				block.setType(Material.AIR);
			}
		}
	}


	public static int[] randomNumber = new int[100];
	static {
		Random rnd = new Random();
		for (int i = 0; i < randomNumber.length; i++) {
			randomNumber[i] = rnd.nextInt(16);
		}
	}
	static int i = 0;

	static HashMap<HumanEntity, Integer> foodlevel = new HashMap<HumanEntity, Integer>();

	@EventHandler
	public void onChangeFoodLevel(FoodLevelChangeEvent e) {
		//1/16の確率で腹減りを起こさせる
		HumanEntity entity = e.getEntity();

		Integer level = foodlevel.get(entity);
		if (level == null || level <= e.getFoodLevel()) {
			foodlevel.put(e.getEntity(), e.getFoodLevel());
			return;
		}

		if (!entity.hasPotionEffect(PotionEffectType.HUNGER)) {
			if (i >= randomNumber.length) {
				i = 0;
			}
			if (randomNumber[i] != 1) {
				e.setCancelled(true);
			}
			i++;
		}

		foodlevel.put(e.getEntity(), e.getFoodLevel());
	}

	@EventHandler
	public void onClick2(PlayerInteractEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getClickedBlock() != null) {
			if (CommandSpecialSign.isWhiteSign(e)) {
				CommandSpecialSign.signClickMap.put(e.getPlayer(), e.getClickedBlock().getLocation());
			} else if (e.getClickedBlock().getType() == Material.CHEST) {
				CommandChest.chestClickMap.put(e.getPlayer(), e.getClickedBlock().getLocation());
			}
		}

		//空白の看板に書き込む
		if (InHandItemClickSign.onWriteSign(e)) {
			return;
		}

		//鍵を持ったままクリックする看板
		InHandItemClickSign tpSign = new InHandItemClickSign(e);
		if (tpSign.isSuccess()) {
			tpSign.doClick(e);
			e.setCancelled(true);
			return;
		}

		//何も持たない状態でクリックしアイテムを取得する看板
		GetItemSign getItemSign = new GetItemSign(e);
		if (getItemSign.isSuccess()) {
			getItemSign.doClick(e);
			e.setCancelled(true);
			return;
		}

		BookshelfCommandRunner bookshelfCommandRunner = new BookshelfCommandRunner(e);
		if (bookshelfCommandRunner.canDo()) {
			bookshelfCommandRunner.doCommand();
		}
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
//		VillagerChunkManager.onLoadChank(e.getChunk());
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		MenuSelectorManager.onSelect(e);

		CommandGiveItem.onClick(e);
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		MenuSelectorManager.onSelect(e);

		CommandGiveItem.onClick(e);
	}

	@EventHandler
	public void ProjectileHitEventBySpawnSet(ProjectileHitEvent e) {
		SimplySetSpawnPointCommand.ProjectileHitEvent(e);
	}

	@EventHandler
	public void PlayerInteractEventBySpawnSet(PlayerInteractEvent e) {
		SimplySetSpawnPointCommand.onClick(e);
	}

	@EventHandler
	public void onBrakeSponge(BlockBreakEvent e) {
		MobSpawnerPointManager.onBrakeSponge(e);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onProjectileLaunchEvent(ProjectileLaunchEvent e) {
		ProjectileManager.onProjectileLaunchEvent(e);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onProjectileHit(ProjectileHitEvent e){
		ProjectileManager.onProjectileHit(e);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDamage2(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity target = e.getEntity();

		if (target.getType().isAlive()) {
			//ProjectileInterfaceを取得
			ProjectileInterface projectileInterface = ProjectileManager.getProjectileInterface(damager);
			ItemStack itemstack = ProjectileManager.getItemStack(damager);


			if (projectileInterface != null && itemstack != null) {
				LivingEntity owner = (LivingEntity)((Projectile) damager).getShooter();

				AbstractAttackItem attackItem = (AbstractAttackItem)ItemManager.getCustomItem(itemstack);

				if (owner != null && owner.getType() == EntityType.PLAYER) {
					//eventを呼ぶ
					PlayerCombatEntityEvent playerCombatEntityEvent = new PlayerCombatEntityEvent((Player)owner, (LivingEntity)target, itemstack,
							e.getDamage() + attackItem.getAttackItemDamage(StrengthOperator.getLevel(itemstack)) - attackItem.getMaterialDamage());
					playerCombatEntityEvent.callEvent();
					//eventからDamageを取得
					e.setDamage(playerCombatEntityEvent.getDamage());
				} else {
					//通常通りの計算を行う
					e.setDamage(e.getDamage() + attackItem.getAttackItemDamage(StrengthOperator.getLevel(itemstack)) - attackItem.getMaterialDamage());
				}

				projectileInterface.onProjectileDamage(e, itemstack, (LivingEntity)((Projectile)damager).getShooter(), (LivingEntity) target);
			}
		}
	}

	@EventHandler
	public void onBlockMultiPlaceEvent(BlockMultiPlaceEvent event) {
		EndPortalOperator.onBlockMultiPlaceEvent(event);
	}

}
