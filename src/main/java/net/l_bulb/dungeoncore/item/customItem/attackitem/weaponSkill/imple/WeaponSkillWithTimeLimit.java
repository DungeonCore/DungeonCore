package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

import com.google.gdata.util.common.base.Pair;

public abstract class WeaponSkillWithTimeLimit extends SpreadSheetWeaponSkill {

  static Set<Pair<UUID, String>> onActiveList = new HashSet<>();

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    ItemStackNbttagAccessor accessor = new ItemStackNbttagAccessor(item);
    String serialData = accessor.getSerialData();

    Pair<UUID, String> pair = Pair.of(p.getUniqueId(), serialData);

    // 効果発動中リストに格納する
    onActiveList.add(pair);

    TheLowExecutor.executeLater(getData(0) * 20.0, () -> {
      // 時間がたったらMapから削除する
      onActiveList.remove(pair);
      // 処理を実行する
      offActive(p, item, customItem);
    });
    return true;
  }

  /**
   * 効果が切れるときの処理
   */
  abstract protected void offActive(Player p, ItemStack item, AbstractAttackItem customItem);

  /**
   * 効果発動中ならTRUE
   *
   * @param p
   * @param item
   * @return
   */
  public boolean isActive(Player p, ItemStack item) {
    return onActiveList.contains(Pair.of(p, new ItemStackNbttagAccessor(item).getSerialData()));
  }
}