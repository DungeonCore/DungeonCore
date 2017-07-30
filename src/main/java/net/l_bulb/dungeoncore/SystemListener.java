package net.l_bulb.dungeoncore;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.event.player.PlayerJoinDungeonGameEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerQuitDungeonGameEvent;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.player.PlayerChecker;
import net.l_bulb.dungeoncore.player.playerIO.PlayerIODataManager;
import net.l_bulb.dungeoncore.player.status.StatusViewerInventory;
import net.l_bulb.dungeoncore.util.DungeonLogger;

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
      DungeonLogger.info(command);
    }
  }

  @EventHandler
  public void onChunkUnLoad(ChunkUnloadEvent e) {
    // チェストを開いている最中にChunkがUnloadされると閉まるのでチェストワールドのChunkはUnloadしない
    if (e.getWorld().getName().equals("chest")) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void joinDungeon(PlayerJoinDungeonGameEvent e) {
    Player player = e.getPlayer();
    PlayerIODataManager.load(player);
  }

  @EventHandler
  public void quitDungeon(PlayerQuitDungeonGameEvent e) {
    PlayerIODataManager.save(e.getPlayer());
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

  @EventHandler
  public void onVehicleDestroy(VehicleDestroyEvent e) {
    EntityType type = e.getVehicle().getType();
    if (type == null) { return; }

    // 乗り物が壊れなようにする
    switch (type) {
      case BOAT:
      case MINECART:
      case MINECART_COMMAND:
      case MINECART_CHEST:
      case MINECART_HOPPER:
      case MINECART_FURNACE:
      case MINECART_MOB_SPAWNER:
      case MINECART_TNT:
        // 管理者でないなら壊さないようにする
        if (!PlayerChecker.isNonNormalPlayer(e.getAttacker())) {
          e.setCancelled(true);
        }
      default:
        break;
    }
  }

  @EventHandler
  public void onEntityChangeBlockEvent(EntityChangeBlockEvent e) {
    Entity entity = e.getEntity();
    if (entity.getType() == EntityType.WITHER || entity.getType() == EntityType.ENDER_DRAGON) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent e) {
    Player player = e.getPlayer();
    if (player.getGameMode() == GameMode.CREATIVE) { return; }
    String message = e.getMessage();
    if (message.trim().equalsIgnoreCase("/kill")) {

      TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
      int galion = (int) (theLowPlayer.getGalions() * -0.05);
      if (theLowPlayer != null) {
        theLowPlayer.addGalions(galion, GalionEditReason.mob_drop);
      }
    }
  }

  @EventHandler
  public void PlayerPortalEvent(PlayerPortalEvent e) {
    Location to = e.getTo();
    if (to == null) { return; }
    if (to.getWorld() == null || to.getWorld().getName() == null) { return; }
    if (to.getWorld().getName().contains("_the_end")
        || to.getWorld().getName().contains("_nether")) {
      e.setCancelled(true);
    }
  }
}