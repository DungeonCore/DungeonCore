package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset1;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.util.LbnRunnable;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

public class DeathDance extends SpreadSheetWeaponSkill {

  @Override
  public String getId() {
    return "wskill4";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    // 剣舞の監視スタート
    Kenbu.startCaptuer(p);

    // 監視終了
    TheLowExecutor.executeLater((long) (20 * getData(0)), () -> {
      if (Kenbu.endCaptuer(p) >= getData(5)) {
        executeHeal(p);
      }
    });
    return true;
  }

  /**
   * デスダンスの効果を発動する
   *
   * @param entity
   */
  private void executeHeal(Player p) {
    new LbnRunnable() {
      @Override
      public void run2() {
        LivingEntityUtil.getNearFrendly(p, getData(1), getData(1), getData(1)).stream()
            .peek(e -> LivingEntityUtil.addHealth(e, getData(3) * 3))
            .forEach(e -> Particles.runParticle(e.getLocation().add(0, 1, 0), ParticleType.heart));

        // 指定した時間以上たってるなら終了する
        if (getAgeTick() > getData(4) * 20) {
          cancel();
        }
      }
    }.runTaskTimer((long) (20 * getData(2)));
  }

}
