package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import org.bukkit.command.CommandSender;

public class Mob2SheetRunnable extends MobSheetRunnable {

  public Mob2SheetRunnable(CommandSender p) {
    super(p);
  }

  @Override
  public String getSheetName() {
    return "mob2";
  }
}
