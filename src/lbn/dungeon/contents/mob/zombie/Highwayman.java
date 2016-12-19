package lbn.dungeon.contents.mob.zombie;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.mob.abstractmob.AbstractZombie;
import lbn.util.LivingEntityUtil;

public class Highwayman extends AbstractZombie{
	ArrayList<EquipmentType> typeList = new ArrayList<>();


	ItemStack handItem = new ItemStack(Material.STONE_AXE);

	public Highwayman() {
		handItem.addEnchantment(Enchantment.DAMAGE_ALL, 1);

		typeList.add(EquipmentType.HELMET);
		typeList.add(EquipmentType.CHEST_PLATE);
		typeList.add(EquipmentType.LEGGINS);
		typeList.add(EquipmentType.BOOTS);
	}

	@Override
	public String getName() {
		return "Highwayman";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		Zombie entity = (Zombie) e.getEntity();
		entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 3));

		LivingEntityUtil.removeEquipment(e.getEntity());
		EntityEquipment equipment = e.getEntity().getEquipment();
		equipment.setItemInHand(handItem);
		equipment.setItemInHandDropChance(0);
	}


	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
		if (!(target instanceof Player)) {
			return;
		}
		Player p = (Player) target;

		if (rnd.nextInt(6) == 0) {
			Collections.shuffle(typeList);

			EquipmentType eType = null;
			for (EquipmentType type : typeList) {
				ItemStack item = type.getItem(p);
				if (item == null || item.getType() == Material.AIR) {
					continue;
				}
				eType = type;
			}

			if (eType != null) {
				ItemStack item = eType.getItem(p);
				eType.removeItem(p);
				mob.getWorld().dropItem(mob.getLocation(), item);

				p.sendMessage(ChatColor.GRAY + "_nayuta_ whispers エロ同人展開不可避");
			}
		}
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

}

enum EquipmentType {
	HELMET,
	CHEST_PLATE,
	LEGGINS,
	BOOTS;

	public ItemStack getItem(Player p) {
		PlayerInventory inventory = p.getInventory();

		ItemStack item;
		switch (this) {
		case HELMET:
			item = inventory.getHelmet();
			return item;
		case CHEST_PLATE:
			item = inventory.getChestplate();
			return item;
		case LEGGINS:
			item = inventory.getLeggings();
			return item;
		case BOOTS:
			item = inventory.getBoots();
			return item;
		default:
			return null;
		}
	}

	public void removeItem(Player p) {
		PlayerInventory inventory = p.getInventory();

		switch (this) {
		case HELMET:
			inventory.setHelmet(null);
			return;
		case CHEST_PLATE:
			inventory.setChestplate(null);
			return;
		case LEGGINS:
			inventory.setLeggings(null);
			return;
		case BOOTS:
			inventory.setBoots(null);
			return;
		default:
		}
	}
}
