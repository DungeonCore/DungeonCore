package lbn.item.customItem.attackitem.weaponSkill.imple;

import java.util.HashMap;
import java.util.UUID;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.player.ItemType;
import lbn.util.LbnRunnable;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 指定した時間内に指定した回数の攻撃を行う際の処理
 *
 */
public abstract class WeaponSkillWithMultiCombat extends WeaponSkillForOneType {
  public WeaponSkillWithMultiCombat(ItemType type) {
    super(type);
  }

  HashMap<UUID, LbnRunnableImplemantion> executePlayer = new HashMap<>();

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    // クリックしたPlayerを保存する
    UUID uniqueId = p.getUniqueId();

    // 5tickに一度、パーティクルを発生させる
    LbnRunnableImplemantion runTaskTimer = new LbnRunnableImplemantion(p) {
      @Override
      public void run2() {
        long ageTick = getAgeTick();
        if (ageTick > 20 * getTimeLimit()) {
          executePlayer.remove(uniqueId);
          cancel();
        }
        runWaitParticleData(p.getLocation().add(0, 1, 0), getRunCount());
      }
    };
    runTaskTimer.runTaskTimer(1);
    executePlayer.put(uniqueId, runTaskTimer);

    return true;
  }

  /**
   * 効果が続く時間
   * 
   * @return
   */
  public double getTimeLimit() {
    return 5;
  }

  /**
   * 攻撃待機中のパーティクル
   * 
   * @param loc
   * @param i
   */
  abstract protected void runWaitParticleData(Location loc, int i);

  @Override
  public void onCombat(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent event) {
    if (executePlayer.containsKey(p.getUniqueId())) {
      LbnRunnableImplemantion implemantion = executePlayer.get(p.getUniqueId());
      // カウントを増加させる
      implemantion.incremantCount();
      // スキルを実行
      onCombat2(p, item, customItem, livingEntity, event, implemantion.getCount());
      // 指定回数攻撃したら終わり
      if (getMaxAttackCount() <= implemantion.getCount()) {
        implemantion.cancel();
        executePlayer.remove(p.getUniqueId());
      }
    }
  }

  /**
   * 攻撃を行った時の効果が発動する最大回数を取得
   * 
   * @return
   */
  abstract protected int getMaxAttackCount();

  /**
   * 効果発動条件を満たして攻撃を行うときの処理
   * 
   * @param p
   * @param item
   * @param customItem
   * @param livingEntity
   * @param e
   * @param attackCount
   */
  abstract protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e,
      int attackCount);

  class LbnRunnableImplemantion extends LbnRunnable {
    Player p;

    public LbnRunnableImplemantion(Player p) {
      this.p = p;
    }

    int count = 0;

    /**
     * カウントを1増加する
     * 
     * @return
     */
    public void incremantCount() {
      count++;
    }

    /**
     * カウントを取得
     * 
     * @return
     */
    public int getCount() {
      return count;
    }

    @Override
    public void run2() {
      long ageTick = getAgeTick();
      if (ageTick > 20 * getTimeLimit()) {
        cancel();
      }
      runWaitParticleData(p.getLocation().add(0, 1, 0), getRunCount());
    }
  }
}
