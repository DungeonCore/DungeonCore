package lbn.dungeon.contents.item.sword.special;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import lbn.item.Cooltimable;
import lbn.item.CooltimeManager;
import lbn.item.ItemInterface;
import lbn.item.strength.StrengthOperator;
import lbn.util.LivingEntityUtil;
import lbn.util.Message;
import lbn.util.particle.CircleParticleData;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpecialSwordIceAspect extends AbstractSpecialSword implements Cooltimable{
	private static HashMap<Integer, ItemInterface> itemList = new HashMap<Integer, ItemInterface>();
	static {
		itemList.put(0, new SpecialSwordIceAspect(0));
		itemList.put(12, new SpecialSwordIceAspect(12));
		itemList.put(24, new SpecialSwordIceAspect(24));
		itemList.put(36, new SpecialSwordIceAspect(36));
		itemList.put(48, new SpecialSwordIceAspect(48));
		itemList.put(60, new SpecialSwordIceAspect(60));
		itemList.put(72, new SpecialSwordIceAspect(72));
		itemList.put(84, new SpecialSwordIceAspect(84));
		itemList.put(96, new SpecialSwordIceAspect(96));
		itemList.put(108, new SpecialSwordIceAspect(108));
		itemList.put(120, new SpecialSwordIceAspect(120));
		itemList.put(132, new SpecialSwordIceAspect(132));
	}

	public SpecialSwordIceAspect() {
	}

	public SpecialSwordIceAspect(int availableLevel) {
		super(null, availableLevel);
	}

	@Override
	public Collection<ItemInterface> getAllItem() {
		return itemList.values();
	}

	@Override
	protected double getSpecialDamagePercent() {
		return 1.2;
	}

	@Override
	public String getSpecialName() {
		return "アイスアスペクタ";
	}

	@Override
	protected String getBaseId() {
		return "ice aspect";
	}

	@Override
	public int getRank() {
		return 4;
	}

	@Override
	public int getMaxSlotCount() {
		return 5;
	}

	@Override
	public int getDefaultSlotCount() {
		return 3;
	}

	CircleParticleData circleParticleData = new CircleParticleData(new ParticleData(ParticleType.cloud, 10), 5);

	@Override
	protected void excuteOnRightClick2(PlayerInteractEvent e) {
		ItemStack item = e.getItem();

		CooltimeManager cooltimeManager = new CooltimeManager(e.getPlayer(), this, item);

		int level = StrengthOperator.getLevel(item);
		if (getIceSkillSecond(level) != 0) {
			if (cooltimeManager.canUse()) {
				circleParticleData.run(e.getPlayer().getLocation());
				cooltimeManager.setCoolTime();
				e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.GLASS, 1, 1);

				List<Entity> nearbyEntities = e.getPlayer().getNearbyEntities(5, 3, 5);
				for (Entity entity : nearbyEntities) {
					if (LivingEntityUtil.isEnemy(entity)) {
						((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, getIceSkillSecond(level) * 20, 10));
						((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, getIceSkillSecond(level) * 10, 10));
					}
				}
			} else {
				cooltimeManager.sendCooltimeMessage(e.getPlayer());
			}
		}
	}

	@Override
	protected String[] getDetail() {
		return new String[]{Message.getMessage("敵を凍結させます。")};
	}

	static Random rnd = new Random();

	static ParticleData particleData = new ParticleData(ParticleType.splash, 20);

	@Override
	protected void excuteOnMeleeAttack2(ItemStack item, LivingEntity owner,
			LivingEntity target, EntityDamageByEntityEvent e) {
		int level = StrengthOperator.getLevel(item);
		if (rnd.nextInt(100) < getIceRate(level)) {
			particleData.run(target.getLocation().add(0, 1, 0));
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 10));
			owner.getWorld().playSound(target.getLocation(), Sound.GLASS, 1, 1);
		}
	}

	protected int getIceRate(int level) {
		return level * 4;
	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		if (level <= getMaxStrengthCount() - 3) {
			return new String[]{Message.getMessage("{0}%の確率で敵を凍結状態にする", getIceRate(level))};
		} else {
			return new String[]{Message.getMessage("{0}%の確率で敵を凍結状態にする", getIceRate(level)),
					Message.getMessage("右クリックで周囲の敵を凍結させます。(クールタイム{0}秒)", getIceSkillCooltimeSecond(level))};
		}
	}

	@Override
	public int getCooltimeTick(ItemStack item) {
		int level = StrengthOperator.getLevel(item);
		return getIceSkillCooltimeSecond(level) * 20;
	}

	protected int getIceSkillCooltimeSecond(int level) {
		if (level == getMaxStrengthCount()) {
			return 30;
		} else if (level == getMaxStrengthCount() - 1) {
			return 45;
		} else if (level == getMaxStrengthCount() - 2) {
			return 60;
		} else {
			return 60;
		}
	}

	protected int getIceSkillSecond(int level) {
		if (level == getMaxStrengthCount()) {
			return 3;
		} else if (level == getMaxStrengthCount() - 1) {
			return 2;
		} else if (level == getMaxStrengthCount() - 2) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	protected Material getMaterial() {
		if (getAvailableLevel() < 40) {
			return Material.IRON_SWORD;
		}
		return Material.DIAMOND_SWORD;
	}
}
