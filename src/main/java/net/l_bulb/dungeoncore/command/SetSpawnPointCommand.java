package net.l_bulb.dungeoncore.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.l_bulb.dungeoncore.SystemListener;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpawnPointSheetRunnable;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpletSheetExecutor;
import net.l_bulb.dungeoncore.mobspawn.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointMonitor;
import net.l_bulb.dungeoncore.mobspawn.gettter.SpawnMobGetterInterface;
import net.l_bulb.dungeoncore.mobspawn.gettter.SpawnMobGetterManager;
import net.l_bulb.dungeoncore.mobspawn.point.MobSpawnerPoint;
import net.l_bulb.dungeoncore.mobspawn.point.MobSpawnerPointManager;
import net.l_bulb.dungeoncore.mobspawn.point.SpawnScheduler;
import net.l_bulb.dungeoncore.mobspawn.point.SpletSheetMobSpawnerPoint;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class SetSpawnPointCommand implements CommandExecutor, TabCompleter {

  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    if (arg3.length == 0) { return false; }

    if (!(arg0 instanceof Player)) {
      arg0.sendMessage(ChatColor.RED + "Playerが実行してください。");
    }

    Location loc = null;
    try {
      loc = ((Player) arg0).getLocation();
    } catch (Exception e) {}

    String operator = arg3[0];
    if ("set".equalsIgnoreCase(operator)) {
      return setOperate(arg0, arg3, loc);
    } else if ("list".equalsIgnoreCase(operator)) {
      return listOperate(arg0, arg3, loc);
    } else if ("remove".equalsIgnoreCase(operator)) {
      return removeOperate(arg0, arg3);
    } else if ("reload".equalsIgnoreCase(operator)) {
      return reloadOperate(arg0, arg3);
    } else if ("monitor".equalsIgnoreCase(operator)) { return monitorOperate(arg0, arg3); }

    return false;
  }

  private boolean monitorOperate(CommandSender arg0, String[] arg3) {
    if (arg3.length < 1) {
      arg0.sendMessage("id必要");
      return true;
    }

    String id = arg3[1];
    try {
      MobSpawnerPoint point = MobSpawnerPointManager.getSpawnerPointbySerialNumber(Integer.parseInt(id));
      if (point == null) {
        arg0.sendMessage("そのIDのスポーンポイントは存在しません");
        return true;
      }
      SpawnScheduler spawnScheduler = MobSpawnerPointManager.getSchedulerList().get(point.getLevel());
      SpawnPointMonitor monitor = spawnScheduler.getMonitor(point);
      monitor.send(arg0);
    } catch (Exception e) {
      arg0.sendMessage("error発生");
    }
    return true;
  }

  private boolean reloadOperate(CommandSender arg0, String[] arg3) {
    if (arg3.length == 1) {
      MobSpawnerPointManager.clear();

      // 全てを更新する
      SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(arg0);
      spawnPointSheetRunnable.getData(null);
      SpletSheetExecutor.onExecute(spawnPointSheetRunnable);
    } else {
      SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(arg0);

      String[] split = arg3[1].split(",");
      for (String id : split) {
        try {
          // idを取得
          int parseInt = Integer.parseInt(id);
          // idからSpawnPointを取得
          MobSpawnerPoint spawnerPointbySerialNumber = MobSpawnerPointManager.getSpawnerPointbySerialNumber(parseInt);
          if (spawnerPointbySerialNumber == null) {
            arg0.sendMessage(id + "存在しないidです。");
            continue;
          }
          // spawnpointを削除
          MobSpawnerPointManager.remove(spawnerPointbySerialNumber);
          // 一つだけ更新する
          spawnPointSheetRunnable.getData("id=" + id);
        } catch (Exception e) {
          arg0.sendMessage(id + "は不正な数字です。");
        }
      }
      SpletSheetExecutor.onExecute(spawnPointSheetRunnable);
    }
    return true;
  }

  protected boolean removeOperate(CommandSender arg0, String[] arg3) {
    if (arg3.length != 2) { return false; }

    SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(arg0);
    String[] split = arg3[1].split(",");
    for (String string : split) {
      if (!NumberUtils.isDigits(string)) {
        arg0.sendMessage(ChatColor.RED + "シリアル番号が不正です。数字で指定してください:" + string);
        continue;
      }
      MobSpawnerPoint spawnerPoint = MobSpawnerPointManager.getSpawnerPointbySerialNumber(Integer.parseInt(string));
      if (spawnerPoint == null) {
        arg0.sendMessage(ChatColor.RED + "指定されたシリアル番号のSpwanPointが存在しません:" + string);
        continue;
      }
      MobSpawnerPointManager.remove(spawnerPoint);
      arg0.sendMessage(ChatColor.GREEN + "id:" + spawnerPoint.getId() + "を削除しました。");

      // スプレットシートのものを削除する
      spawnPointSheetRunnable.deleteData("id=" + string);
    }
    SpletSheetExecutor.onExecute(spawnPointSheetRunnable);
    return true;
  }

  protected boolean listOperate(CommandSender arg0, String[] arg3, Location loc) {
    arg0.sendMessage(ChatColor.GREEN + "========spwan point info========");
    arg0.sendMessage("全スポーンポイント : " + MobSpawnerPointManager.getAllSpawnerPointList().size());
    arg0.sendMessage("ロード済みスポーンポイント : " + MobSpawnerPointManager.getSpawnerPointListByLoadedChunk().size());
    arg0.sendMessage("Player数 : " + SystemListener.loginPlayer);

    HashMap<SpawnLevel, String> spawnDetailMap = MobSpawnerPointManager.getSpawnDetail();

    int count = 0;
    for (Entry<SpawnLevel, SpawnScheduler> entry : MobSpawnerPointManager.getSchedulerList().entrySet()) {
      count += entry.getValue().getSize();
    }
    arg0.sendMessage("スポーン処理実行中スポーンポイント : " + count);
    HashMap<SpawnLevel, SpawnScheduler> schedulerList = MobSpawnerPointManager.getSchedulerList();
    for (SpawnLevel level : SpawnLevel.values()) {
      // スポーンポイントの情報を取得
      String spawnDetail = null;
      if (spawnDetailMap == null) {
        spawnDetail = "spawn point system is not working";
      } else {
        spawnDetail = spawnDetailMap.get(level);
        if (spawnDetail == null) {
          spawnDetail = "only this level is not working";
        }
      }

      SpawnScheduler spawnScheduler = schedulerList.get(level);
      if (spawnScheduler == null) {
        arg0.sendMessage("       " + level + " : 0    " + spawnDetail);
      } else {
        arg0.sendMessage("       " + level + " : " + spawnScheduler.getSize() + "    " + spawnDetail);
      }
    }

    arg0.sendMessage(ChatColor.GREEN + "========spwan point info========");
    return true;
  }

  protected boolean setOperate(CommandSender arg0, String[] arg3, Location loc) {
    if (arg3.length < 3) { return false; }

    if (!NumberUtils.isDigits(arg3[2])) {
      arg0.sendMessage(ChatColor.RED + "max countが不正です。:" + arg3[2]);
      return true;
    }

    SpawnLevel level = SpawnLevel.LEVEL3;
    if (arg3.length == 4) {
      if (SpawnLevel.getLevel(arg3[3]) != null) {
        level = SpawnLevel.getLevel(arg3[3]);
      }
    }

    SpawnMobGetterInterface mobSpawnPointInterface = SpawnMobGetterManager.getSpawnMobGetter(arg3[1].toUpperCase().replace("_", " "));
    if (mobSpawnPointInterface == null) {
      arg0.sendMessage(ChatColor.RED + "spawn point nameが不正です。:" + arg3[1]);
      return true;
    }
    SpletSheetMobSpawnerPoint spawnerPoint = new SpletSheetMobSpawnerPoint(MobSpawnerPointManager.getNextId(), loc, mobSpawnPointInterface,
        Integer.parseInt(arg3[2]), level);
    MobSpawnerPointManager.addSpawnPoint(spawnerPoint);

    // スプレットシートに書き込む
    SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(arg0);
    String memo = "";
    if (arg0 instanceof Player) {
      memo = ((Player) arg0).getDisplayName();
    }
    spawnPointSheetRunnable.addData(spawnerPoint, memo);
    SpletSheetExecutor.onExecute(spawnPointSheetRunnable);

    arg0.sendMessage(ChatColor.GREEN + "spawn pointを設定しました。");
    return true;
  }

  static ArrayList<String> operatorList = new ArrayList<>();
  {
    operatorList.add("set");
    operatorList.add("list");
    operatorList.add("remove");
    operatorList.add("reload");
    operatorList.add("monitor");
  }

  @Override
  public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    if (arg3.length == 1) { return StringUtil.copyPartialMatches(arg3[0].toLowerCase(), operatorList, new ArrayList<String>(operatorList.size())); }

    if (arg3.length > 1) {
      if ("set".equalsIgnoreCase(arg3[0])) {
        if (arg3.length == 2) { return StringUtil.copyPartialMatches(arg3[1].toUpperCase(), SpawnMobGetterManager.getNames(), new ArrayList<String>(
            SpawnMobGetterManager.getNames().size())); }
        if (arg3.length == 4) { return StringUtil.copyPartialMatches(arg3[3].toUpperCase(), SpawnLevel.getNames(),
            new ArrayList<String>(SpawnLevel.values().length)); }
      } else if ("list".equalsIgnoreCase(arg3[0])) {
        if (arg3.length == 2) {
          List<String> rangeList = Arrays.asList("", "all", "here", "loadedChunk");
          return StringUtil.copyPartialMatches(arg3[1].toUpperCase(), rangeList, new ArrayList<String>(rangeList.size()));
        }
      }
    }
    return ImmutableList.of();
  }

}
