package lbn.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lbn.common.place.dungeon.DungeonData;
import lbn.common.place.dungeon.DungeonList;
import lbn.dungeoncore.SpletSheet.AbstractComplexSheetRunable;
import lbn.dungeoncore.SpletSheet.DungeonListRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.util.JavaUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class SetDungeonCommand implements CommandExecutor, TabCompleter{

	static boolean isLook = false;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
			DungeonList.clear();
			DungeonList.load(sender);
			return true;
		} else if (args.length >= 2 && args[0].equalsIgnoreCase("tp")) {
			Player p = Bukkit.getPlayerExact(args[args.length - 1]);
			if (p == null) {
				p = (Player)sender;
			}
			executeTp(p, getName(Bukkit.getPlayerExact(args[args.length - 1]) != null, args));
			return true;
		} else if (args[0].equalsIgnoreCase("set")) {

			Player p = (Player) sender;

			//ダンジョン名
			String dungeonName = getName(false, args);

			//ダンジョンデータを作成
			DungeonData dungeon = new DungeonData(DungeonList.getNextId(), dungeonName);
			DungeonList.addDungeon(dungeon);

			//スプレットシートにデータを送信する
			DungeonListRunnable dungeonListRunnable = new DungeonListRunnable(sender);

			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("name", dungeonName);
			hashMap.put("tploc", AbstractComplexSheetRunable.getLocationString(p.getLocation()));
			hashMap.put("startloc", AbstractComplexSheetRunable.getLocationString(p.getLocation()));
			hashMap.put("id", dungeon.getId());
			dungeonListRunnable.addData(hashMap);

			SpletSheetExecutor.onExecute(dungeonListRunnable);
		} else {
			sender.sendMessage("【/setDungeon set ダンジョン名】 でダンジョンを登録してください" );
		}
		return true;
	}

	private void executeTp(CommandSender sender, String args) {
		int int1 = JavaUtil.getInt(args, -1);
		if (int1 != -1) {
			DungeonData dungeonByID = DungeonList.getDungeonById(int1);
			if (dungeonByID != null) {
				((Player)sender).teleport(dungeonByID.getTeleportLocation());
				return;
			}
		}

		DungeonData dungeon = DungeonList.getDungeonByName(args);
		if (dungeon != null) {
			((Player)sender).teleport(dungeon.getTeleportLocation());
		} else {
			sender.sendMessage("ダンジョンが存在しません。");
		}
	}

	final String[] opeList = {"tp", "reload", "set"};

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 1) {
			return (List<String>) StringUtil.copyPartialMatches(arg3[0], Arrays.asList(opeList), new ArrayList<String>(opeList.length));
		} else if (arg3.length >= 2 && "tp".equalsIgnoreCase(arg3[0])) {
			return (List<String>) StringUtil.copyPartialMatches(getName(false, arg3), DungeonList.names(), new ArrayList<String>());
		}
		return ImmutableList.of();
	}

	/**
	 * ダンジョン名を取得する
	 * @param args
	 * @return
	 */
	public String getName(boolean withOutLast, String[] args) {
		StringBuilder sb = new StringBuilder();
		if (args.length >= 2) {
			for (int i = 1; i < (withOutLast ? args.length - 1 : args.length); i++) {
				sb.append(args[i]);
			}
		}
		return sb.toString();
	}
}
