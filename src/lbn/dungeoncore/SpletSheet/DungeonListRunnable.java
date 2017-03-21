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
	public String getSheetName() {

		return "dungeonlist";
	}

	@Override
	public String[] getTag() {

		return new String[] { "name", "startloc", "level", "id", "entranceloc"};
	}

	@Override
	protected void excuteOnerow(String[] row) {
		String name = row[0];
		Location startLoc = getLocationByString(row[1]);
		String difficulty = row[2];

		if(name == null){
			sendMessage(ChatColor.RED+"不正なNameです。" + name);
		}

		if(startLoc == null){
			sendMessage(ChatColor.RED+"不正なstartlocです。"+ name);
		}

		Location entrance = getLocationByString(row[4]);
		if(startLoc == null){
			sendMessage(ChatColor.RED+"不正なentrancelocです。"+ name);
		}

		if(difficulty == null){
			difficulty = DungeonData.DIFFICULTY_VERY_EASY;
		}
		difficulty = difficulty.toLowerCase();

		int id = JavaUtil.getInt(row[3], -1);

		if (id != -1) {
			DungeonData dungeonData = new DungeonData(name, startLoc, difficulty, id, entrance);
			DungeonList.addDungeon(dungeonData);
		} else {
			sender.sendMessage("IDが不正です。" + row[3]);
		}

	}

}
