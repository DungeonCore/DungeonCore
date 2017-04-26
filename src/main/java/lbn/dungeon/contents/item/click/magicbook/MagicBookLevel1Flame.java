package lbn.dungeon.contents.item.click.magicbook;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import lbn.common.particle.ParticleData;
import lbn.common.particle.ParticleType;

public class MagicBookLevel1Flame extends AbstractMagicBook {

  @Override
  protected boolean excuteOnRightClick2(PlayerInteractEvent e) {
    super.excuteOnRightClick2(e);
    Player player = e.getPlayer();
    // 音を再生
    player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 1, 1);
    // 火を消す
    player.setFireTicks(0);
    return true;
  }

  @Override
  protected void onDamage(Player player, LivingEntity entity) {
    super.onDamage(player, entity);
    entity.setFireTicks(20 * 4);
    // パーティクル
    particleData.run(entity.getLocation());
  }

  ParticleData particleData = new ParticleData(ParticleType.flame, 20);

  @Override
  protected double getDamageVal() {
    return 15.0;
  }

  @Override
  public String getItemName() {
    return "炎術  ~忍~";
  }

  @Override
  public String getId() {
    return "firebook1";
  }

  @Override
  protected Material getMaterial() {
    return Material.BOOK;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "右クリックで周囲の敵に火炎ダメージ中を与える", "使用者が延焼していた場合は火を消す" };
  }

  @Override
  protected List<Entity> getNearEntitys(Player player) {
    return player.getNearbyEntities(6, 3, 6);
  }
}
