package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import java.util.concurrent.Future;

import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillData;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillFactory;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillSelector;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.JavaUtil;

import org.bukkit.command.CommandSender;

public class WeaponSkillSheetRunnable extends AbstractSheetRunable {

  public WeaponSkillSheetRunnable(CommandSender sender) {
    super(sender);
  }

  @Override
  protected String getQuery() {
    return null;
  }

  @Override
  public String getSheetName() {
    return "weaponskill";
  }

  @Override
  public String[] getTag() {
    return new String[] { "type", "name", "level", "cooltime", "mp", "detail", "data0", "data1", "data2", "data3", "data4", "id", "material",
        "materialData" };
  }

  @Override
  protected void excuteOnerow(String[] row) {
    String name = row[1];

    ItemType itemType = getItemType(row[0]);
    if (itemType == null) {
      sendMessage(itemType + "が不正です(name:" + name + ")");
      return;
    }

    int level = JavaUtil.getInt(row[2], -1);
    int cooltime = JavaUtil.getInt(row[3], -1);

    int needMp = JavaUtil.getInt(row[4], -1);

    double data0 = JavaUtil.getDouble(row[6], 0);
    double data1 = JavaUtil.getDouble(row[7], 0);
    double data2 = JavaUtil.getDouble(row[8], 0);
    double data3 = JavaUtil.getDouble(row[9], 0);
    double data4 = JavaUtil.getDouble(row[10], 0);

    String detail = getNull(row[5]);
    detail = detail.replace("{0}", getNull(row[6])).
        replace("{1}", getNull(row[7])).
        replace("{2}", getNull(row[8])).
        replace("{3}", getNull(row[9])).
        replace("{4}", getNull(row[10]));

    if (level == -1 || cooltime == -1 || needMp == -1) {
      sendMessage("level, cooltime, needMpが不正です(name:" + name + ")");
      return;
    }

    WeaponSkillData weaponSkillData = new WeaponSkillData(name, itemType, row[11]);
    weaponSkillData.setCooltime(cooltime);
    weaponSkillData.setSkillLevel(level);
    weaponSkillData.setNeedMp(needMp);
    ;
    weaponSkillData.setDetail(detail);
    weaponSkillData.setData(data0, 0);
    weaponSkillData.setData(data1, 1);
    weaponSkillData.setData(data2, 2);
    weaponSkillData.setData(data3, 3);
    weaponSkillData.setData(data4, 4);

    weaponSkillData.setMaterial(JavaUtil.getInt(row[12], 1));
    weaponSkillData.setMaterialdata((byte) JavaUtil.getInt((row[13]), 0));

    WeaponSkillFactory.regist(weaponSkillData);
  }

  public String getNull(String val) {
    return val == null ? "" : val;
  }

  /**
   * アイテムの種類を説明
   * 
   * @param name
   * @return
   */
  ItemType getItemType(String name) {
    switch (name) {
      case "剣":
        return ItemType.SWORD;
      case "弓":
        return ItemType.BOW;
      case "魔法":
        return ItemType.MAGIC;
      default:
        break;
    }
    return null;
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    // キャッシュをクリアする
    WeaponSkillSelector.clearCache();
    super.onCallbackFunction(submit);
  }
}
