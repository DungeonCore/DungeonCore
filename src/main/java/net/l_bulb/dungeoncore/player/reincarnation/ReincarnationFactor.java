package net.l_bulb.dungeoncore.player.reincarnation;

import java.util.HashMap;
import java.util.Map;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.ReincarnationInterface;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.player.reincarnation.imple.AttackDamageReincarnation;
import net.l_bulb.dungeoncore.player.reincarnation.imple.MaxHpReincarnation;
import net.l_bulb.dungeoncore.player.reincarnation.imple.MaxLevelReincarnation;
import net.l_bulb.dungeoncore.player.reincarnation.imple.MaxMagicPointReincarnation;
import net.l_bulb.dungeoncore.util.DungeonLogger;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ReincarnationFactor {
  /**
   * 全ての転生効果を保持するマップ
   */
  static HashMap<String, ReincarnationInterface> reincarnationMap = new HashMap<String, ReincarnationInterface>();

  public static void openReincarnationInv(Player p) {
    new ReincarnationTypeMune().open(p);
  }

  /**
   * 全ての転生効果を取得する
   * 
   * @return
   */
  public static Map<String, ReincarnationInterface> getAllReincanationMap() {
    return reincarnationMap;
  }

  /**
   * 新しい転生効果を追加する
   * 
   * @param reincarnationInterface
   */
  public static void registReincarnation(ReincarnationInterface reincarnationInterface) {
    reincarnationMap.put(reincarnationInterface.getId(), reincarnationInterface);
  }

  /**
   * IDから転生効果を取得
   * 
   * @return IDが存在しないばあいはUndefinedReincarnationを返す
   */
  public static ReincarnationInterface getReincarnationInterface(String id) {
    ReincarnationInterface reincarnationInterface = reincarnationMap.get(id);
    if (reincarnationInterface != null) { return reincarnationInterface; }
    return UndefinedReincarnation.getInstance(id);
  }

  static {
    registReincarnation(new AttackDamageReincarnation(LevelType.SWORD));
    registReincarnation(new AttackDamageReincarnation(LevelType.MAGIC));
    registReincarnation(new AttackDamageReincarnation(LevelType.BOW));
    registReincarnation(new MaxHpReincarnation());
    registReincarnation(new MaxMagicPointReincarnation());
    registReincarnation(new MaxLevelReincarnation(LevelType.SWORD));
    registReincarnation(new MaxLevelReincarnation(LevelType.MAGIC));
    registReincarnation(new MaxLevelReincarnation(LevelType.BOW));
  }
}

/**
 * データが未定義の場合のReincarnation
 */
class UndefinedReincarnation implements ReincarnationInterface {
  static Map<String, UndefinedReincarnation> cache = new HashMap<String, UndefinedReincarnation>();

  public static UndefinedReincarnation getInstance(String id) {
    if (!cache.containsKey(id)) {
      cache.put(id, new UndefinedReincarnation(id));
    }
    DungeonLogger.error("ReincarnationInterfaceが未実装です。ID：" + id);
    return cache.get(id);
  }

  String id;

  private UndefinedReincarnation(String id) {
    this.id = id;
  }

  @Override
  public boolean isShow(LevelType type, int count) {
    return false;
  }

  @Override
  public void addReincarnationEffect(TheLowPlayer p, LevelType levelType, int count) {

  }

  @Override
  public String getTitle() {
    return "データ未定義";
  }

  @Override
  public String getDetail() {
    return "データ未定義";
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Material getMaterial() {
    return Material.STONE;
  }

  @Override
  public int getItemStackData() {
    return 0;
  }

}
