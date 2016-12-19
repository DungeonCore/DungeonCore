package main.dungeon.contents.slotStone.level4;

import java.util.ArrayList;

import main.common.event.player.PlayerRightShiftClickEvent;
import main.item.Cooltimable;
import main.item.CooltimeManager;
import main.item.slot.SlotLevel;
import main.item.slot.slot.ShiftRightClickSlot;
import main.item.strength.StrengthOperator;
import main.util.LivingEntityUtil;
import main.util.particle.ParticleData;
import main.util.particle.ParticleType;
import main.util.particle.SpringParticleData;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealSlotStone2 extends ShiftRightClickSlot implements Cooltimable{

	@Override
	public String getSlotName() {
		return "リジェネレーション Level2";
	}

	@Override
	public String getSlotDetail() {
		return "シフト右クリックで周囲に再生効果(大)を付与";
	}

	@Override
	public String getId() {
		return "slot_src_heal2";
	}

	@Override
	public ChatColor getNameColor() {
		return ChatColor.LIGHT_PURPLE;
	}

	@Override
	public SlotLevel getLevel() {
		return SlotLevel.LEVEL4;
	}

	static SpringParticleData circleParticleData = new SpringParticleData(new ParticleData(ParticleType.heart, 1), 7, 9, 3, (long) 20);

	@Override
	public void onPlayerRightShiftClick(PlayerRightShiftClickEvent e) {
		CooltimeManager cooltimeManager = new CooltimeManager(e.getPlayer(), this, e.getAttackItem().getItem());
		Player player = e.getPlayer();
		if (cooltimeManager.canUse()) {
			Location location = player.getLocation();
			circleParticleData.run(location);

			ArrayList<Player> nearByPlayer = LivingEntityUtil.getNearByPlayer(player, 8, 4, 8);
			for (Player p : nearByPlayer) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 8, 1), false);
				p.playSound(p.getLocation(), Sound.HORSE_ZOMBIE_IDLE, 1, (float) 0.5);
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 12, 1), false);
			player.playSound(player.getLocation(), Sound.HORSE_ZOMBIE_IDLE, 1, (float) 0.5);

			cooltimeManager.setCoolTime();
		} else {
			cooltimeManager.sendCooltimeMessage(player);
		}

	}

	@Override
	public int getCooltimeTick(ItemStack item) {
		return 20 * 50 - StrengthOperator.getLevel(item) * 18;
	}

}
