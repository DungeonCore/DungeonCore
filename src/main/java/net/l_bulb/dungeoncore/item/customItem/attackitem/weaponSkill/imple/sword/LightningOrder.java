package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithCombat;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningOrder extends WeaponSkillWithCombat {

  public LightningOrder() {
    super(ItemType.SWORD);
  }

  @Override
  public String getId() {
    return "skill1";
  }

  @Override
  public void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e) {
    LivingEntityUtil.strikeLightningEffect(livingEntity.getLocation());

    Stun.addStun(livingEntity, (int) (20 * getData(1)));

    new BukkitRunnable() {
      @Override
      public void run() {
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (20 * getData(2)), 1), true);
      }
    }.runTaskLater(Main.plugin, (long) (20 * getData(1) + 0.1));
  }

  @Override
  protected void runWaitParticleData(Location loc, int count) {
    Particles.runParticle(loc, ParticleType.flame);
  }

}
