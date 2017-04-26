package lbn.command.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lbn.common.other.RouteSearcher;
import lbn.dungeoncore.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class ChangeBiomeCommand implements CommandExecutor, TabCompleter {

  // <command> biomename x z x z
  @Override
  public boolean onCommand(final CommandSender arg0, Command arg1, String arg2,
      String[] arg3) {
    if (arg3.length == 1) {
      String special = arg3[0];
      if (special.equals("search")) {
        RouteSearcher autoPlantInstance = RouteSearcher.getInstance((Player) arg0);
        autoPlantInstance.startSearchingPath();
        return true;
      }
    }

    if (arg3.length != 5) { return false; }

    try {
      String biomeName = arg3[0];
      final Biome valueOf = Biome.valueOf(biomeName);

      final double x1 = Double.parseDouble(arg3[1]);
      final double z1 = Double.parseDouble(arg3[2]);
      final double x2 = Double.parseDouble(arg3[3]);
      final double z2 = Double.parseDouble(arg3[4]);

      final double maxX = Math.max(x1, x2);
      final double minX = Math.min(x1, x2);
      final double maxZ = Math.max(z1, z2);
      final double minZ = Math.min(z1, z2);

      new BukkitRunnable() {
        int count = 0;

        double z = minZ;

        @Override
        public void run() {
          for (int i = 0; i < 20; i++) {
            for (double x = minX; x < maxX; x++) {
              Location location = new Location(((Player) arg0).getWorld(), x, 65, z);
              location.getBlock().setBiome(valueOf);
            }
            z++;
            if (z > maxZ) {
              Bukkit.broadcastMessage(ChatColor.GRAY + "終了しました。");
              cancel();
              return;
            }
          }
          if (count % 5 == 0) {
            Bukkit.broadcastMessage(ChatColor.GRAY + "進捗:" + (int) ((z - minZ) * 100 / (maxZ - minZ)) + "%");
          }
          count++;
        }
      }.runTaskTimer(Main.plugin, 0, 1);

    } catch (NumberFormatException e) {
      arg0.sendMessage(ChatColor.RED + "座標は数値で指定してください。");
    } catch (Exception e) {
      arg0.sendMessage(ChatColor.RED + "バイオームを指定してください。");
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    if (arg3.length == 1) {
      Collection<String> itemNameList = getNames();
      return (List<String>) StringUtil.copyPartialMatches(arg3[0], itemNameList, new ArrayList<String>(itemNameList.size()));
    }
    return ImmutableList.of();
  }

  protected List<String> getNames() {
    ArrayList<String> arrayList = new ArrayList<String>();
    for (Biome biome : Biome.values()) {
      arrayList.add(biome.toString());
    }
    return arrayList;
  }

}
