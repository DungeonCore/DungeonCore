package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset7;

import java.util.HashSet;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

public class IceShot extends SpreadSheetWeaponSkill {

  @Override
  public String getId() {
    return "wskill26";
  }

  static HashSet<Player> damageIncrementFlg = new HashSet<>();

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    damageIncrementFlg.add(p);

    TheLowExecutor.executeLater(getData(0) * 20, () -> damageIncrementFlg.remove(p));

    TheLowExecutor.executeTimer(8, e -> e.isElapsedTick((long) (getData(0) * 20)) || !p.isOnline(),
        e -> Particles.runParticle(p, ParticleType.snowballpoof, 30));
    return true;
  }

  @Override
  public void onCombat(Player p, ItemStack item, CombatItemable customItem, LivingEntity livingEntity, CombatEntityEvent event) {
    // 通常攻撃でない場合は何もしない
    if (!event.isNormalAttack()) { return; }

    if (!damageIncrementFlg.contains(p)) { return; }

    if (livingEntity.hasPotionEffect(PotionEffectType.SLOW)) {
      event.setDamage(event.getDamage() * getData(2));
    } else {
      event.setDamage(event.getDamage() * getData(1));
    }
    p.getWorld().playSound(livingEntity.getLocation(), Sound.ANVIL_BREAK, 1, 10);
  }

}
