package net.l_bulb.dungeoncore.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpawnPointSheetRunnable;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpletSheetExecutor;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mobspawn.SpawnPoint;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointFactory;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointGroupFactory;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointMonitor;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData.TargetType;
import net.l_bulb.dungeoncore.util.JavaUtil;

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

    int idInt = JavaUtil.getInt(id, -1);
    Set<SpawnPoint> spawnPointFromId = SpawnPointFactory.getSpawnPointFromId(idInt);
    if (spawnPointFromId == null || spawnPointFromId.isEmpty()) {
      sender.sendMessage("そのIDのスポーンポイントは存在しません");
      return true;
    }
    SpawnPointMonitor.sendMonitor(spawnPointFromId, sender);
    return true;
  }

  private boolean reloadOperate(CommandSender sender, String[] args) {
    SpawnPointGroupFactory.clear();

    // 全てを更新する
    SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(sender);
    spawnPointSheetRunnable.getData(null);
    SpletSheetExecutor.onExecute(spawnPointSheetRunnable);
    return true;
  }

  protected boolean listOperate(CommandSender sender, String[] args, Location location) {
    sender.sendMessage(ChatColor.GREEN + "現在使えません");
    // sender.sendMessage(ChatColor.GREEN + "========spwan point info========");
    // sender.sendMessage("全スポーンポイント : " + MobSpawnerPointManager.getAllSpawnerPointList().size());
    // sender.sendMessage("ロード済みスポーンポイント : " + MobSpawnerPointManager.getSpawnerPointListByLoadedChunk().size());
    // sender.sendMessage("Player数 : " + SystemListener.loginPlayer);
    //
    // HashMap<SpawnLevel, String> spawnDetailMap = MobSpawnerPointManager.getSpawnDetail();
    //
    // int count = 0;
    // for (Entry<SpawnLevel, SpawnScheduler> entry : MobSpawnerPointManager.getSchedulerList().entrySet()) {
    // count += entry.getValue().getSize();
    // }
    // sender.sendMessage("スポーン処理実行中スポーンポイント : " + count);
    // HashMap<SpawnLevel, SpawnScheduler> schedulerList = MobSpawnerPointManager.getSchedulerList();
    // for (SpawnLevel level : SpawnLevel.values()) {
    // // スポーンポイントの情報を取得
    // String spawnDetail = null;
    // if (spawnDetailMap == null) {
    // spawnDetail = "spawn point system is not working";
    // } else {
    // spawnDetail = spawnDetailMap.get(level);
    // if (spawnDetail == null) {
    // spawnDetail = "only this level is not working";
    // }
    // }
    //
    // SpawnScheduler spawnScheduler = schedulerList.get(level);
    // if (spawnScheduler == null) {
    // sender.sendMessage(" " + level + " : 0 " + spawnDetail);
    // } else {
    // sender.sendMessage(" " + level + " : " + spawnScheduler.getSize() + " " + spawnDetail);
    // }
    // }
    //
    // sender.sendMessage(ChatColor.GREEN + "========spwan point info========");
    return true;
  }

  protected boolean setOperate(CommandSender sender, String[] args, Location location) {
    if (args.length < 3) { return false; }

    if (!NumberUtils.isDigits(args[2])) {
      sender.sendMessage(ChatColor.RED + "max countが不正です。:" + args[2]);
      return true;
    }

    TargetType type = TargetType.MONSTER;
    if (args.length == 4) {
      type = TargetType.getType(args[3]);
    }

    // スプレットシートに書き込む
    SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(sender);
    String memo = "";
    if (sender instanceof Player) {
      memo = ((Player) sender).getDisplayName();
    }
    SpawnPointSpreadSheetData data = new SpawnPointSpreadSheetData();
    data.setId(SpawnPointFactory.getNextId());
    data.setLocation(location);
    data.setTargetName(args[1]);
    data.setMaxSpawnCount(Integer.parseInt(args[2]));
    data.setType(type);

    SpawnPoint newInstance = SpawnPointFactory.getNewInstance(data);
    if (newInstance == null) {
      sender.sendMessage(ChatColor.RED + "コマンドが不正です。モンスター名/item名が違うかも");
    }

    // スポーン登録する
    spawnPointSheetRunnable.addData(data, new String[] { args[1] }, memo);
    SpletSheetExecutor.onExecute(spawnPointSheetRunnable);
    SpawnPointGroupFactory.registSpawnPoint(newInstance);

    sender.sendMessage(ChatColor.GREEN + "spawn pointを設定しました。");
    return true;
  }

  private static final ArrayList<String> operatorList = new ArrayList<>();
  {
    operatorList.add("set");
    // operatorList.add("list");
    operatorList.add("reload");
    operatorList.add("monitor");
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (args.length == 1) { return StringUtil.copyPartialMatches(args[0].toLowerCase(), operatorList,
        new ArrayList<String>(operatorList.size())); }

    if (args.length > 1) {
      if ("set".equalsIgnoreCase(args[0])) {
        if (args.length == 2) { return StringUtil.copyPartialMatches(args[1].toUpperCase(),
            MobHolder.getAllNames().stream().map(s -> s.replace(" ", "_")).collect(Collectors.toList()),
            new ArrayList<String>(MobHolder.getAllNames().size())); }
        if (args.length == 4) { return StringUtil.copyPartialMatches(args[3].toUpperCase(), Arrays.asList("MONSTER,BOSS,ITEM"),
            new ArrayList<String>()); }
        // } else if ("list".equalsIgnoreCase(args[0])) {
        // if (args.length == 2) {
        // List<String> rangeList = Arrays.asList("", "all", "here", "loadedChunk");
        // return StringUtil.copyPartialMatches(args[1].toUpperCase(), rangeList, new ArrayList<String>(rangeList.size()));
        // }
      }
    }
    return ImmutableList.of();
  }

}
