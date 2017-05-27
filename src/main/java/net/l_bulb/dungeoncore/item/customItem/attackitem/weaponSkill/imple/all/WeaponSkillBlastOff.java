package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.all;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.other.ItemStackData;
import net.l_bulb.dungeoncore.common.particle.CircleParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillInterface;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class WeaponSkillBlastOff implements WeaponSkillInterface {

  @Override
  public int getSkillLevel() {
    return 0;
  }

  @Override
  public String getName() {
    return "ブラスト・オフ Lv.1";
  }

  @Override
  public String getId() {
    return "blastoffl0";
  }

  @Override
  public String[] getDetail() {
    return new String[] { "自分を停止させ、", "周りの敵を吹き飛ばす" };
  }

  // パーティクル
  CircleParticleData circleParticleData = new CircleParticleData(new ParticleData(ParticleType.smoke, 3), 7);

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    Vector vector = p.getLocation().toVector();

    // 周囲の敵対モブを取得
    ArrayList<LivingEntity> nearEnemy = LivingEntityUtil.getNearEnemy(p, 7, 4, 7);
    for (LivingEntity livingEntity : nearEnemy) {
      blastOff(vector, livingEntity, customItem, p, item);
    }

    // 鈍足を付与
    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (20 * 1.5), 100));

    // パーティクルを発生させる
    circleParticleData.run(p.getLocation());

    // 音を鳴らす
    p.getWorld().playSound(p.getLocation(), Sound.BAT_LOOP, 0.4f, 2f);

    return true;
  }

  /**
   * モブを吹き飛ばす処理
   *
   * @param vector
   * @param livingEntity
   * @param customItem
   * @param p
   * @param item
   */
  public void blastOff(Vector vector, LivingEntity livingEntity, AbstractAttackItem customItem, Player p, ItemStack item) {
    // 吹き飛ばす
    livingEntity.setVelocity(livingEntity.getLocation().toVector().subtract(vector).normalize().multiply(5).setY(0));
  }

  @Override
  public int getCooltime() {
    return 15;
  }

  @Override
  public int getNeedMagicPoint() {
    return 3;
  }

  @Override
  public ItemStackData getViewItemStackData() {
    return new ItemStackData(Material.FEATHER);
  }

  @Override
  public boolean canUse(ItemType type) {
    return true;
  }

  @Override
  public void onCombat(Player p, ItemStack item, CombatItemable customItem, LivingEntity livingEntity, CombatEntityEvent event) {}

}
