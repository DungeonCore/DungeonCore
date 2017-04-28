package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface OldArmorItemable extends ItemInterface {

  public double getBaseDamageCuteParcent(Player me, EntityDamageEvent e, ItemStack armor);

  public double getBaseBossDamageCuteParcent(Player me, EntityDamageEvent e, ItemStack armor);

  public double getStrengthDamageCuteParcent(Player me, EntityDamageEvent e, ItemStack armor, boolean isArmorCutDamage, boolean isBoss,
      LivingEntity mob);

  public void extraDamageCut(Player me, EntityDamageEvent e, ItemStack armor, boolean isArmorCutDamage, boolean isBoss, LivingEntity mob);
}
