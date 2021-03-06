package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.common.place.PlaceBean;
import net.l_bulb.dungeoncore.common.place.dungeon.DungeonData;
import net.l_bulb.dungeoncore.common.place.dungeon.DungeonList;

public class DungeonListRunnable extends AbstractComplexSheetRunable {

  public DungeonListRunnable(CommandSender sender) {
    super(sender);
  }

  @Override
  public String getSheetName() {
    return "dungeonlist";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "name", "type", "tploc", "entranceloc", "startloc", "level" };
  }

  int count = -1;

  @Override
  protected void excuteOnerow(String[] row) {
    count++;
    if (count == 0) { return; }

    PlaceBean placeBean = new PlaceBean();

    placeBean.setId(row[0]);
    placeBean.setName(row[1]);
    placeBean.setType(row[2]);
    placeBean.setTpLocation(row[3]);
    placeBean.setEntranceLocation(row[4]);
    placeBean.setDungeonStartLocation(row[5]);
    placeBean.setLevel(row[6]);

    if (placeBean.isError()) {
      sendMessage("ダンジョンデータにエラーがあります。[" + StringUtils.join(row) + "]");
      return;
    }

    switch (placeBean.getType()) {
      case DUNGEON:
        DungeonData place = new DungeonData(placeBean);
        place.setShowHologram(true);
        DungeonList.addDungeon(place);
        break;
      // case DUNGEON_IMMATURE:
      // break;
      default:
        DungeonData place2 = new DungeonData(placeBean);
        DungeonList.addDungeon(place2);
        break;
    }
  }

}
