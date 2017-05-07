package lbn.dungeoncore.SpletSheet;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import lbn.item.ItemManager;
import lbn.item.customItem.SpreadSheetItem.FoodItemData;
import lbn.item.customItem.SpreadSheetItem.SpreadSheetFoodItem;
import lbn.util.JavaUtil;

public class FoodSheetRunnable extends AbstractSheetRunable {

  public FoodSheetRunnable(CommandSender sender) {
    super(sender);
  }

  @Override
  protected String getQuery() {
    return null;
  }

  @Override
  public String getSheetName() {
    return "food";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "name", "command", "buff1", "buff2", "buff3", "sound", "particle", "price", "swordexp", "bowexp", "magicexp",
        "detail", "mp" };
  }

  @Override
  protected void excuteOnerow(String[] row) {
    try {
      FoodItemData foodItemData = new FoodItemData(row[0]);
      foodItemData.setName(row[1]);
      foodItemData.setCommand(row[2]);
      foodItemData.setBuff1(row[3]);
      foodItemData.setBuff2(row[4]);
      foodItemData.setBuff3(row[5]);
      foodItemData.setSound(row[6]);
      foodItemData.setParticle(row[7]);
      foodItemData.setPrice(JavaUtil.getInt(row[8], 0));
      foodItemData.setSwordExp(JavaUtil.getInt(row[9], 0));
      foodItemData.setBowExp(JavaUtil.getInt(row[10], 0));
      foodItemData.setMagicExp(JavaUtil.getInt(row[11], 0));
      foodItemData.setDetail(row[12]);
      foodItemData.setRecoveryMp(JavaUtil.getInt(row[10], 0));
      SpreadSheetFoodItem spreadSheetFoodItem = new SpreadSheetFoodItem(foodItemData);
      ItemManager.registItem(spreadSheetFoodItem);
    } catch (Exception e) {
      sendMessage("入力データが不正です：" + Arrays.toString(row));
    }

  }

}
