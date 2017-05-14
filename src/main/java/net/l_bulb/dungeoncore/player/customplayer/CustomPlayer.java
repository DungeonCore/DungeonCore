package net.l_bulb.dungeoncore.player.customplayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.PlayerStatusType;
import net.l_bulb.dungeoncore.api.player.AbilityInterface;
import net.l_bulb.dungeoncore.api.player.OneReincarnationData;
import net.l_bulb.dungeoncore.api.player.ReincarnationInterface;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.common.event.player.PlayerChangeGalionsEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerChangeStatusExpEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerChangeStatusLevelEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerCompleteReincarnationEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerLevelUpEvent;
import net.l_bulb.dungeoncore.common.place.dungeon.DungeonData;
import net.l_bulb.dungeoncore.common.place.dungeon.DungeonDataOld;
import net.l_bulb.dungeoncore.common.place.dungeon.DungeonList;
import net.l_bulb.dungeoncore.item.setItem.SetItemManager;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.player.ExpTable;
import net.l_bulb.dungeoncore.player.ability.AbilityType;
import net.l_bulb.dungeoncore.player.ability.AbstractTimeLimitAbility;
import net.l_bulb.dungeoncore.player.ability.impl.LevelUpAbility;
import net.l_bulb.dungeoncore.player.reincarnation.ReincarnationFactor;
import net.l_bulb.dungeoncore.player.reincarnation.ReincarnationSelector;
import net.l_bulb.dungeoncore.player.status.StatusAddReason;

public class CustomPlayer implements TheLowPlayer, Serializable {
  private static final long serialVersionUID = 2400114842430732048L;

  public CustomPlayer(OfflinePlayer p) {
    this.player = Bukkit.getOfflinePlayer(p.getUniqueId());
    levelData = new PlayerLevelIntData(0, 0, 0);
    expData = new PlayerLevelIntData(0, 0, 0);
    maxLevelData = new PlayerLevelIntData(ReincarnationSelector.REINC_LEVEL, ReincarnationSelector.REINC_LEVEL, ReincarnationSelector.REINC_LEVEL);
  }

  /**
   * プレイヤーデータ初期者処理
   */
  public void init(OfflinePlayer player) {
    this.player = player;

    maxLevelData = new PlayerLevelIntData(ReincarnationSelector.REINC_LEVEL, ReincarnationSelector.REINC_LEVEL, ReincarnationSelector.REINC_LEVEL);
    playerStatusData = new PlayerStatusData(this);
    reincarnationData = new PlayerReincarnationData(this);

    // 転生データを適用させる
    for (Entry<LevelType, ArrayList<String>> entry : reincNameData.entrySet()) {
      for (String id : entry.getValue()) {
        reincarnationData.addReincarnation(ReincarnationFactor.getReincarnationInterface(id), entry.getKey());
      }
    }
  }

  // レベル
  PlayerLevelIntData levelData = null;

  // 経験値
  PlayerLevelIntData expData = null;

  // 最大レベル
  transient PlayerLevelIntData maxLevelData = null;

  // 所持金
  int galions = 0;

  transient OfflinePlayer player;

  // 現在いるダンジョンID
  transient int inDungeonId = -1;

  // PlayerStatusData
  transient PlayerStatusData playerStatusData = new PlayerStatusData(this);

  // 転生データを管理するクラス
  transient PlayerReincarnationData reincarnationData = new PlayerReincarnationData(this);

  // 転生名だけのデータ
  HashMap<LevelType, ArrayList<String>> reincNameData = new HashMap<>();

  transient Location lastOverWorldLocation = null;

  @Override
  public int getLevel(LevelType type) {
    // 最大レベルを上回っていた場合は最大レベルを返す
    return Math.min(levelData.get(type), getMaxLevel(type));
  }

  @Override
  public void setLevel(LevelType type, int level) {
    // 最大レベルを上回っていたら最大レベルを返す
    levelData.put(type, Math.min(level, getMaxLevel(type)));
    // Eventを呼ぶ
    new PlayerChangeStatusLevelEvent(this, level, type).callEvent();
  }

  @Override
  public int getExp(LevelType type) {
    if (type == LevelType.MAIN) { return 0; }
    return expData.get(type);
  }

  @Override
  public void addExp(LevelType type, int addExp, StatusAddReason reason) {
    // メインレベルが指定された時は全ての経験値をセットする
    if (type == LevelType.MAIN) {
      addExp(LevelType.SWORD, addExp, reason);
      addExp(LevelType.BOW, addExp, reason);
      addExp(LevelType.MAGIC, addExp, reason);
      return;
    }

    PlayerChangeStatusExpEvent event = new PlayerChangeStatusExpEvent(this, addExp, type, reason);
    event.callEvent();
    addExp = event.getAddExp();

    // 現在の経験値
    int nowExp = getExp(type) + addExp;

    // 現在のレベル
    int nowLevel = getLevel(type) + 1;

    for (; nowExp >= getNeedExp(type, nowLevel); nowLevel++) {
      // 最大レベルを超えていたらレベルアップさせない
      if (getMaxLevel(type) < nowLevel) {
        nowExp = 0;
        break;
      }
      // レベルアップする
      levelUp(type, nowLevel);
      // 必要な経験値を引く
      nowExp -= getNeedExp(type, nowLevel);
    }
    expData.put(type, nowExp);
  }

  /**
   * レベルをアップさせる
   *
   * @param type
   * @param level
   */
  protected void levelUp(LevelType type, int level) {
    // レベルをセットする
    setLevel(type, level);
    // eventの発生させる
    PlayerLevelUpEvent event = new PlayerLevelUpEvent(this, type);
    event.callEvent();
  }

  @Override
  public int getMaxLevel(LevelType type) {
    return maxLevelData.get(type);
  }

  @Override
  public void setMaxLevel(LevelType type, int value) {
    maxLevelData.put(type, value);
  }

  @Override
  public int getNeedExp(LevelType type, int level) {
    return (int) ExpTable.getNeedExp(level);
  }

  @Override
  public int getGalions() {
    return galions;
  }

  @Override
  public void setGalions(int galions, GalionEditReason reason) {
    addGalions(galions - this.galions, reason);
  }

  @Override
  public void addGalions(int galions, GalionEditReason reason) {
    PlayerChangeGalionsEvent event = new PlayerChangeGalionsEvent(this, galions, reason);
    Bukkit.getServer().getPluginManager().callEvent(event);
    this.galions += event.getGalions();
  }

  @Override
  public OfflinePlayer getOfflinePlayer() {
    return Bukkit.getOfflinePlayer(getUUID());
  }

  @Override
  public Player getOnlinePlayer() {
    return Bukkit.getPlayer(getUUID());
  }

  @Override
  public boolean isOnline() {
    return player.isOnline();
  }

  @Override
  public DungeonData getInDungeonId() {
    if (inDungeonId == -1) { return null; }
    return DungeonList.getDungeonById(inDungeonId);
  }

  @Override
  public void setInDungeonId(DungeonDataOld dungeon) {
    if (dungeon == null) {
      inDungeonId = -1;
    } else {
      inDungeonId = dungeon.getId();
    }
  }

  @Override
  public String getName() {
    return player.getName();
  }

  @Override
  public UUID getUUID() {
    return player.getUniqueId();
  }

  String dataType = "typeA";

  @Override
  public String getSaveType() {
    return dataType;
  }

  long lastDeathMilleTime = -1;

  @Override
  public Long getLastDeathTimeMillis() {
    return lastDeathMilleTime;
  }

  @Override
  public void setLastDeathTimeMillis(long time) {
    this.lastDeathMilleTime = time;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((dataType == null) ? 0 : dataType.hashCode());
    result = prime * result + ((player == null) ? 0 : player.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CustomPlayer other = (CustomPlayer) obj;
    if (dataType == null) {
      if (other.dataType != null)
        return false;
    } else if (!dataType.equals(other.dataType))
      return false;
    if (player == null) {
      if (other.player != null)
        return false;
    } else if (!player.equals(other.player))
      return false;
    return true;
  }

  @Override
  public void addAbility(AbilityInterface ability) {
    playerStatusData.addData(ability);
  }

  @Override
  public void removeAbility(AbilityInterface ability) {
    playerStatusData.removeData(ability);
  }

  @Override
  public double getStatusData(PlayerStatusType type) {
    return playerStatusData.getData(type);
  }

  @Override
  public boolean doReincarnation(ReincarnationInterface reincarnationInterface, LevelType levelType) {
    if (!canReincarnation(levelType)) { return false; }
    // 転生を行う
    OneReincarnationData oneReincarnationData = reincarnationData.addReincarnation(reincarnationInterface, levelType);

    // 保存しておく
    ArrayList<String> reincList = reincNameData.getOrDefault(levelType, new ArrayList<String>());
    reincList.add(reincarnationInterface.getId());
    reincNameData.put(levelType, reincList);

    // 60レベル引いたレベルをセットする
    setLevel(levelType, getLevel(levelType) - ReincarnationSelector.REINC_LEVEL);
    // Eventを発火させる
    new PlayerCompleteReincarnationEvent(this, oneReincarnationData).callEvent();
    return true;
  }

  @Override
  public boolean canReincarnation(LevelType levelType) {
    int level = getLevel(levelType);
    // 現在のレベルが60レベル以下なら転生できない
    if (level < ReincarnationSelector.REINC_LEVEL) { return false; }
    return true;
  }

  @Override
  public int getEachReincarnationCount(LevelType levelType) {
    return reincarnationData.getNowReincarnationCount(levelType);
  }

  @Override
  public void fixIntegrity(CheckIntegrityLevel level) {
    switch (level) {
      case LEVEL2:
        if (isOnline()) {
          // ItemStackのAbilityをすべて消す
          playerStatusData.clear(AbilityType.SET_ITEM_ABILITY);
          // SetItemのAbilityをセットしなおす
          SetItemManager.updateAllSetItem(getOnlinePlayer());

          // 転生のAbilityをすべて消す
          playerStatusData.clear(AbilityType.REINCARNATION_ABILITY);
          for (OneReincarnationData oneReincarnationData : reincarnationData.getAllOneReincarnationDataList()) {
            // 転生を行ったときの効果を追加する
            oneReincarnationData.getReincarnationInterface().addReincarnationEffect(this, oneReincarnationData.getLevelType(),
                oneReincarnationData.getCount());
          }

          // 転生のAbilityをすべて消す
          playerStatusData.clear(AbilityType.LEVEL_UP);
          addAbility(new LevelUpAbility(getLevel(LevelType.MAIN)));

          // 時間制限付きのAbilityをチェックする
          Set<AbilityInterface> ablitys = playerStatusData.getApplyedAbility(AbilityType.TIME_LIMIT_ABILITY);
          AbstractTimeLimitAbility.fixIntegrity(this, ablitys);
        }
      case LEVEL1:
        // データの適応を行う
        playerStatusData.applyAllAbility();
      default:
        break;
    }
  }

  @Override
  public boolean equalsPlayer(Player p) {
    if (p == null) { return false; }
    return getUUID().equals(p.getUniqueId());
  }
}
