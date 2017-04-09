package lbn;

import lbn.command.CommandChest;
import lbn.command.MobCommand;
import lbn.command.QuestCommand;
import lbn.command.SpletSheetCommand;
import lbn.command.VillagerCommand;
import lbn.common.book.BookManager;
import lbn.common.other.CitizenBugFixPatch;
import lbn.common.other.SystemLog;
import lbn.common.place.HolographicDisplaysManager;
import lbn.common.place.dungeon.DungeonList;
import lbn.dungeon.contents.ItemRegister;
import lbn.dungeon.contents.MobRegister;
import lbn.dungeon.contents.SpawnMobGetterRegister;
import lbn.dungeoncore.Main;
import lbn.dungeoncore.SpletSheet.SoundSheetRunnable;
import lbn.item.customItem.attackitem.weaponSkill.WeaponSkillFactory;
import lbn.item.setItem.SetItemManager;
import lbn.mob.mobskill.MobSkillManager;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.npc.NpcManager;
import lbn.player.playerIO.PlayerIODataManager;
import lbn.player.playerIO.PlayerLastSaveType;
import lbn.util.DungeonLogger;
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

			SetItemManager.initServer();

			NpcManager.init();

			//スプレットシートを読み込む
			reloadSpreadSheet();

			PlayerLastSaveType.load();
			PlayerIODataManager.allLoad();

			//Citizenのバグを治す
			CitizenBugFixPatch.doPatch();

			WeaponSkillFactory.allRegist();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reloadSpreadSheet() {
		SpletSheetCommand.reloadSheet(null, "weapon");


		VillagerCommand.reloadAllVillager(Bukkit.getConsoleSender(), true);

		SpletSheetCommand.reloadSheet(null, "weaponskill");
		if (Main.isDebugging()) {
			DungeonLogger.info("デバッグモードなのでスプレットシートのデータ取得を無視します。");
			return;
		}
		SpletSheetCommand.reloadSheet(null, "armor");

		SpletSheetCommand.reloadSheet(null, "item");

		CommandChest.allReload();

		QuestCommand.questReload();

		SoundSheetRunnable.allReload();

		SpletSheetCommand.reloadSheet(null, "buff");

		SpletSheetCommand.reloadSheet(null, "particle");


		SpletSheetCommand.reloadSheet(null, "magicore");

		SpletSheetCommand.reloadSheet(null, "food");

		BookManager.reloadSpletSheet(Bukkit.getConsoleSender());

		//	SystemSqlExecutor.execute();
		new LbnRunnable() {
			@Override
			public void run2() {
				int count = getRunCount();
				switch (count) {
				case 0:
					break;
				case 3:
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

		MobSkillManager.reloadDataBySystem();

		DungeonList.load(Bukkit.getConsoleSender());
	}
}
