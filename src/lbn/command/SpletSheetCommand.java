package lbn.command;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import lbn.dungeoncore.SpletSheet.AbstractComplexSheetRunable;
import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;
import lbn.dungeoncore.SpletSheet.ArmorSheetRunnable;
import lbn.dungeoncore.SpletSheet.Book2SheetRunnable;
import lbn.dungeoncore.SpletSheet.BookSheetRunnable;
import lbn.dungeoncore.SpletSheet.BuffSheetRunnable;
import lbn.dungeoncore.SpletSheet.ChestSheetRunnable;
import lbn.dungeoncore.SpletSheet.DungeonListRunnable;
import lbn.dungeoncore.SpletSheet.FoodSheetRunnable;
import lbn.dungeoncore.SpletSheet.ItemSheetRunnable;
import lbn.dungeoncore.SpletSheet.MagicStoneOreSheetRunnable;
import lbn.dungeoncore.SpletSheet.MobSheetRunnable;
import lbn.dungeoncore.SpletSheet.MobSkillSheetRunnable;
import lbn.dungeoncore.SpletSheet.ParticleSheetRunnable;
import lbn.dungeoncore.SpletSheet.QuestSheetRunnable;
import lbn.dungeoncore.SpletSheet.SheetRunnable;
import lbn.dungeoncore.SpletSheet.SoundSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpawnPointSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.dungeoncore.SpletSheet.VillagerSheetRunnable;
import lbn.dungeoncore.SpletSheet.WeaponSheetRunnable;
import lbn.dungeoncore.SpletSheet.WeaponSkillSheetRunnable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class SpletSheetCommand implements CommandExecutor, TabCompleter{
	static HashMap<String, Class<?>> sheetMap = new HashMap<String, Class<?>>();
	static {
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		regist(new ChestSheetRunnable(sender));
		regist(new DungeonListRunnable(sender));
		regist(new ItemSheetRunnable(sender));
		regist(new MobSheetRunnable(sender));
		regist(new MobSkillSheetRunnable(sender));
		regist(new ParticleSheetRunnable(sender));
		regist(new QuestSheetRunnable(sender));
		regist(new SoundSheetRunnable(sender));
		regist(new SpawnPointSheetRunnable(sender));
		regist(new BuffSheetRunnable(sender));
		regist(new VillagerSheetRunnable(sender));
		regist(new WeaponSheetRunnable(sender));
		regist(new MagicStoneOreSheetRunnable(sender));
		regist(new WeaponSkillSheetRunnable(sender));
		regist(new FoodSheetRunnable(sender));
		regist(new BookSheetRunnable(sender));
		regist(new Book2SheetRunnable(sender));
		regist(new ArmorSheetRunnable(sender));
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

		return reloadSheet(arg0, sheetName);
	}

	public static boolean reloadSheet(CommandSender arg0, String sheetName) {
		if (arg0 == null) {
			arg0 = Bukkit.getConsoleSender();
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

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 1) {
			Set<String> sheetNameList = sheetMap.keySet();
			return (List<String>)StringUtil.copyPartialMatches(arg3[0], sheetNameList, new ArrayList<String>(sheetNameList.size()));
		}
		return null;
	}
}
