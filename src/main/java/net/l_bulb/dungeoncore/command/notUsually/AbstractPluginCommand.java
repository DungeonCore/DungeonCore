package net.l_bulb.dungeoncore.command.notUsually;

public abstract class AbstractPluginCommand extends AbstractNotUsuallyCommand {

  // ここまでしっかりとした処理は不要なのでコメントアウト
  // @Override
  // public void execute(ICommandListener paramICommandListener, String[] paramArrayOfString)
  // throws ExceptionUsage, CommandException {
  // CommandSender sender = null;
  // if (paramICommandListener instanceof net.minecraft.server.v1_8_R1.Entity) {
  // sender = ((net.minecraft.server.v1_8_R1.Entity) paramICommandListener).getBukkitEntity();
  // } else if (paramICommandListener instanceof CommandBlockListenerAbstract) {
  // //未定
  // } else if (paramICommandListener instanceof MinecraftServer) {
  // sender = Bukkit.getConsoleSender();
  // } else {
  // new UnsupportedOperationException(paramICommandListener.getClass() + " is unknow sender");
  // }
  // }

}
