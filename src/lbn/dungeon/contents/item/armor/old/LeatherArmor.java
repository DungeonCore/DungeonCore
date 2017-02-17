package lbn.dungeon.contents.item.armor.old;

import lbn.dungeon.contents.strength_template.AromorLevel5;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.AbstractItem;
import lbn.item.itemInterface.ArmorItemable;
import lbn.item.itemInterface.Strengthenable;
import lbn.item.strength.StrengthOperator;
import lbn.util.Message;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class LeatherArmor extends AbstractItem implements ArmorItemable, Strengthenable{
	@Override
	public String getItemName() {
		return getMaterialName() + " " + getParts();
	}

	protected String getMaterialName() {
		return "LEATHER";
	}

	public LeatherArmor(Material m, String partsName) {
		this.m = m;
		this.partsName = partsName;
	}

	String partsName;

	Material m;

	protected String getParts() {
		return partsName;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"+5まで強化可能"};
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return AromorLevel5.getInstance();
	}

	@Override
	public String[] getStrengthDetail(int level) {
		double val = getDefVal(level);

		return new String[]{"防御力+" + val + "%"};
	}

	protected double getDefVal(int level) {
		return level * 3;
	}

	@Override
	protected Material getMaterial() {
		return m;
	}

	public static LeatherArmor getLeatherHelmet() {
		return new LeatherArmor(Material.LEATHER_HELMET, "HELMET");
	}

	public static LeatherArmor getLeatherChestplate() {
		return new LeatherArmor(Material.LEATHER_CHESTPLATE, "CHESTPLATE");
	}

	public static LeatherArmor getLeatherLeggings() {
		return new LeatherArmor(Material.LEATHER_LEGGINGS, "LEGGINGS");
	}

	public static LeatherArmor getLeatherBoots() {
		return new LeatherArmor(Material.LEATHER_BOOTS, "BOOTS");
	}

	public static LeatherArmor[] getAllArmor() {
		return new LeatherArmor[]{getLeatherHelmet(), getLeatherChestplate(), getLeatherLeggings(), getLeatherBoots()};
	}

	@Override
	public String getId() {
		return getMaterialName() + getParts().toLowerCase();
	}

	@Override
	public int getMaxStrengthCount() {
		return 0;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		enchant(item);
		return item;
	}

	protected void enchant(ItemStack item) {
		item.addEnchantment(Enchantment.DURABILITY, 2);
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return (int) (70 * (StrengthOperator.getLevel(item) * 1.2 + 1));
	}

	@Override
	public double getBaseDamageCuteParcent(Player me, EntityDamageEvent e,
			ItemStack armor) {
		Message.sendMessage(me, ChatColor.RED + "この装備は現在使えないため、防具によるダメージ軽減はありません。売るか耐久値をMAXにしてクラフト欄に置くかしてください。{0} ", getItemName());
		return 0;
	}

	@Override
	public double getBaseBossDamageCuteParcent(Player me, EntityDamageEvent e,
			ItemStack armor) {
		Message.sendMessage(me, ChatColor.RED + "この装備は現在使えないため、防具によるダメージ軽減はありません。{0}", getItemName());
		return 0;
	}

	@Override
	public double getStrengthDamageCuteParcent(Player me, EntityDamageEvent e,
			ItemStack armor, boolean isArmorCutDamage, boolean isBoss, LivingEntity mob) {
		return 0;
	}

	@Override
	public boolean isShowItemList() {
		return false;
	}

	@Override
	public void extraDamageCut(Player me, EntityDamageEvent e,
			ItemStack armor, boolean isArmorCutDamage, boolean isBoss,
			LivingEntity mob) {
	}
}
