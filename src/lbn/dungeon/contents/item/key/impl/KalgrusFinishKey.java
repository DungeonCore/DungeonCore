package lbn.dungeon.contents.item.key.impl;

import lbn.util.ItemStackUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class KalgrusFinishKey extends SuikaCastle {

	public KalgrusFinishKey() {
		super("", "x:? y:? z:?");
	}

	@Override
	public String getItemName() {
		return ChatColor.GOLD + "Kalgrus Finish";
	}

	@Override
	protected Material getMaterial() {
		return Material.QUARTZ;
	}

	@Override
	public String[] getDetail() {
		return new String[] { "Kalgrusで使用可能", loc };
	}

	@Override
	public void onClick(PlayerInteractEvent e, String[] lines, ItemStack item) {
		super.onClick(e, lines, item);

		PlayerInventory inventory = e.getPlayer().getInventory();
		ItemStackUtil.removeAll(inventory, getItem(), new KalgrusKey().getItem());
		e.getPlayer().updateInventory();
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		return item;
	}
}
