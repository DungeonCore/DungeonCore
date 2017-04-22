package lbn.command.util;

import lbn.dungeoncore.Main;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TmCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		if (paramArrayOfString.length == 0) {
			return false;
		}

		final Player p = (Player) paramCommandSender;
		World world = Bukkit.getWorld(paramArrayOfString[0]);
		if (p.getWorld().equals(world)) {
			p.sendMessage("同じワールド");
			return true;
		}
		if (world != null) {
			Location location = p.getLocation();
			location.setWorld(world);
			p.teleport(location);
			p.sendMessage(world + "にTPしました。");
		} else {
			p.sendMessage(world + "が存在しません。");
		}

		final GameMode gameMode = p.getGameMode();
		new BukkitRunnable() {
			@Override
			public void run() {
				p.setGameMode(gameMode);
			}
		}.runTaskLater(Main.plugin, 2);
		return true;
	}

}
