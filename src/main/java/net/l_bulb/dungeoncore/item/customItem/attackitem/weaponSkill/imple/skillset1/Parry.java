package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset1;

import java.util.Collection;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.util.MinecraftUtil;

public class Parry extends SpreadSheetWeaponSkill {

  @Override
  public String getId() {
    return "wskill3";
  }

  final PotionEffect SLOW_POTION = new PotionEffect(PotionEffectType.SLOW, (int) (20 * getData(0)), 100);
  final PotionEffect REGENERATION_POTION = new PotionEffect(PotionEffectType.REGENERATION, (int) (20 * getData(0)), 100);

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    // ポーションエフェクトをつける
    p.addPotionEffect(SLOW_POTION, true);
    p.addPotionEffect(REGENERATION_POTION, true);

    // パーティクル
    Particles.runCircleParticle(p, ParticleType.portal, 5, 10);
    // 音
    MinecraftUtil.playSoundForAll(p.getLocation(), Sound.ANVIL_LAND, 1, 10);
    return true;
  }

  @Override
  public void onDamage(Player player, ItemStack item, EntityDamageByEntityEvent e) {
    // 攻撃者が生き物でないなら何もしない
    if (!e.getEntity().getType().isAlive()) { return; }

    // ポーションエフェクトが存在するなら実行
    Collection<PotionEffect> activePotionEffects = player.getActivePotionEffects();
    if (activePotionEffects.contains(SLOW_POTION) && activePotionEffects.contains(REGENERATION_POTION)) {
      // customItemがnullはあり得ないので無視
      CombatItemable customItem = ItemManager.getCustomItem(CombatItemable.class, item);
      new CombatEntityEvent(player, customItem.getAttackItemDamage(item) * getData(1), customItem, item, false, (LivingEntity) e.getEntity())
          .callEvent()
          .damageEntity();
    }
  }

}
