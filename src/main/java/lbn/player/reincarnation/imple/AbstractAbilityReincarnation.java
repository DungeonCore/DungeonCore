package lbn.player.reincarnation.imple;

import lbn.api.LevelType;
import lbn.api.PlayerStatusType;
import lbn.api.player.ReincarnationInterface;
import lbn.api.player.TheLowPlayer;
import lbn.player.ability.impl.ReincarnationAbilityStatusWrapper;

/**
 * Ability効果を増加させるための転生効果
 */
public abstract class AbstractAbilityReincarnation implements ReincarnationInterface {
  @Override
  public void addReincarnationEffect(TheLowPlayer p, LevelType levelType, int count) {
    // 転生用のAbilityを取得
    ReincarnationAbilityStatusWrapper statusWrapper = new ReincarnationAbilityStatusWrapper(getStatusType(), getAddValue(), levelType, count);
    // Abilityを追加
    p.addAbility(statusWrapper);
  }

  /**
   * 増加させるStatusType
   * 
   * @return
   */
  abstract protected PlayerStatusType getStatusType();

  /**
   * 増加値
   * 
   * @return
   */
  abstract protected double getAddValue();

  @Override
  public String getId() {
    return "ability_" + getStatusType();
  }

  @Override
  public int getItemStackData() {
    return 0;
  }

  @Override
  public boolean isShow(LevelType type, int count) {
    return true;
  }
}
