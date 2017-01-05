package lbn.dungeon.contents.mob.zombie;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.mob.abstractmob.AbstractZombie;
import lbn.util.LivingEntityUtil;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Book extends AbstractZombie{

	@Override
	public String getName() {
		return "BOOK";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();
		entity.setHealth(5.0);

		LivingEntityUtil.setEquipment(entity, new ItemStack(Material.BOOKSHELF),
				setAddLether(Material.LEATHER_CHESTPLATE) , setAddLether(Material.LEATHER_LEGGINGS), setAddLether(Material.LEATHER_BOOTS), 0);
		ItemStack itemStack = new ItemStack(Material.BOOK);
		itemStack.addEnchantment(Enchantment.DURABILITY, 1);
		LivingEntityUtil.setItemInHand(entity, itemStack, 0);
	}

	private ItemStack setAddLether(Material m) {
		ItemStack itemStack = new ItemStack(m);
		itemStack.addEnchantment(Enchantment.DURABILITY, 1);
		LeatherArmorMeta meta=(LeatherArmorMeta)itemStack.getItemMeta();
		meta.setColor(Color.fromRGB(102, 51, 0));
		return itemStack;
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {

	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}
}
