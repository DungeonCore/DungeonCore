package lbn.common.other;

import java.util.HashSet;
import java.util.UUID;

import lbn.common.particle.ParticleData;
import lbn.common.particle.ParticleType;
import lbn.dungeoncore.Main;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Stun {
  static HashSet<UUID> stunList = new HashSet<UUID>();

  static ParticleData particleData = new ParticleData(ParticleType.instantSpell, 70);
  {
    particleData.setDispersion(1, 1, 1);
  }

  /**
   * スタン状態にする
   * 
   * @param e
   * @param tick
   */
  public static void addStun(LivingEntity e, int tick) {
    if (isStun(e)) { return; }

    final UUID id = e.getUniqueId();
    stunList.add(id);

    e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, tick, 127), true);

    e.getWorld().playSound(e.getLocation(), Sound.ENDERDRAGON_HIT, 1f, 0.5f);
    particleData.run(e.getLocation().add(0, 1.5, 0));

    new BukkitRunnable() {
      @Override
      public void run() {
        stunList.remove(id);
      }
    }.runTaskLater(Main.plugin, tick);
  }

  public static void onClick(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    if (isStun(player)) {
      e.setCancelled(true);
    }
  }

  public static void onDamage(EntityDamageByEntityEvent e) {
    Entity damager = e.getDamager();
    if (damager instanceof LivingEntity) {
      if (isStun((LivingEntity) damager)) {
        e.setCancelled(true);
      }
    }
  }

  public static void onShootbow(EntityShootBowEvent e) {
    LivingEntity entity = e.getEntity();
    if (isStun(entity)) {
      e.setCancelled(true);
    }
  }

  /**
   * スタン状態ならTRUE
   * 
   * @param e
   * @return
   */
  public static boolean isStun(LivingEntity e) {
    return stunList.contains(e.getUniqueId());
  }

  /**
   * スタン状態を解除する
   * 
   * @param e
   */
  public static void removeStun(LivingEntity e) {
    stunList.remove(e);
    e.removePotionEffect(PotionEffectType.SLOW);
    e.removePotionEffect(PotionEffectType.JUMP);
  }
}
