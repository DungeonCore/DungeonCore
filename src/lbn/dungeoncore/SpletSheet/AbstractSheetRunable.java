package lbn.dungeoncore.SpletSheet;

import java.util.HashMap;
import java.util.concurrent.Future;

import lbn.spread.api.LbnSpreadSheet;
import lbn.util.DungeonLogger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

/**
 * SpletSheetから読み込む処理だけを実装するための抽象クラス
 *
 */
public abstract class AbstractSheetRunable implements SheetRunnable<String[][]> {

  public AbstractSheetRunable(CommandSender p) {
    this.p = p;
  }

  static HashMap<Class<?>, Long> lastUpDate = new HashMap<Class<?>, Long>();

  public long getLastUpdate() {
    Long long1 = lastUpDate.get(this.getClass());
    if (long1 == null) {
      return -1;
    }
    return long1;
  }

  protected static boolean isTransaction = false;

  public boolean isTransaction() {
    return isTransaction;
  }

  public void closeTransaction() {
    isTransaction = false;
  }

  abstract protected String getQuery();

  CommandSender p;

  abstract public String getSheetName();

  abstract public String[] getTag();

  @Override
  public String[][] call() throws Exception {
    DungeonLogger.development("start read spread sheet:" + getSheetName());
    try {
      LbnSpreadSheet instance = LbnSpreadSheet.getInstance(getSheetName());
      if (instance == null) {
        return null;
      }
      if (getQuery() != null) {
        return instance.getAllDataByQuery(getTag(), getQuery());
      } else {
        return instance.getAllData(getTag());
      }
    } finally {
      closeTransaction();
      DungeonLogger.development("complate read spread sheet:" + getSheetName());
    }
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    try {
      String[][] allData = submit.get();
      if (allData == null) {
        p.sendMessage("内部でエラーが発生しました。" + getSheetName());
        return;
      }
      p.sendMessage("処理を開始します。:" + getSheetName());
      int i = 0;
      for (String[] row : allData) {
        i++;
        if (startRow() >= i) {
          continue;
        }
        excuteOnerow(row);
      }
      p.sendMessage("更新が完了しました。" + getSheetName());
      lastUpDate.put(getClass(), System.currentTimeMillis());
    } finally {
      isTransaction = false;
    }

  }

  protected int startRow() {
    return 1;
  }

  abstract protected void excuteOnerow(String[] row);

  protected void sendMessage(String msg) {
    // if (p instanceof ConsoleCommandSender) {
    // return;
    // }
    p.sendMessage(msg);
  }

  public static Location getLocationByString(String str) {
    if (str == null || str.isEmpty()) {
      return null;
    }
    try {
      String[] split = str.split(":");
      World w = Bukkit.getWorld(split[0]);

      String[] split2 = split[1].split(",");
      double x = Double.parseDouble(split2[0]);
      double y = Double.parseDouble(split2[1]);
      double z = Double.parseDouble(split2[2]);

      if (w == null) {
        return null;
      }

      return new Location(w, x, y, z);
    } catch (Exception e) {
      return null;
    }
  }
}
