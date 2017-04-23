package lbn.dungeoncore.SpletSheet;

import java.util.HashMap;
import java.util.concurrent.Future;

import lbn.chest.AllPlayerSameContentChest;
import lbn.chest.CustomChestManager;
import lbn.chest.EachPlayerContentChest;
import lbn.chest.SpletSheetChest;
import lbn.util.JavaUtil;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestSheetRunnable extends AbstractComplexSheetRunable {

	public ChestSheetRunnable(CommandSender sender) {
		super(sender);
	}

	@Override
	public String getSheetName() {
		return "chest";
	}

	public static boolean complateRead = false;

	@Override
	public String[] getTag() {
		return new String[]{"chestlocation", "contentlocation", "refuel", "type", "min", "max", "movelocation", "movetime","random"};
	}

	public static HashMap<String, Object> createDataMap(Location chestLoc, Location contentLoc, double refuelSecond, boolean allPlayerSameflg,
			Location moveLoc, int minItemCount, int maxItemCount) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//setする
		map.put("chestlocation", getLocationString(chestLoc));
		map.put("contentlocation", getLocationString(contentLoc));
		map.put("refuel", refuelSecond);
		map.put("type", Boolean.toString(allPlayerSameflg).toUpperCase());
		map.put("min", minItemCount);
		map.put("max", maxItemCount);
		map.put("movelocation", getLocationString(moveLoc));
		map.put("movetime", "");
		map.put("random", "");
		return map;
	}

	@Override
	protected void excuteOnerow(String[] row) {
		try {
			Location chestLoc = getLocationByString(row[0]);
			Location contentLoc = getLocationByString(row[1]);

			int refuelTick = (int) (Double.parseDouble(row[2]) * 20);
			boolean allPlayerSameChestFlg = Boolean.parseBoolean(row[3]);

			int minItemCount = Integer.parseInt(row[4]);
			int maxItemCount = Integer.parseInt(row[5]);

			Location moveLoc = null;
			if (row[6] != null) {
				moveLoc = getLocationByString(row[6]);
			}

			int moveTime = 10;
			try {
				moveTime = (int)Double.parseDouble(row[7]);
			} catch (Exception e) {
				moveTime = 10;
			}

			boolean isRandom = JavaUtil.getBoolean(row[8], true);

			SpletSheetChest chest;
			if (allPlayerSameChestFlg) {
				chest = new AllPlayerSameContentChest(chestLoc, contentLoc, refuelTick, moveLoc, minItemCount, maxItemCount, moveTime, isRandom);
			} else {
				chest = new EachPlayerContentChest(chestLoc, contentLoc, refuelTick, moveLoc, minItemCount, maxItemCount, moveTime, isRandom);
			}
			CustomChestManager.registChest(chestLoc, chest);

		} catch (Exception e) {
			complateRead = false;
		}
	}

	@Override
	public void onCallbackFunction(Future<String[][]> submit) throws Exception {
		complateRead = true;
		super.onCallbackFunction(submit);
		if (sender instanceof Player) {
			sender.sendMessage("詳細の設定は下記のURLから行って下さい。URLの内容変更後は/chest reloadを実行して下さい。");
			sender.sendMessage("https://goo.gl/UJAC8Y");
		}
	}
}
