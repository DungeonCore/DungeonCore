package lbn.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.util.DungeonLogger;

public class CommandExtraMob implements CommandExecutor, TabCompleter {
  
  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    if (arg0 instanceof Player) {
      if (arg3.length == 1 && arg3[0].equals("all")) {
        spawnAll((Player) arg0);
        return true;
      } else {
        return spawnByPlayer(arg3, (Player) arg0);
      }
    } else if (arg0 instanceof BlockCommandSender) {
      return spawnByCommandBlock(arg3, (BlockCommandSender) arg0);
    }
    return false;
    
  }
  
  static int count = 0;
  
  private void spawnAll(Player p) {
    // int i = -1;
    Collection<AbstractMob<?>> allMobs = MobHolder.getAllMobs();
    ArrayList<AbstractMob<?>> arrayList = new ArrayList<AbstractMob<?>>(allMobs);
    Collections.sort(arrayList, new Comparator<AbstractMob<?>>() {
      @Override
      public int compare(AbstractMob<?> o1, AbstractMob<?> o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    
    for (AbstractMob<?> mob : arrayList) {
      if (mob.getEntityType() == EntityType.VILLAGER) {
        continue;
      }
      // i++;
      try {
        Entity spawn = mob.spawn(p.getLocation());
        if (spawn == null || !spawn.isValid()) {
          Bukkit.broadcastMessage("error2:" + mob.getName());
        }
      } catch (Exception e) {
        DungeonLogger.error("error2:" + mob.getName());
        e.printStackTrace();
        Bukkit.broadcastMessage("error2:" + mob.getName());
      }
    }
    // count++;
  }
  
  /**
   * mob名 x y z [count]
   *
   * @param arg3
   * @param arg0
   * @return
   */
  private boolean spawnByCommandBlock(String[] arg3, BlockCommandSender arg0) {
    String mobName = arg3[0];
    int count = 1;
    try {
      if (arg3.length == 5) {
        count = Integer.parseInt(arg3[4]);
      }
      
      if (count > 50) {
        count = 50;
      }
      
      AbstractMob<?> mob = MobHolder.getMob(mobName.replace("_", " "));
      if (mob == null || mob.isNullMob()) {
        arg0.sendMessage("mobが存在しません。");
        return false;
      }
      
      for (int i = 0; i < count; i++) {
        if (arg3.length == 1) {
          mob.spawn(arg0.getBlock().getLocation());
        } else {
          mob.spawn(new Location(arg0.getBlock().getWorld(), Double.parseDouble(arg3[1]), Double.parseDouble(arg3[2]),
              Double.parseDouble(arg3[3])));
        }
      }
      return true;
    } catch (NumberFormatException e) {
      arg0.sendMessage("数値が不正です");
      return false;
    }
  }
  
  protected boolean spawnByPlayer(String[] arg3, Player p) {
    String mobName = arg3[0];
    int count = 1;
    try {
      if (arg3.length == 4) {
        count = Integer.parseInt(arg3[3]);
      }
      
      AbstractMob<?> mob = MobHolder.getMob(mobName.replace("_", " "));
      if (mob == null || mob.isNullMob()) {
        p.sendMessage("mobが存在しません。");
        return false;
      }
      
      for (int i = 0; i < count; i++) {
        if (arg3.length == 1) {
          mob.spawn(p.getLocation());
        } else {
          mob.spawn(new Location(p.getWorld(), Integer.parseInt(arg3[1]), Integer.parseInt(arg3[2]),
              Integer.parseInt(arg3[3])));
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      p.sendMessage("入力データが不正です");
      return false;
    }
  }
  
  @Override
  public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    if (arg3.length == 1) {
      Collection<String> itemNameList = getItemMap();
      return (List<String>) StringUtil.copyPartialMatches(arg3[0], itemNameList,
          new ArrayList<String>(itemNameList.size()));
    }
    return ImmutableList.of();
  }
  
  protected Collection<String> getItemMap() {
    ArrayList<String> arrayList = new ArrayList<String>();
    for (AbstractMob<?> mob : MobHolder.getAllMobs()) {
      if (mob.getEntityType() == EntityType.VILLAGER) {
        continue;
      }
      arrayList.add(mob.getName().replace(" ", "_"));
    }
    return arrayList;
  }
}
