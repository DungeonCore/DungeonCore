package lbn.item.customItem.attackitem.weaponSkill.imple.sword;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.other.Stun;
import lbn.common.particle.ParticleType;
import lbn.common.particle.Particles;
import lbn.dungeoncore.Main;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithCombat;
import lbn.player.ItemType;
import lbn.util.LivingEntityUtil;

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
