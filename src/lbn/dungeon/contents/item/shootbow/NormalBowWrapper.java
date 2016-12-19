package lbn.dungeon.contents.item.shootbow;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemInterface;

public class NormalBowWrapper extends LevelBow{

	public NormalBowWrapper(String name, String id, int availableLevel, int level) {
		super();
		this.name = name;
		this.id = id;
		this.enchLevel = level;
		this.availableLevel = availableLevel;
	}

	String name;
	String id;
	int enchLevel = 0;
	int availableLevel;


	public NormalBowWrapper(String name, String id, int availableLevel) {
		this(name, id, availableLevel, 0);
	}

	public static List<ItemInterface> getAllNormalItem() {
		ArrayList<ItemInterface> swords = new ArrayList<ItemInterface>();
		swords.add(new NormalBowWrapper("始まりの弓", "level0Bow", 0));
		swords.add(new NormalBowWrapper("トリスタンの弓", "level10Bow", 10));
		swords.add(new NormalBowWrapper("烏号 -UGO-", "level20Bow", 20, 1));
		swords.add(new NormalBowWrapper("チャンドラダヌス", "level30Bow", 30, 1));
		swords.add(new NormalBowWrapper("ネイリング", "level40Bow", 40, 1));
		swords.add(new NormalBowWrapper("ハラダヌ", "level50Bow", 50, 2));
		swords.add(new NormalBowWrapper("神弓 -SINKUN-", "level60Bow", 60, 2));
		swords.add(new NormalBowWrapper("プテウス", "level70Bow", 70, 2));
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
	protected String[] getStrengthDetail2(int level) {
		return null;
	}

	@Override
	public int getAvailableLevel() {
		return availableLevel;
	}

	@Override
	protected Material getMaterial() {
		return Material.BOW;
	}

	@Override
	protected String[] getDetail() {
		return null;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		if (enchLevel != 0) {
			item.addEnchantment(Enchantment.DURABILITY, enchLevel);
		}
		return item;
	}

	@Override
	public void excuteOnProjectileHit(ProjectileHitEvent e, ItemStack bow) {

	}

	@Override
	protected void excuteOnShootBow2(EntityShootBowEvent e) {

	}

	@Override
	protected void excuteOnLeftClick2(PlayerInteractEvent e) {
	}

	@Override
	protected int getBaseBuyPrice() {
		return 100;
	}

}
