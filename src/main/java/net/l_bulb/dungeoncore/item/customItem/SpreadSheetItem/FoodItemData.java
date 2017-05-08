package net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem;

import net.l_bulb.dungeoncore.api.LevelType;

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

  String[] detail;

  public String getName() {
    return name;
  }

  public int getRecoveryMp() {
    return recoveryMp;
  }

  public void setRecoveryMp(int recoveryMp) {
    this.recoveryMp = recoveryMp;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getBuff1() {
    return buff1;
  }

  public void setBuff1(String buff1) {
    this.buff1 = buff1;
  }

  public String getBuff2() {
    return buff2;
  }

  public void setBuff2(String buff2) {
    this.buff2 = buff2;
  }

  public String getBuff3() {
    return buff3;
  }

  public void setBuff3(String buff3) {
    this.buff3 = buff3;
  }

  public String getSound() {
    return sound;
  }

  public void setSound(String sound) {
    this.sound = sound;
  }

  public String getParticle() {
    return particle;
  }

  public void setParticle(String particle) {
    this.particle = particle;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

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

  public void setSwordExp(int swordExp) {
    this.swordExp = swordExp;
  }

  public void setBowExp(int bowExp) {
    this.bowExp = bowExp;
  }

  public void setMagicExp(int magicExp) {
    this.magicExp = magicExp;
  }

  public String[] getDetail() {
    if (detail == null) { return new String[0]; }
    return detail;
  }

  public void setDetail(String detail) {
    if (detail == null) {
      this.detail = new String[0];
    } else {
      this.detail = detail.split(",");
    }
  }

  public String getId() {
    return id;
  }

}
