package net.l_bulb.dungeoncore.chest.wireless;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.l_bulb.dungeoncore.common.other.SystemLog;
import net.l_bulb.dungeoncore.util.DungeonLogger;
import net.l_bulb.dungeoncore.util.InOutputUtil;

import com.google.common.collect.HashMultimap;
import com.google.common.reflect.TypeToken;

public enum WireLessChestManager {
  INSTANCE;

  public static WireLessChestManager getInstance() {
    return WireLessChestManager.INSTANCE;
  }

  /** key=player_id, val = chest */
  HashMultimap<UUID, PersonalChestData> create = HashMultimap.create();

  /**
   * チェストの座標を取得, 存在しない場合はnull
   *
   * @param p
   * @param type
   * @return
   */
  public Location getChestContentsLocation(Player p, RepositoryType type) {
    Set<PersonalChestData> set = create.get(p.getUniqueId());
    if (set != null) {
      for (PersonalChestData personalChestData : set) {
        if (personalChestData.type.equals(type.getType())) { return personalChestData.getLocation(); }
      }
    }
    return null;
  }

  /**
   * 倉庫のインベントリを取得する
   *
   * @param p
   * @param type
   * @return
   */
  public Inventory getRepositoryInventory(Player p, RepositoryType type) {
    Location containsLocation = getChestContentsLocation(p, type);
    if (containsLocation == null) { return null; }

    if (containsLocation.getBlock().getState() instanceof Chest) {
      Chest c = (Chest) containsLocation.getBlock().getState();
      return c.getInventory();
    }
    return null;
  }

  /**
   * チェストが作成されていたらTRUE
   *
   * @param p
   * @param type
   * @return
   */
  public boolean exist(Player p, RepositoryType type) {
    Set<PersonalChestData> set = create.get(p.getUniqueId());
    if (set != null) {
      for (PersonalChestData personalChestData : set) {
        if (personalChestData.type.equals(type.getType())) { return true; }
      }
    }
    return false;
  }

  public static final World chestWorld = Bukkit.getWorld("chest");

  transient int startX = -1000;
  transient int startY = 2;
  transient int startZ = -1000;

  int nowX = startX;
  int nowY = startY;
  int nowZ = startZ;

  /**
   * チェストを作成する → x ↓ cc cc cc cc cc
   *
   * z cc cc cc cc cc
   *
   * cc cc cc cc cc
   *
   */
  public Location createChest(Player p, RepositoryType type) {
    incrementPotin();

    saveManageData();

    Location location = new Location(chestWorld, nowX, nowY, nowZ);
    location.getBlock().setType(Material.CHEST);
    location.clone().add(1, 0, 0).getBlock().setType(Material.CHEST);

    // Logに残す
    SystemLog.addLog(p.getName() + "(" + p.getUniqueId() + ")がチェストを購入しました。(" + nowX + "," + nowY + "," + nowZ + ")");

    PersonalChestData personalChestData = new PersonalChestData(p, location, type);
    create.put(p.getUniqueId(), personalChestData);
    WireLessChestManager.getInstance().save(p);
    return location;
  }

  /**
   * チェストの座標を更新する
   */
  public void incrementPotin() {
    nowY++;
    if (nowY > 210) {
      nowY = startY;
      nowX += 3;
      if (nowX > 16) {
        nowX = startX;
        nowZ += 2;
      }
    }
  }

  /**
   * ファイルを保存する
   *
   * @param p
   */
  public void save(Player p) {
    String json = "";
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      Type type = new TypeToken<Set<PersonalChestData>>() {
        private static final long serialVersionUID = -453957688834487594L;
      }.getType();
      json = gson.toJson(create.get(p.getUniqueId()), type);
      if (!InOutputUtil.write(json, "chest" + File.separator + p.getUniqueId() + ".txt")) { throw new RuntimeException("IO ERROE"); }
    } catch (Exception e) {
      DungeonLogger.error("===========Chest保存中にエラーが発生しました。:start=============");
      DungeonLogger.error("ファイル名：" + "chest" + File.separator + p.getUniqueId() + ".txt");
      DungeonLogger.error(json);
      DungeonLogger.error("===========Chest保存中にエラーが発生しました。:end=============");
      e.printStackTrace();
    }
  }

  public void saveManageData() {
    String json = "";
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      json = gson.toJson(Arrays.asList(nowX, nowY, nowZ));
      if (!InOutputUtil.write(json, "chest_world_manage.txt")) { throw new RuntimeException("IO ERROE"); }
    } catch (Exception e) {
      DungeonLogger.error("===========Chest保存中にエラーが発生しました。:start=============");
      DungeonLogger.error("ファイル名：" + "chest_world_manage.txt");
      DungeonLogger.error(json);
      DungeonLogger.error("===========Chest保存中にエラーが発生しました。:end=============");
      e.printStackTrace();
    }
  }

  public void loadManageData() {
    try {
      ArrayList<String> readFile = InOutputUtil.readFile("chest_world_manage.txt");
      // ファイルが存在しない時は何もしない
      if (readFile.isEmpty()) { return; }
      StringBuilder sb = new StringBuilder();
      for (String string : readFile) {
        sb.append(string);
      }
      Gson gson = new Gson();
      @SuppressWarnings("unchecked")
      ArrayList<Double> fromJson = gson.fromJson(sb.toString(), ArrayList.class);
      nowX = (int) (double) fromJson.get(0);
      nowY = (int) (double) fromJson.get(1);
      nowZ = (int) (double) fromJson.get(2);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * ファイルを呼び出す
   *
   * @param p
   * @throws IOException
   */
  public void load(Player p) throws IOException {
    ArrayList<String> readFile = InOutputUtil.readFile("chest" + File.separator + p.getUniqueId() + ".txt");
    // ファイルが存在しない時は何もしない
    if (readFile.isEmpty()) { return; }
    StringBuilder sb = new StringBuilder();
    for (String string : readFile) {
      sb.append(string);
    }
    Gson gson = new Gson();
    Type collectionType = new TypeToken<Set<PersonalChestData>>() {
      private static final long serialVersionUID = 4621218509866202794L;
    }.getType();
    gson.fromJson(sb.toString(), collectionType);
    Set<PersonalChestData> fromJson = gson.fromJson(sb.toString(), collectionType);
    for (PersonalChestData personalChestData : fromJson) {
      create.put(p.getUniqueId(), personalChestData);
    }
  }
}
