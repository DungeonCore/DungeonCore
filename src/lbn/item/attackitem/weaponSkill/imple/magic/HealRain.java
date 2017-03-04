package lbn.item.attackitem.weaponSkill.imple.magic;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	public String getId() {
		return "skill5";
	}

	ParticleData particleData = new ParticleData(ParticleType.heart, 5);

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {

		new LbnRunnable() {
			@Override
			public void run2() {
				//周囲の味方を回復させる
				double radius = getData(0);
				List<Entity> nearbyEntities = p.getNearbyEntities(radius, radius, radius);
				for (Entity entity : nearbyEntities) {
					if (LivingEntityUtil.isFriendship(entity) && entity.getType().isAlive()) {
						LivingEntityUtil.addHealth((LivingEntity) entity, getData(2)*2);
					}
					particleData.run(entity.getLocation().add(0, 2, 0));
				}

				//自分を回復
				LivingEntityUtil.addHealth(p, getData(2)*2);
				particleData.run(p.getLocation().add(0, 1, 0));

				//10秒たったら終わりにする
				if (getAgeTick() >= 20 * getData(3)) {
					cancel();
				}
			}
		}.runTaskTimer((long) (20 * getData(1)));
		return true;
	}
}
