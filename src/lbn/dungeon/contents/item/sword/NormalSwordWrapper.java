package lbn.dungeon.contents.item.sword;

import java.util.ArrayList;
import java.util.List;

import lbn.item.ItemInterface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class NormalSwordWrapper extends LevelSword{

	public NormalSwordWrapper(String name, Material m, String id, int availableLevel,
			Enchantment e, int level) {
		super();
		this.name = name;
		this.m = m;
		this.id = id;
		this.e = e;
		this.enchLevel = level;
		this.availableLevel = availableLevel;
	}

	String name;
	Material m;
	String id;
	Enchantment e = null;
	int enchLevel = 0;
	int availableLevel;


	public NormalSwordWrapper(String name, Material m, String id, int availableLevel) {
		this(name, m, id, availableLevel, null, 0);
	}

	public static List<ItemInterface> getAllNormalItem() {
		ArrayList<ItemInterface> swords = new ArrayList<ItemInterface>();
		swords.add(new NormalSwordWrapper("始まりの剣", Material.WOOD_SWORD, "level0Sword", 0, Enchantment.DURABILITY, 3));
		swords.add(new NormalSwordWrapper("Cross Sword", Material.GOLD_SWORD, "level10Sword", 10, Enchantment.DURABILITY, 5));
		swords.add(new NormalSwordWrapper("鬼切 -ONIKIRI-", Material.STONE_AXE, "level20Sword", 20, Enchantment.DURABILITY, 5));
		swords.add(new NormalSwordWrapper("タンキエム", Material.STONE_SWORD, "level30Sword", 30, Enchantment.DURABILITY, 1));
		swords.add(new NormalSwordWrapper("フェザークロー", Material.STONE_SWORD, "level40Sword", 40, Enchantment.DURABILITY, 2));
		swords.add(new NormalSwordWrapper("ファイヤーロべリスク", Material.IRON_SWORD, "level50Sword", 50));
		swords.add(new NormalSwordWrapper("MURAMASA", Material.IRON_SWORD, "level60Sword", 60, Enchantment.DURABILITY, 2));
		swords.add(new NormalSwordWrapper("トリシューラ", Material.DIAMOND_SWORD, "level70Sword", 70));

		return swords;
	}

	@Override
	public String getItemName() {
		return ChatColor.stripColor(name);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	protected void excuteOnMeleeAttack2(ItemStack item, LivingEntity owner,
			LivingEntity target, EntityDamageByEntityEvent e) {

	}

	@Override
	protected void excuteOnRightClick2(PlayerInteractEvent e) {

	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		return null;
	}

	@Override
	public int getAvailableLevel() {
		return availableLevel;
	}

	@Override
	protected Material getMaterial() {
		return m;
	}

	@Override
	protected String[] getDetail() {
		return null;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		if (e != null) {
			item.addUnsafeEnchantment(e, enchLevel);
		}
		return item;
	}

}
