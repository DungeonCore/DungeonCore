package main;

import main.command.CommandChest;
import main.command.MobCommand;
import main.command.VillagerCommand;
import main.common.other.DungeonList;
import main.common.other.HolographicDisplaysManager;
import main.common.other.SystemLog;
import main.dungeon.contents.ItemRegister;
import main.dungeon.contents.MobRegister;
import main.dungeon.contents.SpawnMobGetterRegister;
import main.item.setItem.SetItemManager;
import main.lbn.SpletSheet.ItemSheetRunnable;
import main.lbn.SpletSheet.SpletSheetExecutor;
import main.mob.mobskill.MobSkillManager;
import main.mobspawn.point.MobSpawnerPointManager;
import main.player.playerIO.PlayerIODataManager;
import main.player.playerIO.PlayerLastSaveType;
import main.util.LbnRunnable;

import org.bukkit.Bukkit;

public class InitManager {

	public void init() {
		try {
			HolographicDisplaysManager.setUseHolographicDisplays(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"));
			//ホログラムを全て削除
			HolographicDisplaysManager.removeAllHologram();

			ItemRegister.registItem();
			MobRegister.registMob();
			SpawnMobGetterRegister.registMobGetter();

			SystemLog.init();

			SetItemManager.startRutine();

			CommandChest.allReload();

			SetItemManager.initServer();

//			SystemSqlExecutor.execute();

			new LbnRunnable() {
				@Override
				public void run2() {
					int count = getRunCount();
					switch (count) {
					case 0:
						SpletSheetExecutor.onExecute(new ItemSheetRunnable(Bukkit.getConsoleSender()));
						break;
					case 3:
						VillagerCommand.reloadAllVillager(Bukkit.getConsoleSender(), true);
						MobCommand.reloadAllMob(null);
						break;
					case 7:
						MobSpawnerPointManager.load();
						break;
					case 12:
						MobSpawnerPointManager.startSpawnManage();
						break;
					default:
						break;
					}

					if (count > 50) {
						cancel();
					}
				}
			}.runTaskTimer(20 * 2);

			PlayerLastSaveType.load();
			PlayerIODataManager.allLoad();
			DungeonList.load(Bukkit.getConsoleSender());
			MobSkillManager.reloadDataBySystem();

//		SayTextCommand.reloadAlltext();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
