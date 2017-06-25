package net.l_bulb.dungeoncore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftAnimals;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftMonster;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.mob.SummonPlayerManager;
import net.l_bulb.dungeoncore.mob.minecraftEntity.CustomVillager;
import net.l_bulb.dungeoncore.npc.NpcManager;

import net.minecraft.server.v1_8_R1.EntityAnimal;
import net.minecraft.server.v1_8_R1.EntityLightning;
import net.minecraft.server.v1_8_R1.EntityMonster;
import net.minecraft.server.v1_8_R1.PacketPlayOutSpawnEntityWeather;

public class LivingEntityUtil {
  public static boolean isEnemy(Entity e) {
    EntityType type = e.getType();

    // 生き物でないならFALSE
    if (!e.getType().isAlive()) { return false; }

    // プレイヤーか村人ならFALSE
    if (type == EntityType.PLAYER || type == EntityType.VILLAGER) { return false; }

    // ゾンビビッグマンで怒っていないならFALSE
    if (type == EntityType.PIG_ZOMBIE) {
      if (!((PigZombie) e).isAngry()) { return false; }
    }

    // //動物ならFALSE
    // if (e instanceof Animals) {
    // //狼で怒っていないならFALSE
    // if (type == EntityType.WOLF) {
    // if (!((Wolf)e).isAngry()) {
    // return false;
    // }
    // } else {
    // //狼でないならFALSE
    // return false;
    // }
    // }

    // NPCならTRUE
    if (NpcManager.isNpc(e)) { return false; }

    // スノーゴーレムならFALSE
    if (type == EntityType.SNOWMAN) { return false; }

    if (type == EntityType.ARMOR_STAND) { return false; }

    if (SummonPlayerManager.isSummonMob(e)) { return false; }
    return true;
  }

  public static boolean isCustomVillager(Entity entity) {
    return (((CraftEntity) entity).getHandle() instanceof CustomVillager);
  }

  public static EntityEquipment setEquipment(LivingEntity e, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots,
      float chance) {
    EntityEquipment equipment = e.getEquipment();
    equipment.setHelmet(getNull(helmet));
    equipment.setChestplate(getNull(chestplate));
    equipment.setLeggings(getNull(leggings));
    equipment.setBoots(getNull(boots));
    if (e.getType() != EntityType.PLAYER) {
      equipment.setHelmetDropChance(chance);
      equipment.setChestplateDropChance(chance);
      equipment.setLeggingsDropChance(chance);
      equipment.setBootsDropChance(chance);
    }
    return equipment;
  }

  public static ItemStack getNull(ItemStack item) {
    if (item == null) { return new ItemStack(Material.AIR); }
    return item;
  }

  public static EntityEquipment setEquipment(LivingEntity e, ItemStack[] randomArmors, float chance) {
    return setEquipment(e, randomArmors[0], randomArmors[1], randomArmors[2], randomArmors[3], 0);
  }

  public static void removeEquipment(LivingEntity e) {
    removeEquipment(e, true);
  }

  public static void removeEquipment(LivingEntity e, boolean removeItemInHand) {
    EntityEquipment equipment = e.getEquipment();
    ItemStack air = new ItemStack(Material.AIR);
    equipment.setBoots(air);
    equipment.setLeggings(air);
    equipment.setChestplate(air);
    equipment.setHelmet(air);
    if (removeItemInHand) {
      equipment.setItemInHand(air);
    }
  }

  public static EntityEquipment setItemInHand(LivingEntity e, ItemStack item, float chance) {
    EntityEquipment equipment = e.getEquipment();
    equipment.setItemInHand(item);
    equipment.setItemInHandDropChance(0);
    return equipment;
  }

  /**
   * 周りのプレイヤーを取得
   *
   * @param e
   * @param x
   * @param y
   * @param z
   * @return
   */
  public static ArrayList<Player> getNearByPlayer(Entity e, double x, double y, double z) {
    ArrayList<Player> rtn = new ArrayList<>();
    for (Entity entity : e.getNearbyEntities(x, y, z)) {
      if (entity.getType() == EntityType.PLAYER) {
        rtn.add((Player) entity);
      }
    }
    return rtn;
  }

  /**
   * 指定したEntityの周囲にいるPlayerと友好的なmobを取得
   *
   * @param e
   * @param x
   * @param y
   * @param z
   * @return
   */
  public static ArrayList<LivingEntity> getNearFrendly(Entity e, double x, double y, double z) {
    ArrayList<LivingEntity> rtn = new ArrayList<>();
    for (Entity entity : e.getNearbyEntities(x, y, z)) {
      if (isFriendship(entity)) {
        rtn.add((LivingEntity) entity);
      }
    }
    return rtn;
  }

  /**
   * 指定したEntityの周囲にいるPlayerと敵対するmobを取得
   *
   * @param e
   * @param x
   * @param y
   * @param z
   * @return
   */
  public static ArrayList<LivingEntity> getNearEnemy(Entity e, double x, double y, double z) {
    ArrayList<LivingEntity> rtn = new ArrayList<>();
    for (Entity entity : e.getNearbyEntities(x, y, z)) {
      if (isEnemy(entity)) {
        rtn.add((LivingEntity) entity);
      }
    }
    return rtn;
  }

  /**
   * handleのクラス名を取得
   *
   * @param e
   * @return
   */
  public static String getHandleClassName(Entity e) {
    if (e instanceof CraftMonster) {
      EntityMonster handle = ((CraftMonster) e).getHandle();
      return handle.getClass().getSimpleName();
    }
    if (e instanceof CraftAnimals) {
      EntityAnimal handle = ((CraftAnimals) e).getHandle();
      return handle.getClass().getSimpleName();
    }
    return null;
  }

  public static boolean isFriendship(Entity e) {
    if (e.getType() == EntityType.PLAYER) {
      return true;
    } else if (e.getType() == EntityType.VILLAGER) {
      return true;
    } else if (e.getType().isAlive()) {
      return SummonPlayerManager.isSummonMob(e);
    } else {
      return false;
    }
  }

  public static void addHealth(LivingEntity e, double val) {
    double nextHealth = ((Damageable) e).getHealth() + val;

    if (nextHealth <= 0) {
      if (e.getType() == EntityType.BAT) {
        nextHealth = 0;
      } else {
        nextHealth = 0.01;
      }
    } else if (nextHealth > ((Damageable) e).getMaxHealth()) {
      nextHealth = ((Damageable) e).getMaxHealth();
    }

    e.setHealth(nextHealth);
  }

  // 一旦コメントアウト
  // public static void trueDamage(LivingEntity target, double val, LivingEntity damager, LastDamageMethodType type, ItemStack item) {
  // // ダメージを与える対象がクリエイティブなら何もしない
  // if (target.getType() == EntityType.PLAYER && ((Player) target).getGameMode() == GameMode.CREATIVE) { return; }
  //
  // val *= -1;
  // target.damage(0.0);
  // // HP処理
  // addHealth(target, val);
  // }

  public static void setNoFallDamage(Player player) {
    new BukkitRunnable() {
      int count = 0;

      @Override
      public void run() {
        if (count == 60 * 20) {
          cancel();
        }

        if (player.isDead() || !player.isOnline() || player.getGameMode() == GameMode.CREATIVE) {
          cancel();
        }

        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
          cancel();
        }
        count++;
        player.setFallDistance(0);
      }
    }.runTaskTimer(Main.plugin, 20, 2);
  }

  public static void setNoDamageTick(LivingEntity entity, int tick) {
    new BukkitRunnable() {
      @Override
      public void run() {
        entity.setNoDamageTicks(tick);
      }
    }.runTaskLater(Main.plugin, 1);
  }

  public static void setNoDamageTickZero(LivingEntity entity) {
    setNoDamageTick(entity, 0);
  }

  /**
   * 雷のエフェクトを周囲のPlayerの知らせる
   *
   * @param location
   */
  public static void strikeLightningEffect(Location location) {
    World world = location.getWorld();
    List<Player> players = world.getPlayers();
    for (Player player : players) {
      double x = player.getLocation().getX() - location.getX();
      double y = player.getLocation().getY() - location.getY();
      double z = player.getLocation().getZ() - location.getZ();
      if ((x * x) + (y * y) + (z * z) < 30 * 30) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityWeather(
            new EntityLightning(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(), true)));
        player.playSound(location, Sound.AMBIENCE_THUNDER, 1, 1);
      }
    }
  }

  public static void strikeLightningEffect(Location location, Player... p) {
    if (p == null) { return; }

    for (Player player : p) {
      if (player == null) {
        continue;
      }
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityWeather(
          new EntityLightning(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(), true)));
      player.playSound(location, Sound.AMBIENCE_THUNDER, 1, 1);
    }
  }

  public static void strikeLightningEffect(Location location, Collection<Player> p) {
    if (p == null) { return; }

    for (Player player : p) {
      if (player == null) {
        continue;
      }
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityWeather(
          new EntityLightning(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(), true)));
      player.playSound(location, Sound.AMBIENCE_THUNDER, 1, 1);
    }
  }

  public static boolean isAnimal(EntityType type) {
    if (type == null) { return false; }

    switch (type) {
      case COW:
      case BAT:
      case SHEEP:
      case PIG:
      case CHICKEN:
      case SQUID:
      case WOLF:
      case MUSHROOM_COW:
      case OCELOT:
      case HORSE:
      case RABBIT:
        return true;
      default:
        return false;
    }
  }
}
