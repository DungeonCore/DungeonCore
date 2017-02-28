package lbn.item.attackitem.weaponSkill.imple.all;

import java.util.ArrayList;

import lbn.common.other.ItemStackData;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.WeaponSkillInterface;
import lbn.player.ItemType;
import lbn.util.LivingEntityUtil;
import lbn.util.particle.CircleParticleData;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class WeaponSkillBlastOff implements WeaponSkillInterface{

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
		return new String[]{"自分を停止させ、", "周りの敵を吹き飛ばす"};
	}

	//パーティクル
	CircleParticleData circleParticleData = new CircleParticleData(new ParticleData(ParticleType.smoke, 3), 7);

	@Override
	public void onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		Vector vector = p.getLocation().toVector();

		//周囲の敵対モブを取得
		ArrayList<LivingEntity> nearEnemy = LivingEntityUtil.getNearEnemy(p, 7, 4, 7);
		for (LivingEntity livingEntity : nearEnemy) {
			blastOff(vector, livingEntity, customItem, p);
		}

		//鈍足を付与
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)(20 * 1.5), 100));

		//パーティクルを発生させる
		circleParticleData.run(p.getLocation());

		//音を鳴らす
		p.getWorld().playSound(p.getLocation(), Sound.BAT_LOOP, 0.4f, 2f);
	}

	/**
	 * モブを吹き飛ばす処理
	 * @param vector
	 * @param livingEntity
	 * @param customItem
	 * @param p
	 */
	public void blastOff(Vector vector, LivingEntity livingEntity, AbstractAttackItem customItem, Player p) {
		//吹き飛ばす
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
	public ItemStackData getItemStackData() {
		return new ItemStackData(Material.FEATHER);
	}

	@Override
	public boolean canUse(ItemType type) {
		return true;
	}

}
