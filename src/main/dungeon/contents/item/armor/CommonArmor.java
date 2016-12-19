package main.dungeon.contents.item.armor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.common.event.ChangeStrengthLevelItemEvent;
import main.common.event.player.PlayerSetStrengthItemResultEvent;
import main.common.event.player.PlayerStrengthFinishEvent;
import main.dungeon.contents.strength_template.CommonArmorTemplate;
import main.dungeon.contents.strength_template.StrengthTemplate;
import main.item.ItemInterface;
import main.item.armoritem.ArmorMaterial;
import main.item.itemAbstract.AbstractArmor;
import main.item.itemInterface.StrengthChangeItemable;
import main.item.strength.StrengthOperator;
import main.util.JavaUtil;
import main.util.Message;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class CommonArmor extends AbstractArmor implements StrengthChangeItemable{

	public CommonArmor(Material m) {
		this.m = m;
	}

	public static List<ItemInterface> getAllArmor() {
		return Arrays.asList(
				new CommonArmor(Material.LEATHER_HELMET),
				new CommonArmor(Material.LEATHER_CHESTPLATE),
				new CommonArmor(Material.LEATHER_LEGGINGS),
				new CommonArmor(Material.LEATHER_BOOTS),
				new CommonArmor(Material.GOLD_HELMET),
				new CommonArmor(Material.GOLD_CHESTPLATE),
				new CommonArmor(Material.GOLD_LEGGINGS),
				new CommonArmor(Material.GOLD_BOOTS),
				new CommonArmor(Material.CHAINMAIL_HELMET),
				new CommonArmor(Material.CHAINMAIL_CHESTPLATE),
				new CommonArmor(Material.CHAINMAIL_LEGGINGS),
				new CommonArmor(Material.CHAINMAIL_BOOTS),
				new CommonArmor(Material.IRON_HELMET),
				new CommonArmor(Material.IRON_CHESTPLATE),
				new CommonArmor(Material.IRON_LEGGINGS),
				new CommonArmor(Material.IRON_BOOTS),
				new CommonArmor(Material.DIAMOND_HELMET),
				new CommonArmor(Material.DIAMOND_CHESTPLATE),
				new CommonArmor(Material.DIAMOND_LEGGINGS),
				new CommonArmor(Material.DIAMOND_BOOTS)
		);
	}

	@Override
	public String getItemName() {
		return getArmorMaterial() + " " + getPartsName();
	}

	public String getPartsName() {
		String material = getMaterial().toString();
		if (material.contains("HELMET")) {
			return "HELMET";
		} else if (material.contains("CHESTPLATE")) {
			return "CHESTPLATE";
		} else if (material.contains("LEGGINGS")) {
			return "LEGGINGS";
		} else if (material.contains("BOOTS")) {
			return "BOOTS";
		}
		return "ARMOR";
	}

	@Override
	public String getId() {
		return "common_" + getArmorMaterial().toString().toLowerCase() + "_" + getPartsName().toLowerCase();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		int price = 0;
		switch (getArmorMaterial()) {
		case LEATHER:
			price = 100;
			break;
		case GOLD:
			price = 220;
			break;
		case CHAINMAIL:
			price = 380;
			break;
		case IRON:
			price = 600;
			break;
		case DIAMOND:
			price = 900;
			break;
		default:
			break;
		}
		int level = StrengthOperator.getLevel(item);
		return price + 50 * level;
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return new CommonArmorTemplate(getMaxStrengthCount(), getArmorMaterial());
	}

	@Override
	public int getMaxStrengthCount() {
		ArmorMaterial armorMaterial = getArmorMaterial();
		if (armorMaterial == null) {
			return 0;
		}

		switch (armorMaterial) {
		case LEATHER:
			return 3;
		case GOLD:
			return 4;
		case CHAINMAIL:
			return 5;
		case IRON:
			return 6;
		case DIAMOND:
			return 6;
		default:
			break;
		}
		return 0;
	}

	@Override
	public String[] getStrengthDetail(int level) {
		int mobStar = 0;
		double bossStar = 0;
		switch (getArmorMaterial()) {
		case LEATHER:
		case GOLD:
		case CHAINMAIL:
		case IRON:
			mobStar = 1 * level;
			bossStar = 1.25 * level;
			break;
		case DIAMOND:
			bossStar = 1.75 * level;
			break;
		default:
			break;
		}
		bossStar = JavaUtil.round(bossStar, 1);
		return new String[]{Message.getMessage("防御力: ☆ × {0}", mobStar) , Message.getMessage("ボスに対して防御力: ☆ × {0}", bossStar)};
	}

	@Override
	public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {
		if (event.getNextLevel() == getMaxStrengthCount()) {
			BeneCommonAromor beneCommonAromor = new BeneCommonAromor(getMaterial());
			event.setItem(beneCommonAromor.getItem());
		}
		event.getItem().addUnsafeEnchantment(Enchantment.DURABILITY, getDurabilityLevel(event.getNextLevel()));
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		//耐久力えんちゃのレベル取得
		int enchLevel = getDurabilityLevel(0);

		if (enchLevel != 0) {
			item.addUnsafeEnchantment(Enchantment.DURABILITY, enchLevel);
		}
		return item;
	}

	public int getDurabilityLevel(int level) {
		int enchLevel = 0;
		double stepSize = 0;
		switch (getArmorMaterial()) {
		case LEATHER:
			enchLevel = 6;
			stepSize = 2;
			break;
		case GOLD:
			enchLevel = 5;
			stepSize = 2;
			break;
		case CHAINMAIL:
			enchLevel = 2;
			stepSize = 1;
			break;
		case IRON:
			enchLevel = 2;
			stepSize = 1;
			break;
		case DIAMOND:
			stepSize = 0.5;
			break;
		default:
			break;
		}
		return (int) (enchLevel + level * stepSize);
	}


	Material m = null;
	@Override
	protected Material getMaterial() {
		return m;
	}

	protected List<String> getBaseDefanceDetail() {
		String mobStar = "?";
		String bossStar = "?";
		switch (getArmorMaterial()) {
		case LEATHER:
			mobStar = "3";
			bossStar = "2";
			break;
		case GOLD:
			mobStar = "3.5";
			bossStar = "3";
			break;
		case CHAINMAIL:
			mobStar = "4";
			bossStar = "4";
			break;
		case IRON:
			mobStar = "4.5";
			bossStar = "5";
			break;
		case DIAMOND:
			mobStar = "5";
			bossStar = "6";
			break;
		default:
			break;
		}
		return Arrays.asList(Message.getMessage("防御力: ★ × {0}", mobStar) , Message.getMessage("ボスに対して防御力: ★ × {0}", bossStar));
	}

	@Override
	protected String[] getDetail() {
		return null;
	}

	@Override
	public void onChangeStrengthLevelItemEvent(
			ChangeStrengthLevelItemEvent event) {
	}

	Random rnd = new Random();

	@Override
	public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {
	}

	@Override
	public void extraDamageCut(Player me, EntityDamageEvent e,
			ItemStack armor, boolean isArmorCutDamage, boolean isBoss,
			LivingEntity mob) {
	}

}
