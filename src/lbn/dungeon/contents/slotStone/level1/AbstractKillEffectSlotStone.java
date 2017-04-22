package lbn.dungeon.contents.slotStone.level1;

import lbn.common.event.player.PlayerKillEntityEvent;
import lbn.common.particle.ParticleData;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.slot.KillSlot;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public abstract class AbstractKillEffectSlotStone extends KillSlot {

	@Override
	public String getSlotName() {
		return "キルエフェクト " + getEffectName();
	}

	abstract protected String getEffectName();

	abstract protected String getEffectId();

	@Override
	public String getSlotDetail() {
		return "キルエフェクト" + getEffectName() + "を追加";
	}

	@Override
	public String getId() {
		return "slot_ke_" + getEffectId();
	}

	@Override
	public ChatColor getNameColor() {
		return ChatColor.GOLD;
	}

	@Override
	public SlotLevel getLevel() {
		return SlotLevel.LEVEL1;
	}

	@Override
	public void onKill(PlayerKillEntityEvent e) {
		LivingEntity enemy = e.getEnemy();
		Location location = enemy.getLocation();
		getParticleData().run(location.add(0, 1, 0));
		playSound(location);
	}

	abstract protected void playSound(Location location);

	abstract protected ParticleData getParticleData();
}
