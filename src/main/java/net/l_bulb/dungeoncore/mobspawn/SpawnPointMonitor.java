package net.l_bulb.dungeoncore.mobspawn;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockBreakEvent;

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
      sender.sendMessage(ChatColor.YELLOW + "[spawn point]　ID:" + spawnPoint.getId() + "  (" + spawnPoint.getTargetType() + ")");
      sender.sendMessage(ChatColor.GREEN + "Entity名:" + spawnPoint.getSpawnTargetName());
      // 現在のインデックスと選択されたスポーンポイントのインデックスとの差
      int diffIndex = spawnPointGroup.getBeforeSpawnScheduleNumber() - SpawnManager.getSpawnScheduleNumber();
      if (diffIndex >= 0) {
        sender.sendMessage(ChatColor.GREEN + "残り時間:" + JavaUtil.round(diffIndex / 20, 2) + "秒");
      } else {
        sender.sendMessage(ChatColor.GREEN + "残り時間:" + JavaUtil.round((SpawnManager.SpawnPointRouteTick - diffIndex) / 20, 2) + "秒");
      }
      sender.sendMessage(ChatColor.GREEN + "メッセージ:" + result.getMessage());
      sender.sendMessage(
          ChatColor.GREEN + "最終湧き数:" + result.getLastSpawnCount() + ",  "
              + "最終スポーン時間:" + (int) ((System.currentTimeMillis() - result.getLastSpawnDate()) / 100) + "秒前");
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
