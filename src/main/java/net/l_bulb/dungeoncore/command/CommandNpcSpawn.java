package net.l_bulb.dungeoncore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import net.l_bulb.dungeoncore.npc.followNpc.FollowerNpc;
import net.l_bulb.dungeoncore.npc.followNpc.FollowerNpcManager;

public class CommandNpcSpawn implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
    Player p = (Player) paramCommandSender;
    FollowerNpc npc = FollowerNpcManager.getNpc(p);
    if (npc == null) {
      npc = FollowerNpcManager.createNpc(p);
    }
    if (npc.getNpc().getEntity().getType() == EntityType.PLAYER) {
      p.showPlayer(((Player) npc.getNpc().getEntity()));
    }
    npc.getNpc().teleport(p.getLocation(), TeleportCause.PLUGIN);
    return true;
  }
}
