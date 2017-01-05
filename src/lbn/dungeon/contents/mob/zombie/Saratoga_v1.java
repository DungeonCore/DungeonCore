package lbn.dungeon.contents.mob.zombie;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.util.LivingEntityUtil;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Saratoga_v1 extends WaterZombie{
	@Override
	public String getName() {
		return "Saratoga v1";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		Zombie entity = (Zombie) e.getEntity();
		LivingEntityUtil.setEquipment(entity, new ItemStack[]{new ItemStack(Material.BEACON), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)}, 0);
		LivingEntityUtil.setItemInHand(entity, new ItemStack(Material.DIAMOND_HOE), 0);
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {

		if (rnd.nextInt(3) == 1) {
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 1));
		}
	}
}
