package main.dungeon.contents.mob.combination;

import main.dungeon.contents.mob.NormalMob;
import main.dungeon.contents.mob.skelton.SkeltonRiderSkelton;
import main.mob.AbstractMob;
import main.mob.mob.abstractmob.AbstractCombinationMob;
import main.util.LivingEntityUtil;

import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SkeltonRider extends AbstractCombinationMob<Bat>{
	@Override
	protected AbstractMob<?> getBaseMob() {
		if (mobList == null) {
			mobList = new AbstractMob<?>[]{new SkeltonRiderSkelton()};
		}
		return new NormalMob(EntityType.BAT);
	}

	static AbstractMob<?>[] mobList;

	@Override
	public AbstractMob<?>[] getCombinationMobListForSpawn() {
		return mobList;
	}

	@Override
	public String getName() {
		return "Skelton Rider";
	}

	@Override
	protected void onSpawn(LivingEntity entity) {
		entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));

		LivingEntity spawn = (LivingEntity) mobList[0].spawn(entity.getLocation());
		entity.setPassenger(spawn);

		ItemStack item = getSkeltonItem();
		if (item != null) {
			LivingEntityUtil.setItemInHand(spawn, getSkeltonItem(), 0);
		}
	}

	private ItemStack getSkeltonItem() {
		return null;
	}

}
