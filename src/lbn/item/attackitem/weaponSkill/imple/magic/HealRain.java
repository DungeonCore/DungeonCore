package lbn.item.attackitem.weaponSkill.imple.magic;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lbn.common.other.ItemStackData;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.player.ItemType;
import lbn.util.LbnRunnable;
import lbn.util.LivingEntityUtil;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

public class HealRain extends WeaponSkillForOneType{

	public HealRain() {
		super(ItemType.MAGIC);
	}

	@Override
	public int getSkillLevel() {
		return 40;
	}

	@Override
	public String getName() {
		return "ヒールレイン";
	}

	@Override
	public String getId() {
		return "heal_rain";
	}

	@Override
	public String[] getDetail() {
		return new String[]{"10秒の間、自分の周囲の味方を回復させる"};
	}

	@Override
	public int getCooltime() {
		return 360;
	}

	@Override
	public int getNeedMagicPoint() {
		return 80;
	}

	@Override
	public ItemStackData getViewItemStackData() {
		return new ItemStackData(Material.GHAST_TEAR, 0);
	}

	ParticleData particleData = new ParticleData(ParticleType.heart, 5);

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {

		new LbnRunnable() {
			@Override
			public void run2() {
				//周囲の味方を回復させる
				List<Entity> nearbyEntities = p.getNearbyEntities(5, 5, 5);
				for (Entity entity : nearbyEntities) {
					if (LivingEntityUtil.isFriendship(entity) && entity.getType().isAlive()) {
						LivingEntityUtil.addHealth((LivingEntity) entity, 2);
					}
					particleData.run(entity.getLocation().add(0, 2, 0));
				}

				//自分を回復
				LivingEntityUtil.addHealth(p, 2);
				particleData.run(p.getLocation().add(0, 1, 0));

				//10秒たったら終わりにする
				if (getAgeTick() >= 20 * 10) {
					cancel();
				}
			}
		}.runTaskTimer(20);
		return true;
	}
}
