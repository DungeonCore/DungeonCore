package lbn.mobspawn.point;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import lbn.dungeoncore.LbnRuntimeException;
import lbn.dungeoncore.Main;
import lbn.dungeoncore.SpletSheet.SpawnPointSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.mobspawn.ChunkWrapper;
import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.SpawnPointMonitor;
import lbn.util.DungeonLogger;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

import com.google.common.collect.HashMultimap;

public class MobSpawnerPointManager {
  static HashMultimap<ChunkWrapper, MobSpawnerPoint> spawnPointListAndChunk = HashMultimap.create();
  
  static HashMap<Integer, MobSpawnerPoint>           mobSpawnerPointSerial  = new HashMap<>();
  
  static HashMap<SpawnLevel, SpawnScheduler>         schedulerList          = new HashMap<>();
  
  public static HashSet<ChunkWrapper>                loadedChunk            = new HashSet<ChunkWrapper>();
  
  static int                                         spletSheetId           = 0;
  
  public static String                               ignoreSpawnWorld       = null;
  
  public static void addSpawnPoint(MobSpawnerPoint spawnerPoint) {
    if (spawnerPoint.getChunk().getWorld().getName().equalsIgnoreCase(ignoreSpawnWorld)) {
      return;
    }
    
    if (mobSpawnerPointSerial.containsKey(spawnerPoint.getId())) {
      new LbnRuntimeException("monspawnlist id がかぶっています:" + spawnerPoint.getId()).printStackTrace();
      return;
    }
    
    // 召喚するモンスターがいない時は何もしない
    if (spawnerPoint.getSpawnMobGetter().getAllMobList().isEmpty()) {
      // スプレットシートの場合はIDを記録しておく
      if (spawnerPoint instanceof SpletSheetMobSpawnerPoint) {
        spletSheetId = Math.max(spletSheetId, spawnerPoint.getId());
      }
      return;
    }
    
    ChunkWrapper chunkWrapper = new ChunkWrapper(spawnerPoint);
    spawnPointListAndChunk.put(chunkWrapper, spawnerPoint);
    
    int id = spawnerPoint.getId();
    
    // スプレットシートの場合はIDを記録しておく
    if (spawnerPoint instanceof SpletSheetMobSpawnerPoint) {
      spletSheetId = Math.max(spletSheetId, spawnerPoint.getId());
    }
    
    mobSpawnerPointSerial.put(id, spawnerPoint);
    
    // チャンクがロードされているならLoadChunkを呼び出す
    if (spawnerPoint.getChunk().isLoaded()) {
      loadChunk(spawnerPoint.getChunk());
    }
  }
  
  public static HashMap<SpawnLevel, SpawnScheduler> getSchedulerList() {
    return schedulerList;
  }
  
  public static int getNextId() {
    return spletSheetId + 1;
  }
  
  /**
   * 全てのスポーンポイントを取得
   *
   * @return
   */
  public static Collection<MobSpawnerPoint> getAllSpawnerPointList() {
    return spawnPointListAndChunk.values();
  }
  
  /**
   * 指定されたチャンクに存在するスポーンポイントを取得
   *
   * @param c
   * @return
   */
  public static Collection<MobSpawnerPoint> getSpawnerPointListByChunk(Chunk c) {
    Set<MobSpawnerPoint> set = spawnPointListAndChunk.get(new ChunkWrapper(c));
    if (set.size() == 0) {
      return null;
    }
    return set;
  }
  
  /**
   * 現在ロードされているチャンクに存在するスポーンポイントを取得
   *
   * @return
   */
  public static Collection<MobSpawnerPoint> getSpawnerPointListByLoadedChunk() {
    HashSet<MobSpawnerPoint> rtn = new HashSet<MobSpawnerPoint>();
    for (ChunkWrapper chunk : loadedChunk) {
      rtn.addAll(spawnPointListAndChunk.get(chunk));
    }
    return rtn;
  }
  
  public static MobSpawnerPoint getSpawnerPointbySerialNumber(int i) {
    return mobSpawnerPointSerial.get(i);
  }
  
  public static void clear() {
    spawnPointListAndChunk.clear();
    mobSpawnerPointSerial.clear();
    schedulerList.clear();
    loadedChunk.clear();
  }
  
  public static void remove(MobSpawnerPoint spawnerPoint) {
    // chunkとの結びつきを削除
    Chunk chunk = spawnerPoint.getChunk();
    ChunkWrapper chunkWrapper = new ChunkWrapper(chunk);
    spawnPointListAndChunk.remove(chunkWrapper, spawnerPoint);
    
    // シリアルを削除
    mobSpawnerPointSerial.remove(spawnerPoint);
    
    if (loadedChunk.contains(chunkWrapper)) {
      loadChunk(chunk);
    }
    
    // スケジューラーから削除
    SpawnScheduler spawnScheduler = schedulerList.get(spawnerPoint.getSpawnLevel());
    if (spawnScheduler != null) {
      spawnScheduler.removeSpawnPoint(true, spawnerPoint);
    }
  }
  
  /**
   * チャンクがロードされた時の処理
   *
   * @param c
   */
  public static void loadChunk(Chunk c) {
    // スポーンポイントがないチャンクなら何もしない
    ChunkWrapper chunkWrapper = new ChunkWrapper(c);
    if (!spawnPointListAndChunk.containsKey(chunkWrapper)) {
      return;
    }
    loadedChunk.add(chunkWrapper);
    
    // 指定されたチャンクにあるスポーンポイントを全て取得し、スケジューラーにセットする
    Set<MobSpawnerPoint> spawnPoint = spawnPointListAndChunk.get(new ChunkWrapper(c));
    for (MobSpawnerPoint mobSpawnerPoint : spawnPoint) {
      SpawnScheduler spawnScheduler = schedulerList.get(mobSpawnerPoint.getSpawnLevel());
      if (spawnScheduler == null) {
        spawnScheduler = new SpawnScheduler(mobSpawnerPoint.getSpawnLevel());
      }
      spawnScheduler.addSpawnPoint(mobSpawnerPoint);
      schedulerList.put(mobSpawnerPoint.getSpawnLevel(), spawnScheduler);
    }
  }
  
  public static void unloadChunk(Chunk c) {
    // スポーンポイントがないチャンクなら何もしない
    ChunkWrapper chunkWrapper = new ChunkWrapper(c);
    if (!spawnPointListAndChunk.containsKey(chunkWrapper)) {
      return;
    }
    loadedChunk.remove(chunkWrapper);
    
    // スケジューラーから削除する
    Set<MobSpawnerPoint> spawnPoint = spawnPointListAndChunk.get(new ChunkWrapper(c));
    for (MobSpawnerPoint mobSpawnPointInterface : spawnPoint) {
      SpawnScheduler spawnScheduler = schedulerList.get(mobSpawnPointInterface.getSpawnLevel());
      spawnScheduler.removeSpawnPoint(false, mobSpawnPointInterface);
    }
  }
  
  public static boolean isRunSpawnManage() {
    return runnable != null;
  }
  
  public static void startSpawnManage() {
    if (runnable == null) {
      DungeonLogger.info("==START SPAWN SYSTEM==");
      runnable = new SpawnRunnable(true, schedulerList, loadedChunk);
      ;
      runnable.runTaskTimer(Main.plugin, 1, 1);
    }
  }
  
  static SpawnRunnable runnable = null;
  
  public static HashMap<SpawnLevel, String> getSpawnDetail() {
    if (runnable == null) {
      return null;
    }
    
    return runnable.schedulerDetail;
  }
  
  
  public static void stopSpawnManage() {
    // ずっと動かすため一旦コメントアウト
    // isRunManage = false;
  }
  
  public static boolean load() {
    DungeonLogger.info("spawn pointをLoadします");
    clear();
    try {
      SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(Bukkit.getConsoleSender());
      spawnPointSheetRunnable.getData(null);
      
      SpletSheetExecutor.onExecute(spawnPointSheetRunnable);
      
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      loadSuccess = false;
      return false;
    }
  }
  
  static boolean loadSuccess = true;
  
  public static void onBrakeSponge(BlockBreakEvent e) {
    Block block = e.getBlock();
    if (block.getType() != Material.SPONGE) {
      return;
    }
    
    if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
      return;
    }
    Chunk chunk = block.getChunk();
    Collection<MobSpawnerPoint> spawnerPointListByChunk = getSpawnerPointListByChunk(chunk);
    if (spawnerPointListByChunk == null) {
      return;
    }
    
    for (MobSpawnerPoint point : spawnerPointListByChunk) {
      if (point.getLocation().equals(block.getLocation())) {
        SpawnScheduler spawnScheduler = MobSpawnerPointManager.getSchedulerList().get(point.getLevel());
        SpawnPointMonitor monitor = spawnScheduler.getMonitor(point);
        monitor.send(e.getPlayer());
        e.setCancelled(true);
      }
    }
  }
}

