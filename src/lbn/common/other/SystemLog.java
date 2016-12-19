package lbn.common.other;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.command.CommandSender;

import lbn.dungeoncore.Main;
import lbn.util.DungeonLog;
import lbn.util.InOutputUtil;

public class SystemLog{
	private static ArrayList<String> logs = new ArrayList<String>();

	public static void addLog(String log) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String format = sdf.format(new Date());

		log = "[" + format + "]" + log;

		DungeonLog.println(log);
		logs.add(log);
	}

	public static void outPutSystemIn() {
		for (String log : logs) {
			DungeonLog.println(log);
		}
	}

	public static void outPutPlayer(CommandSender p) {
		p.sendMessage(logs.toArray(new String[0]));
		for (String log : logs) {
			DungeonLog.println(log);
		}
	}

	public static void init() {
		logs.clear();
		logs.add("=== System log ===");
	}

	final static String FILE_NAME = File.separator + "system_log_";

	public static void outPut() {
		if (logs.size() < 2) {
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
		String format = sdf.format(new Date());
		String fileName = FILE_NAME + format + ".txt";

		StringBuilder sb = new StringBuilder();
		for (String string : logs) {
			sb.append(string);
			sb.append("\n");
		}
		InOutputUtil.write(sb.toString(), fileName, Main.dataFolder + File.separator + "system_log");
	}
}
