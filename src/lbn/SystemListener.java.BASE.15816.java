package lbn;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;

import lbn.common.event.player.PlayerJoinDungeonGameEvent;
import lbn.common.event.player.PlayerQuitDungeonGameEvent;
import lbn.dungeoncore.Main;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.money.galion.GalionEditReason;
import lbn.money.galion.GalionManager;
import lbn.player.playerIO.PlayerIODataManager;
import lbn.player.status.StatusViewerInventory;
import lbn.util.DungeonLog;

public class SystemListener implements Listener {

	public static int loginPlayer = 0;

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void StopClicking(InventoryClickEvent event) {
		Inventory topInventory = event.getView().getTopInventory();
		if (topInventory.getTitle().equals(StatusViewerInventory.TITLE)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void ServerCommandEvent(ServerCommandEvent event) {
		String command = event.getCommand();
		if (command.contains("summon")) {
			DungeonLog.println(command);
		}
	}

	@EventHandler
	public void joinDungeon(PlayerJoinDungeonGameEvent e) {
		e.getPlayer().sendMessage(getLoginMessage(e.getPlayer()));

		Player player = e.getPlayer();
		PlayerIODataManager.load(player);

		if (!MobSpawnerPointManager.isRunSpawnManage()) {
			MobSpawnerPointManager.startSpawnManage();
		}
	}

	@EventHandler
	public void quitDungeon(PlayerQuitDungeonGameEvent e) {
		if (Main.getOnlinePlayer().size() == 0) {
			MobSpawnerPointManager.stopSpawnManage();
		}

		PlayerIODataManager.save(e.getPlayer());
		PlayerIODataManager.remove(e.getPlayer());
	}

	@EventHandler
	public void login(final PlayerJoinEvent e) {
		if (PlayerJoinDungeonGameEvent.inDungeon(e.getPlayer())) {
			PlayerJoinDungeonGameEvent event = new PlayerJoinDungeonGameEvent(
					e.getPlayer());
			Bukkit.getServer().getPluginManager().callEvent(event);
		}

		List<World> worlds = Bukkit.getWorlds();
		for (World world : worlds) {
			List<Player> players = world.getPlayers();
			loginPlayer += players.size();
		}
		loginPlayer++;
	}

	@EventHandler
	public void loadChunk(ChunkLoadEvent e) {
		MobSpawnerPointManager.loadChunk(e.getChunk());
	}

	@EventHandler
	public void unloadChunk(ChunkUnloadEvent e) {
		MobSpawnerPointManager.unloadChunk(e.getChunk());
	}

	private static String[] getLoginMessage(Player player) {
		if (player.hasPermission("main.lbnDungeonUtil.command.mod.login_msg")) {
			String[] msg = new String[] {
					ChatColor.RED + "====LOGIN====",
					ChatColor.AQUA + "以下のコマンドを使うことが出来ます。",
					"/sequence_setblock second blockid:blockdata x y z & x y z & x y z &... ",
					ChatColor.GREEN
							+ "---- 連続でブロックを設置する(/help sequence_setblock で詳細表示)",
					"/sequencecommand second command1 & command2 & command3 &...",
					ChatColor.GREEN
							+ "---- 連続でコマンドを実行する(/help sequencecommand で詳細表示)",
					"/delaycommand second command",
					ChatColor.GREEN
							+ "---- 一定時間後にコマンドを実行(/help delaycommand で詳細表示)",
					"/soundPlaye list or /soundPlaye sound_name volume pitch",
					ChatColor.GREEN + "---- 音を再生する(/help soundPlaye で詳細表示)",
					"/timer_command id scound type comand1 & command2",
					ChatColor.GREEN + "---- 感圧板を一定時間踏んだ時実行 or 踏んでいる間ずっと実行 ",
					ChatColor.GREEN + "------- (/help timer_command で詳細表示)",
					ChatColor.RED + "====LOGIN====" };
			return msg;
		} else {
			String[] msg = new String[] { ChatColor.RED + "====LOGIN====",
					ChatColor.AQUA + "以下のコマンドを使うことが出来ます。", "/statusview",
					ChatColor.RED + "====LOGIN====" };
			return msg;
		}

	}

	@EventHandler
	public void logout(PlayerQuitEvent e) {
		if (PlayerJoinDungeonGameEvent.inDungeon(e.getPlayer())) {
			PlayerQuitDungeonGameEvent event = new PlayerQuitDungeonGameEvent(
					e.getPlayer());
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
		loginPlayer--;
	}

	@EventHandler
	public void onExplosionPrimeEvent(ExplosionPrimeEvent e) {
		e.setFire(false);
	}

	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent e) {
		e.blockList().clear();
	}

	@EventHandler
	public void onEnable(PluginEnableEvent e) {
		List<World> worlds = Bukkit.getWorlds();
		for (World world : worlds) {
			List<Player> players = world.getPlayers();
			loginPlayer += players.size();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onSpawn(CreatureSpawnEvent e) {
		// Logは一旦止めておく
		// SpawnReason spawnReason = e.getSpawnReason();
		// if (!e.isCancelled()) {
		// if ((e.getEntity().getCustomName() == null ||
		// e.getEntity().getCustomName().isEmpty())) {
		// Location location = e.getLocation();
		// DungeonLog.errorln(StringUtils.join(new Object[]{
		// "spawned Creature[", (int)location.getBlockX(), ",",
		// (int)location.getBlockY(),",",
		// (int)location.getBlockZ(), "], type:", e.getEntityType(), ", reason:"
		// + spawnReason}));
		// }
		// }
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		String message = e.getMessage();
		if (message.trim().equalsIgnoreCase("/kill")) {
			int galion = (int) (GalionManager.getGalion(player) * -0.05);
			GalionManager.addGalion(player, galion, GalionEditReason.penalty);
		}
	}

	@EventHandler
	public void PlayerPortalEvent(PlayerPortalEvent e) {
		Location to = e.getTo();
		if (to.getWorld() == null || to.getWorld().getName() == null) {
			return;
		}
		if (to.getWorld().getName().contains("_the_end")
				|| to.getWorld().getName().contains("_nether")) {
			e.setCancelled(true);
		}
	}
}
