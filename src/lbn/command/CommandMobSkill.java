package lbn.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lbn.mob.mobskill.MobSkillManager;

public class CommandMobSkill implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		MobSkillManager.reloadDataByCommand(paramCommandSender);
		return true;
	}

}
