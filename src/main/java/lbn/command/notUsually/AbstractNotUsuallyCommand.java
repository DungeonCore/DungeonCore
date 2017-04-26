package lbn.command.notUsually;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.command.VanillaCommandWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;
import net.minecraft.server.v1_8_R1.CommandAbstract;
import net.minecraft.server.v1_8_R1.ICommandListener;
import net.minecraft.server.v1_8_R1.PlayerSelector;

public abstract class AbstractNotUsuallyCommand extends CommandAbstract {

  public int a() {
    return 2;
  }

  public void regist() {
    String fallbackPrefix = getCommandWrapper() instanceof VanillaCommandWrapper ? "minecraft" : Main.plugin.getName();
    ((CraftServer) Bukkit.getServer()).getCommandMap().register(fallbackPrefix, getCommandWrapper());

    // 再度登録をする
    new BukkitRunnable() {
      @Override
      public void run() {
        ((CraftServer) Bukkit.getServer()).getCommandMap().register(fallbackPrefix, getCommandWrapper());
      }
    }.runTaskLater(Main.plugin, 10);
  }

  public List<Entity> getEntityListByToken(ICommandListener icommandlistener, String token) {
    ArrayList<Entity> rtnList = new ArrayList<Entity>();
    @SuppressWarnings("unchecked")
    List<net.minecraft.server.v1_8_R1.Entity> list = PlayerSelector.getPlayers(icommandlistener, token, net.minecraft.server.v1_8_R1.Entity.class);
    for (net.minecraft.server.v1_8_R1.Entity entity : list) {
      rtnList.add(entity.getBukkitEntity());
    }
    return rtnList;
  }

  public Command getCommandWrapper() {
    return new VanillaCommandWrapper(this, getUsage(null));
  }

}
