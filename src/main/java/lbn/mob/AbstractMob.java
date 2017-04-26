package lbn.mob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.api.player.TheLowPlayer;
import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.dungeoncore.Main;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.mob.customMob.BossMobable;
import lbn.mob.customMob.LbnMobTag;
import lbn.mob.customMob.SummonMobable;
import lbn.player.status.StatusAddReason;
import lbn.quest.QuestProcessingStatus;
import lbn.quest.abstractQuest.PickItemQuest;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;
import lbn.util.JavaUtil;
import lbn.util.Message;

public abstract class AbstractMob<T extends Entity> {
  protected Random rnd = new Random();

  boolean isBoss = this instanceof BossMobable;

  public boolean isBoss() {
    return isBoss;
  }

  boolean isSummon = this instanceof SummonMobable;

  public boolean isSummonMob() {
    return isSummon;
  }

  /**
   * LbnNBTTagを取得
   * 
   * @return
   */
  public LbnMobTag getLbnMobTag() {
    return new LbnMobTag(getEntityType());
  }

  abstract public String getName();

  abstract public void onSpawn(PlayerCustomMobSpawnEvent e);

  public void onAttackBefore(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {}

  public boolean isRiding() {
    return false;
  }

  abstract public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e);

  abstract public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e);

  public int getDropGalions() {
    return 10;
  }

  /**
   * クリエイティブの時、ダメージ量を表示させる
   * 
   * @param mob
   * @param damager
   * @param e
   */
  public void onDamageBefore(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
    if (isSummonMob()) { return; }

    Player player = LastDamageManager.getLastDamagePlayer(mob);
    LastDamageMethodType type = LastDamageManager.getLastDamageAttackType(mob);
    if (player == null) { return; }
    e.setDamage(e.getDamage());

    if (mob.getType().isAlive()) {
      // クエリの時だけダメージを表示させる
      if (player.getGameMode() == GameMode.CREATIVE) {
        new BukkitRunnable() {
          double health = ((Damageable) mob).getHealth();

          @Override
          public void run() {
            Message.sendMessage(player, "{0}:{1}ダメージ！！(MobHP : {2}/{3})",
                type.getText(), JavaUtil.round(health - ((Damageable) mob).getHealth(), 2),
                JavaUtil.round(((Damageable) mob).getHealth(), 2), JavaUtil.round(((Damageable) mob).getMaxHealth(), 2));
          }
        }.runTaskLater(Main.plugin, 2);
      }
    }
  }

  abstract public void onOtherDamage(EntityDamageEvent e);

  public void onDeath(EntityDeathEvent e) {
    // もしモンスターの上にモンスターが載っているとして上のモンスターが死んだら下のモンスターも殺す
    if (isRiding()) {
      List<Entity> nearbyEntities = e.getEntity().getNearbyEntities(1, 1, 1);
      for (Entity entity : nearbyEntities) {
        if (entity.getType().isAlive()) {
          if (((LivingEntity) entity).getPassenger() != null && ((LivingEntity) entity).getPassenger().isDead()) {
            entity.remove();
          }
        }
      }
    }
    onDeathPrivate(e);
  }

  abstract public void onDeathPrivate(EntityDeathEvent e);

  public void onShotbow(EntityShootBowEvent e) {}

  public void onInteractEntity(PlayerInteractEntityEvent e) {

  }

  /**
   * モンスターがProjectileを発射した時
   * 
   * @param mob
   * @param target
   * @param e
   */
  public void onProjectileHitEntity(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {}

  /**
   * Null mobならTRUE
   * 
   * @return
   */
  public boolean isNullMob() {
    return false;
  }

  /**
   * モンスターがターゲットを定めた時
   * 
   * @param event
   */
  public void onTarget(EntityTargetLivingEntityEvent event) {}

  /**
   * モンスターをスポーンさせる
   * 
   * @param loc
   * @return
   */
  final public T spawn(Location loc) {
    T entity = spawnPrivate(loc);
    if (entity == null) { return null; }

    if ((getName() != null || !getName().isEmpty()) && entity.getType().isAlive()) {
      ((LivingEntity) entity).setCustomName(getName());
    }

    // ボスと召喚モブの時は名前をずっと表示する
    if ((isBoss() || SummonPlayerManager.isSummonMob(entity)) && entity.getType().isAlive()) {
      ((LivingEntity) entity).setCustomNameVisible(true);
    }

    if (entity.getType().isAlive()) {
      // eventを発火させる
      PlayerCustomMobSpawnEvent event = new PlayerCustomMobSpawnEvent((LivingEntity) entity);
      Bukkit.getServer().getPluginManager().callEvent(event);
    }

    return entity;
  }

  @SuppressWarnings("unchecked")
  protected T spawnPrivate(Location loc) {
    T spawnCreature = (T) loc.getWorld().spawnEntity(loc, getEntityType());
    return spawnCreature;
  }

  abstract public EntityType getEntityType();

  public boolean isThisMob(LivingEntity e) {
    if (e.getCustomName() == null) {
      if (getName() == null) { return true; }
    } else {
      return e.getCustomName().equalsIgnoreCase(getName());
    }

    return false;
  }

  /**
   * ドロップするアイテムを取得する
   * 
   * @param lastDamagePlayer
   * @return
   */
  public List<ItemStack> getDropItem(Player lastDamagePlayer) {
    return new ArrayList<>();
  }

  /**
   * 現在の指定されたアイテムの中で現在受けていないクエストアイテムを削除する
   * 
   * @param p
   * @param itemList
   */
  public static void removeOtherQuestItem(Player p, List<ItemStack> itemList) {
    // DropItemが存在しないなら何もしない
    if (itemList.isEmpty()) { return; }

    PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p.getPlayer());
    ;

    // DROPするのがクエストアイテムの場合、クエスト進行中でないからドロップさせない
    Iterator<ItemStack> iterator = itemList.iterator();
    label1: while (iterator.hasNext()) {
      ItemStack next = iterator.next();
      ItemInterface customItem = ItemManager.getCustomItem(next);
      // カスタムアイテムでないなら何もしない
      if (customItem == null) {
        continue;
      }
      // クエストアイテムでないなら何もしない
      if (!customItem.isQuestItem()) {
        continue;
      }
      // クエスト実行中か調べる
      Set<PickItemQuest> quest = PickItemQuest.getQuest(customItem);
      for (PickItemQuest pickItemQuest : quest) {
        // 1つでもクエストが実行中なら許可する
        if (questSession.getProcessingStatus(pickItemQuest) == QuestProcessingStatus.PROCESSING) {
          continue label1;
        }
      }
      // 一つも関連クエストが実行されていないなら許可しない
      iterator.remove();
    }
  }

  /**
   * モンスターの名前を更新
   * 
   * @param islater
   */
  public void updateName(boolean islater) {}

  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof AbstractMob<?>) { return ((AbstractMob<?>) obj).getName().equals(getName())
        && ((AbstractMob<?>) obj).getEntityType() == getEntityType(); }
    return false;
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }

  public int getExp(LastDamageMethodType type) {
    return -1;
  }

  public void onDisablePlugin(PluginDisableEvent e) {

  }

  /**
   * PlayerにExpを加算させる
   * 
   * @param entity
   * @param type
   * @param p
   */
  public void addExp(LivingEntity entity, LastDamageMethodType type, TheLowPlayer p) {
    // コウモリの場合は経験値を加算しない
    if (entity.getType() == EntityType.BAT) { return; }

    // EXPを与える対象のLEVEL TYPEが存在しない時は無視
    if (type.getLevelType() == null) { return; }

    // EXPを加算する
    AbstractMob<?> mob = MobHolder.getMob(entity);
    int exp = getExp(type);
    double health = ((Damageable) entity).getMaxHealth();

    // 体力の1.3倍
    if (mob.isBoss()) {
      exp = (int) (health * 1.3);
    } else {
      // 9~16まで体力に合わせて経験値を決める
      if (exp == -1) {
        if (health < 20) {
          exp = 9;
        } else if (health > 40) {
          exp = 16;
        } else {
          exp = (int) ((health - 20.0) * 6.0 / 20 + 10);
        }
      }
    }
    p.addExp(type.getLevelType(), exp, StatusAddReason.monster_drop);
  }
}
