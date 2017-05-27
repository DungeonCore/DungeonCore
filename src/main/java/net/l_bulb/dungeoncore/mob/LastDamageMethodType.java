package net.l_bulb.dungeoncore.mob;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.player.ItemType;

/**
 * プレイヤーがモンスターに攻撃をした攻撃方法
 */
public enum LastDamageMethodType {
  SWORD("剣", LevelType.SWORD),	// 剣で攻撃
  BOW("弓", LevelType.BOW),		// 弓で攻撃
  MAGIC("魔法", LevelType.MAGIC),		// 魔法で攻撃
  SWORD_UNNORMAL("剣(通常攻撃以外)", LevelType.SWORD),		// 通常攻撃以外の剣攻撃
  BOW_UNNORMAL("弓(通常攻撃以外)", LevelType.BOW),			// 通常攻撃以外の弓攻撃
  MAGIC_UNNORMAL("魔法(通常攻撃以外)", LevelType.MAGIC),		// 通常攻撃以外の魔法攻撃
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
    return fromAttackType(type, true);
  }

  /**
   * @param type
   * @param isNormalAttack 通常攻撃でない
   * @return
   */
  public static LastDamageMethodType fromAttackType(ItemType type, boolean isNormalAttack) {
    switch (type) {
      case SWORD:
        if (isNormalAttack) {
          return SWORD;
        } else {
          return SWORD_UNNORMAL;
        }
      case BOW:
        if (isNormalAttack) {
          return BOW;
        } else {
          return BOW_UNNORMAL;
        }
      case MAGIC:
        if (isNormalAttack) {
          return MAGIC;
        } else {
          return MAGIC_UNNORMAL;
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
