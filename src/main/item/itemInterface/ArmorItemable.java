package main.item.itemInterface;

import main.item.ItemInterface;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public interface ArmorItemable extends ItemInterface {

	public double getBaseDamageCuteParcent(Player me, EntityDamageEvent e, ItemStack armor);
	public double getBaseBossDamageCuteParcent(Player me, EntityDamageEvent e, ItemStack armor);

	public double getStrengthDamageCuteParcent(Player me, EntityDamageEvent e, ItemStack armor, boolean isArmorCutDamage, boolean isBoss, LivingEntity mob);

	public void extraDamageCut(Player me, EntityDamageEvent e, ItemStack armor, boolean isArmorCutDamage, boolean isBoss, LivingEntity mob);
}
