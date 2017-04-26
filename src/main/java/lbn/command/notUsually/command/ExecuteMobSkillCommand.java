package lbn.command.notUsually.command;

import java.util.List;

import org.bukkit.entity.Entity;

import lbn.command.notUsually.AbstractPluginCommand;
import lbn.mob.mobskill.MobSkillInterface;
import lbn.mob.mobskill.MobSkillManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R1.ChatComponentText;
import net.minecraft.server.v1_8_R1.CommandException;
import net.minecraft.server.v1_8_R1.ExceptionUsage;
import net.minecraft.server.v1_8_R1.ICommandListener;

public class ExecuteMobSkillCommand extends AbstractPluginCommand {

  @Override
  public String getCommand() {
    return "execMobSkill";
  }

  @Override
  public String getUsage(ICommandListener paramICommandListener) {
    return getCommand() + " mobskillId 発動者 ターゲット";
  }

  @Override
  public void execute(ICommandListener paramICommandListener, String[] paramArrayOfString)
      throws ExceptionUsage, CommandException {
    if (paramArrayOfString.length != 3) {
      String string = ChatColor.RED + getUsage(paramICommandListener);
      sendMessage(paramICommandListener, string);
      return;
    }

    MobSkillInterface fromId = MobSkillManager.fromId(paramArrayOfString[0]);
    if (fromId == null) {
      sendMessage(paramICommandListener, ChatColor.RED + "mobskillIdが不正です");
      return;
    }

    List<Entity> executeEntitys = getEntityListByToken(paramICommandListener, paramArrayOfString[1]);
    List<Entity> targetEntitys = getEntityListByToken(paramICommandListener, paramArrayOfString[2]);
    for (Entity executeor : executeEntitys) {
      for (Entity target : targetEntitys) {
        // スキルを発動する
        fromId.execute(target, executeor);
      }
    }
  }

  protected void sendMessage(ICommandListener paramICommandListener, String string) {
    paramICommandListener.sendMessage(new ChatComponentText(string));
  }

  @Override
  public int compareTo(Object o) {
    return 0;
  }
}
