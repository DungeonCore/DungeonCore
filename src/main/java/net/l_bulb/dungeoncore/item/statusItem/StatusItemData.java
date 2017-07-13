package net.l_bulb.dungeoncore.item.statusItem;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.PlayerStatusType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.player.ability.impl.SetItemAbility;

public class StatusItemData {

  private Optional<SetItemAbility> setItemAbility;

  SetItemPartsType type;

  public StatusItemData(ItemStack item) {
    ItemStackNbttagAccessor itemStackNbttagAccessor = new ItemStackNbttagAccessor(item);
    double addMaxHealth = itemStackNbttagAccessor.getAddMaxHealth();

    // 増加分のHP
    if (addMaxHealth > 0) {
      setItemAbility = Optional.of(new SetItemAbility(getItemPartsType()));
    } else {
      setItemAbility = Optional.empty();
    }
    setItemAbility.ifPresent(a -> a.addData(PlayerStatusType.MAX_HP, addMaxHealth));

    String type = item.getType().toString();
    if (type.contains("BOOTS")) {
      this.type = SetItemPartsType.BOOTS;
    } else if (type.contains("LEGGINGS")) {
      this.type = SetItemPartsType.LEGGINSE;
    } else if (type.contains("CHESTPLATE")) {
      this.type = SetItemPartsType.CHEST_PLATE;
    } else if (type.contains("HELMET") || type.contains("SKULL")) {
      this.type = SetItemPartsType.HELMET;
    }
  }

  /**
   * ステータスアイテムを装備した時の処理
   *
   * @param p
   */
  public void onStartJob(Player p) {
    TheLowPlayerManager.consume(p, p1 -> setItemAbility.ifPresent(p1::addAbility));
  }

  /**
   * ステータスアイテムの装備を解除した時の処理
   *
   * @param p
   */
  public void onEndJob(Player p) {
    TheLowPlayerManager.consume(p, p1 -> setItemAbility.ifPresent(p1::removeAbility));
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
