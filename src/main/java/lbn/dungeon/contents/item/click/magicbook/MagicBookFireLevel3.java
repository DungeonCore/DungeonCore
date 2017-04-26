package lbn.dungeon.contents.item.click.magicbook;

import java.util.List;

import lbn.common.particle.ParticleData;
import lbn.common.particle.ParticleType;
import lbn.common.particle.SpringParticleData;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MagicBookFireLevel3 extends AbstractMagicBook {

  @Override
  public String getItemName() {
    return "炎術 ~極~";
  }

  @Override
  public String getId() {
    return "firebook3";
  }

  @Override
  protected double getDamageVal() {
    return 35;
  }

  @Override
  protected Material getMaterial() {
    return Material.ENCHANTED_BOOK;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "右クリックで広範囲の敵にダメージ極大を与える", "使用者と周囲のプレイヤーに火炎耐性を8秒間付与" };
  }

  ParticleData particleData1 = new SpringParticleData(new ParticleData(ParticleType.lava, 3),
      6, 8, 1, 10);

  ParticleData particleData = new ParticleData(ParticleType.flame, 20);

  @Override
  protected boolean excuteOnRightClick2(PlayerInteractEvent e) {
    super.excuteOnRightClick2(e);

    Player player = e.getPlayer();
    // 周りのプレイヤーに火炎耐性をつける
    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 8, 1));
    for (Entity entity : player.getNearbyEntities(10, 4, 10)) {
      if (entity.getType() == EntityType.PLAYER) {
        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 10, 1));
      }
    }

    particleData1.run(player.getLocation());

    player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 1, 1);
    player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 0.5f, 1);
    return true;
  }

  @Override
  protected void onDamage(Player player, LivingEntity entity) {
    super.onDamage(player, entity);
    entity.setFireTicks(20 * 12);
    // パーティクル
    particleData.run(entity.getLocation());
  }

  @Override
  protected List<Entity> getNearEntitys(Player player) {
    return player.getNearbyEntities(10, 5, 10);
  }
}
