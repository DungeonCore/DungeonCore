package lbn.dungeon.contents.slotStone.level3;

import java.util.ArrayList;

import lbn.common.event.player.PlayerRightShiftClickEvent;
import lbn.item.Cooltimable;
import lbn.item.CooltimeManager;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.slot.ShiftRightClickSlot;
import lbn.item.strength.StrengthOperator;
import lbn.util.LivingEntityUtil;
import lbn.util.particle.CircleParticleData;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealSlotStone extends ShiftRightClickSlot implements Cooltimable{

	@Override
	public String getSlotName() {
		return "リジェネレーション Level1";
	}

	@Override
	public String getSlotDetail() {
		return "シフト右クリックで周囲に再生効果(中)を付与";
	}

	@Override
	public String getId() {
		return "slot_src_heal";
	}

	@Override
	public ChatColor getNameColor() {
		return ChatColor.LIGHT_PURPLE;
	}

	@Override
	public SlotLevel getLevel() {
		return SlotLevel.LEVEL3;
	}

	static CircleParticleData circleParticleData = new CircleParticleData(new ParticleData(ParticleType.heart, 1), 5);

	@Override
	public void onPlayerRightShiftClick(PlayerRightShiftClickEvent e) {

		CooltimeManager cooltimeManager = new CooltimeManager(e.getPlayer(), this, e.getAttackItem().getItem());

		Player player = e.getPlayer();
		if (cooltimeManager.canUse()) {
			Location location = player.getLocation();
			circleParticleData.run(location.add(0, 1, 0));

			ArrayList<Player> nearByPlayer = LivingEntityUtil.getNearByPlayer(player, 5, 3, 5);
			for (Player p : nearByPlayer) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 6, 0), false);
				p.playSound(p.getLocation(), Sound.HORSE_ZOMBIE_IDLE, 1, (float) 0.3);
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 0), false);
			player.playSound(player.getLocation(), Sound.HORSE_ZOMBIE_IDLE, 1, (float) 0.3);

			cooltimeManager.setCoolTime();
		} else {
			cooltimeManager.sendCooltimeMessage(player);
		}

	}

	@Override
	public int getCooltimeTick(ItemStack item) {
		return 20 * 40 - StrengthOperator.getLevel(item) * 18;
	}

}
