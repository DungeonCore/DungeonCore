package main.dungeon.contents.slotStone.other;

import main.common.event.player.PlayerCombatEntityEvent;
import main.item.slot.SlotLevel;
import main.item.slot.slot.CombatSlot;
import main.util.JavaUtil;
import main.util.LivingEntityUtil;
import main.util.particle.ParticleData;
import main.util.particle.ParticleType;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class MagicStoneJade extends CombatSlot{

	@Override
	public String getSlotName() {
		return "ジェダイト";
	}

	@Override
	public String getSlotDetail() {
		return "攻撃速度上昇+25%,攻撃時一定確率で回復,攻撃力+20%";
	}

	@Override
	public String getId() {
		return "red_jade";
	}

	@Override
	public ChatColor getNameColor() {
		return ChatColor.RED;
	}

	@Override
	public SlotLevel getLevel() {
		return SlotLevel.LEGENT;
	}

	ParticleData particleData = new ParticleData(ParticleType.heart, 30);

	@Override
	public void onCombat(PlayerCombatEntityEvent e) {
		LivingEntityUtil.setNoDamageTick(e.getEnemy(), 15);

		if (JavaUtil.isRandomTrue(5)) {
			LivingEntityUtil.addHealth(e.getPlayer(), Math.min(e.getDamage() * 0.2, 3));
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.SILVERFISH_IDLE, 1, (float) 0.1);
			particleData.run(e.getPlayer().getLocation().add(0, 2, 0));
		}

		e.setDamage(e.getDamage() * 1.2);;
	}

}
