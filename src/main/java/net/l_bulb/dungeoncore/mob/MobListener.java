package net.l_bulb.dungeoncore.mob;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.l_bulb.dungeoncore.chest.CustomChestManager;
import net.l_bulb.dungeoncore.common.event.player.PlayerCustomMobSpawnEvent;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.mob.customMob.BossMobable;
import net.l_bulb.dungeoncore.mob.customMob.SummonMobable;
import net.l_bulb.dungeoncore.player.PlayerChecker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

public class MobListener implements Listener {
  public static Set<BossMobable> bossoList = new HashSet<BossMobable>();

  @EventHandler
  public void onSpawn2(final PlayerCustomMobSpawnEvent e) {
    AbstractMob<?> mob = MobHolder.getMob(e);
    if (mob == null) {
      Bukkit.broadcastMessage(ChatColor.GRAY + e.getName() + "is not registered");
    }
  }

  @EventHandler
  public void onSpawn(final PlayerCustomMobSpawnEvent e) {
    AbstractMob<?> mob = MobHolder.getMob(e);

    // 前のボスが存在する場合は削除する
    if (mob instanceof BossMobable) {
      LivingEntity entity = ((BossMobable) mob).getEntity();
      if (entity == null || !entity.isValid()) {} else {
        entity.remove();
      }
      bossoList.add((BossMobable) mob);
    }

    // アイテムを拾わない
    LivingEntity entity = e.getEntity();
    entity.setCanPickupItems(false);

    // スポーンする
    mob.onSpawn(e);
    mob.updateName(false);

    // もしSummonの場合は指定時間後に削除する
    if (mob.isSummonMob()) {
      new BukkitRunnable() {
        @Override
        public void run() {
          // チャンクがロードされてなかったらロードする
          Location loc = entity.getLocation();
          if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load();
          }
          if (e.getEntity().isValid()) {
            e.getEntity().damage(10000.0);
            SummonPlayerManager.removeSummon(e.getEntity());
          }
        }
      }.runTaskLater(Main.plugin, ((SummonMobable) mob).getDeadlineTick());
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onDagame(EntityDamageByEntityEvent e) {
    // ダメージを与えたEntity
    Entity damager = e.getDamager();
    // ダメージを受けたEntity
    Entity entity = e.getEntity();

    if (MobHolder.isCustomMob(entity)) {
      // mobがダメージを受けるとき
      AbstractMob<?> mob = MobHolder.getMob(entity);
      mob.onDamageBefore((LivingEntity) entity, damager, e);
      mob.onDamage((LivingEntity) entity, damager, e);
      mob.updateName(true);
    }
    if (MobHolder.isCustomMob(damager)) {
      // mobがダメージを与える時
      AbstractMob<?> mob = MobHolder.getMob(damager);
      mob.onAttackBefore((LivingEntity) damager, (LivingEntity) entity, e);
      mob.onAttack((LivingEntity) damager, (LivingEntity) entity, e);
    } else if (damager instanceof Projectile) {
      // mobが放ったProjectileがダメージを与えた時
      ProjectileSource shooter = ((Projectile) damager).getShooter();
      if (MobHolder.isExtraMobByProjectile(shooter) && e.getEntity() instanceof LivingEntity) {
        AbstractMob<?> mob = MobHolder.getMob((LivingEntity) shooter);
        mob.onProjectileHitEntity((LivingEntity) shooter, (LivingEntity) e.getEntity(), e);
      }
    }

    if (entity.getType() == EntityType.VILLAGER) {
      e.setCancelled(true);
    } else if (entity.getType() == EntityType.ARMOR_STAND) {
      // 管理者でないなら攻撃をキャンセルする
      if (!PlayerChecker.isNonNormalPlayer(e.getDamager())) {
        e.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onDamageOther(EntityDamageEvent e) {
    Entity entity = e.getEntity();
    if (MobHolder.isCustomMob(entity)) {
      AbstractMob<?> mob = MobHolder.getMob(entity);
      mob.onOtherDamage(e);
      mob.updateName(false);
    }
  }

  static Random rnd = new Random();

  @EventHandler
  public void onDeath(EntityDeathEvent e) {
    // mobが死んだ時はデフォルトのアイテムドロップはなしにする
    LivingEntity entity = e.getEntity();
    if (entity.getType() != EntityType.PLAYER) {
      e.getDrops().clear();
    }

    AbstractMob<?> mob = MobHolder.getMob(e.getEntity());
    mob.onDeath(e);

    Player lastDamagePlayer = LastDamageManager.getLastDamagePlayer(e.getEntity());
    if (lastDamagePlayer != null) {
      // ドロップアイテムをセットする
      List<ItemStack> dropItem = mob.getDropItem(lastDamagePlayer);
      e.getDrops().addAll(dropItem);
    }

    // もしボスの場合はチェストを設置する
    if (mob.isBoss()) {
      CustomChestManager.setBossRewardChest((BossMobable) mob);
    }
  }

  @EventHandler
  public void onShotbow(EntityShootBowEvent e) {
    AbstractMob<?> mob = MobHolder.getMob(e.getEntity());
    mob.onShotbow(e);
  }

  @EventHandler
  public void onInteractEntity(PlayerInteractEntityEvent e) {
    if (e.getRightClicked() != null && e.getRightClicked() instanceof LivingEntity) {
      AbstractMob<?> mob = MobHolder.getMob(e.getRightClicked());
      mob.onInteractEntity(e);
    }
  }

  @EventHandler
  public void onChangeTarget(EntityTargetLivingEntityEvent event) {
    AbstractMob<?> mob = MobHolder.getMob(event);
    mob.onTarget(event);
  }

  @EventHandler
  public void onTeleport(EntityTeleportEvent e) {
    if (e.getEntityType() == EntityType.ENDERMAN) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onDisablePlugin(PluginDisableEvent e) {
    for (AbstractMob<?> mobs : MobHolder.getAllMobs()) {
      mobs.onDisablePlugin(e);
    }
  }

  @EventHandler
  public void chunkUnload(ChunkUnloadEvent e) {
    Entity[] entities = e.getChunk().getEntities();
    for (Entity entity : entities) {
      if (entity.getType().isAlive()) {
        AbstractMob<?> mob = MobHolder.getMob(entity);
        if (mob != null && mob.isBoss()) {
          entity.remove();
        }
      }
    }
  }

}
