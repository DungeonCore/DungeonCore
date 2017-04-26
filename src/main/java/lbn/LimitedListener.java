package lbn;

import java.util.Collection;
import java.util.List;

import lbn.dungeoncore.Main;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.scheduler.BukkitRunnable;

public class LimitedListener implements Listener{
	static String targetWorldName = "world";

	public static boolean isTarget(World w) {
		return true;
	}

	public boolean isTarget(PlayerEvent e) {
		return isTarget(e.getPlayer().getWorld());
	}

	public boolean isTarget(EntityEvent e) {
		return isTarget(e.getEntity().getWorld());
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (!isTarget(event)) {
			return;
		}
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return ;
		}

		//クリックをしていけないブロックの処理
		Block clickedBlock = event.getClickedBlock();
		if (clickedBlock != null) {
			Material material = clickedBlock.getType();
			if (Config.getClickCancelblocks().contains(material)) {
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onTouch(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		if (damager.getType() != EntityType.PLAYER) {
			return;
		}
		Player p = (Player) damager;

		//クリエイティブでないならアーマースタンドをけさない
		if (e.getEntity().getType() == EntityType.ARMOR_STAND && p.getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void BlockDamageEvent(BlockDamageEvent e) {
		if (!Config.getDamageAllowBlock().contains(e.getBlock().getType())) {
			e.setCancelled(true);
		}
	}

//
//	@EventHandler
//	public void onCommand(PlayerCommandPreprocessEvent e) {
//		Player p = e.getPlayer();
//		String command = e.getMessage();
//		String[] split = command.split(" ");
//		if (command.trim().toLowerCase().startsWith("/clear")) {
//			if (split.length >=2 && !p.getDisplayName().equalsIgnoreCase(split[1])) {
//				e.setMessage(split[0]);
//			}
//		} else if (command.trim().toLowerCase().startsWith("/gamemode")) {
//			if (split.length >=3 && !p.getDisplayName().equalsIgnoreCase(split[2])) {
//				e.setMessage(split[0] + " " + split[1]);
//			}
//		} else if (command.trim().toLowerCase().startsWith("/tp")) {
//			if (split.length == 3 && !p.getDisplayName().equalsIgnoreCase(split[1])) {
//				e.setMessage("/");
//				p.sendMessage("他の人をTPすることは出来ません。");
//			}
//		}
//	}

	@EventHandler
	public void LeavesDecayEvent(LeavesDecayEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void WeatherChange(WeatherChangeEvent event){
		if (!isTarget(event.getWorld())) {
			return;
		}
		boolean weatherState = event.toWeatherState();
		if (weatherState) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onExplodeEvent(EntityExplodeEvent event) {
		if (!isTarget(event)) {
			return;
		}
		if (event.getEntity() == null) {
			notBreakBlock(event);
			return;
		}
		EntityType type = event.getEntityType();
		if (type == null) {
			notBreakBlock(event);
		} else if (type == EntityType.PRIMED_TNT) {
			notBreakBlock(event);
		} else if (type == EntityType.MINECART_TNT) {
			notBreakBlock(event);
		} else if (type == EntityType.UNKNOWN) {
			notBreakBlock(event);
		}
	}

	private void notBreakBlock(EntityExplodeEvent event) {
		if (event.blockList() != null) {
			event.blockList().clear();
		}
	}

	@EventHandler
	public void CreatureSpawnEvent(CreatureSpawnEvent e) {
		if (!isTarget(e.getEntity().getWorld())) {
			return;
		}
		if (e.getSpawnReason() == SpawnReason.CUSTOM ||
				e.getSpawnReason() == SpawnReason.DEFAULT) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void click2(PlayerInteractEvent e) {
		if (!isTarget(e)) {
			return;
		}
		ItemStack itemInHand = e.getPlayer().getItemInHand();
		if (itemInHand == null) {
			return;
		}

		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return ;
		}

		if (Config.getClickCancelItems().contains(itemInHand.getType())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void clickEntity(PlayerInteractEntityEvent e) {
		if (!isTarget(e)) {
			return;
		}
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}

		Entity rightClicked = e.getRightClicked();
		if (rightClicked == null) {
			return;
		}

		EntityType type = rightClicked.getType();
		if (Config.getClickCancelEntityTypes().contains(type)) {
			e.setCancelled(true);
		}
	}

	/**
	 * 砂と砂利以外はブロックを設置しない
	 * @param e
	 */
	@EventHandler
	public void EntityChangeBlockEvent(EntityChangeBlockEvent e) {
		if (!isTarget(e)) {
			return;
		}
		if (e.getEntityType() != EntityType.FALLING_BLOCK) {
			return;
		}
		if (e.getTo().equals(Material.SAND) || e.getTo().equals(Material.GRAVEL) || e.getTo().equals(Material.AIR)) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void damageEntity(EntityDamageByEntityEvent e) {
		if (!isTarget(e)) {
			return;
		}
		if (e.getDamager().getType() == EntityType.PLAYER && ((Player)e.getDamager()).getGameMode() == GameMode.CREATIVE) {
			return;
		}

		EntityType type = e.getEntityType();
		if (type == null) {
			return;
		}

		if (Config.getDamageCancelEntityTypes().contains(type)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void HangingBreakByEntityEvent(HangingBreakByEntityEvent e) {
		if (!isTarget(e.getEntity().getWorld())) {
			return;
		}
		Entity remover = e.getRemover();
		if (remover.getType() == EntityType.PLAYER && ((Player)remover).getGameMode() == GameMode.CREATIVE) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void HangingEntityEvent(HangingBreakEvent e) {
		if (!isTarget(e.getEntity().getWorld())) {
			return;
		}

		RemoveCause cause = e.getCause();
		if (cause == RemoveCause.ENTITY || cause == RemoveCause.PHYSICS) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void EntityDamageByBlockEvent(EntityDamageByBlockEvent e) {
		if (!isTarget(e.getEntity().getWorld())) {
			return;
		}

		if (e.getEntityType() == EntityType.ITEM_FRAME) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void BlockFadeEvent(BlockFadeEvent e) {
		if (!isTarget(e.getBlock().getWorld())) {
			return;
		}
		Block block = e.getBlock();
		if (block.getType() == Material.ICE) {
			e.setCancelled(true);
		}
	}

	/**
	 * クリエ以外は特定のものを除き、クラフトできない
	 * @param e
	 */
	@EventHandler
	public void PrepareItemCraftEvent(PrepareItemCraftEvent e) {
		Player player = (Player) e.getView().getPlayer();
		if (!isTarget(player.getWorld())) {
			return;
		}
		if (player.getGameMode() == GameMode.CREATIVE) {
			return;
		}

		boolean allowCraft = false;
		//クラフトできるか確認
		Recipe recipe = e.getRecipe();
		if (recipe instanceof ShapedRecipe) {
			Collection<ItemStack> values = ((ShapedRecipe) recipe).getIngredientMap().values();
			allowCraft = Config.allowCraft(recipe.getResult(), values);
		} else if (recipe instanceof ShapelessRecipe) {
			List<ItemStack> ingredientList = ((ShapelessRecipe) recipe).getIngredientList();
			allowCraft = Config.allowCraft(recipe.getResult(), ingredientList);
		} else {
			return;
		}

		//クラフトできなければAIRをセットする
		if (!allowCraft) {
			new BukkitRunnable() {
				@Override
				public void run() {
					e.getInventory().setResult(new ItemStack(Material.AIR));
					player.updateInventory();
				}
			}.runTaskLater(Main.plugin, 1);
		}
	}

	@EventHandler
	public void EntityCreatePortalEvent(EntityCreatePortalEvent e) {
		if (!isTarget(e.getEntity().getWorld())) {
			return;
		}

		e.setCancelled(true);
	}
	@EventHandler
	public void PlayerBucketEmptyEvent(PlayerBucketEmptyEvent e) {
		if (!isTarget(e)) {
			return;
		}
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}

		e.setCancelled(true);
	}
	@EventHandler
	public void PlayerBucketFillEvent(PlayerBucketFillEvent e) {
		if (!isTarget(e)) {
			return;
		}
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void PlayerShearEntityEvent(PlayerShearEntityEvent e) {
		if (!isTarget(e)) {
			return;
		}
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void PlayerBedEnterEvent(PlayerBedEnterEvent e) {
		if (!isTarget(e)) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onBreakBlock(BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (player.getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onSetBlock(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		if (player.getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}


	@EventHandler
	public void soilChangePlayer(PlayerInteractEvent event) {
		if ((event.getAction() == Action.PHYSICAL)
				&& (event.getClickedBlock().getType() == Material.SOIL)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void soilChangeEntity(EntityInteractEvent event) {
		if ((event.getEntityType() != EntityType.PLAYER)
				&& (event.getBlock().getType() == Material.SOIL)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void BlockIgniteEvent(BlockIgniteEvent e) {
		Entity ignitingEntity = e.getIgnitingEntity();
		if (ignitingEntity != null && ignitingEntity.getType() == EntityType.SMALL_FIREBALL) {
			e.setCancelled(true);
		}
	}
}
