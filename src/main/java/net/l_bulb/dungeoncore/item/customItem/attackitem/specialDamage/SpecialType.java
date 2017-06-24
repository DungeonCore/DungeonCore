package net.l_bulb.dungeoncore.item.customItem.attackitem.specialDamage;

import java.util.function.Predicate;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SpecialType {
  ZOMBIE(e -> e.getType() == EntityType.ZOMBIE, "ゾンビ特攻"), SKELETON(e -> e.getType() == EntityType.SKELETON,
      "スケルトン特攻"), SPIDER(e -> e.getType() == EntityType.SPIDER,
          "クモ特攻"), PIG_ZOMBIE(e -> e.getType() == EntityType.PIG_ZOMBIE,
              "ピグ特攻"), IRON_GOLEM(e -> e.getType() == EntityType.IRON_GOLEM,
                  "ゴーレム特攻"), UNDEAD(SpecialType::isUndead, "アンデット特攻"), INSECT(SpecialType::isInsect,
                      "虫特攻"), GUARDIAN(e -> e.getType() == EntityType.GUARDIAN, "ガーディアン特攻"), ANIMAL(e -> e instanceof Animals, "動物特攻");

  private Predicate<Entity> isTarget;
  private String name;

  private static boolean isUndead(Entity e) {
    switch (e.getType()) {
      case ZOMBIE:
      case SKELETON:
      case PIG_ZOMBIE:
      case WITHER:
        return true;
      default:
        return false;
    }
  }

  private static boolean isInsect(Entity e) {
    switch (e.getType()) {
      case SPIDER:
      case SILVERFISH:
        return true;
      default:
        return false;
    }
  }

  /**
   * nameからインスタンスを取得
   *
   * @param name
   * @return
   */
  public static SpecialType fromName(String name) {
    for (SpecialType type : values()) {
      if (type.getName().equals(name)) { return type; }
    }
    return null;
  }
}
