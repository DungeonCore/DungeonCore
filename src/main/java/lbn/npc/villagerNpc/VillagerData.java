package lbn.npc.villagerNpc;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;

import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;
import lbn.npc.CustomNpcInterface;
import lbn.npc.NpcManager;

/**
 * NPCのデータを管理するためのクラス
 * 
 * @author KENSUKE
 *
 */
public class VillagerData {
  static HashMap<String, VillagerData> villagerMap = new HashMap<String, VillagerData>();

  public static CustomNpcInterface registSpletsheetVillager(CommandSender p, String name, String type, String texts, String location, String adult,
      String data, String mobtype, String skin, String id) {
    VillagerData villagerData = new VillagerData(p, name, type, texts, location, adult, data, mobtype, skin, id, null);
    if (villagerData.isError()) {
      if (type != null && !type.isEmpty() && !(p instanceof ConsoleCommandSender)) {
        p.sendMessage("エラーがあったため、スキップしました。[" + StringUtils.join(new Object[] { name, type, texts }, ",") + "]");
      }
      return null;
    }
    villagerMap.put(id, villagerData);

    // もしまだNPCが登録されていなければ新しく登録する
    VillagerNpc villagerNpc = VillagerNpcManager.getVillagerNpcById(id);
    if (villagerNpc == null) {
      NpcManager.regist(new VillagerNpc(villagerData));
    } else {
      // 登録されている時はデータを上書きする
      villagerNpc.data = villagerData;
      // NPCを更新する
      villagerNpc.updateNpc();
    }

    return villagerNpc;
  }

  public static VillagerData getInstance(String id) {
    return villagerMap.get(id);
  }

  String name;
  String data;
  VillagerType type;
  String[] texts;
  boolean isAdult = true;

  Location location;

  String id;

  EntityType entityType = EntityType.VILLAGER;

  String skin = null;

  boolean isError = false;

  private VillagerData(CommandSender p, String name, String type, String texts, String location, String adult, String data, String mobtype,
      String skin, String id, String uuid) {
    this.name = name;
    if (name == null || name.isEmpty()) {
      sendMsg(p, "名前は絶対必要です。");
      isError = true;
    }

    this.id = id;
    if (id == null || id.isEmpty()) {
      sendMsg(p, "IDは絶対必要です。");
      isError = true;
    }

    this.type = VillagerType.getValue(type);
    if (this.type == null) {
      this.type = VillagerType.NORMAL;
      // sendMsg(p, "typeは[normal, shop, BLACKSMITH]のみ許可されます");
      isError = true;
    }

    // データ処理
    this.data = data;

    this.location = AbstractSheetRunable.getLocationByString(location);

    if (texts != null) {
      this.texts = texts.split(",");
    } else {
      this.texts = new String[0];
    }

    if ("子供".equals(adult)) {
      isAdult = false;
    }

    try {
      entityType = EntityType.valueOf(mobtype);
    } catch (Exception e) {
      entityType = EntityType.VILLAGER;
    }

    this.skin = skin;

  }

  protected void sendMsg(CommandSender p, String msg) {
    if (!(p instanceof ConsoleCommandSender)) {
      p.sendMessage(msg);
    }
  }

  public String getName() {
    return name;
  }

  public VillagerType getType() {
    return type;
  }

  public String[] getTexts() {
    return texts;
  }

  public boolean isError() {
    return isError;
  }

  public boolean isAdult() {
    return isAdult;
  }

  public String getData() {
    return data;
  }

  public String getSkin() {
    return skin;
  }

  public String getId() {
    return id;
  }

  public EntityType getEntityType() {
    if (entityType == null) { return EntityType.VILLAGER; }
    return entityType;
  }

  public Location getLocation() {
    return location;
  }

  @Override
  public String toString() {
    return StringUtils.join(new Object[] { "name:", name, ", type:", type, ", texts:", texts });
  }

}
