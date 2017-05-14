package net.l_bulb.dungeoncore.player.customplayer;

import java.util.ArrayList;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.OneReincarnationData;
import net.l_bulb.dungeoncore.api.player.ReincarnationInterface;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;

/**
 * ある一人のPlayerの転生データを管理するためのクラス
 *
 */
public class PlayerReincarnationData {
  TheLowPlayer player;

  public PlayerReincarnationData(TheLowPlayer player) {
    this.player = player;
  }

  // 剣の転生データ
  ArrayList<OneReincarnationData> swordReincarnationData = new ArrayList<>();
  // 魔法の転生データ
  ArrayList<OneReincarnationData> magicReincarnationData = new ArrayList<>();
  // 弓の転生データ
  ArrayList<OneReincarnationData> bowReincarnationData = new ArrayList<>();

  // すべての転生データ (剣＋魔法＋弓の転生データ)
  ArrayList<OneReincarnationData> allReincarnationData = new ArrayList<>();

  /**
   * 転生を行うときのデータを追加
   *
   * @param reincarnationInterface
   * @param levelType
   * @return 今回追加されたOneReincarnationData
   */
  public OneReincarnationData addReincarnation(ReincarnationInterface reincarnationInterface, LevelType levelType) {
    // 現在の転生回数を取得
    int nowReincarnationCount = getNowReincarnationCount(levelType);
    // 転生データを作成
    OneReincarnationData oneReincarnationData = new OneReincarnationData(reincarnationInterface, levelType, nowReincarnationCount + 1);
    // 転生を行ったときの効果を追加する
    reincarnationInterface.addReincarnationEffect(player, levelType, oneReincarnationData.getCount());
    // データを追加
    getDataMap(levelType).add(oneReincarnationData);
    // すべての転生データに追加
    allReincarnationData.add(oneReincarnationData);

    return oneReincarnationData;
  }

  /**
   * 現在指定されたレベルタイプで何回転生を行ったのかを取得
   *
   * @return
   */
  public int getNowReincarnationCount(LevelType levelType) {
    ArrayList<OneReincarnationData> dataMap = getDataMap(levelType);
    return dataMap.size();
  }

  /**
   * 現在すべてのレベルタイプをを合計して何回転生を行ったのかを取得
   *
   * @return
   */
  public int getNowTotalReincarnationCount() {
    return allReincarnationData.size();
  }

  /**
   * 対応するレベルタイプに対応したMapを取得
   *
   * @param levelType
   * @return
   */
  protected ArrayList<OneReincarnationData> getDataMap(LevelType levelType) {
    switch (levelType) {
      case SWORD:
        return swordReincarnationData;
      case MAGIC:
        return magicReincarnationData;
      case BOW:
        return bowReincarnationData;
      default:
        throw new RuntimeException("invalied level type for reincaranation:" + levelType);
    }
  }

  /**
   * 指定したレベルタイプに対応した転生データを取得
   *
   * @param levelType
   * @return
   */
  public ArrayList<OneReincarnationData> getOneReincarnationDataList(LevelType levelType) {
    try {
      return getDataMap(levelType);
    } catch (RuntimeException e) {
      return null;
    }
  }

  /**
   * すべての転生データを取得する
   */
  public ArrayList<OneReincarnationData> getAllOneReincarnationDataList() {
    return allReincarnationData;
  }
}
