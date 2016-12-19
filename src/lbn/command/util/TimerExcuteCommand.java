package lbn.command.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;

import lbn.dungeoncore.Main;
import lbn.util.BlockUtil;

public class TimerExcuteCommand implements CommandExecutor, UsageCommandable, TabExecutor{
	static HashMultimap<String, Location> create = HashMultimap.create();
	static HashMap<String, CommandInfo> waitTimeMap = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		if (paramArrayOfString.length < 4) {
			return false;
		}

		final String id = paramArrayOfString[0].trim().toLowerCase();

		double waitSecound;
		try {
			waitSecound = Double.parseDouble(paramArrayOfString[1]);
		} catch (NumberFormatException e) {
			paramCommandSender.sendMessage(ChatColor.RED + paramArrayOfString[2] + "を数値に変換出来ません。");
			return false;
		}

		String type = paramArrayOfString[2];
		if (!type.equalsIgnoreCase("pressing") && !type.equalsIgnoreCase("passed")) {
			paramCommandSender.sendMessage(ChatColor.RED + "typeはpressingとpassedしか認められていません。");
		}

		String commandAll = StringUtils.join(Arrays.copyOfRange(paramArrayOfString, 3, paramArrayOfString.length), " ").trim();
		String successCommand;
		String failureCommand = "";
		String[] split = commandAll.split("&");
		successCommand = split[0];
		if (split.length >= 2) {
			failureCommand = split[1];
		}

		if (!(paramCommandSender instanceof BlockCommandSender)) {
			paramCommandSender.sendMessage(StringUtils.join(new Object[]{"type:", type, ", id:", id, ", time:" , waitSecound, ", command1:", successCommand, ", command2:", failureCommand }));
			return true;
		}
		Block block = ((BlockCommandSender)paramCommandSender).getBlock();

		//2つ上がプレートであることを確認
		Location plateLoc = block.getLocation().add(0, 2, 0);
		final Block plate = plateLoc.getBlock();
		if (!BlockUtil.isPressable(plate)) {
			paramCommandSender.sendMessage("コマンドブロックの2つ上は感圧板である必要があります。");
			return false;
		}

		//すでに登録されている場合は何もしない
		if (waitTimeMap.containsKey(id) && create.containsEntry(id, plateLoc)) {
			return true;
		}

		//IDだけ登録されている場合は何もしない
		if (waitTimeMap.containsKey(id) && !create.containsEntry(id, plateLoc)) {
			create.put(id, plateLoc);
			return true;
		}

		CommandInfo commandInfo = new CommandInfo(id,(long)(waitSecound * 20), failureCommand, successCommand, (BlockCommandSender)paramCommandSender);
		//IDも登録されていない新規の場合
		waitTimeMap.put(id, commandInfo);
		create.put(id, plateLoc);

		if (type.equalsIgnoreCase("passed")) {
			new PassedExcuteRunnable(id).runTaskTimer(Main.plugin, 0, 1);
		} else if (type.equalsIgnoreCase("pressing")) {
			new PressingExcuteRunnable(id).runTaskTimer(Main.plugin, 0, 1);
		} else {
			paramCommandSender.sendMessage("予期せぬエラー。Typeが不正です。");
		}

		return true;
	}

	@Override
	public String getUsage() {
		return "/<command> id secound type command1 & command2     (& command2は省略可)"
				+ "\n idは他の人と被らないID。typeは'passed'か'presing'を選択 "
				+ ChatColor.GREEN + "\n n秒間感圧板を押していたらコマンドを実行する場合"
				+ ChatColor.DARK_GREEN + "\n <command> id n passed command1 & command2"
				+ ChatColor.GRAY + "\n n秒間感圧板を押していたらcommand1が実行され、途中で離したらcommand2が実行"
				+ ChatColor.GREEN + "\n 感圧板を押している間、n秒間に一回コマンドを実行し続ける場合"
				+ ChatColor.DARK_GREEN + "\n <command> id n pressing command1 & command2"
				+ ChatColor.GRAY + "\n 感圧板を押していたらn秒に一回command1が実行され、離したらcommand2を実行";
	}

	@Override
	public String getDescription() {
		return "一定時間感圧板を押していたらコマンドを実行 or 感圧板を押している間だけ実行";
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 3) {
			return (List<String>)StringUtil.copyPartialMatches(arg3[2], Arrays.asList("pressing", "passed"), new ArrayList<String>(2));
		}
		return ImmutableList.of();
	}
}

class CommandInfo {
	String id;
	long paramTick;
	String secoundCommand;
	String firstCommand;
	BlockCommandSender sender;

	public CommandInfo(String id, long paramTick, String failureCommand, String successCommand, BlockCommandSender sender) {
		this.id = id;
		this.paramTick = paramTick;
		this.secoundCommand = failureCommand;
		this.firstCommand = successCommand;
		this.sender = sender;
	}

	public World getworld() {
		return sender.getBlock().getWorld();
	}

}

class PassedExcuteRunnable extends BukkitRunnable {
	String id;

	long excuteTime = -1;
	public PassedExcuteRunnable(String id) {
		this.id = id;
		CommandInfo commandInfo2 = TimerExcuteCommand.waitTimeMap.get(id);
		if (commandInfo2 != null) {
			excuteTime = commandInfo2.paramTick + commandInfo2.getworld().getFullTime();
		}
	}

	@Override
	public void run() {
		CommandInfo commandInfo2 = TimerExcuteCommand.waitTimeMap.get(id);
		if (commandInfo2 == null || excuteTime == -1) {
			TimerExcuteCommand.create.removeAll(id);
			cancel();
			return;
		}

		//時間が指定時間になった時コマンドを実行する
		if (commandInfo2.getworld().getFullTime() >= excuteTime) {
			excuteCommand(commandInfo2.sender, commandInfo2.firstCommand);
			TimerExcuteCommand.waitTimeMap.remove(id);
			TimerExcuteCommand.create.removeAll(id);
			return;
		}

		//一つでもプレスされていればそのまま監視を続ける
		if (pushed()) {
			//何もしない
		} else {
		//一つもプレスされていなければ監視を修了する
			TimerExcuteCommand.waitTimeMap.remove(id);
			TimerExcuteCommand.create.removeAll(id);
			cancel();
			excuteCommand(commandInfo2.sender, commandInfo2.secoundCommand);
		}


	}

	protected boolean pushed() {
		Set<Location> set = TimerExcuteCommand.create.get(id);
		for (Location location : set) {
			Block plate = location.getBlock();
			//感圧板以外のものに変わっている場合は無視する
			if (!BlockUtil.isPressable(plate)) {
				continue;
			}
			//一つでもプレスされていたらTRUE
			if (BlockUtil.isPressed(plate)) {
				return true;
			}
		}
		//一つもプレスされていなければFALSE
		return false;
	}

	protected void excuteCommand(CommandSender sender, String command) {
		if (command == null || command.isEmpty()) {
			return;
		}
		Bukkit.dispatchCommand(sender, command.trim());
	}
}

class PressingExcuteRunnable extends PassedExcuteRunnable {
	long remainder = 0;

	public PressingExcuteRunnable(String id) {
		super(id);
		CommandInfo commandInfo2 = TimerExcuteCommand.waitTimeMap.get(id);
		if (commandInfo2 != null) {
			remainder = commandInfo2.getworld().getFullTime() % commandInfo2.paramTick;
		}
	}

	@Override
	public void run() {
		CommandInfo commandInfo2 = TimerExcuteCommand.waitTimeMap.get(id);
		if (commandInfo2 == null) {
			TimerExcuteCommand.create.removeAll(id);
			cancel();
			return;
		}

		//時間が指定時間になった時コマンドを実行する
		if (commandInfo2.getworld().getFullTime() % commandInfo2.paramTick == remainder) {
			excuteCommand(commandInfo2.sender, commandInfo2.firstCommand);
		}

		//一つでもプレスされていればそのまま監視を続ける
		if (pushed()) {
			//何もしない
		} else {
			excuteCommand(commandInfo2.sender, commandInfo2.secoundCommand);
			TimerExcuteCommand.waitTimeMap.remove(id);
			TimerExcuteCommand.create.removeAll(id);
			cancel();
		}

	}

}
