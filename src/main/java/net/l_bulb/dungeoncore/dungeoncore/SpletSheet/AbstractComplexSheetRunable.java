package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import lbn.spread.api.LbnSpreadSheet;
import net.l_bulb.dungeoncore.util.DungeonLogger;

/**
 * SpletSheetから読み込みと編集の処理を実装するための抽象クラス
 *
 */
public abstract class AbstractComplexSheetRunable implements SheetRunnable<String[][]> {
  ArrayList<Task> taskList = new ArrayList<>();

  public static final int TASK_KIND_ADD = 1;
  public static final int TASK_KIND_DELETE = 2;
  public static final int TASK_KIND_GET = 3;
  public static final int TASK_KIND_UPDATE = 4;

  static HashMap<Class<?>, Long> lastUpDate = new HashMap<>();

  public long getLastUpdate() {
    Long long1 = lastUpDate.get(this.getClass());
    if (long1 == null) { return -1; }
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

  boolean getDataFlg = false;

  protected static boolean isTransaction = false;

  @Override
  public boolean isTransaction() {
    return isTransaction;
  }

  @Override
  public void closeTransaction() {
    isTransaction = false;
  }

  CommandSender sender;

  @Override
  abstract public String getSheetName();

  @Override
  abstract public String[] getTag();

  @Override
  public String[][] call() throws Exception {
    isTransaction = true;
    try {
      ArrayList<String[]> arrayList = new ArrayList<>();

      for (LbnSpreadSheet instance : getInstanceList()) {
        String[][] rtn = new String[0][0];
        if (instance == null) { return null; }
        for (Task task : taskList) {
          rtn = execute(instance, rtn, task);
        }
        arrayList.addAll(Arrays.asList(rtn));
      }
      return arrayList.toArray(new String[0][]);
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
      if (getDataFlg) {
        String[][] allData = submit.get();
        if (allData == null || allData.length == 0) {
          sender.sendMessage("結果を返しませんでした。:" + getSheetName());
          return;
        }

        sender.sendMessage("処理を開始します。:" + getSheetName());
        for (String[] row : allData) {
          try {
            excuteOnerow(row);
          } catch (Exception e) {
            sendMessage("[" + getSheetName() + "]" + StringUtils.join(row) + "でエラーがありました。");
            e.printStackTrace();
          }
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

  protected void sendMessage(String msg) {
    if (sender instanceof ConsoleCommandSender) { return; }
    sender.sendMessage(msg);
  }

  public static String getLocationString(Location loc) {
    if (loc == null) { return ""; }
    return StringUtils.join(new Object[] { loc.getWorld().getName(), ":", loc.getBlockX(), ",", loc.getBlockY(), ",", loc.getBlockZ() });
  }

  public static Location getLocationByString(String str) {
    try {
      if (str == null) { return null; }

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

  @Override
  public boolean hasSecoundSheet() {
    return false;
  }

  abstract protected void excuteOnerow(String[] row);

}

class Task {
  int kind;
  HashMap<String, Object> map;
  String query;
}
