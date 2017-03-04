package lbn.item.attackitem.weaponSkill.imple.sword;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.other.ItemStackData;
import lbn.common.other.Stun;
import lbn.dungeoncore.Main;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillWithCombat;
import lbn.player.ItemType;
import lbn.util.LivingEntityUtil;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningOrder extends WeaponSkillWithCombat{

	public LightningOrder() {
		super(ItemType.SWORD);
	}

	@Override
	public int getSkillLevel() {
		return 30;
	}

	@Override
	public String getName() {
		return "ライトニング・オーダー";
	}

	@Override
	public String getId() {
		return "lightningorder";
	}

	@Override
	public String[] getDetail() {
		return new String[]{"スキル発動後５秒以内の最初の攻撃で、", "敵に雷を落とし、1.5秒間スタンさせる", "加えて移動速度を10秒低下させる"};
	}

	@Override
	public int getCooltime() {
		return 120;
	}

	@Override
	public int getNeedMagicPoint() {
		return 50;
	}

	@Override
	public ItemStackData getViewItemStackData() {
		return new ItemStackData(Material.WEB);
	}

	@Override
	public void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e) {
		LivingEntityUtil.strikeLightningEffect(livingEntity.getLocation(), p);

		Stun.addStun(livingEntity, (int) (20 * 1.5));

		new BukkitRunnable() {
			@Override
			public void run() {
				livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 1), true);
			}
		}.runTaskLater(Main.plugin, (long) (20 * 1.6));
	}

}
