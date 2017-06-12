package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.common.buff.BuffData;
import net.l_bulb.dungeoncore.common.buff.BuffDataFactory;
import net.l_bulb.dungeoncore.common.buff.BuffType;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.FoodItemData;
import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.SpreadSheetFoodItem;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class Food2SheetRunnable extends AbstractSheetRunable {

  public Food2SheetRunnable(CommandSender sender) {
    super(sender);
  }

  @Override
  protected String getQuery() {
    return null;
  }

  @Override
  public String getSheetName() {
    return "food2";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "name", "command", "price", "buff1", "time1", "level1", "buff2", "time2", "level2", "buff3", "time3", "level3", "mp",// 13
        "buff1delay", "buff2delay", "buff3delay" };
  }

  @Override
  protected void excuteOnerow(String[] row) {
    try {
      FoodItemData foodItemData = new FoodItemData(row[0]);
      foodItemData.setName(row[1]);
      foodItemData.setCommand(row[2]);
      foodItemData.setPrice(JavaUtil.getInt(row[8], 0));

      List<String> buffList = IntStream.of(4, 7, 10).filter(i -> row[i] != null && row[i].length() != 0)
          .mapToObj(i -> createData(row[0], i, row[i], row[i + 1], row[i + 2]))
          .filter(val -> val != null)
          .peek(BuffDataFactory::register)
          .map(buff -> buff.getId())
          .collect(Collectors.toList());

      switch (buffList.size()) {
        case 3:
          foodItemData.setBuff3(buffList.get(2));
        case 2:
          foodItemData.setBuff2(buffList.get(1));
        case 1:
          foodItemData.setBuff1(buffList.get(0));
        default:
          break;
      }

      foodItemData.setMagicExp(JavaUtil.getInt(row[13], 0));
      foodItemData.setBuff1DelayTick((int) (JavaUtil.getDouble(row[14], 0) * 20));
      foodItemData.setBuff2DelayTick((int) (JavaUtil.getDouble(row[15], 0) * 20));
      foodItemData.setBuff3DelayTick((int) (JavaUtil.getDouble(row[16], 0) * 20));

      SpreadSheetFoodItem spreadSheetFoodItem = new SpreadSheetFoodItem(foodItemData);
      ItemManager.registItem(spreadSheetFoodItem);
    } catch (Exception e) {
      sendMessage("入力データが不正です：" + Arrays.toString(row));
    }
  }

  /**
   * バフを作成する
   *
   * @param foodID
   * @param index
   * @param buffName
   * @param time
   * @param level
   * @return
   */
  public BuffData createData(String foodID, int index, String buffName, String time, String level) {
    String id = "food_" + foodID + "_buff_" + index;

    BuffType debuffType = BuffType.getDebuffType(buffName);

    double timeVal = JavaUtil.getDouble(time, -1);
    int levelVal = JavaUtil.getInt(level, -1);

    if (debuffType == null || timeVal == -1 || levelVal == -1) { return null; }

    return BuffDataFactory.create(id, debuffType.getType(), timeVal, levelVal);
  }

  @Override
  public boolean hasSecoundSheet() {
    return true;
  }
}
