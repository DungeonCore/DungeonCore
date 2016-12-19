package main.command.util_command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class SoundPlayCommand implements CommandExecutor, TabCompleter, UsageCommandable{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] arg3) {
		if (arg3.length == 1 && arg3[0].equalsIgnoreCase("list")) {
			sender.sendMessage(Arrays.toString(Sound.values()));
			return true;
		}

		if (arg3 == null || arg3.length < 1) {
			return false;
		}

		Float size = 1f;
		Float pitch = 1f;

		try {
			if (arg3.length >= 2) {
				size = Float.parseFloat(arg3[1]);
			}
			if (arg3.length >= 3) {
				pitch = Float.parseFloat(arg3[2]);
			}
		} catch (NumberFormatException e) {
			sender.sendMessage(arg3[1] + "または" + arg3[2] + "は数値ではありません。");
			return false;
		}

		try {
			if ((sender instanceof BlockCommandSender)) {
				World world = ((BlockCommandSender) sender).getBlock().getWorld();
				world.playSound(((BlockCommandSender) sender).getBlock().getLocation(), Sound.valueOf(arg3[0].toUpperCase()), size, pitch);
				return true;
			} else if (sender instanceof Player) {
				((Player) sender).getWorld().playSound(((Player) sender).getLocation(), Sound.valueOf(arg3[0].toUpperCase()), size, pitch);
				return true;
			}
		} catch (Exception e) {
			sender.sendMessage(arg3[0] + "は存在しない可能性があります。/soundPlay listを実行し確認してください。");
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 1) {
			HashSet<String> itemNameList = getNameList();
			return (List<String>)StringUtil.copyPartialMatches(arg3[0], itemNameList, new ArrayList<String>(itemNameList.size()));
		}
		return ImmutableList.of();
	}

	static HashSet<String> soundNameList = null;

	private HashSet<String> getNameList() {
		if (soundNameList == null) {
			soundNameList = new HashSet<String>();

			for (Sound sound : Sound.values()) {
				soundNameList.add(sound.name());
			}
		}

		return soundNameList;
	}

	@Override
	public String getUsage() {
		return "/<command> list or /<command> sound_name volume pitch"
				+ "\n volumeとは音の大きさで0から1までの値を指定できます。初期値は1です。1以上だと1になります。"
				+ "\n pitchとは音の速さで1より小さいと早くなり、1より大きいとゆっくりになります。初期値は1です。"
				+ ChatColor.GREEN + "\n '/<command> list"
				+ ChatColor.BLACK + "\n ---- sound_name一覧を取得します"
				+ ChatColor.GREEN + "\n '/<command> DOOR_OPEN 0.5 2"
				+ ChatColor.BLACK + "\n ---- DOOR_OPENを大きさ0.5、速さ2で再生します。";
	}
	@Override
	public String getDescription() {
		return "音を実行するためのコマンド";
	}


}
