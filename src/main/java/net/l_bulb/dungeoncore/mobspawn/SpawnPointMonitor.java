package net.l_bulb.dungeoncore.mobspawn;

import java.text.MessageFormat;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockBreakEvent;

import net.l_bulb.dungeoncore.mobspawn.chunk.ChunkGroup;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class SpawnPointMonitor {
  public static void sendMonitor(Set<SpawnPoint> spawnPointFromLocation, CommandSender sender) {
    sender.sendMessage(ChatColor.RED + "====Spawn Point====");
    boolean isFirst = true;
    for (SpawnPoint spawnPoint : spawnPointFromLocation) {
      if (isFirst) {
        isFirst = false;
      } else {
        sender.sendMessage("");
      }

      SpawnPointGroup spawnPointGroup = SpawnPointGroupFactory.getSpawnPointGroup(spawnPoint);
      SpawnResult result = spawnPoint.getSpawnResult();
      // spawn point情報
      sender.sendMessage(ChatColor.YELLOW + "[spawn point] ID:" + spawnPoint.getId() + "  (" + spawnPoint.getTargetType() + ")");
      // チャンク情報
      ChunkGroup chunkGroup = spawnPointGroup.getChunkGroup();
      sender.sendMessage(
          MessageFormat.format("{0}ChunkData  x:{1}, z:{2}, y:{3}", ChatColor.GREEN, chunkGroup.getX(), chunkGroup.getZ(), chunkGroup.getY()));
      // スポーンmob情報
      sender.sendMessage(ChatColor.GREEN + "Entity名:" + spawnPoint.getSpawnTargetName());
      // 待ち時間
      // 現在のインデックスと選択されたスポーンポイントのインデックスとの差
      int diffIndex = spawnPointGroup.getBeforeSpawnScheduleNumber() - SpawnManager.getSpawnScheduleNumber();
      if (diffIndex >= 0) {
        sender.sendMessage(MessageFormat.format("{0}残り時間:{1}秒  (this->{2}: system->{3})", ChatColor.GREEN, JavaUtil.round(diffIndex / 20.0, 2),
            spawnPointGroup.getBeforeSpawnScheduleNumber(), SpawnManager.getSpawnScheduleNumber()));
      } else {
        sender.sendMessage(MessageFormat.format("{0}残り時間:{1}秒   (this->{2}: system->{3})", ChatColor.GREEN,
            JavaUtil.round((SpawnManager.SpawnPointRouteTick + diffIndex) / 20.0, 2),
            spawnPointGroup.getBeforeSpawnScheduleNumber(), SpawnManager.getSpawnScheduleNumber()));
      }
      // メッセージ
      sender.sendMessage(ChatColor.GREEN + "メッセージ:" + result.getMessage());
      sender.sendMessage(
          MessageFormat.format("{0}最終湧き数:{1}(max:{2}体),  最終スポーン時間:{3}秒前", ChatColor.GREEN, result.getLastSpawnCount(), spawnPointGroup.getMaxCount(),
              result.getSpawnSecoundAge()));
    }

    sender.sendMessage(ChatColor.RED + "====Spawn Point====");
  }

  /**
   * スポンジを破壊したときモニターを表示する
   *
   * @param e
   */
  public static void onBrakeSponge(BlockBreakEvent e) {
    Block block = e.getBlock();
    if (block.getType() != Material.SPONGE) { return; }

    if (e.getPlayer().getGameMode() != GameMode.CREATIVE) { return; }

    Set<SpawnPoint> points = SpawnPointFactory.getSpawnPointFromLocation(block.getLocation());
    if (points == null || points.isEmpty()) { return; }

    e.setCancelled(true);
    SpawnPointMonitor.sendMonitor(points, e.getPlayer());
  }
}
