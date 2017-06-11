package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.mobspawn.SpawnPointFactory;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointGroupFactory;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData.TargetType;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class SpawnPointSheetRunnable extends AbstractComplexSheetRunable {

  public SpawnPointSheetRunnable(CommandSender p) {
    super(p);
  }

  public void addData(SpawnPointSpreadSheetData point, String[] array, String memo) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("id", point.getId());
    Location location = point.getLocation();
    map.put("world", location.getWorld().getName());
    map.put("x", location.getX());
    map.put("y", location.getY());
    map.put("z", location.getZ());
    map.put("最大湧き数", point.getMaxSpawnCount());
    map.put("type", point.getType());

    IntStream.range(0, array.length).filter(i -> array[i] != null && array[i].length() != 0)
        .forEach(i -> map.put("mob" + (i + 1), array[i]));
    if (memo != null && !memo.isEmpty()) {
      map.put("memo", memo);
    }
    addData(map);
  }

  @Override
  public String getSheetName() {
    return "spawnpoint";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "world", "x", "y", "z", "最大湧き数", "type", "dungeonhight", "looknearchunk",// 8
        "mob1", "mob2", "mob3", "mob4", "mob5" };
  }

  @Override
  protected void excuteOnerow(String[] row) {
    try {
      SpawnPointSpreadSheetData data = new SpawnPointSpreadSheetData();
      data.setId(Integer.parseInt(row[0]));

      World world = Bukkit.getWorld(row[1]);
      if (world == null) { return; }

      data.setLocation(new Location(world, Integer.parseInt(row[2]), Integer.parseInt(row[3]), Integer.parseInt(row[4])));
      data.setMaxSpawnCount(Integer.parseInt(row[5]));
      data.setType(TargetType.getType(row[6]));
      data.setDungeonHight((int) JavaUtil.getDouble(row[7], 8));
      data.setLookNearChunk(JavaUtil.getBoolean(row[8], TargetType.valueOf(row[6]).isCheckNearChunk()));

      // スポーンポイントを登録する
      IntStream.rangeClosed(9, 13)
          .filter(i -> row[i] != null && !row[i].isEmpty())
          .mapToObj(i -> new SpawnPointSpreadSheetData(data, row[i]))
          .map(SpawnPointFactory::getNewInstance)
          .filter(d -> d != null)
          .forEach(SpawnPointGroupFactory::registSpawnPoint);
    } catch (Exception e) {
      e.printStackTrace();
      sendMessage("入力されたデータが不正です。(spawn mob 設定 ID:" + row[0] + ")");
    }
  }

  @Override
  public void getData(String query) {
    if (query == null || query.isEmpty()) {
      query = "world!=\"\"";
    }
    super.getData(query);
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    SpawnPointGroupFactory.clear();
    super.onCallbackFunction(submit);
  }

}