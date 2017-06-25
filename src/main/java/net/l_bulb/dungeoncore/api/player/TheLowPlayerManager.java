package net.l_bulb.dungeoncore.api.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import net.l_bulb.dungeoncore.common.event.player.PlayerLoadedDataEvent;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.player.customplayer.CustomPlayer;
import net.l_bulb.dungeoncore.util.InOutputUtil;

import net.md_5.bungee.api.ChatColor;

public class TheLowPlayerManager {
  static ConcurrentHashMap<UUID, TheLowPlayer> loadedPlayerMap = new ConcurrentHashMap<>();

  static Set<OfflinePlayer> loadingNow = Collections.synchronizedSet(new HashSet<OfflinePlayer>());

  public static void loadData(OfflinePlayer p) throws Exception {
    // もし情報取得中なら無視する
    if (loadingNow.contains(p)) { return; }

    // すでにロードされているなら何もしない
    if (loadedPlayerMap.containsKey(p.getUniqueId())) { return; }

    // TODO ロードする
    CustomPlayer customPlayer = loadFile(p);
    customPlayer.init(p);
    // eventを発火させる
    new PlayerLoadedDataEvent(customPlayer, p).callEvent();
    loadedPlayerMap.put(p.getUniqueId(), customPlayer);
  }

  public static void saveData(OfflinePlayer p) {
    try {
      TheLowPlayer theLowPlayer = getTheLowPlayer(p);
      if (theLowPlayer == null) { return; }
      saveFile((CustomPlayer) theLowPlayer);
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * TheLowPlayerに処理を行う。
   * データがロードされていなければロードされた後に指定した処理を行う。
   *
   * @param p
   * @param consumer
   */
  public static void consume(Player p, Consumer<TheLowPlayer> consumer) {
    try {
      if (!isLoaded(p)) {
        loadData(p);
      }
      TheLowPlayer theLowPlayer = getTheLowPlayer(p);
      consumer.accept(theLowPlayer);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * ファイルから読み込む
   *
   * @param p
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static CustomPlayer loadFile(OfflinePlayer p) throws IOException, ClassNotFoundException {
    try {
      UUID uniqueId = p.getUniqueId();
      String folder = Main.dataFolder + File.separator + "player_data" + File.separator;
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(folder + uniqueId + ".dat")))) {
        return (CustomPlayer) ois.readObject();
      }
    } catch (FileNotFoundException e) {
      return new CustomPlayer(p);
    }
  }

  /**
   * ファイルへ出力する
   *
   * @param p
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static void saveFile(CustomPlayer p) throws IOException, ClassNotFoundException {
    String folder = Main.dataFolder + File.separator + "player_data" + File.separator;
    File file = new File(folder + p.getUUID() + ".dat");
    if (file.exists()) {
      String backup = Main.dataFolder + File.separator + "player_data_old" + File.separator;
      InOutputUtil.moveFile(file, new File(backup + p.getUUID() + "_" + (new Date().getTime()) + ".dat"));
    }

    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }

    try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(file))) {
      ois.writeObject(p);
    }
  }

  /**
   * THELoW Playerを取得, ロードされていない場合はNullを返す
   *
   * @param p
   * @return
   */
  public static TheLowPlayer getTheLowPlayer(OfflinePlayer p) {
    return getTheLowPlayer(p.getUniqueId());
  }

  /**
   * THELoW Playerを取得, ロードされていない場合はNullを返す
   *
   * @param id
   * @return
   */
  public static TheLowPlayer getTheLowPlayer(UUID id) {
    return loadedPlayerMap.get(id);
  }

  /**
   * THELoW Playerを取得, ロードされていない場合はNullを返す
   *
   * @param id
   * @return
   */
  public static TheLowPlayer getTheLowPlayer(PlayerEvent event) {
    return getTheLowPlayer(event.getPlayer());
  }

  /**
   * ロードしていたらTRUE
   *
   * @return
   */
  public static boolean isLoaded(OfflinePlayer p) {
    return loadedPlayerMap.containsKey(p);
  }

  /**
   * ロード中のメッセージを表示
   *
   * @param p
   */
  public static void sendLoingingMessage(Player p) {
    p.sendMessage(ChatColor.RED + "現在データをロード中です。もう暫くお待ち下さい。");
  }
}
