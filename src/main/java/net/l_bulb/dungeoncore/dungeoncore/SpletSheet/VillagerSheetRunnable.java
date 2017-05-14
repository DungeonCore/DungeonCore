package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerData;

public class VillagerSheetRunnable extends AbstractComplexSheetRunable {

  public VillagerSheetRunnable(CommandSender p) {
    super(p);
  }

  @Override
  public String getSheetName() {
    return "villager";
  }

  @Override
  public String[] getTag() {
    return new String[] { "name", "type", "text", "location", "adult", "data", "mobtype", "skin", "id" };
  }

  @Override
  protected void excuteOnerow(String[] row) {
    VillagerData.registSpletsheetVillager(sender, row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], row[8]);
  }

  @Override
  public boolean hasSecoundSheet() {
    return true;
  }
}
