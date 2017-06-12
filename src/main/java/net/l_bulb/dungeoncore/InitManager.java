package net.l_bulb.dungeoncore;

import org.bukkit.Bukkit;

import net.l_bulb.dungeoncore.command.CommandChest;
import net.l_bulb.dungeoncore.command.MobCommand;
import net.l_bulb.dungeoncore.command.QuestCommand;
import net.l_bulb.dungeoncore.command.SpletSheetCommand;
import net.l_bulb.dungeoncore.command.VillagerCommand;
import net.l_bulb.dungeoncore.common.book.BookManager;
import net.l_bulb.dungeoncore.common.other.CitizenBugFixPatch;
import net.l_bulb.dungeoncore.common.other.SystemLog;
import net.l_bulb.dungeoncore.common.place.HolographicDisplaysManager;
import net.l_bulb.dungeoncore.common.place.dungeon.DungeonList;
import net.l_bulb.dungeoncore.dungeon.contents.ItemRegister;
import net.l_bulb.dungeoncore.dungeon.contents.MobRegister;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SoundSheetRunnable;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillFactory;
import net.l_bulb.dungeoncore.item.setItem.SetItemManager;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillManager;
import net.l_bulb.dungeoncore.mobspawn.SpawnManager;
import net.l_bulb.dungeoncore.npc.NpcManager;
import net.l_bulb.dungeoncore.player.playerIO.PlayerIODataManager;
import net.l_bulb.dungeoncore.player.playerIO.PlayerLastSaveType;
import net.l_bulb.dungeoncore.util.DungeonLogger;
import net.l_bulb.dungeoncore.util.LbnRunnable;

public class InitManager {
  public void init() {
    try {
      HolographicDisplaysManager.setUseHolographicDisplays(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"));
      // ホログラムを全て削除
      HolographicDisplaysManager.removeAllHologram();
      ItemRegister.registItem();
      MobRegister.registMob();
      SystemLog.init();
      SetItemManager.startRutine();
      SetItemManager.initServer();
      NpcManager.init();
      // スプレットシートを読み込む
      reloadSpreadSheet();
      PlayerLastSaveType.load();
      PlayerIODataManager.allLoad();
      // Citizenのバグを治す
      CitizenBugFixPatch.doPatch();
      WeaponSkillFactory.allRegist();
      // スポーンを開始する
      SpawnManager.startSpawnEntity();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void reloadSpreadSheet() {
    SpletSheetCommand.reloadSheet(null, "buff");
    SpletSheetCommand.reloadSheet(null, "particle");
    MobCommand.reloadAllMob(null);
    MobSkillManager.reloadDataBySystem();
    SpletSheetCommand.reloadSheet(null, "item");
    SpletSheetCommand.reloadSheet(null, "food2");
    if (Main.isDebugging()) {
      DungeonLogger.info("デバッグモードなのでスプレットシートのデータ取得を無視します。");
      return;
    }
    SpletSheetCommand.reloadSheet(null, "weaponskill");
    SpletSheetCommand.reloadSheet(null, "craftrecipe");
    SpletSheetCommand.reloadSheet(null, "weapon");
    SpletSheetCommand.reloadSheet(null, "armor");
    CommandChest.allReload();
    SoundSheetRunnable.allReload();
    VillagerCommand.reloadAllVillager(Bukkit.getConsoleSender(), true);
    QuestCommand.questReload();
    SpletSheetCommand.reloadSheet(null, "magicore");
    SpletSheetCommand.reloadSheet(null, "food");
    BookManager.reloadSpletSheet(Bukkit.getConsoleSender());
    new LbnRunnable() {
      @Override
      public void run2() {
        int count = getRunCount();
        switch (count) {
          case 0:
            break;
          case 3:
            break;
          case 7:
            SpletSheetCommand.reloadSheet(null, "spawnpoint");
            break;
          case 12:
            break;
          default:
            break;
        }
        if (count > 50) {
          cancel();
        }
      }
    }.runTaskTimer(20 * 2);
    DungeonList.load(Bukkit.getConsoleSender());
  }
}