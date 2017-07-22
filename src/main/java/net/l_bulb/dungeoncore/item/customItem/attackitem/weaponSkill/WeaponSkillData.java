package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import org.bukkit.Material;

import lombok.Getter;
import lombok.Setter;

public class WeaponSkillData {
  double[] data = new double[10];

  public WeaponSkillData(String name, String id) {
    this.name = name;
    this.id = id;
  }

  @Getter
  @Setter
  int cooltime = 0;

  @Getter
  @Setter
  int needMp = 0;

  String[] detail;

  @Getter
  String name;

  @Getter
  String id;

  @Getter
  Material material = Material.STONE;

  @Setter
  @Getter
  WeaponSkillType weaponSkillType = null;

  @Getter
  @Setter
  byte materialdata = 0;

  @Getter
  @Setter
  String skillSetId;

  @Getter
  @Setter
  String skillSetName;

  public void setMaterial(int materialId) {
    @SuppressWarnings("deprecation")
    Material material2 = Material.getMaterial(materialId);
    if (material2 != null) {
      material = material2;
    }
  }

  public void setDetail(String detail) {
    if (detail == null) {
      this.detail = new String[0];
    } else {
      this.detail = detail.split(",");
    }
  }

  public String[] getDetail() {
    if (detail == null) { return new String[0]; }
    return detail;
  }

  public double getData(int i) {
    return data[i];
  }

  public void setData(double data0, int index) {
    data[index] = data0;
  }
}
