package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple;

import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.cooltime.Cooltimable;
import net.l_bulb.dungeoncore.common.cooltime.Cooltimes;
import net.l_bulb.dungeoncore.common.other.BattleTrap;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;

import com.google.common.collect.HashMultimap;

public abstract class WeaponSkillWithTrap extends SpreadSheetWeaponSkill {
  // playerと保持しているTrapのマップ
  protected static HashMultimap<UUID, BattleTrapCooltime> playerTrapMap = HashMultimap.create();

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    // タイマーが切れているTrapを削除する
    removeWithoutCooltimeTrap();

    UUID uniqueId = p.getUniqueId();
    // トラップが最大数になっていないか確認する
    int runningTrapCount = playerTrapMap.get(uniqueId).size();
    if (runningTrapCount > getMaxTrapCount()) {
      p.sendMessage("現在" + getMaxTrapCount() + "つのトラップが稼働中です。");
      return false;
    }

    // トラップを取得
    BattleTrap battleTrap = getBattleTrap(p, item, customItem);
    battleTrap.setTrap(p.getLocation());

    // クールタイム情報を格納
    BattleTrapCooltime cooltime = new BattleTrapCooltime();
    playerTrapMap.put(p.getUniqueId(), cooltime);

    // クールタイム開始
    Cooltimes.setCoolTime(p.getUniqueId(), cooltime, null);

    return false;
  }

  /**
   * トラップを取得
   *
   * @param p 設置するPlayer
   * @param item 設置に使った武器
   * @param customItem
   * @return
   */
  abstract public BattleTrap getBattleTrap(Player p, ItemStack item, AbstractAttackItem customItem);

  /**
   * 最大トラップ設置数を取得
   *
   * @return
   */
  abstract public double getMaxTrapCount();

  /**
   * クールタイムが切れているTrapを削除する
   */
  public void removeWithoutCooltimeTrap() {
    Iterator<UUID> it = playerTrapMap.keySet().iterator();
    while (it.hasNext()) {
      UUID player = it.next();

      // Trap１つづつを確認する
      Iterator<BattleTrapCooltime> iterator = playerTrapMap.get(player).iterator();
      while (iterator.hasNext()) {
        BattleTrapCooltime trap = iterator.next();
        // もう使えるならMapから消す
        if (Cooltimes.canUse(player, trap)) {
          playerTrapMap.remove(player, trap);
        }
      }
    }
  }

  /**
   * 1つあたりのトラップのクールタイムを取得
   *
   * @return
   */
  abstract public double getOneTrapCooltime();

  public class BattleTrapCooltime implements Cooltimable {
    private String id = RandomStringUtils.randomAlphanumeric(6);

    @Override
    public int getCooltimeTick(ItemStack item) {
      return (int) (getOneTrapCooltime() * 20);
    }

    @Override
    public String getId() {
      return id;
    }
  }
}
