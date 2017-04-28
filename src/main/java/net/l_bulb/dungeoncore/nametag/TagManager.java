package net.l_bulb.dungeoncore.nametag;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.milkbowl.vault.chat.Chat;

public class TagManager implements Listener {
  private static Chat chatPlugin;

  @EventHandler
  public static void onJoin(PlayerJoinEvent event) {
    String prefix = getPlayerPrefix(event.getPlayer());
    setprefix(prefix);
  }

  private static String getPlayerPrefix(Player player) {
    return chatPlugin.getPlayerPrefix(player);
  }

  private static void setprefix(String prefix) {

  }
}
