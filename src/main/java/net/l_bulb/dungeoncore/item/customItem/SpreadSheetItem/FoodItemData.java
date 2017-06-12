package net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem;

import net.l_bulb.dungeoncore.api.LevelType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodItemData {
  public FoodItemData(String id) {
    this.id = id;
  }

  String id;
  String name;
  String command;
  String buff1;
  String buff2;
  String buff3;
  String sound;
  String particle;
  int price;
  int swordExp;
  int bowExp;
  int magicExp;
  int recoveryMp;
  int buff1DelayTick = 0;
  int buff2DelayTick = 0;
  int buff3DelayTick = 0;

  String[] detail;

  public int getExp(LevelType type) {
    switch (type) {
      case SWORD:
        return swordExp;
      case BOW:
        return bowExp;
      case MAGIC:
        return magicExp;
      default:
        return 0;
    }
  }

  public void setDetail(String detail) {
    if (detail == null) {
      this.detail = new String[0];
    } else {
      this.detail = detail.split(",");
    }
  }

}