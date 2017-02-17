package lbn.dungeon.contents.item.sword.special;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import lbn.item.ItemInterface;
import lbn.item.strength.StrengthOperator;
import lbn.util.LivingEntityUtil;
import lbn.util.Message;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpecialSwordMebiusSword extends AbstractSpecialSword{
	protected SpecialSwordMebiusSword( int availableLevel, Material m) {
		super(m, availableLevel);
	}

	private static HashMap<Integer, ItemInterface> itemMap = new HashMap<Integer, ItemInterface>();
	static {
		itemMap.put(10, new SpecialSwordMebiusSword(10, Material.STONE_SWORD));
		itemMap.put(20, new SpecialSwordMebiusSword(20, Material.STONE_SWORD));
		itemMap.put(30, new SpecialSwordMebiusSword(30, Material.STONE_SWORD));
		itemMap.put(40, new SpecialSwordMebiusSword(40, Material.IRON_SWORD));
		itemMap.put(50, new SpecialSwordMebiusSword(50, Material.IRON_SWORD));
		itemMap.put(60, new SpecialSwordMebiusSword(60, Material.IRON_SWORD));
		itemMap.put(70, new SpecialSwordMebiusSword(70, Material.DIAMOND_SWORD));
		itemMap.put(80, new SpecialSwordMebiusSword(80, Material.DIAMOND_SWORD));
		itemMap.put(90, new SpecialSwordMebiusSword(90, Material.DIAMOND_SWORD));
		itemMap.put(100, new SpecialSwordMebiusSword(100, Material.DIAMOND_SWORD));
		itemMap.put(110, new SpecialSwordMebiusSword(110, Material.DIAMOND_SWORD));
		itemMap.put(120, new SpecialSwordMebiusSword(120, Material.DIAMOND_SWORD));
	}

	public SpecialSwordMebiusSword() {
	}

	@Override
	protected void excuteOnRightClick2(PlayerInteractEvent e) {
	}

	@Override
	public String[] getDetail() {
		return new String[]{"剣からいでる稲妻は神をも凌駕する"};
	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		return new String[]{Message.getMessage("{0}%の確率で攻撃時に雷を落とす", getLightningRate(level))};
	}

	Random rnd = new Random();

	@Override
	protected void excuteOnMeleeAttack2(ItemStack item, LivingEntity owner,
			LivingEntity target, EntityDamageByEntityEvent e) {
		if (rnd.nextInt(100) <= getLightningRate(StrengthOperator.getLevel(item))) {
			runLightning(owner, target, e);
		}
	}

	protected int getLightningRate(int level) {
		return 4 + level * 2;
	}

	protected void runLightning(LivingEntity owner, LivingEntity target, EntityDamageByEntityEvent e) {
		Location location = target.getLocation();
		e.setDamage(8.0 + e.getDamage());
		if (owner.getType() == EntityType.PLAYER) {
			LivingEntityUtil.strikeLightningEffect(location, (Player)owner);
		}
		target.setFireTicks(20 * 6);
	}

	@Override
	public String getSpecialName() {
		return "メビウスソード";
	}

	@Override
	public Collection<ItemInterface> getAllItem() {
		return itemMap.values();
	}

	@Override
	public int getRank() {
		return 1;
	}

	@Override
	protected double getSpecialDamagePercent() {
		return 1.05;
	}

	@Override
	protected String getBaseId() {
		return "mebius sword";
	}

	@Override
	public int getMaxSlotCount() {
		return 4;
	}

	@Override
	public int getDefaultSlotCount() {
		return 2;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		item.addEnchantment(Enchantment.DURABILITY, 1);
		return item;
	}
}
