package net.l_bulb.dungeoncore.dungeon.contents.slotStone;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.slot.CombatSlot;

public class OneMobDamageUpSlot extends CombatSlot {

  public static ArrayList<OneMobDamageUpSlot> slotList = new ArrayList<>();

  static {
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL2, 1, EntityType.ZOMBIE, "ゾンビ"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL3, 2, EntityType.ZOMBIE, "ゾンビ"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL4, 3, EntityType.ZOMBIE, "ゾンビ"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL5, 4, EntityType.ZOMBIE, "ゾンビ"));

    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL1, 1, EntityType.SKELETON, "スケルトン"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL2, 2, EntityType.SKELETON, "スケルトン"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL3, 3, EntityType.SKELETON, "スケルトン"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL4, 4, EntityType.SKELETON, "スケルトン"));

    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL1, 1, EntityType.SPIDER, "クモ"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL2, 2, EntityType.SPIDER, "クモ"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL3, 3, EntityType.SPIDER, "クモ"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL4, 4, EntityType.SPIDER, "クモ"));

    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL1, 1, EntityType.PIG_ZOMBIE, "ゾンビピッグマン"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL2, 2, EntityType.PIG_ZOMBIE, "ゾンビピッグマン"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL3, 3, EntityType.PIG_ZOMBIE, "ゾンビピッグマン"));
    slotList.add(new OneMobDamageUpSlot(SlotLevel.LEVEL4, 4, EntityType.PIG_ZOMBIE, "ゾンビピッグマン"));
  }

  static public void regist() {
    for (OneMobDamageUpSlot oneMobDamageUpSlot : slotList) {
      ItemManager.registItem(oneMobDamageUpSlot);
    }
  }

  public OneMobDamageUpSlot(SlotLevel slotLevel, int damageLevel,
      EntityType targetType, String targetName) {
    this.slotLevel = slotLevel;
    this.damageLevel = damageLevel;
    this.targetType = targetType;
    this.targetName = targetName;
  }

  private SlotLevel slotLevel;
  private int damageLevel;
  private EntityType targetType;
  private String targetName;

  @Override
  public String getSlotName() {
    return getEntityName() + "特攻 +" + getDamageLevel();
  }

  protected String getEntityName() {
    return targetName;
  }

  protected EntityType getTargetType() {
    return targetType;
  }

  @Override
  public String getSlotDetail() {
    return getEntityName() + "に対して" + getAddDamagePer() + "%の追加ダメージ";
  }

  @Override
  public String getId() {
    return "slot_damegeup_" + getTargetType() + "_" + getDamageLevel();
  }

  protected int getDamageLevel() {
    return damageLevel;
  }

  @Override
  public ChatColor getNameColor() {
    return ChatColor.DARK_RED;
  }

  @Override
  public void onCombat(PlayerCombatEntityEvent e) {
    if (e.getEnemy().getType() == getTargetType()) {
      e.setDamage(e.getDamage() * (1.0 + getAddDamagePer() / 100.0));
    }
  }

  /**
   * 9 → 10 → 11 → 12 → 13
   * 
   * @return
   */
  public int getAddDamagePer() {
    return (int) ((9.0 + getDamageLevel()) / 9.0 * 100) - 100;
  }

  @Override
  public SlotLevel getLevel() {
    return slotLevel;
  }
}
