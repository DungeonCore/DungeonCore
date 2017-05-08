package net.l_bulb.dungeoncore.chest.wireless;

import java.util.StringJoiner;

public enum RepositoryType {
  TYPE_A("TypeA", 2000), TYPE_B("TypeB", 5000), TYPE_C("TypeC", 10000), TYPE_D("TypeD", 20000), TYPE_E("TypeE", 20000), TYPE_F("TypeF", 20000);

  String type;
  int price;

  private RepositoryType(String type, int price) {
    this.type = type;
    this.price = price;
  }

  public String getType() {
    return type;
  }

  public int getPrice() {
    return price;
  }

  public static RepositoryType getInstance(String type) {
    for (RepositoryType repositoryType : values()) {
      if (repositoryType.getType().equalsIgnoreCase(type)) { return repositoryType; }
    }
    return null;
  }

  public static String getNames() {
    StringJoiner stringJoiner = new StringJoiner(", ");
    for (RepositoryType repositoryType : values()) {
      stringJoiner.add(repositoryType.getType());
    }
    return stringJoiner.toString();
  }
}
