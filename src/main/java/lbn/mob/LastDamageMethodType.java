package lbn.mob;

import lbn.api.LevelType;
import lbn.player.ItemType;

/**
 * プレイヤーがモンスターに攻撃をした攻撃方法
 */
public enum LastDamageMethodType {
  SWORD("剣", LevelType.SWORD),	// 剣で攻撃
  BOW("弓", LevelType.BOW),		// 弓で攻撃
  MAGIC("魔法", LevelType.MAGIC),		// 魔法で攻撃
  SWORD_SUMMON("剣(Summon)", LevelType.SWORD),		// 剣で召喚したSummonモンスターが攻撃
  BOW_SUMMON("弓(Summon)", LevelType.BOW),			// 弓で召喚したSummonモンスターが攻撃
  MAGIC_SUMMON("魔法(Summon)", LevelType.MAGIC),		// 魔法で召喚したSummonモンスターが攻撃
  BARE_HAND("素手", LevelType.SWORD),				// 素手
  MELEE_ATTACK_WITH_OTHER("アイテムで殴る", LevelType.SWORD),	// その他のアイテムを持って直接攻撃
  USE_ITEM("アイテム使用"),		// アイテムを使って攻撃
  OTHER("その他");				// その他の方法

  String text;
  LevelType levelType;

  private LastDamageMethodType(String text) {
    this(text, null);
  }

  private LastDamageMethodType(String text, LevelType type) {
    this.text = text;
    this.levelType = type;
  }

  /**
   * 日本語による説明を取得
   * 
   * @return
   */
  public String getText() {
    return text;
  }

  public static LastDamageMethodType fromAttackType(ItemType type) {
    return fromAttackType(type, false);
  }

  public static LastDamageMethodType fromAttackType(ItemType type, boolean isSummon) {
    switch (type) {
      case SWORD:
        if (isSummon) {
          return SWORD_SUMMON;
        } else {
          return SWORD;
        }
      case BOW:
        if (isSummon) {
          return BOW_SUMMON;
        } else {
          return BOW;
        }
      case MAGIC:
        if (isSummon) {
          return MAGIC_SUMMON;
        } else {
          return MAGIC;
        }
      default:
        return OTHER;
    }
  }

  /**
   * 攻撃方法に対応するLevelTypeを取得。どれにも当てはまらない場合はnullを返す
   * 
   * @return
   */
  public LevelType getLevelType() {
    return levelType;
  }
}
