package net.l_bulb.dungeoncore.item.statusItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.PlayerStatusType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.player.ability.impl.SetItemAbility;

public class StatusItemData {

  private SetItemAbility setItemAbility;

  SetItemPartsType type;

  public StatusItemData(ItemStack item) {
    ItemStackNbttagAccessor itemStackNbttagAccessor = new ItemStackNbttagAccessor(item);
    double addMaxHealth = itemStackNbttagAccessor.getAddMaxHealth();

    String type = item.getType().toString();
    if (type.contains("BOOTS")) {
      this.type = SetItemPartsType.BOOTS;
    } else if (type.contains("LEGGINGS")) {
      this.type = SetItemPartsType.LEGGINSE;
    } else if (type.contains("CHESTPLATE")) {
      this.type = SetItemPartsType.CHEST_PLATE;
    } else if (type.contains("HELMET") || type.contains("SKULL")) {
      this.type = SetItemPartsType.HELMET;
    } else {
      throw new RuntimeException("防具として不正なアイテムです。:" + type);
    }

    // 増加分のHP
    if (addMaxHealth > 0) {
      setItemAbility = new SetItemAbility(getItemPartsType());
      setItemAbility.addData(PlayerStatusType.MAX_HP, addMaxHealth);
    }

  }

  /**
   * ステータスアイテムを装備した時の処理
   *
   * @param p
   */
  public void onStartJob(Player p) {
    if (setItemAbility != null) {
      TheLowPlayerManager.consume(p, thelowPlayer -> thelowPlayer.addAbility(setItemAbility));
    }
  }

  /**
   * ステータスアイテムの装備を解除した時の処理
   *
   * @param p
   */
  public void onEndJob(Player p) {
    if (setItemAbility != null) {
      TheLowPlayerManager.consume(p, thelowPlayer -> thelowPlayer.removeAbility(setItemAbility));
    }
  }

  /**
   * 繰り返し処理を行う
   *
   * @param p
   */
  public void onRoutine(Player p) {}

  /**
   * この装備のアイテム種類を取得する
   *
   * @return
   */
  public SetItemPartsType getItemPartsType() {
    return type;
  }
}
