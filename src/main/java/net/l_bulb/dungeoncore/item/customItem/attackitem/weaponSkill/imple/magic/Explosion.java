package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.magic;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.dropingEntity.DropingEntityForPlayer;
import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class Explosion extends WeaponSkillForOneType {

  public Explosion() {
    super(ItemType.MAGIC);
  }

  @Override
  public String getId() {
    return "skill6";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    new DropItemImplement(p, customItem).runTaskTimer();
    return true;
  }

  class DropItemImplement extends DropingEntityForPlayer {

    Player p;

    AbstractAttackItem customItem;

    public DropItemImplement(Player p, AbstractAttackItem customItem) {
      super(p.getLocation().getDirection().add(new Vector(0, 0.5, 0)).multiply(0.8), p.getLocation(), Material.NETHER_STAR, (byte) 0);
      this.customItem = customItem;
      this.p = p;
    }

    ParticleData particleDataLava = new ParticleData(ParticleType.flame, 5);

    @Override
    public void tickRutine(int count) {
      super.tickRutine(count);

      if (count % 4 == 0) {
        particleDataLava.run(spawnEntity.getLocation());
      }
    }

    @Override
    protected boolean damageLivingentity(Entity entity) {
      return LivingEntityUtil.isEnemy(entity);
    }

    @Override
    public void onHitDamagedEntity(Entity target) {
      // 生き物のときのみ
      if (target.getType().isAlive()) {
        LivingEntity entity = (LivingEntity) target;
        entity.damage(customItem.getAttackItemDamage(0) * getData(0), p);
        entity.setFireTicks((int) (20 * getData(1)));
        Stun.addStun(entity, (int) (20 * getData(2)));

        Particles.runParticle(target, ParticleType.hugeexplosion, 1);
        target.getWorld().playSound(entity.getLocation(), Sound.EXPLODE, 1, 1);
      }
    }
  }
}
