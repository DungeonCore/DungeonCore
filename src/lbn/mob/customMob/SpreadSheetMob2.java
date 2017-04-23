package lbn.mob.customMob;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

public class SpreadSheetMob2 extends SpreadSheetMob{

	LbnMobTag2 mobData;

	public SpreadSheetMob2(LbnMobTag2 nbtTag, String[] command, String name) {
		super(nbtTag.getLbnMobTag(), command, name);
		this.mobData = nbtTag;
	}

	@Override
	protected Entity spawnPrivate(Location loc) {
		Entity spawnPrivate = super.spawnPrivate(loc);

		//HPが設定されてるならHPをセットする
		double mobHp = mobData.getHp();
		if (mobHp >= 0 && spawnPrivate.getType().isAlive()) {
			((LivingEntity)spawnPrivate).setMaxHealth(mobHp);
			((LivingEntity)spawnPrivate).setHealth(mobHp);
		}
		return spawnPrivate;
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		super.onDamage(mob, damager, e);

		if (mobData.getHp() >= 0) {
			//防具分の防御力を消す
			if (e.isApplicable(DamageModifier.ARMOR)) {
				e.setDamage(DamageModifier.ARMOR, 0);
			}
		}
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		super.onAttack(mob, target, e);

		if (mobData.getAttack() >= 0) {
			if (e.isApplicable(DamageModifier.BASE)) {
				e.setDamage(DamageModifier.BASE, mobData.getAttack() );
			}
		}
	}

}
