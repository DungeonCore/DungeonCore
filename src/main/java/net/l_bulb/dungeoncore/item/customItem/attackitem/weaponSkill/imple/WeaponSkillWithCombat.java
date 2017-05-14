package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.LbnRunnable;

public abstract class WeaponSkillWithCombat extends WeaponSkillForOneType {
  public WeaponSkillWithCombat(ItemType type) {
    super(type);
  }

  HashMap<UUID, BukkitTask> executePlayer = new HashMap<>();

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    // クリックしたPlayerを保存する
    UUID uniqueId = p.getUniqueId();

    // 5tickに一度、パーティクルを発生させる
    BukkitTask runTaskTimer = new LbnRunnable() {
      @Override
      public void run2() {
        long ageTick = getAgeTick();
        if (ageTick > 20 * getTimeLimit()) {
          executePlayer.remove(uniqueId);
          cancel();
        }
        runWaitParticleData(p.getLocation().add(0, 1, 0), getRunCount());
      }

    }.runTaskTimer(5);
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
    if (isWaitingSkill(p)) {
      onCombat2(p, item, customItem, livingEntity, event);
      BukkitTask remove = executePlayer.remove(p.getUniqueId());
      remove.cancel();
    }
  }

  /**
   * スキル発動待機中ならTRUE
   * 
   * @param p
   * @return
   */
  public boolean isWaitingSkill(Player p) {
    return executePlayer.containsKey(p.getUniqueId());
  }

  /**
   * 効果発動条件を満たして攻撃を行うときの処理
   * 
   * @param p
   * @param item
   * @param customItem
   * @param livingEntity
   * @param e
   */
  abstract protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e);
}
