package lbn.item.attackitem.weaponSkill.imple.sword;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillWithCombat;
import lbn.player.ItemType;
import lbn.util.LivingEntityUtil;
import lbn.util.particle.ParticleData;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BloodyHeal extends WeaponSkillWithCombat{

	public BloodyHeal() {
		super(ItemType.SWORD);
	}
	
	@Override
	public String getId() {
		return "skill2";
	}

	@Override
	protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e) {
		//パーティクルを発生させる
		ParticleData.runLava(p.getLocation());
		//ダメージを1.2倍
		e.setDamage(e.getDamage() * getData(1));
		//体力回復
		LivingEntityUtil.addHealth(p, getData(2)*2);
	}
}
