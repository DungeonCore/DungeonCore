package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.dropingEntity.DropingEntityForPlayer;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;

public abstract class WeaponSkillWithThrowItem extends SpreadSheetWeaponSkill {

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    new DropItemImplement(p).runTaskTimer();
    return true;
  }

  /**
   * 投げたアイテムが着地したときの処理
   *
   * @param p
   * @param item
   * @param spawnEntity
   */
  abstract public void onGround(Player p, ItemStack item, Entity spawnEntity);

  /**
   * 投げるアイテムのマテリアル
   *
   * @return
   */
  abstract public Material getThrowItemType();

  /**
   * 投げるアイテムのデータ
   *
   * @return
   */
  public byte getThrowItemData() {
    return 0;
  }

  class DropItemImplement extends DropingEntityForPlayer {
    ItemStack itemInHand = null;

    public DropItemImplement(Player p) {
      super(p.getLocation().getDirection().add(new Vector(0, 0.5, 0)).multiply(0.8), p.getLocation(),
          WeaponSkillWithThrowItem.this.getThrowItemType(), WeaponSkillWithThrowItem.this.getThrowItemData());
      itemInHand = p.getItemInHand();
      this.p = p;
    }

    Player p;

    ParticleData particleDataLava = new ParticleData(ParticleType.lava, 5);

    @Override
    public void onGround(Entity spawnEntity) {
      WeaponSkillWithThrowItem.this.onGround(p, itemInHand, spawnEntity);
    }

    @Override
    public void tickRutine(int count) {
      super.tickRutine(count);

      if (count % 4 == 0) {
        particleDataLava.run(spawnEntity.getLocation());
      }
    }

  }
}
