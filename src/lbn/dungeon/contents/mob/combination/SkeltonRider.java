package lbn.dungeon.contents.mob.combination;

import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lbn.dungeon.contents.mob.NormalMob;
import lbn.dungeon.contents.mob.skelton.SkeltonRiderSkelton;
import lbn.mob.AbstractMob;
import lbn.mob.mob.abstractmob.AbstractCombinationMob;
import lbn.util.LivingEntityUtil;

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
