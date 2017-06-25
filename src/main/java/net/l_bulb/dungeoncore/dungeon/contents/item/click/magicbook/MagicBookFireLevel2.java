package net.l_bulb.dungeoncore.dungeon.contents.item.click.magicbook;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.particle.CircleParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;

public class MagicBookFireLevel2 extends AbstractMagicBook {

  @Override
  public String getItemName() {
    return "炎術 ~豪~";
  }

  @Override
  public String getId() {
    return "firebook2";
  }

  @Override
  protected double getDamageVal() {
    return 25;
  }

  @Override
  protected Material getMaterial() {
    return Material.BOOK;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "右クリックで中範囲の敵にダメージ大を与える", "使用者に火炎耐性を5秒間付与する" };
  }

  ParticleData particleData = new ParticleData(ParticleType.flame, 20);

  ParticleData particleData2 = new CircleParticleData(new ParticleData(ParticleType.lava, 3), 6);

  @Override
  public boolean excuteOnRightClick(PlayerInteractEvent e) {
    super.excuteOnRightClick(e);

    Player player = e.getPlayer();
    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 5, 1));

    particleData2.run(player.getLocation());

    player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 1, 1);
    return true;
  }

  @Override
  protected void onDamage(Player player, LivingEntity entity, ItemStack item) {
    super.onDamage(player, entity, item);
    entity.setFireTicks(20 * 8);
    // パーティクル
    particleData.run(entity.getLocation());
  }

  @Override
  protected List<Entity> getNearEntitys(Player player) {
    return player.getNearbyEntities(10, 5, 10);
  }
}
