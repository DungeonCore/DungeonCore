package lbn.item.attackitem.weaponSkill.imple.sword;

import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.player.ItemType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ProtectionArmor extends WeaponSkillForOneType{

	public ProtectionArmor() {
		super(ItemType.SWORD);
	}

	@Override
	public String getName() {
		return "プロテクションアーマー";
	}

	@Override
	public String getId() {
		return "skill7";
	}

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (20 * getData(0)), 6));
		return true;
	}

}
