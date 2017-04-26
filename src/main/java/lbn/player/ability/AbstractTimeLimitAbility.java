package lbn.player.ability;

import java.util.Set;

import lbn.api.player.AbilityInterface;
import lbn.api.player.TheLowPlayer;
import lbn.util.JavaUtil;

/**
 * 期限付きのAbilityを保持する
 */
public abstract class AbstractTimeLimitAbility implements AbilityInterface {
  @Override
  public AbilityType getAbilityType() {
    return AbilityType.TIME_LIMIT_ABILITY;
  }

  abstract public double getLimitTimeSec();

  // インスタンスを作成した時間を開始した時間とする
  long startMilliTime = JavaUtil.getJapanTimeInMillis();

  /**
   * 期限が切れているならTRUE
   */
  public boolean isExpired() {
    return JavaUtil.getJapanTimeInMillis() > startMilliTime + getLimitTimeSec() * 1000;
  }

  /**
   * 整合性を修正する
   */
  public static void fixIntegrity(TheLowPlayer p, Set<AbilityInterface> ablitys) {
    for (AbilityInterface abilityInterface : ablitys) {
      try {
        // 期限が切れていたら消す
        if (((AbstractTimeLimitAbility) abilityInterface).isExpired()) {
          p.removeAbility(abilityInterface);
          // エラーにする
          new RuntimeException(p.getName() + "のアビリティ:" + abilityInterface.getId() + "の期限が切れています").printStackTrace();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
