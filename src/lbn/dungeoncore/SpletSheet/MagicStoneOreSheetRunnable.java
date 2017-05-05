package lbn.dungeoncore.SpletSheet;

import lbn.player.magicstoneOre.MagicStoneFactor;
import lbn.player.magicstoneOre.MagicStoneOreType;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class MagicStoneOreSheetRunnable extends AbstractComplexSheetRunable{

	public MagicStoneOreSheetRunnable(CommandSender sender) {
		super(sender);
	}

	@Override
	public String getSheetName() {
		return "magicore";
	}

	@Override
	public String[] getTag() {
		String[] s = {"type","location"};
		return s;
	}

	@Override
	protected void excuteOnerow(String[] row) {
		MagicStoneOreType type = MagicStoneOreType.FromJpName(row[0]);
		Location loc = getLocationByString(row[1]);

		if(type == null){
			sendMessage("鉱石タイプが無効です。:" + row[0]);
			return;
		}
		if(loc == null){
			sendMessage("座標が無効です。:" + row[1]);
			return;
		}
		MagicStoneFactor.regist(loc, type);
		//ブロックを再配置する
		MagicStoneFactor.relocationOre(loc);
	}
}
