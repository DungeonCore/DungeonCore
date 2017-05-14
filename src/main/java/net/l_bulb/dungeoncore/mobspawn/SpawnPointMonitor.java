package net.l_bulb.dungeoncore.mobspawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;

import net.l_bulb.dungeoncore.mobspawn.point.MobSpawnerPoint;
import net.l_bulb.dungeoncore.mobspawn.point.MobSpawnerPointManager;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class SpawnPointMonitor {
  MobSpawnerPoint point;

  public SpawnPointMonitor(MobSpawnerPoint point) {
    this.point = point;
  }

  boolean isWorking;
  boolean isWillDelete;
  int waitTime;
  String detail;

  public void setWorking(boolean isWorking) {
    this.isWorking = isWorking;
  }

  public void setWaitTime(int waitTime) {
    this.waitTime = waitTime;
  }

  public void setWillDeleteList(boolean contains) {
    this.isWillDelete = contains;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public void send(CommandSender sender) {
    Location location = point.getLocation();
    sender.sendMessage(ChatColor.GREEN + "==========モニター==========");
    sender.sendMessage("id : " + point.getId() + ", Location : " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
    sender.sendMessage("working : " + isWorking + "(willdelete : " + isWillDelete + ",  chunk load : "
        + MobSpawnerPointManager.loadedChunk.contains(new ChunkWrapper(point)) + ")");

    if (point.nearMob.size() == 0) {
      sender.sendMessage("near Entity:none");
    } else {
      LivingEntity livingEntity = point.nearMob.get(0);
      sender.sendMessage("near Entity:" + livingEntity.getCustomName() + "(" + livingEntity.getType() + ") "
          + getLocationString(livingEntity.getLocation()));
    }
    sender.sendMessage("waitingTime : " + JavaUtil.round((waitTime / 20), 2) + "秒,  detail : " + detail);
    int lastSpawnTime = (int) ((System.currentTimeMillis() - point.lastSpawnTime) / 1000);
    if (point.lastSpawnTime == -1) {
      lastSpawnTime = -1;
    }
    sender.sendMessage("lastspawn : " + lastSpawnTime + "秒前 (" + point.lastSpawnCount + "匹), キャンセル理由:" + point.cancelReson);
    sender.sendMessage("LEVEL:" + point.getLevel() + "(" + point.getLevel().getSpawnTick() / 20 + "秒に一回スポーン)");
    sender.sendMessage("最大湧き数:" + point.getMaxMobCount() + ", nearChunk:" + point.isLookNearChunk() + ", hight:" + point.getDungeonHight());
    sender.sendMessage(ChatColor.GREEN + "==========モニター==========");
  }

  public String getLocationString(Location loc) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(loc.getBlockX());
    sb.append(",");
    sb.append(loc.getBlockY());
    sb.append(",");
    sb.append(loc.getBlockZ());
    sb.append("]");
    return sb.toString();
  }
}
