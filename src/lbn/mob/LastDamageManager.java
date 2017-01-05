package lbn.mob;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import lbn.player.AttackType;
import lbn.util.DungeonLogger;
import lbn.util.ItemStackUtil;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class LastDamageManager {
  static HashMap<Integer, Player>     entityPlayerMap     = new HashMap<>();
  
  static HashMap<Integer, AttackType> entityAttackTypeMap = new HashMap<>();
  
  static Queue<Integer>               idList              = new LinkedList<Integer>();
  
  public static void onDamage(LivingEntity e, Player p, AttackType type) {
    if (e == null || p == null) {
      return;
    }
    
    if (e.getType() != EntityType.PLAYER && !SummonPlayerManager.isSummonMob(e)) {
      int id = e.getEntityId();
      addData(p, type, id);
    }
  }
  
  protected static void addData(Player p, AttackType type, int id) {
    // 常に21000以下になるようにする
    if (idList.size() > 25000) {
      while (25000 - 5000 < idList.size()) {
        Integer remove = idList.remove();
        entityPlayerMap.remove(remove);
        entityAttackTypeMap.remove(remove);
      }
      DungeonLogger.development("Last Damage Manaer size over 25000");
    }
    idList.remove(id);
    idList.add(id);
    entityPlayerMap.put(id, p);
    entityAttackTypeMap.put(id, type);
  }
  
  public static void onDamage(LivingEntity e, LivingEntity summonMob) {
    if (e.getType() == EntityType.PLAYER || SummonPlayerManager.isSummonMob(e)) {
      return;
    }
    Player owner = SummonPlayerManager.getOwner(summonMob);
    if (owner != null) {
      addData(owner, AttackType.MAGIC, e.getEntityId());
    }
  }
  
  public static void onDamage(LivingEntity e, Projectile projectile) {
    if (e.getType() == EntityType.PLAYER || SummonPlayerManager.isSummonMob(e)) {
      return;
    }
    ProjectileSource shooter = projectile.getShooter();
    if (shooter == null) {
      return;
    }
    if (shooter instanceof Player) {
      addData((Player) shooter, AttackType.BOW, e.getEntityId());
    } else if (shooter instanceof Monster) {
      onDamage(e, (Monster) shooter);
    }
  }
  
  public static Player getLastDamagePlayer(Entity e) {
    return entityPlayerMap.get(e.getEntityId());
  }
  
  public static AttackType getLastDamageAttackType(Entity e) {
    AttackType attackType = entityAttackTypeMap.get(e.getEntityId());
    if (attackType == null) {
      return AttackType.IGNORE;
    } else {
      return attackType;
    }
  }
  
  public static void remove(Entity e) {
    entityAttackTypeMap.remove(e.getEntityId());
    entityPlayerMap.remove(e.getEntityId());
  }
  
  
  /**
   * LastDamageを登録する 弓と剣とsummon
   *
   * @param e
   */
  public static void setLastDamageStatic(EntityDamageByEntityEvent e) {
    if (e.isCancelled()) {
      return;
    }
    
    // ダメージを与えたモブ
    Entity damager = e.getDamager();
    
    // ダメージを受けたモブ
    if (!(e.getEntity() instanceof LivingEntity)) {
      return;
    }
    LivingEntity entityDamaged = (LivingEntity) e.getEntity();
    
    EntityType type = damager.getType();
    if (type == null || type == EntityType.UNKNOWN) {
      return;
    }
    
    // ダメージを与えたのがPlayerでないなら無視
    if (type == EntityType.PLAYER) {
      Player p = (Player) damager;
      onPlayerDamage(p, entityDamaged);
    } else if (type == EntityType.ARROW) {
      onBowDamage((Arrow) damager, entityDamaged);
    }
    
    // 攻撃者がSummonなら魔法ダメージとする
    if (SummonPlayerManager.isSummonMob(damager)) {
      onMagicDamage(SummonPlayerManager.getOwner(damager), entityDamaged);
    }
  }
  
  private static void onMagicDamage(Player owner, LivingEntity entity) {
    if (owner == null || entity == null) {
      return;
    }
    LastDamageManager.onDamage(entity, owner, AttackType.MAGIC);
  }
  
  private static void onBowDamage(Projectile projectile, LivingEntity entity) {
    LastDamageManager.onDamage(entity, projectile);
  }
  
  private static void onPlayerDamage(Player p, LivingEntity entity) {
    if (p == null || entity == null) {
      return;
    }
    ItemStack itemInHand = p.getItemInHand();
    // ダメージを与えたのが剣
    if (ItemStackUtil.isSword(itemInHand)) {
      LastDamageManager.onDamage(entity, p, AttackType.SWORD);
    }
  }
}
