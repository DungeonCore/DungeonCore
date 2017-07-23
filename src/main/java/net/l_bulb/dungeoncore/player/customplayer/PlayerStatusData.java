package net.l_bulb.dungeoncore.player.customplayer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.api.PlayerStatusType;
import net.l_bulb.dungeoncore.api.player.AbilityInterface;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.player.ability.AbilityType;

import com.google.common.collect.HashMultimap;

/**
 * プレイヤーの追加ステータスを変更・取得を行う <br />
 * 追加ステータスの変更はAbilityを用いて行う <br />
 *
 * @see AbilityInterface
 *
 */
public class PlayerStatusData implements Serializable {
  private static final long serialVersionUID = 5162626958325984577L;

  // 実際のステータスデータ値をいれておくMap
  protected HashMap<PlayerStatusType, Double> dataMapDouble = new HashMap<>();

  /**
   * 現在適応中のAbilityをセットするためのMap
   */
  HashMultimap<AbilityType, AbilityInterface> abilityType = HashMultimap.create();

  /**
   * 現在適応中のAbilityをセットするためのMap
   */
  HashMap<String, AbilityInterface> idMap = new HashMap<>();

  TheLowPlayer player;

  public PlayerStatusData(TheLowPlayer player) {
    dataMapDouble.put(PlayerStatusType.MAX_HP, 20.0);
    dataMapDouble.put(PlayerStatusType.MAX_MAGIC_POINT, 100.0);
    this.player = player;
  }

  /**
   * 指定したAbilityをセットします
   *
   * @param status
   * @param data
   */
  public void addData(AbilityInterface ability) {
    // 過去に登録されたAbilityを取得
    AbilityInterface beforeAbility = idMap.get(ability.getId());
    // もし過去に存在したAbilityがある場合は削除する
    if (beforeAbility != null) {
      unsafeDeapplayAbility(beforeAbility);
    }
    // 今のを適応させる
    unsafeApplayAbility(ability);

    // データを残す
    abilityType.put(ability.getAbilityType(), ability);
    idMap.put(ability.getId(), ability);

    // データを適応させる
    applyAllAbility();
  }

  /**
   * 指定したAbilityを削除します
   *
   * @param status
   * @param data
   */
  public void removeData(AbilityInterface ability) {
    // 過去に登録されたAbilityを取得
    AbilityInterface beforeAbility = idMap.get(ability.getId());
    // もし過去に存在したAbilityがない場合は無視する
    if (beforeAbility == null) { return; }
    // 前のを削除する
    unsafeDeapplayAbility(beforeAbility);

    // データを削除する
    abilityType.remove(beforeAbility.getAbilityType(), beforeAbility);
    idMap.remove(beforeAbility.getId());

    // データを適応させる
    applyAllAbility();
  }

  /**
   * このメソッドではすでにAbilityが対応済みか、対応済みでないかチェックを行わないでAbilityを適応します。
   * 事前に適応されてないことを確認しないと効果が重複する可能性があります
   *
   * @param ability
   */
  private void unsafeApplayAbility(AbilityInterface ability) {
    Map<PlayerStatusType, Double> dataMap = ability.getAbilityMap();
    // 適応効果がない場合は何もしない
    if (dataMap == null) { return; }

    // 1つずつ効果を追加していく
    for (Entry<PlayerStatusType, Double> entry : dataMap.entrySet()) {
      // もしすでにデータがある場合はそれに追加する
      if (dataMapDouble.containsKey(entry.getKey())) {
        switch (entry.getKey().getApplyMethod()) {
          case ADDITION:
            // 直接の値の時は加算する
            dataMapDouble.put(entry.getKey(), dataMapDouble.get(entry.getKey()) + entry.getValue());
          case MULTIPLICATION:
            // 割合で表している時はかけていく
            dataMapDouble.put(entry.getKey(), dataMapDouble.get(entry.getKey()) * entry.getValue());
          case OVERWRITE:
            dataMapDouble.put(entry.getKey(), entry.getValue());
            break;
          default:
            break;
        }
      } else {
        dataMapDouble.put(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * このメソッドではすでにAbilityが対応済みか、対応済みでないかチェックを行わないでAbilityを削除します。
   * 事前に適応されてないことを確認しないと効果がついていないのにステータスが減少する可能性があります
   *
   * @param ability
   */
  private void unsafeDeapplayAbility(AbilityInterface ability) {
    Map<PlayerStatusType, Double> dataMap = ability.getAbilityMap();
    // 適応効果がない場合は何もしない
    if (dataMap == null) { return; }

    // 1つずつ効果を削除していく
    for (Entry<PlayerStatusType, Double> entry : dataMap.entrySet()) {
      switch (entry.getKey().getApplyMethod()) {
        case ADDITION:
          dataMapDouble.put(entry.getKey(), dataMapDouble.get(entry.getKey()) - entry.getValue());
          break;
        case MULTIPLICATION:
          dataMapDouble.put(entry.getKey(), dataMapDouble.get(entry.getKey()) / entry.getValue());
          break;
        case OVERWRITE:
          // TODO 前の効果のものがあればそれを適応させる
          break;
        default:
          break;
      }
    }
  }

  /**
   * 指定したStatusデータを取得します
   *
   * @param status
   * @return
   */
  public double getData(PlayerStatusType status) {
    if (dataMapDouble.containsKey(status)) { return dataMapDouble.get(status); }
    return 0;
  }

  /**
   * プレイヤーデータを実際のPlayerに適応させる
   *
   * @param p
   */
  protected void applyAllAbility() {
    Player onlinePlayer = player.getOnlinePlayer();
    if (onlinePlayer == null || !onlinePlayer.isOnline()) { return; }
    // 最大HPを加算する
    if (dataMapDouble.containsKey(PlayerStatusType.MAX_HP)) {
      onlinePlayer.setMaxHealth(getData(PlayerStatusType.MAX_HP));
    }

    // 最大HPを固定する
    if (dataMapDouble.containsKey(PlayerStatusType.SET_MAX_HP) && getData(PlayerStatusType.SET_MAX_HP) > 0) {
      onlinePlayer.setMaxHealth(getData(PlayerStatusType.SET_MAX_HP));
    }

    // マジックポイントの回復を開始する
    if (MagicPointManager.getMaxMagicPoint(onlinePlayer) > MagicPointManager.getNowMagicPoint(onlinePlayer)) {
      MagicPointManager.startHealMagicPoint(onlinePlayer);
    }
  }

  /**
   * 指定されたAbilityTypeを全て削除する
   *
   * @param type
   */
  public void clear(AbilityType type) {
    // 全て取得し、1つずつ削除する
    Set<AbilityInterface> set = new HashSet<>(abilityType.get(type));
    for (AbilityInterface abilityInterface : set) {
      removeData(abilityInterface);
    }
  }

  /**
   * 適応中のAbilityを取得する
   *
   * @param type
   * @return
   */
  public Set<AbilityInterface> getApplyedAbility(AbilityType type) {
    return abilityType.get(type);
  }

  // /**
  // * Abilityの整合性をチェック, 修正する
  // */
  // public void checkAbility() {
  // //すべてクリアする
  // clear(AbilityType.REINCARNATION_ABILITY);
  // //転生済みの転生データを全て取得し1つずつ適応する
  // List<OneReincarnationData> reincarnationData = player.getReincarnationData();
  // for (OneReincarnationData oneReincarnationData : reincarnationData) {
  // ReincarnationInterface reincarnationInterface = oneReincarnationData.getReincarnationInterface();
  // if (oneReincarnationData instanceof AbstractAbilityReincarnation) {
  //
  // }
  // }
  // }
}
