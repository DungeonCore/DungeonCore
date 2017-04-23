package lbn.command.util;

import java.util.Arrays;

import lbn.dungeoncore.Main;
import lbn.util.MinecraftUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class DelayCommand implements CommandExecutor, UsageCommandable{
	static TaskManager taskManager = new TaskManager();
	@Override
	public boolean onCommand(final CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 1 && arg3[0].equals("list")) {
			taskManager.showList(arg0);
			return true;
		}

		if (arg3.length == 0) {
			arg0.sendMessage("need delay sec!!");
			return false;
		}

		final String command = StringUtils.join(Arrays.copyOfRange(arg3, 1, arg3.length), " ");

		if (!NumberUtils.isNumber(arg3[0])) {
			arg0.sendMessage(arg3[0] + " is not number!!");
			return false;
		}

		//実行者の座標を取得
		Location blockLoc = MinecraftUtil.getSenderLocation(arg0);

		if (blockLoc == null) {
			return false;
		}

		BukkitRunnable bukkitRunnable = new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(arg0, command);
				taskManager.remove(blockLoc);
			}
		};
		bukkitRunnable.runTaskLater(Main.plugin, (long) (Double.parseDouble(arg3[0]) * 20));

		taskManager.regist(arg0, (long) (Double.parseDouble(arg3[0]) * 20));

		return true;
	}

	@Override
	public String getUsage() {
		return "/delaycommand second command "
				+ "\n command を second秒後に実行します。 "
				+ ChatColor.GREEN + "\n '/delaycommand 10 setblock 100 65 100 2' "
				+ ChatColor.GRAY + "\n ---- 10秒後に'/setblock 100 65 100 2'を実行します。"
				+ ChatColor.GREEN + "\n  '/delaycommand 0.5 say こんにちは' "
				+ ChatColor.GRAY + "\n ---- 0.5秒後に'/say こんにちわ'を実行します。";
	}

	@Override
	public String getDescription() {
		return "指定したコマンドを指定時間後に実行します。";
	}

}
