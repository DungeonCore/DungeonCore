package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.concurrent.Future;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.command.CommandGetItem;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.SpreadSheetKeyCommandBlockExecuteItem;
import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.SpreadSheetKeyTpItem;
import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.SpreadSheetMaterialItem;
import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.SpreadSheetOtherItem;
import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.SpreadSheetQuestItem;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class ItemSheetRunnable extends AbstractSheetRunable {

  public ItemSheetRunnable(CommandSender p) {
    super(p);
  }

  public ItemSheetRunnable(CommandSender p, String itemId) {
    super(p);
    query = "id=" + itemId;
  }

  String query = "id!=\"\"";

  @Override
  protected String getQuery() {
    return query;
  }

  @Override
  public String getSheetName() {
    return "item";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "name", "command", "type", "data", "price", "dungeonname", "dungeonlocation", "detail" };
  }

  @Override
  protected void excuteOnerow(String[] row) {
    try {
      String id = row[0];
      if (id == null || id.isEmpty()) {
        sendMessage("idは必須です。");
        return;
      }
      String name = row[1];
      if (name == null || name.isEmpty()) {
        sendMessage("nameは必須です。");
        return;
      }

      String command = row[2];

      ItemStack itemByCommand = ItemStackUtil.getItemStackByCommand(command, sender);
      if (itemByCommand == null || itemByCommand.getType() == Material.AIR) {
        sendMessage(MessageFormat.format("commandが不正です。 コマンド:{0}, itemid:{1}", command, id));
        return;
      }

      String data = row[4];
      int price = JavaUtil.getInt(row[5], 0);

      String dungeonName = row[6];

      Location dungeonLoc = getLocationByString(row[7]);

      String detail = row[8];

      // アイテムを生成
      ItemInterface item;
      if (row[3] != null) {
        if (row[3].startsWith("1.")) {
          item = new SpreadSheetKeyCommandBlockExecuteItem(name, id, price, command, dungeonName, dungeonLoc);
        } else if (row[3].startsWith("2.")) {
          Location tpLoc = getLocationByString(data);
          if (tpLoc == null) {
            sendMessage("TP先の座標が不正です。(dataで指定してください)");
            return;
          }
          item = new SpreadSheetKeyTpItem(name, id, price, command, dungeonName, dungeonLoc, tpLoc);
        } else if (row[3].startsWith("3.")) {
          item = new SpreadSheetQuestItem(name, id, price, command, detail);
        } else if (row[3].startsWith("5.")) {
          item = new SpreadSheetMaterialItem(name, id, price, command, detail);
        } else {
          item = new SpreadSheetOtherItem(name, id, price, command, detail);
        }
      } else {
        sendMessage("Typeを選択していないため自動的に「その他」に設定されました。:" + Arrays.toString(row));
        item = new SpreadSheetOtherItem(name, id, price, command, detail);
      }
      ItemManager.registItem(item);

    } catch (Exception e) {
      sendMessage("エラーが発生したため登録出来ませんでした。:" + Arrays.toString(row));
      e.printStackTrace();
    }
  }

  @Override
  protected int startRow() {
    return super.startRow();
  }

  @Override
  public boolean hasSecoundSheet() {
    return true;
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    super.onCallbackFunction(submit);

    CommandGetItem.initFlg = true;
  }
}
