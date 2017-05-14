package net.l_bulb.dungeoncore.player;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.l_bulb.dungeoncore.dungeoncore.Main;

public class PlayerTeamManager {
  private static final String DEFAULT_TEAM = "default_team";

  static Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

  static {
    if (board.getTeam(DEFAULT_TEAM) == null) {
      Team defaultTeam = board.registerNewTeam(DEFAULT_TEAM);
      defaultTeam.setCanSeeFriendlyInvisibles(true);
      defaultTeam.setAllowFriendlyFire(false);
    }
  }

  public static void setTeam(Player p) {
    Team team = board.getTeam(DEFAULT_TEAM);
    team.addPlayer(p);
  }

  public static void setTeamAllPlayer() {

    new BukkitRunnable() {
      @Override
      public void run() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
          setTeam(player);
        }
      }
    }.runTaskLater(Main.plugin, 2);
  }
}
