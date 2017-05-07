package lbn.player.magicstoneOre;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;

public class MagicStoneFactor {

  private static HashMap<Location, MagicStoneOreType> magicStoneOres = new HashMap<>();

  static HashMap<Location, RelocationOreRunnable> magicStoneRelocationScheduler = new HashMap<>();

  /**
   * 鉱石ブロックの情報を送る
   * 
   * @param p
   * @param loc
   */
  public static void sendOreSchedulerInfo(Player p, Location loc) {
    MagicStoneOreType magicStoneOreType = magicStoneOres.get(loc);
    // 鉱石情報がないなら何もしない
    if (magicStoneOreType == null) { return; }

    // 残り復活時間
    long remaindTime = -2;
    RelocationOreRunnable relocationOreRunnable = magicStoneRelocationScheduler.get(loc);
    if (relocationOreRunnable != null) {
      remaindTime = relocationOreRunnable.remainSec();
    }
    // 情報を送信する
    if (remaindTime == -2) {
      p.sendMessage("鉱石の種類：" + magicStoneOreType.getJpName() + ", 残り時間：再配置処理が稼働していません");
    } else {
      p.sendMessage("鉱石の種類：" + magicStoneOreType.getJpName() + ", 残り時間：" + remaindTime + "秒");
    }
  }

  /**
   * 全ての魔法鉱石の種類と座標を取得する
   * 
   * @return
   */
  public static HashMap<Location, MagicStoneOreType> getAllMagicStone() {
    return magicStoneOres;
  }

  /**
   * 指定された座標から魔法鉱石のタイプを取得
   * 
   * @param loc
   * @return
   */
  public static MagicStoneOreType getMagicStoneByLocation(Location loc) {
    return magicStoneOres.get(loc);
  }

  /**
   * 魔法鉱石と座標を取得する
   */
  public static void regist(Location loc, MagicStoneOreType type) {
    magicStoneOres.put(loc, type);
  }

  /**
   * 全ての鉱石ブロックを再配置する。スケジューラーが動いている場所なら何もしない
   */
  public static void resetMagicOre() {
    for (Location loc : magicStoneOres.keySet()) {
      relocationOre(loc);
    }
  }

  /**
   * ブロックをランダム時間後に再配置する すでに再配置スケジューラーが動いているなら何もしない
   * 
   * @param loc
   */
  public static void relocationOre(Location loc) {
    MagicStoneOreType magicStoneOreType = magicStoneOres.get(loc);
    // 鉱石がある場所でないなら何もしない
    if (magicStoneOreType == null) { return; }

    // すでに鉱石が設置されているなら何もしない
    if (loc.getBlock().getType() == magicStoneOreType.getMaterial()) { return; }

    // ブロック設置のスケジューラーを取得
    RelocationOreRunnable scheduler = magicStoneRelocationScheduler.get(loc);
    // 現在動いてるなら何もしない
    if (scheduler != null) { return; }

    // 鉱石再配置スケジューラーを動かす
    RelocationOreRunnable relocationOreRunnable = new RelocationOreRunnable(loc, magicStoneOreType);
    relocationOreRunnable.executeRelocation();
    // リストに追加する
    magicStoneRelocationScheduler.put(loc, relocationOreRunnable);
  }
}

class RelocationOreRunnable extends BukkitRunnable {
  public RelocationOreRunnable(Location loc, MagicStoneOreType magicStoneOreType) {
    this.loc = loc;
    this.magicStoneOreType = magicStoneOreType;
  }

  Location loc;
  MagicStoneOreType magicStoneOreType;

  @Override
  public void run() {
    // リストから削除する
    MagicStoneFactor.magicStoneRelocationScheduler.remove(loc);

    // チャンクがロードされていないならロードする
    if (!loc.getChunk().isLoaded()) {
      // 生成はしない
      loc.getChunk().load(false);
    }
    // ブロックを設置
    loc.getBlock().setType(magicStoneOreType.getMaterial());
  }

  long relocationMillisTime = -1;

  /**
   * 指定された時間後にブロックの再配置を行う
   */
  public void executeRelocation() {
    long relocationTick = magicStoneOreType.getRelocationTick();
    runTaskLater(Main.plugin, relocationTick);
    // 終了する時間を設定
    relocationMillisTime = System.currentTimeMillis() + relocationTick * 50;
  }

  /**
   * 残り時間を取得 設置済みの場合は-1を返す
   * 
   * @return
   */
  public long remainSec() {
    return (long) Math.max((relocationMillisTime - System.currentTimeMillis()) / 1000.0, -1);
  }
}
