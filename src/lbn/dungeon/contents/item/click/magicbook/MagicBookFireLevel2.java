package lbn.dungeon.contents.item.click.magicbook;

import java.util.List;

import lbn.util.particle.CircleParticleData;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MagicBookFireLevel2 extends AbstractMagicBook{

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
		return new String[]{"右クリックで中範囲の敵にダメージ大を与える", "使用者に火炎耐性を5秒間付与する"};
	}

	ParticleData particleData = new ParticleData(ParticleType.flame, 20);

	ParticleData particleData2 = new CircleParticleData(new ParticleData(ParticleType.lava, 3), 6);

	@Override
	protected boolean excuteOnRightClick2(PlayerInteractEvent e) {
		super.excuteOnRightClick2(e);

		Player player = e.getPlayer();
		player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 5, 1));

		particleData2.run(player.getLocation());

		player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 1, 1);
		return true;
	}

	@Override
	protected void onDamage(Player player, LivingEntity entity) {
		super.onDamage(player, entity);
		entity.setFireTicks(20 * 8);
		//パーティクル
		particleData.run(entity.getLocation());
	}

	@Override
	protected List<Entity> getNearEntitys(Player player) {
		return player.getNearbyEntities(10, 5, 10);
	}
}
