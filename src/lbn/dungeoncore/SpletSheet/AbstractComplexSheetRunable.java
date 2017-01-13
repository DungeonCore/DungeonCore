package lbn.dungeoncore.SpletSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

import lbn.spread.api.LbnSpreadSheet;
import lbn.util.DungeonLogger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

/**
 * SpletSheetから読み込みと編集の処理を実装するための抽象クラス
 *
 */
public abstract class AbstractComplexSheetRunable implements SheetRunnable<String[][]> {
  ArrayList<Task>                taskList         = new ArrayList<Task>();

  public static final int        TASK_KIND_ADD    = 1;
  public static final int        TASK_KIND_DELETE = 2;
  public static final int        TASK_KIND_GET    = 3;
  public static final int        TASK_KIND_UPDATE = 4;

  static HashMap<Class<?>, Long> lastUpDate       = new HashMap<Class<?>, Long>();

  public long getLastUpdate() {
    Long long1 = lastUpDate.get(this.getClass());
    if (long1 == null) {
      return -1;
    }
    return long1;
  }

  /**
   * データ一覧を取得するためのコンストラクタ
   *
   * @param sender
   */
  public AbstractComplexSheetRunable(CommandSender sender) {
    this.sender = sender;
  }

  public void addData(HashMap<String, Object> map) {
    Task task = new Task();
    task.kind = TASK_KIND_ADD;
    task.map = map;
    taskList.add(task);
  }

  public void deleteData(String query) {
    Task task = new Task();
    task.kind = TASK_KIND_DELETE;
    task.query = query;
    taskList.add(task);
  }

  public void getData(String query) {
    Task task = new Task();
    task.kind = TASK_KIND_GET;
    task.query = query;
    taskList.add(task);
    getDataFlg = true;
  }

  public void updateData(HashMap<String, Object> map, String query) {
    Task task = new Task();
    task.kind = TASK_KIND_UPDATE;
    task.query = query;
    taskList.add(task);
    task.map = map;
    getDataFlg = true;
  }

  boolean                  getDataFlg    = false;

  protected static boolean isTransaction = false;

  public boolean isTransaction() {
    return isTransaction;
  }

  public void closeTransaction() {
    isTransaction = false;
  }

  CommandSender sender;

  abstract public String getSheetName();

  abstract public String[] getTag();

  @Override
  public String[][] call() throws Exception {
    LbnSpreadSheet instance = LbnSpreadSheet.getInstance(getSheetName());
    if (instance == null) {
      return null;
    }
    isTransaction = true;
    try {
      String[][] rtn = new String[0][0];
      for (Task task : taskList) {
        rtn = execute(instance, rtn, task);
      }
      return rtn;
    } finally {
      isTransaction = false;
    }

  }

  protected String[][] execute(LbnSpreadSheet instance, String[][] rtn, Task task) throws IOException, Exception {
    DungeonLogger.development("start read spread sheet:" + getSheetName());
    try {
      switch (task.kind) {
        case TASK_KIND_GET:
          if (task.query != null) {
            rtn = instance.getAllDataByQuery(getTag(), task.query);
          } else {
            rtn = instance.getAllData(getTag());
          }
          break;
        case TASK_KIND_DELETE:
          instance.deleteDataRow(task.query);
          break;
        case TASK_KIND_ADD:
          instance.addDataRow(task.map);
          break;
        case TASK_KIND_UPDATE:
          instance.updateDataRow(task.map, task.query);
          ;
          break;
        default:
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    DungeonLogger.development("end read spread sheet:" + getSheetName());
    return rtn;
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    try {
      if (getDataFlg) {
        String[][] allData = submit.get();
        if (allData == null || allData.length == 0) {
          sender.sendMessage("結果を返しませんでした。:" + getSheetName());
          return;
        }

        sender.sendMessage("処理を開始します。:" + getSheetName());
        for (String[] row : allData) {
          excuteOnerow(row);
        }
        sender.sendMessage(allData.length + "行の更新が完了しました。:" + getSheetName());
      } else {
        sender.sendMessage("更新が完了しました:" + getSheetName());
      }
      lastUpDate.put(getClass(), System.currentTimeMillis());
    } finally {
      isTransaction = false;
    }

  }

  public static String getLocationString(Location loc) {
    if (loc == null) {
      return "";
    }
    return StringUtils.join(
        new Object[] { loc.getWorld().getName(), ":", loc.getBlockX(), ",", loc.getBlockY(), ",", loc.getBlockZ() });
  }

  public static Location getLocationByString(String str) {
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


  abstract protected void excuteOnerow(String[] row);

}

class Task {
  int                     kind;
  HashMap<String, Object> map;
  String                  query;
}
