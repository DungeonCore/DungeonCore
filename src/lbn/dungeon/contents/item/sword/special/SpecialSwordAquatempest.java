package lbn.dungeon.contents.item.sword.special;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lbn.common.cooltime.Cooltimable;
import lbn.common.cooltime.CooltimeManager;
import lbn.item.ItemInterface;
import lbn.item.strength.StrengthOperator;
import lbn.mob.LastDamageMethodType;
import lbn.util.JavaUtil;
import lbn.util.LivingEntityUtil;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;
import lbn.util.particle.SpringParticleData;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpecialSwordAquatempest extends AbstractSpecialSword implements Cooltimable{
	static ArrayList<ItemInterface> itemList = new ArrayList<ItemInterface>();
	static{
		itemList.add(new SpecialSwordAquatempest(5, Material.IRON_SWORD));
		itemList.add(new SpecialSwordAquatempest(15, Material.IRON_SWORD));
		itemList.add(new SpecialSwordAquatempest(25, Material.IRON_SWORD));
		itemList.add(new SpecialSwordAquatempest(35, Material.IRON_SWORD));
		itemList.add(new SpecialSwordAquatempest(45, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordAquatempest(55, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordAquatempest(65, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordAquatempest(75, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordAquatempest(85, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordAquatempest(95, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordAquatempest(105, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordAquatempest(115, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordAquatempest(125, Material.DIAMOND_SWORD));
		itemList.add(new SpecialSwordAquatempest(135, Material.DIAMOND_SWORD));
	}

	public SpecialSwordAquatempest(int availableLevel, Material m) {
		super(m, availableLevel);
	}

	public SpecialSwordAquatempest() {
	}

	@Override
	protected void excuteOnRightClick2(PlayerInteractEvent e) {

		CooltimeManager cooltimeManager = new CooltimeManager(e, this);

		if (!cooltimeManager.canUse()) {
			cooltimeManager.sendCooltimeMessage(e.getPlayer());
			return;
		}


		Player player = e.getPlayer();
		Location location = player.getLocation();
		int level = StrengthOperator.getLevel(e.getItem());

		SpringParticleData spd = new SpringParticleData(new ParticleData(ParticleType.splash, 20), 3, 5, 1, 10);
		spd.run(location);

		List<Entity> nearbyEntities = player.getNearbyEntities(5, 5, 5);
		for (Entity entity : nearbyEntities) {
			if(LivingEntityUtil.isEnemy(entity)){
				LivingEntityUtil.strikeLightningEffect(entity.getLocation(), player);
				if(level >= 10){
					LivingEntityUtil.trueDamage((LivingEntity) entity, 40, player, LastDamageMethodType.SWORD);
				}else if(level >= 5){
					LivingEntityUtil.trueDamage((LivingEntity) entity, 20, player, LastDamageMethodType.SWORD);
				}else {
					LivingEntityUtil.trueDamage((LivingEntity) entity, 10, player, LastDamageMethodType.SWORD);
				}
			}
		}
		cooltimeManager.setCoolTime();
	}

	@Override
	public String[] getDetail() {
		return null;
	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		return null;
	}

	@Override
	protected void excuteOnMeleeAttack2(ItemStack item, LivingEntity owner,
			LivingEntity target, EntityDamageByEntityEvent e) {

		int level = StrengthOperator.getLevel(item);
		double damage = e.getDamage();

		ParticleData parData = new ParticleData(ParticleType.portal, 100);

		int prcnt = (int) (5 + level *2.5);
		double dPlus = 0.06 * level +1;

		if(JavaUtil.isRandomTrue(prcnt)){

			damage += damage * dPlus;
			parData.run(target.getLocation());
			target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2, 1));
			owner.getWorld().playSound(owner.getLocation(), Sound.BLAZE_HIT, 1, (float) 1.5);
		}
	}

	@Override
	public int getCooltimeTick(ItemStack item) {
		return 20 * 60;
	}

	@Override
	public String getSpecialName() {
		return "アクアテンペスト";
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
		return 1.15;
	}

	@Override
	protected String getBaseId() {
		return "AquaTempest";
	}

	@Override
	public int getMaxSlotCount() {
		return 4;
	}

	@Override
	public int getDefaultSlotCount() {
		return 3;
	}

}
