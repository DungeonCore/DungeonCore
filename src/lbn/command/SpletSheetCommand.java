package lbn.command;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import lbn.dungeoncore.SpletSheet.AbstractComplexSheetRunable;
import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;
import lbn.dungeoncore.SpletSheet.ChestSheetRunable;
import lbn.dungeoncore.SpletSheet.DungeonListRunnable;
import lbn.dungeoncore.SpletSheet.ItemSheetRunnable;
import lbn.dungeoncore.SpletSheet.MobSheetRunnable;
import lbn.dungeoncore.SpletSheet.QuestSheetRunnable;
import lbn.dungeoncore.SpletSheet.SheetRunnable;
import lbn.dungeoncore.SpletSheet.SoundSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpawnPointSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.dungeoncore.SpletSheet.VillagerSheetRunnable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class SpletSheetCommand implements CommandExecutor{
	static HashMap<String, Class<?>> sheetMap = new HashMap<String, Class<?>>();
	static {
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		regist(new ChestSheetRunable(sender));
		regist(new DungeonListRunnable(sender));
		regist(new ItemSheetRunnable(sender));
		regist(new MobSheetRunnable(sender));
		regist(new MobSheetRunnable(sender));
		regist(new QuestSheetRunnable(sender));
		regist(new SoundSheetRunnable(sender));
		regist(new SpawnPointSheetRunnable(sender));
		regist(new VillagerSheetRunnable(sender));
	}

	public static void regist(SheetRunnable<String[][]> sheet) {
		sheetMap.put(sheet.getSheetName().toLowerCase(), sheet.getClass());
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 0) {
			arg0.sendMessage(sheetMap.keySet().toString());
			return true;
		}

		String sheetName = arg3[0].toLowerCase();
		if (sheetName.equals("list")) {
			arg0.sendMessage(sheetMap.keySet().toString());
			return true;
		}

		Class<?> c = sheetMap.get(sheetName);
		if (c == null) {
			arg0.sendMessage(ChatColor.RED + "sheet名が不正です:" + sheetName);
			return true;
		}

		try {
			Constructor<?> constructor = c.getConstructor(CommandSender.class);
			Object newInstance = constructor.newInstance(arg0);
			if (newInstance instanceof AbstractSheetRunable) {
				AbstractSheetRunable run = (AbstractSheetRunable) newInstance;
				SpletSheetExecutor.onExecute(run);
			} else if (newInstance instanceof AbstractComplexSheetRunable) {
				AbstractComplexSheetRunable run = (AbstractComplexSheetRunable) newInstance;
				run.getData(null);
				SpletSheetExecutor.onExecute(run);
			} else {
				arg0.sendMessage(ChatColor.RED + "このシートは未対応です:" + sheetName);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			arg0.sendMessage(ChatColor.RED + "エラーが発生しました。");
			return true;
		}
	}
}