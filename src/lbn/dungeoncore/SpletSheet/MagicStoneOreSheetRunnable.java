package lbn.dungeoncore.SpletSheet;

import java.util.concurrent.Future;

import org.bukkit.command.CommandSender;

import lbn.player.magicstoneOre.MagicStoneFactor;
import lbn.player.magicstoneOre.MagicStoneOreScheduler;
import lbn.player.magicstoneOre.MagicStoneOreType;

public class MagicStoneOreSheetRunnable extends AbstractComplexSheetRunable{
	
	public static boolean isComplete = false;
	
	public MagicStoneOreSheetRunnable(CommandSender sender) {
		super(sender);
	
	
	}

	@Override
	public String getSheetName() {
		return "magicore";
	}

	@Override
	public String[] getTag() {
		String[] s={"type","location"};
		return s;
	}

	@Override
	protected void excuteOnerow(String[] row) {
		
		String oreType = row[0];
		String location = row[1];
		
		System.out.print(row[0]);
		System.out.println(row[1]);
		
		if(oreType == null){
			sendMessage("鉱石タイプが無効です。");
			return;
		}
		if(location == null){
			sendMessage("座標が無効です。");
			return;
		}
		
		MagicStoneFactor.magicStoneOres.put(getLocationByString(location), MagicStoneOreType.FromJpName(oreType));
		
	}
	
	@Override
	public void onCallbackFunction(Future<String[][]> submit) throws Exception {
		isComplete = true;
		super.onCallbackFunction(submit);
		MagicStoneOreScheduler.resetMagicOres(isComplete);
	}
	
}
