package net.l_bulb.dungeoncore.dungeon.contents.mob.animal;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.l_bulb.dungeoncore.common.event.player.PlayerCustomMobSpawnEvent;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.SummonPlayerManager;
import net.l_bulb.dungeoncore.mob.customMob.SummonMobable;
import net.l_bulb.dungeoncore.util.JavaUtil;
import net.l_bulb.dungeoncore.util.LbnRunnable;

public abstract class SummonSheep extends AbstractMob<Sheep> implements SummonMobable {

  public SummonSheep(int level, int strengthLevel) {
    this.level = level;
    this.strengthLevel = strengthLevel;
  }

  int level;
  int strengthLevel;

  @Override
  public int getDeadlineTick() {
    return 25 * 20;
  }

  @Override
  abstract public String getName();

  @Override
  protected Sheep spawnPrivate(Location loc) {
    Sheep spawnPrivate = super.spawnPrivate(loc);
    spawnPrivate.setMetadata("availableLevel", new FixedMetadataValue(Main.plugin, level));
    spawnPrivate.setMetadata("strengthLevel", new FixedMetadataValue(Main.plugin, strengthLevel));
    spawnPrivate.setColor(getColor());
    return spawnPrivate;
  }

  abstract public DyeColor getColor();

  @Override
  public void onSpawn(PlayerCustomMobSpawnEvent e) {
    LivingEntity entity = e.getEntity();
    new LbnRunnable() {
      @Override
      public void run2() {
        if (JavaUtil.isRandomTrue(30)) {
          Player owner = SummonPlayerManager.getOwner(entity);
          if (owner != null) {
            executeRun(entity, owner);
          }
        }

        if (!entity.isValid()) {
          cancel();
        }
      }

      @Override
      protected void runIfServerEnd() {
        entity.remove();
      }
    }.runTaskTimer(20);
  }

  abstract protected void executeRun(LivingEntity entity, Player owner);

  @Override
  public void onAttack(LivingEntity mob, LivingEntity target,
      EntityDamageByEntityEvent e) {}

  @Override
  public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {}

  @Override
  public void onOtherDamage(EntityDamageEvent e) {}

  @Override
  public void onDeathPrivate(EntityDeathEvent e) {}

  @Override
  public EntityType getEntityType() {
    return EntityType.SHEEP;
  }
}
