package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import net.l_bulb.dungeoncore.util.DungeonLogger;

import lbn.spread.api.LbnSpreadSheet;

/**
 * SpletSheetから読み込む処理だけを実装するための抽象クラス
 *
 */
public abstract class AbstractSheetRunable implements SheetRunnable<String[][]> {

  public AbstractSheetRunable(CommandSender sender) {
    this.sender = sender;
  }

  static HashMap<Class<?>, Long> lastUpDate = new HashMap<>();

  public long getLastUpdate() {
    Long long1 = lastUpDate.get(this.getClass());
    if (long1 == null) { return -1; }
    return long1;
  }

  protected static boolean isTransaction = false;

  @Override
  public boolean isTransaction() {
    return isTransaction;
  }

  @Override
  public void closeTransaction() {
    isTransaction = false;
  }

  abstract protected String getQuery();

  CommandSender sender;

  @Override
  abstract public String getSheetName();

  @Override
  abstract public String[] getTag();

  @Override
  public String[][] call() throws Exception {
    DungeonLogger.development("start read spread sheet:" + getSheetName());
    try {
      ArrayList<String[]> dataList = new ArrayList<>();

      for (LbnSpreadSheet instance : getInstanceList()) {
        if (instance == null) {
          continue;
        }

        String[][] data = null;
        if (getQuery() != null) {
          data = instance.getAllDataByQuery(getTag(), getQuery());
        } else {
          data = instance.getAllData(getTag());
        }

        dataList.addAll(Arrays.asList(data));
      }
      return dataList.toArray(new String[0][]);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      closeTransaction();
      DungeonLogger.development("complate read spread sheet:" + getSheetName());
    }
  }

  /**
   * スプレットシート接続のセッション用インスタンスを作成する
   *
   * @return
   * @throws Exception
   */
  private List<LbnSpreadSheet> getInstanceList() throws Exception {
    ArrayList<LbnSpreadSheet> instanceList = new ArrayList<>();
    instanceList.add(LbnSpreadSheet.getInstance(getSheetName()));
    // 他の参照先がある場合はそちらのインスタンスも作成する
    if (hasSecoundSheet()) {
      instanceList.add(LbnSpreadSheet.getInstance(THE_LOW_SHEET2, getSheetName()));
    }
    return instanceList;
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    try {
      String[][] allData = submit.get();
      if (allData == null) {
        // sender.sendMessage("内部でエラーが発生しました。" + getSheetName());
        return;
      }
      sender.sendMessage("処理を開始します。:" + getSheetName());
      int i = 0;
      for (String[] row : allData) {
        i++;
        if (startRow() >= i) {
          continue;
        }
        try {
          excuteOnerow(row);
        } catch (Exception e) {
          DungeonLogger.error("row:" + Arrays.toString(row));
          e.printStackTrace();
        }
      }
      sender.sendMessage("更新が完了しました。" + getSheetName() + ": " + allData.length + "件");
      lastUpDate.put(getClass(), System.currentTimeMillis());
    } finally {
      isTransaction = false;
    }

  }

  protected int startRow() {
    return 1;
  }

  @Override
  public boolean hasSecoundSheet() {
    return false;
  }

  abstract protected void excuteOnerow(String[] row);

  protected void sendMessage(String msg) {
    if (sender instanceof ConsoleCommandSender) { return; }
    sender.sendMessage(msg);
  }

  public static Location getLocationByString(String str) {
    if (str == null || str.isEmpty()) { return null; }
    try {
      String[] split = str.split(":");
      World w = Bukkit.getWorld(split[0]);

      String[] split2 = split[1].split(",");
      double x = Double.parseDouble(split2[0]);
      double y = Double.parseDouble(split2[1]);
      double z = Double.parseDouble(split2[2]);

      if (w == null) { return null; }

      return new Location(w, x, y, z);
    } catch (Exception e) {
      return null;
    }
  }
}
