package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.all;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.particle.CircleParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;

public class WeaponSkillBlastOffLevel2 extends WeaponSkillBlastOff {

  @Override
  public int getSkillLevel() {
    return 2;
  }

  @Override
  public String getName() {
    return "ブラスト・オフ Lv.2";
  }

  @Override
  public String getId() {
    return "blastoffl2";
  }

  @Override
  public String[] getDetail() {
    return new String[] { "自分を停止させ、", "周りの敵を吹き飛ばす。", "更に敵にダメージを与える" };
  }

  // パーティクル
  CircleParticleData circleParticleData = new CircleParticleData(new ParticleData(ParticleType.smoke, 3), 7);

  @Override
  public void blastOff(Vector vector, LivingEntity livingEntity, AbstractAttackItem customItem, Player p, ItemStack item) {
    // 敵を吹き飛ばす
    super.blastOff(vector, livingEntity, customItem, p, item);

    // ダメージを与える
    double attackItemDamage = new ItemStackNbttagAccessor(item).getDamage();
    // eventを発動する
    CombatEntityEvent event = new CombatEntityEvent(p, attackItemDamage, customItem, item, false, livingEntity).callEvent();
    event.damageEntity();
  }

  @Override
  public int getCooltime() {
    return 20;
  }

  @Override
  public int getNeedMagicPoint() {
    return 10;
  }
}
