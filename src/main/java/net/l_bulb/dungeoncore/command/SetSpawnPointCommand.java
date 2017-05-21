package net.l_bulb.dungeoncore.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.l_bulb.dungeoncore.SystemListener;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpawnPointSheetRunnable;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpletSheetExecutor;
import net.l_bulb.dungeoncore.mobspawn.old.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.old.SpawnPointMonitor;
import net.l_bulb.dungeoncore.mobspawn.old.gettter.SpawnMobGetterInterface;
import net.l_bulb.dungeoncore.mobspawn.old.gettter.SpawnMobGetterManager;
import net.l_bulb.dungeoncore.mobspawn.old.point.MobSpawnerPoint;
import net.l_bulb.dungeoncore.mobspawn.old.point.MobSpawnerPointManager;
import net.l_bulb.dungeoncore.mobspawn.old.point.SpawnScheduler;
import net.l_bulb.dungeoncore.mobspawn.old.point.SpletSheetMobSpawnerPoint;

import com.google.common.collect.ImmutableList;

public class SetSpawnPointCommand implements CommandExecutor, TabCompleter {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) { return false; }

    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "Playerが実行してください。");
    }

    Location loc = null;
    try {
      loc = ((Player) sender).getLocation();
    } catch (Exception e) {}

    String operator = args[0];
    if ("set".equalsIgnoreCase(operator)) {
      return setOperate(sender, args, loc);
    } else if ("list".equalsIgnoreCase(operator)) {
      return listOperate(sender, args, loc);
    } else if ("remove".equalsIgnoreCase(operator)) {
      return removeOperate(sender, args);
    } else if ("reload".equalsIgnoreCase(operator)) {
      return reloadOperate(sender, args);
    } else if ("monitor".equalsIgnoreCase(operator)) { return monitorOperate(sender, args); }

    return false;
  }

  private boolean monitorOperate(CommandSender sender, String[] args) {
    if (args.length < 1) {
      sender.sendMessage("id必要");
      return true;
    }

    String id = args[1];
    try {
      MobSpawnerPoint point = MobSpawnerPointManager.getSpawnerPointbySerialNumber(Integer.parseInt(id));
      if (point == null) {
        sender.sendMessage("そのIDのスポーンポイントは存在しません");
        return true;
      }
      SpawnScheduler spawnScheduler = MobSpawnerPointManager.getSchedulerList().get(point.getLevel());
      SpawnPointMonitor monitor = spawnScheduler.getMonitor(point);
      monitor.send(sender);
    } catch (Exception e) {
      sender.sendMessage("error発生");
    }
    return true;
  }

  private boolean reloadOperate(CommandSender sender, String[] args) {
    if (args.length == 1) {
      MobSpawnerPointManager.clear();

      // 全てを更新する
      SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(sender);
      spawnPointSheetRunnable.getData(null);
      SpletSheetExecutor.onExecute(spawnPointSheetRunnable);
    } else {
      SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(sender);

      String[] split = args[1].split(",");
      for (String id : split) {
        try {
          // idを取得
          int parseInt = Integer.parseInt(id);
          // idからSpawnPointを取得
          MobSpawnerPoint spawnerPointbySerialNumber = MobSpawnerPointManager.getSpawnerPointbySerialNumber(parseInt);
          if (spawnerPointbySerialNumber == null) {
            sender.sendMessage(id + "存在しないidです。");
            continue;
          }
          // spawnpointを削除
          MobSpawnerPointManager.remove(spawnerPointbySerialNumber);
          // 一つだけ更新する
          spawnPointSheetRunnable.getData("id=" + id);
        } catch (Exception e) {
          sender.sendMessage(id + "は不正な数字です。");
        }
      }
      SpletSheetExecutor.onExecute(spawnPointSheetRunnable);
    }
    return true;
  }

  protected boolean removeOperate(CommandSender sender, String[] args) {
    if (args.length != 2) { return false; }

    SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(sender);
    String[] split = args[1].split(",");
    for (String string : split) {
      if (!NumberUtils.isDigits(string)) {
        sender.sendMessage(ChatColor.RED + "シリアル番号が不正です。数字で指定してください:" + string);
        continue;
      }
      MobSpawnerPoint spawnerPoint = MobSpawnerPointManager.getSpawnerPointbySerialNumber(Integer.parseInt(string));
      if (spawnerPoint == null) {
        sender.sendMessage(ChatColor.RED + "指定されたシリアル番号のSpwanPointが存在しません:" + string);
        continue;
      }
      MobSpawnerPointManager.remove(spawnerPoint);
      sender.sendMessage(ChatColor.GREEN + "id:" + spawnerPoint.getId() + "を削除しました。");

      // スプレットシートのものを削除する
      spawnPointSheetRunnable.deleteData("id=" + string);
    }
    SpletSheetExecutor.onExecute(spawnPointSheetRunnable);
    return true;
  }

  protected boolean listOperate(CommandSender sender, String[] args, Location location) {
    sender.sendMessage(ChatColor.GREEN + "========spwan point info========");
    sender.sendMessage("全スポーンポイント : " + MobSpawnerPointManager.getAllSpawnerPointList().size());
    sender.sendMessage("ロード済みスポーンポイント : " + MobSpawnerPointManager.getSpawnerPointListByLoadedChunk().size());
    sender.sendMessage("Player数 : " + SystemListener.loginPlayer);

    HashMap<SpawnLevel, String> spawnDetailMap = MobSpawnerPointManager.getSpawnDetail();

    int count = 0;
    for (Entry<SpawnLevel, SpawnScheduler> entry : MobSpawnerPointManager.getSchedulerList().entrySet()) {
      count += entry.getValue().getSize();
    }
    sender.sendMessage("スポーン処理実行中スポーンポイント : " + count);
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
        sender.sendMessage("       " + level + " : 0    " + spawnDetail);
      } else {
        sender.sendMessage("       " + level + " : " + spawnScheduler.getSize() + "    " + spawnDetail);
      }
    }

    sender.sendMessage(ChatColor.GREEN + "========spwan point info========");
    return true;
  }

  protected boolean setOperate(CommandSender sender, String[] args, Location location) {
    if (args.length < 3) { return false; }

    if (!NumberUtils.isDigits(args[2])) {
      sender.sendMessage(ChatColor.RED + "max countが不正です。:" + args[2]);
      return true;
    }

    SpawnLevel level = SpawnLevel.LEVEL3;
    if (args.length == 4) {
      if (SpawnLevel.getLevel(args[3]) != null) {
        level = SpawnLevel.getLevel(args[3]);
      }
    }

    SpawnMobGetterInterface mobSpawnPointInterface = SpawnMobGetterManager.getSpawnMobGetter(args[1].toUpperCase().replace("_", " "));
    if (mobSpawnPointInterface == null) {
      sender.sendMessage(ChatColor.RED + "spawn point nameが不正です。:" + args[1]);
      return true;
    }
    SpletSheetMobSpawnerPoint spawnerPoint = new SpletSheetMobSpawnerPoint(MobSpawnerPointManager.getNextId(), location, mobSpawnPointInterface,
        Integer.parseInt(args[2]), level);
    MobSpawnerPointManager.addSpawnPoint(spawnerPoint);

    // スプレットシートに書き込む
    SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(sender);
    String memo = "";
    if (sender instanceof Player) {
      memo = ((Player) sender).getDisplayName();
    }
    spawnPointSheetRunnable.addData(spawnerPoint, memo);
    SpletSheetExecutor.onExecute(spawnPointSheetRunnable);

    sender.sendMessage(ChatColor.GREEN + "spawn pointを設定しました。");
    return true;
  }

  private static final ArrayList<String> operatorList = new ArrayList<>();
  {
    operatorList.add("set");
    operatorList.add("list");
    operatorList.add("remove");
    operatorList.add("reload");
    operatorList.add("monitor");
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (args.length == 1) { return StringUtil.copyPartialMatches(args[0].toLowerCase(), operatorList,
        new ArrayList<String>(operatorList.size())); }

    if (args.length > 1) {
      if ("set".equalsIgnoreCase(args[0])) {
        if (args.length == 2) { return StringUtil.copyPartialMatches(args[1].toUpperCase(), SpawnMobGetterManager.getNames(),
            new ArrayList<String>(SpawnMobGetterManager.getNames().size())); }
        if (args.length == 4) { return StringUtil.copyPartialMatches(args[3].toUpperCase(), SpawnLevel.getNames(),
            new ArrayList<String>(SpawnLevel.values().length)); }
      } else if ("list".equalsIgnoreCase(args[0])) {
        if (args.length == 2) {
          List<String> rangeList = Arrays.asList("", "all", "here", "loadedChunk");
          return StringUtil.copyPartialMatches(args[1].toUpperCase(), rangeList, new ArrayList<String>(rangeList.size()));
        }
      }
    }
    return ImmutableList.of();
  }

}
