package lbn.dungeon.contents.item.sword.special;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lbn.common.cooltime.Cooltimable;
import lbn.common.cooltime.CooltimeManager;
import lbn.dungeoncore.Main;
import lbn.item.ItemInterface;
import lbn.item.strength.StrengthOperator;
import lbn.mob.LastDamageManager;
import lbn.mob.LastDamageMethodType;
import lbn.util.JavaUtil;
import lbn.util.LivingEntityUtil;
import lbn.util.Message;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SpecialSwordSoulSword extends AbstractSpecialSword implements Cooltimable{

	static ArrayList<ItemInterface> itemList = new ArrayList<ItemInterface>();
	static {
		itemList.add(new SpecialSwordSoulSword(7, Material.STONE_SWORD));
		itemList.add(new SpecialSwordSoulSword(22, Material.IRON_SWORD));
		itemList.add(new SpecialSwordSoulSword(30, Material.IRON_SWORD));
		itemList.add(new SpecialSwordSoulSword(42, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordSoulSword(56, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordSoulSword(72, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordSoulSword(88, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordSoulSword(104, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordSoulSword(120, Material.DIAMOND_SWORD));
	}

	public SpecialSwordSoulSword(int availavleLevel, Material m) {
		super(m, availavleLevel);
	}

	public SpecialSwordSoulSword() {
	}

	ParticleData clickParticle = new ParticleData(ParticleType.portal, 100).setLastArgument(5);

	@Override
	protected void excuteOnRightClick2(PlayerInteractEvent e) {
		int level = StrengthOperator.getLevel(e.getItem());
		Player player = e.getPlayer();

		if (getCooltimeTick(e.getItem()) == 0) {
			return;
		}

		CooltimeManager cooltimeManager = new CooltimeManager(e, this);
		//クールタイム中でないならダメージを与える
		if (cooltimeManager.canUse()) {
			cooltimeManager.setCoolTime();
			//自分の周りのEntityを取得
			List<Entity> nearbyEntities = player.getNearbyEntities(5, 3, 5);
			for (Entity entity : nearbyEntities) {
				//もし敵ならダメージを与える
				if (LivingEntityUtil.isEnemy(entity)) {
					LastDamageManager.onDamage(((LivingEntity)entity), player, LastDamageMethodType.SWORD);
					((LivingEntity)entity).damage(getDamage(level));
					clickParticle.run(entity.getLocation().add(0, 1, 0));
				}
			}
			//自分にダメージを与える
			LivingEntityUtil.trueDamage(player, 1.0 * getPlayerDamage(level), player, LastDamageMethodType.SWORD);
			clickParticle.run(player.getLocation().add(0, 1, 0));
			player.getWorld().playSound(player.getLocation(), Sound.BLAZE_DEATH, 1, 1);
		} else {
			cooltimeManager.sendCooltimeMessage(player);
		}
	}

	protected double getDamage(int level) {
		return level * 3 + 5;
	}

	protected double getPlayerDamage(int level) {
		return level * 0.5;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"敵の魂を吸収する"};
	}

	ParticleData particleData = new ParticleData(ParticleType.heart, 30);

	@Override
	protected void excuteOnMeleeAttack2(ItemStack item, LivingEntity owner,
			LivingEntity target, EntityDamageByEntityEvent e) {
		if (!JavaUtil.isRandomTrue(getAddHealthRate(StrengthOperator.getLevel(item)))) {
			return;
		}

		new BukkitRunnable() {
//			double nowHealth = ((Damageable)target).getHealth();
			@Override
			public void run() {
//				double health = ((Damageable)target).getHealth();
				LivingEntityUtil.addHealth(owner, 0.2 + StrengthOperator.getLevel(item) * 0.01);
				if (owner.getType() == EntityType.PLAYER) {
					((Player)owner).playSound(owner.getLocation(), Sound.SILVERFISH_IDLE, 1, (float) 0.1);
				}
				particleData.run(owner.getLocation().add(0, 1, 0));
			}
		}.runTaskLater(Main.plugin, 2);
	}

	protected int getAddHealthRate(int level) {
		return (int) (2 + 1.5 * level);
	}

	@Override
	public int getCooltimeTick(ItemStack item) {
		int level = StrengthOperator.getLevel(item);
		return getCooltime(level);
	}

	protected int getCooltime(int level) {
		switch (getMaxStrengthCount() - level) {
		case 0:
			return 20 * 40;
		case 1:
			return 20 * 50;
		case 2:
			return 20 * 60;
		case 3:
			return 20 * 70;
		default:
			break;
		}
		return 0;
	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		if (getCooltime(level) != 0) {
			return new String[]{Message.getMessage("{0}％の確率で攻撃時、体力を吸収する", getAddHealthRate(level)),
					Message.getMessage("右クリックで自分の体力を使い、敵にダメージを与える。(クールタイム{0}秒)", (int)(getCooltime(level) / 20))};
		}
		return new String[]{Message.getMessage("{0}％の確率で攻撃時、体力を吸収する", getAddHealthRate(level))};
	}

	@Override
	public String getSpecialName() {
		return "ソウルソード";
	}

	@Override
	public Collection<ItemInterface> getAllItem() {
		return itemList;
	}

	@Override
	public int getRank() {
		return 3;
	}

	@Override
	protected double getSpecialDamagePercent() {
		return 1.2;
	}

	@Override
	protected String getBaseId() {
		return "soul sword";
	}

	@Override
	public int getMaxSlotCount() {
		return 3;
	}

	@Override
	public int getDefaultSlotCount() {
		return 1;
	}
}
