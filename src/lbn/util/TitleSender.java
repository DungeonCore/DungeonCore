package lbn.util;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class TitleSender {
	ArrayList<String[]> commandJobList = new ArrayList<String[]>();

	public void setTitle(String text, ChatColor c, boolean isBold ) {
		commandJobList.add(new String[]{null, "title", MessageFormat.format("{3}text:\"{0}\",color:\"{1}\",bold:\"{2}\"}", text, c.name().toLowerCase(), isBold, "{")});
	}

	public void setSubTitle(String text, ChatColor c, boolean isBold ) {
		commandJobList.add(new String[]{null, "subtitle", MessageFormat.format("{3}text:\"{0}\",color:\"{1}\",bold:\"{2}\"}", text, c.name().toLowerCase(), isBold, "{")});
	}

	public void setTimes(int fadeIn, int stay, int fadeOut) {
		commandJobList.add(new String[]{null, "times", Integer.toString(fadeIn), Integer.toString(stay), Integer.toString(fadeOut)});
	}

	public void setReset() {
		commandJobList.add(new String[]{null, "reset"});
	}

	public void setClear() {
		commandJobList.add(new String[]{null, "clear"});
	}

	public void execute(Player p) {
		//CommandTitleを直接実行だとバグるのでコンソールからコマンド実行に変更
//		CommandTitle commandTitle = new CommandTitle();
//		MinecraftServer server = MinecraftServer.getServer();
//		for (String[] param : commandJobList) {
//			param[0] = p.getName();
//			commandTitle.execute(server, param);
//		}

		ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
		for (String[] param : commandJobList) {
			param[0] = p.getName();
			Bukkit.dispatchCommand(consoleSender, "title " + StringUtils.join(param, " "));
		}
	}

}