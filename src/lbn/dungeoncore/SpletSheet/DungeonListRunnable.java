package lbn.dungeoncore.SpletSheet;

import lbn.common.other.DungeonData;
import lbn.common.other.DungeonList;
import lbn.util.JavaUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;


public class DungeonListRunnable extends AbstractComplexSheetRunable {

	public DungeonListRunnable(CommandSender sender) {
		super(sender);

	}

	@Override
	String getSheetName() {

		return "dungeonlist";
	}

	@Override
	public String[] getTag() {

		return new String[] { "name", "location", "difficulty", "id" };
	}

	@Override
	protected void excuteOnerow(String[] row) {
		String name = row[0];
		Location loc = getLocationByString(row[1]);
		String difficulty = row[2];

		if(name == null){
			sender.sendMessage(ChatColor.RED+"不正なNameです。" + name);
		}

		if(loc == null){
			sender.sendMessage(ChatColor.RED+"不正なLocationです。"+ name);
		}
		if(difficulty == null){
			difficulty = DungeonData.DIFFICULTY_VERY_EASY;
		}
		difficulty = difficulty.toLowerCase();

		int id = JavaUtil.getInt(row[3], -1);

		if (id != -1) {
			DungeonData dungeonData = new DungeonData(name, loc, difficulty, id);
			DungeonList.addDungeon(dungeonData);
		} else {
			sender.sendMessage("IDが不正です。" + row[3]);
		}

	}

}
