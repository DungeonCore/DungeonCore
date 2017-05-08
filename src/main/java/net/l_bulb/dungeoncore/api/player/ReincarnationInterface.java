package net.l_bulb.dungeoncore.api.player;

import net.l_bulb.dungeoncore.api.LevelType;

import org.bukkit.Material;

public interface ReincarnationInterface {
  /**
   * 選択肢に表示させるかどうか
   *
   * @param type レベルタイプ
   * @param count 何回目の転生か
   * @return
   */
  boolean isShow(LevelType type, int count);

  /**
   * 転生を行ったときの処理
   *
   * @param p
   * @param levelType 転生を行うLevelType
   * @param 対象のLevelTypeで何回目の転生か
   */
  void addReincarnationEffect(TheLowPlayer p, LevelType levelType, int count);

  /**
   * 表示するときのタイトル
   *
   * @return
   */
  String getTitle();

  /**
   * 表示するときの説明
   *
   * @return
   */
  String getDetail();

  /**
   * 転生ID
   *
   * @return
   */
  String getId();

  /**
   * 表示する際のMaterial
   *
   * @return
   */
  Material getMaterial();

  /**
   * 表示する際のアイテムのデータ値
   *
   * @return
   */
  int getItemStackData();
}
