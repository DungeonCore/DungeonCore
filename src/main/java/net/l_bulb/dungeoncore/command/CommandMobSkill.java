package net.l_bulb.dungeoncore.command;

import net.l_bulb.dungeoncore.mob.mobskill.MobSkillManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandMobSkill implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender paramCommandSender,
      Command paramCommand, String paramString,
      String[] paramArrayOfString) {
    MobSkillManager.reloadDataByCommand(paramCommandSender);
    return true;
  }

}
