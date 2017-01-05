package lbn.dungeon.contents.mob.combination;

import lbn.dungeon.contents.mob.NormalMob;
import lbn.dungeon.contents.mob.slime.FireCube;
import lbn.mob.AbstractMob;
import lbn.mob.mob.abstractmob.AbstractCombinationMob;
import lbn.util.LivingEntityUtil;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireSkeltonCube extends AbstractCombinationMob<Slime>{

	@Override
	protected AbstractMob<?> getBaseMob() {
		return new FireCube();
	}

	@Override
	public AbstractMob<?>[] getCombinationMobListForSpawn() {
		return new AbstractMob<?>[]{getBaseMob(), new NormalMob(EntityType.SKELETON)};
	}

	@Override
	public String getName() {
		return "Fire Skelton Cube";
	}

	@Override
	protected void onSpawn(LivingEntity e) {
		Slime slime = (Slime) e;
		LivingEntity spawn = new NormalMob(EntityType.SKELETON).spawn(slime.getLocation());
		spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000, 1), true);

		ItemStack itemStack = new ItemStack(Material.BOW);
		itemStack.addEnchantment(Enchantment.ARROW_FIRE, 1);
		LivingEntityUtil.setItemInHand(spawn, itemStack, 0);

		slime.setPassenger(spawn);

		slime.setSize(2);
	}

}
