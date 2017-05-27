package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.LbnRunnable;

/**
 * 指定した時間内に指定した回数のクリックを行う際の処理
 *
 */
public abstract class WeaponSkillWithMultiClick extends WeaponSkillForOneType {
  public WeaponSkillWithMultiClick(ItemType type) {
    super(type);
  }

  HashMap<UUID, LbnRunnable> executePlayer = new HashMap<>();

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    // クリックしたPlayerを保存する
    UUID uniqueId = p.getUniqueId();

    // 発動中なら処理をおこなう
    if (executePlayer.containsKey(uniqueId)) {
      boolean isEnd = onClick2(p, item, customItem);
      if (isEnd) {
        // 発動中のパーティクルを終了する
        LbnRunnable remove = executePlayer.remove(uniqueId);
        remove.cancel();
        endSkill(p, item);
      }
      return isEnd;
    }

    // 5tickに一度、パーティクルを発生させる
    LbnRunnable runTaskTimer = new LbnRunnable() {
      @Override
      public void run2() {
        long ageTick = getAgeTick();
        if (ageTick > 20 * getTimeLimit()) {
          executePlayer.remove(uniqueId);
          cancel();
          endSkill(p, item);
        }
        runWaitParticleData(p.getLocation().add(0, 1, 0), getRunCount());
      }
    };
    runTaskTimer.runTaskTimer(1);

    startSkill(p, item);

    executePlayer.put(uniqueId, runTaskTimer);
    return false;
  }

  /**
   * スキル発動時の処理
   *
   * @param p
   * @param item
   */
  abstract protected void startSkill(Player p, ItemStack item);

  /**
   * スキル終了時の処理
   *
   * @param p
   * @param item
   */
  abstract protected void endSkill(Player p, ItemStack item);

  /**
   * 発動中のクリックをした時の処理。
   *
   * @param p
   * @param item
   * @param customItem
   * @return 処理を終了するならTRUE
   */
  abstract protected boolean onClick2(Player p, ItemStack item, AbstractAttackItem customItem);

  /**
   * 効果が続く時間
   *
   * @return
   */
  abstract public double getTimeLimit();

  /**
   * 攻撃待機中のパーティクル
   *
   * @param loc
   * @param i
   */
  abstract protected void runWaitParticleData(Location loc, int i);

  @Override
  public void onCombat(Player p, ItemStack item, CombatItemable customItem, LivingEntity livingEntity, CombatEntityEvent event) {}
}
