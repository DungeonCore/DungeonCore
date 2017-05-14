package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.all;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.particle.CircleParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.mob.LastDamageManager;
import net.l_bulb.dungeoncore.mob.LastDamageMethodType;

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
  public void blastOff(Vector vector, LivingEntity livingEntity, AbstractAttackItem customItem, Player p) {
    // 敵を吹き飛ばす
    super.blastOff(vector, livingEntity, customItem, p);

    // ダメージを与える
    livingEntity.damage(customItem.getAttackItemDamage(0));
    LastDamageManager.addData(p, LastDamageMethodType.fromAttackType(customItem.getAttackType()), livingEntity);
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
