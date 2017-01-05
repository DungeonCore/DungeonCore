package lbn.command;

import java.util.Arrays;
import java.util.HashMap;

import lbn.util.JavaUtil;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandExecuteLockByTimeCommand implements CommandExecutor{

	static HashMap<Location, Long> executeMap = new HashMap<Location, Long>();

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		Location blockLoc = null;
		if (paramCommandSender instanceof BlockCommandSender) {
			blockLoc = ((BlockCommandSender)paramCommandSender).getBlock().getLocation();
		} else {
			paramCommandSender.sendMessage("このコマンドはコマンドブロック用です");
			return true;
		}

		double second = 0;
		if (paramArrayOfString.length >= 2) {
			second = JavaUtil.getDouble(paramArrayOfString[0], -1);
		} else {
			return false;
		}

		if (second < 0) {
			paramCommandSender.sendMessage("実行時間間隔は0秒以上にしてください");
			return true;
		}


		if (!executeMap.containsKey(blockLoc)) {
			String command = StringUtils.join(Arrays.copyOfRange(paramArrayOfString, 1, paramArrayOfString.length), " ");
			Bukkit.dispatchCommand(paramCommandSender, command);
			executeMap.put(blockLoc, System.currentTimeMillis());
			return true;
		}

		long beforeExecuteTime = executeMap.get(blockLoc);
		//もし(前実行した時間  + 指定した待ち時間) < (今の時間)　ならコマンド実行
		if (beforeExecuteTime + second * 1000 < System.currentTimeMillis()) {
			String command = StringUtils.join(Arrays.copyOfRange(paramArrayOfString, 1, paramArrayOfString.length), " ");
			Bukkit.dispatchCommand(paramCommandSender, command);
			executeMap.put(blockLoc, System.currentTimeMillis());
		}
		return true;
	}

}
