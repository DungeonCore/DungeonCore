package lbn.item.itemAbstract;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import lbn.item.AbstractItem;
import lbn.item.armoritem.ArmorMaterial;
import lbn.item.itemInterface.ArmorItemable;
import lbn.item.itemInterface.Strengthenable;
import lbn.item.strength.StrengthOperator;

public abstract class AbstractArmor extends AbstractItem implements ArmorItemable, Strengthenable{

	@Override
	public double getBaseDamageCuteParcent(Player me, EntityDamageEvent e,
			ItemStack armor) {
		ArmorMaterial material = getArmorMaterial();
		if (material != null) {
			return material.getBaseDamageCut();
		}
		return 0;
	}

	@Override
	protected List<String> getAddDetail() {
		List<String> addDetail = super.getAddDetail();
		addDetail.addAll(getBaseDefanceDetail());
		return addDetail;
	}

	abstract protected List<String> getBaseDefanceDetail();

	@Override
	public double getBaseBossDamageCuteParcent(Player me, EntityDamageEvent e,
			ItemStack armor) {
		ArmorMaterial material = getArmorMaterial();
		if (material != null) {
			return material.getBaseBossDamageCut();
		}
		return 0;
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

		int level = StrengthOperator.getLevel(armor);
		if (!isBoss) {
			return material.getStrengthTotalDamageCut() * level / getMaxStrengthCount();
		} else {
			return material.getStrengthBossTotalDamageCut() * level / getMaxStrengthCount();
		}
	}

	public ArmorMaterial getArmorMaterial() {
		if (getMaterial() == null) {
			return null;
		}

		switch (getMaterial()) {
		case LEATHER_HELMET:
		case LEATHER_CHESTPLATE:
		case LEATHER_LEGGINGS:
		case LEATHER_BOOTS:
			return ArmorMaterial.LEATHER;
		case GOLD_HELMET:
		case GOLD_CHESTPLATE:
		case GOLD_LEGGINGS:
		case GOLD_BOOTS:
			return ArmorMaterial.GOLD;
		case CHAINMAIL_HELMET:
		case CHAINMAIL_CHESTPLATE:
		case CHAINMAIL_LEGGINGS:
		case CHAINMAIL_BOOTS:
			return ArmorMaterial.CHAINMAIL;
		case IRON_HELMET:
		case IRON_CHESTPLATE:
		case IRON_LEGGINGS:
		case IRON_BOOTS:
			return ArmorMaterial.IRON;
		case DIAMOND_HELMET:
		case DIAMOND_CHESTPLATE:
		case DIAMOND_LEGGINGS:
		case DIAMOND_BOOTS:
			return ArmorMaterial.DIAMOND;
		default:
			return null;
		}
	}

}
