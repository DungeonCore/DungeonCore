package lbn.dungeon.contents.item.armor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemInterface;
import lbn.item.customItem.armoritem.ArmorStrengthTemplate;
import lbn.item.customItem.armoritem.old.ArmorMaterial;
import lbn.item.system.lore.ItemLoreToken;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class BeneCommonAromor extends CommonArmor implements Beneable{
	public BeneCommonAromor(Material m) {
		super(m);
	}

	public static List<ItemInterface> getAllArmor() {
		return Arrays.asList(
				new BeneCommonAromor(Material.LEATHER_HELMET),
				new BeneCommonAromor(Material.LEATHER_CHESTPLATE),
				new BeneCommonAromor(Material.LEATHER_LEGGINGS),
				new BeneCommonAromor(Material.LEATHER_BOOTS),
				new BeneCommonAromor(Material.GOLD_HELMET),
				new BeneCommonAromor(Material.GOLD_CHESTPLATE),
				new BeneCommonAromor(Material.GOLD_LEGGINGS),
				new BeneCommonAromor(Material.GOLD_BOOTS),
				new BeneCommonAromor(Material.CHAINMAIL_HELMET),
				new BeneCommonAromor(Material.CHAINMAIL_CHESTPLATE),
				new BeneCommonAromor(Material.CHAINMAIL_LEGGINGS),
				new BeneCommonAromor(Material.CHAINMAIL_BOOTS),
				new BeneCommonAromor(Material.IRON_HELMET),
				new BeneCommonAromor(Material.IRON_CHESTPLATE),
				new BeneCommonAromor(Material.IRON_LEGGINGS),
				new BeneCommonAromor(Material.IRON_BOOTS),
				new BeneCommonAromor(Material.DIAMOND_HELMET),
				new BeneCommonAromor(Material.DIAMOND_CHESTPLATE),
				new BeneCommonAromor(Material.DIAMOND_LEGGINGS),
				new BeneCommonAromor(Material.DIAMOND_BOOTS)
		);
	}

	@Override
	public String getItemName() {
		return "Bene " + super.getItemName();
	}

	@Override
	public String getId() {
		return "bene_" + super.getId();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return (int) (super.getBuyPrice(item) * 1.2);
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return new ArmorStrengthTemplate();
	}

	@Override
	public int getMaxStrengthCount() {
		return 5;
	}

	Random rnd = new Random();

	@Override
	public void setStrengthDetail(int level, ItemLoreToken loreToken) {
		//もう使わないので何もしない
	}

	@Override
	protected List<String> getBaseDefanceDetail() {
		return new ArrayList<String>();
	}

	@Override
	public double getStrengthDamageCuteParcent(Player me, EntityDamageEvent e,
			ItemStack armor, boolean isArmorCutDamage, boolean isBoss,
			LivingEntity mob) {
		if (!isArmorCutDamage) {
			return 0;
		}

		ArmorMaterial material = getArmorMaterial();
		if (material == null) {
			return 0;
		}

		//最大強化分だけカットする
		if (!isBoss) {
			return material.getStrengthTotalDamageCut();
		} else {
			return material.getStrengthBossTotalDamageCut();
		}
	}

	@Override
	public String[] getDetail() {
		return new String[]{"Bene装備", "ランダムで特殊効果が付与されます"};
	}

	@Override
	public boolean isShowItemList() {
		return false;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		item.addUnsafeEnchantment(Enchantment.DURABILITY, getDurabilityLevel(super.getMaxStrengthCount()));
		return item;
	}

	@Override
	public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {

	}

	@Override
	public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {
	}

	@Override
	public void extraDamageCut(Player me, EntityDamageEvent e, ItemStack armor, boolean isArmorCutDamage, boolean isBoss, LivingEntity mob) {
	}
}
