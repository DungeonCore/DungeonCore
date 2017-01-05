package lbn.common.other;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import lbn.dungeoncore.SpletSheet.DungeonListRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

public class DungeonList {

	public static int maxId = -1;

	public static HashMap<String, DungeonData> dungeonMap = new HashMap<>();
	public static HashMap<Integer, DungeonData> iDMap = new HashMap<>();
	static HashMultimap<String, DungeonData> difficultyMap = HashMultimap.create();

	public static int getNextId() {
		return maxId + 1;
	}

	public static DungeonData getDungeonByName(String name){
		return dungeonMap.get(name);
	}

	public static DungeonData getDungeonByID(int id){
		return iDMap.get(id);
	}

	public static Set<DungeonData> getDungeonByDifficulty(String difficulty){
		return difficultyMap.get(difficulty);
	}

	/**
	 * 自分の位置から近い順にソートしたダンジョンのリストを指定したサイズだけ返す。
	 * もし指定したサイズよりもダンジョン数が小さければダンジョン数の分だけの長さのリストが返されます
	 * @param difficulty
	 * @param center
	 * @return
	 */
	public static DungeonData[] getSortedDungeonWithNearList(String difficulty, Location center, int size) {
		if (difficultyMap.containsKey(difficulty)) {
			return new DungeonData[0];
		}

		TreeSet<DungeonData> sortedList = new TreeSet<>(new Comparator<DungeonData>() {
			@Override
			public int compare(DungeonData o1, DungeonData o2) {
				return Double.compare(getDistance(center, o1),getDistance(center, o2));
			}

			protected double getDistance(Location center, DungeonData o1) {
				return o1.getDungeonLoc().distance(center);
			}
		});

		DungeonData[] list = new DungeonData[Math.min(size, sortedList.size())];
		int i = 0;
		for (DungeonData dungeonData : sortedList) {
			if (i >= list.length) {
				break;
			}
			list[i] = dungeonData;
		}
		return list;
	}

	public static void clear(){
		//全てのホログラムを削除
		HolographicDisplaysManager.removeAllHologram();
		dungeonMap.clear();
		difficultyMap.clear();
		iDMap.clear();
		maxId = -1;
	}

	public static void addDungeon(DungeonData data){
		//ホログラムを登録
		HolographicDisplaysManager.addDungeon(data);
		dungeonMap.put(data.getDungeonName(), data);
		difficultyMap.put(data.getDifficulty(), data);
		iDMap.put(data.getId(), data);
		maxId = Math.max(data.id, maxId);
	}

	public static void load(CommandSender sender){
		DungeonListRunnable dungeonListRunnable = new DungeonListRunnable(sender);
		dungeonListRunnable.getData(null);
		SpletSheetExecutor.onExecute(dungeonListRunnable);
	}

	public static void sendDungeonInfo(Location loc, Player p) {
		for (DungeonData dungeon : dungeonMap.values()) {
			if (dungeon.getDungeonLoc().equals(loc)) {
				dungeon.sendInfo(p);
				return;
			}
		}
	}
}
