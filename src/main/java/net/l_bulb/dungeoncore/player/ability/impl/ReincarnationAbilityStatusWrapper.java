package net.l_bulb.dungeoncore.player.ability.impl;

import java.text.MessageFormat;
import java.util.HashMap;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.PlayerStatusType;
import net.l_bulb.dungeoncore.player.ability.AbstractReincarnationAbility;

/**
 * PlayerStatusに結びついたAbility
 *
 */
public class ReincarnationAbilityStatusWrapper extends AbstractReincarnationAbility {
  HashMap<PlayerStatusType, Double> ability = new HashMap<PlayerStatusType, Double>();

  /**
   * @param status
   * @param addValue
   * @param type 転生を行うレベルタイプ
   * @param count 何回目の転生か
   */
  public ReincarnationAbilityStatusWrapper(PlayerStatusType status, double addValue, LevelType type, int count) {
    ability.put(status, addValue);
    // LevelTypeと何回目の転生かという情報をIDに持つ
    id = MessageFormat.format("reincarnation_{0}_{1}", type, count);
  }

  String id;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public HashMap<PlayerStatusType, Double> getAbilityMap() {
    return ability;
  }
}
