package net.l_bulb.dungeoncore.command.notUsually.command;

import net.l_bulb.dungeoncore.command.notUsually.AbstractVanillaCommand;
import net.minecraft.server.v1_8_R1.CommandKill;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.ICommandListener;

public class CustomKillCommand extends AbstractVanillaCommand {

  public CustomKillCommand() {
    super(new CommandKill());
  }

  @Override
  protected void execute2(ICommandListener icommandlistener, String[] astring) {
    if (astring.length == 0) {
      EntityPlayer entityplayer = b(icommandlistener);
      entityplayer.G();
      a(icommandlistener, this, "commands.kill.successful", new Object[] { entityplayer.getScoreboardDisplayName() });
    } else {
      // パーミッションがない場合は自殺する
      if (icommandlistener instanceof EntityPlayer) {
        if (!((EntityPlayer) icommandlistener).getBukkitEntity().hasPermission("main.lbnDungeonUtil.command.mod.killother")) {
          EntityPlayer entityplayer = b(icommandlistener);
          entityplayer.G();
          a(icommandlistener, this, "commands.kill.successful", new Object[] { entityplayer.getScoreboardDisplayName() });
          return;
        }
      }
      Entity entity = b(icommandlistener, astring[0]);
      entity.G();
      a(icommandlistener, this, "commands.kill.successful", new Object[] { entity.getScoreboardDisplayName() });
    }
  }

}
