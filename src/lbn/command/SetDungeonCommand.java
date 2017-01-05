package lbn.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lbn.common.other.DungeonData;
import lbn.common.other.DungeonList;
import lbn.dungeoncore.SpletSheet.AbstractComplexSheetRunable;
import lbn.dungeoncore.SpletSheet.DungeonListRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.util.JavaUtil;
import lbn.util.LbnRunnable;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class SetDungeonCommand implements CommandExecutor, TabCompleter{

	static ArrayList<String> difficulties = new ArrayList<>();
	static{
		difficulties.add(DungeonData.DIFFICULTY_VERY_EASY);
		difficulties.add(DungeonData.DIFFICULTY_EASY);
		difficulties.add(DungeonData.DIFFICULTY_NORMAL);
		difficulties.add(DungeonData.DIFFICULTY_HARD);
		difficulties.add(DungeonData.DIFFICULTY_VERY_HARD);
		difficulties.add(DungeonData.DIFFICULTY_IMPOSSIBLE);

	}
	static boolean isLook = true;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
			DungeonList.clear();
			DungeonList.load(sender);
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("look")) {
			executeLook(sender);
			return true;
		} else if (args.length == 2 && args[0].equalsIgnoreCase("tp")) {
			executeTp(sender, args[1]);
			return true;
		}

		if(args.length < 2){
			return false;
		}
		String difficulty = args[0];
		String d_name ="";
		Location loc = ((Player) sender).getLocation();
		for(int i = 1; i <args.length; i++){
			d_name += args[i] +" ";
		}
		d_name = d_name.trim();
		DungeonData dungeonData = new DungeonData(d_name, loc.add(0, 1.5, 0), difficulty, DungeonList.getNextId());
		DungeonList.addDungeon(dungeonData);


		DungeonListRunnable dungeonListRunnable = new DungeonListRunnable(sender);

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("name", d_name);
		hashMap.put("location", AbstractComplexSheetRunable.getLocationString(loc));
		hashMap.put("difficulty", difficulty);
		hashMap.put("id", dungeonData.getId());
		dungeonListRunnable.addData(hashMap);

		SpletSheetExecutor.onExecute(dungeonListRunnable);
		//登録
		DungeonList.addDungeon(dungeonData);

		loc.getBlock().setType(Material.LAPIS_BLOCK);
		return true;
	}

	private void executeTp(CommandSender sender, String args) {
		int int1 = JavaUtil.getInt(args, -1);
		if (int1 == -1) {
			sender.sendMessage("IDが不正です：" + int1);
			return;
		}

		DungeonData dungeonByID = DungeonList.getDungeonByID(int1);
		if (dungeonByID == null) {
			sender.sendMessage("IDが不正です：" + int1);
			return;
		}

		((Player)sender).teleport(dungeonByID.getDungeonLoc());
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 1) {
			return (List<String>)StringUtil.copyPartialMatches(arg3[0], difficulties, new ArrayList<String>(difficulties.size()));
		}
		return ImmutableList.of();
	}

	private static void executeLook(CommandSender sender) {
		ArrayList<DungeonData> allList = new ArrayList<DungeonData>(DungeonList.dungeonMap.values());

		new LbnRunnable() {
			int i = 0;
			@Override
			public void run2() {
				for (;i < allList.size() || (i != 0 && i % 2000 == 0); i++ ) {
					DungeonData dungeon = allList.get(i);
					if (isLook) {
						Chunk chunk = dungeon.getDungeonLoc().getChunk();
						if (!chunk.isLoaded()) {
							chunk.load();
						}
						dungeon.getDungeonLoc().getBlock().setType(Material.LAPIS_BLOCK);
					} else {
						Chunk chunk = dungeon.getDungeonLoc().getChunk();
						if (!chunk.isLoaded()) {
							chunk.load();
						}
						dungeon.getDungeonLoc().getBlock().setType(Material.AIR);
					}
				}

				if (i >= allList.size()) {
					if (!isLook) {
						sender.sendMessage("ダンジョンにラピスブロックを削除しました。");
					} else {
						sender.sendMessage("ダンジョンにラピスブロックを設置しました。");
					}
					isLook = !isLook;
					cancel();
					return;
				}
			}
		}.runTaskTimer(1);
	}


}
