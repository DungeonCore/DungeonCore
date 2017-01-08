package lbn;

import lbn.command.CommandChest;
import lbn.command.MobCommand;
import lbn.command.QuestCommand;
import lbn.command.VillagerCommand;
import lbn.common.other.DungeonList;
import lbn.common.other.HolographicDisplaysManager;
import lbn.common.other.SystemLog;
import lbn.dungeon.contents.ItemRegister;
import lbn.dungeon.contents.MobRegister;
import lbn.dungeon.contents.SpawnMobGetterRegister;
import lbn.dungeoncore.SpletSheet.ItemSheetRunnable;
import lbn.dungeoncore.SpletSheet.SoundSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.item.setItem.SetItemManager;
import lbn.mob.mobskill.MobSkillManager;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.player.playerIO.PlayerIODataManager;
import lbn.player.playerIO.PlayerLastSaveType;
import lbn.util.LbnRunnable;

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

			QuestCommand.questReload();

			SoundSheetRunnable.allReload();

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
