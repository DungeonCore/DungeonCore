package main.lbn.SpletSheet;

import java.util.concurrent.Future;

import main.VillagerChunkManager;
import main.mob.mob.abstractmob.villager.VillagerData;

import org.bukkit.command.CommandSender;

public class VillagerSheetRunnable extends AbstractComplexSheetRunable{

	public VillagerSheetRunnable(CommandSender p) {
		super(p);
	}

	@Override
	public String getSheetName() {
		return "villager";
	}

	@Override
	public String[] getTag() {
		return new String[]{"name", "type", "quest", "text", "location", "adult", "data"};
	}

	boolean init = false;
	public void setChunkLoadInit(boolean init) {
		this.init = init;
	}

	@Override
	protected void excuteOnerow(String[] row) {
		VillagerData.registSpletsheetVillager(sender, row[0], row[1], row[2], row[3], row[4], row[5], row[6]);
	}

	@Override
	public void onCallbackFunction(Future<String[][]> submit) throws Exception {
		super.onCallbackFunction(submit);
		if (init) {
			VillagerChunkManager.setAllRegistedVillager();
			VillagerChunkManager.onPluginLoad();
		}
	}
}

