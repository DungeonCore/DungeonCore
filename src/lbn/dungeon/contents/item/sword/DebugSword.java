package lbn.dungeon.contents.item.sword;

import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.dungeoncore.Main;
import lbn.item.itemAbstract.SwordItem;
import lbn.item.strength.StrengthOperator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DebugSword extends SwordItem{

	@Override
	public String getItemName() {
		return "デバックソード";
	}

	@Override
	public String getId() {
		return "DebugSword";
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return null;
	}

	@Override
	protected void excuteOnMeleeAttack2(ItemStack item, LivingEntity owner,
			LivingEntity target, EntityDamageByEntityEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				target.setNoDamageTicks(0);
			}
		}.runTaskLater(Main.plugin, 1);
	}

	@Override
	protected void excuteOnRightClick2(PlayerInteractEvent e) {
		e.getPlayer().sendMessage("平均:" + (getMaxAttackDamage() + getMinAttackDamage()) / 2);

		ItemStack item = e.getItem();
		int level = StrengthOperator.getLevel(item);
		availableLevel = level;

		if (e.getPlayer().isSneaking()) {
			Bukkit.dispatchCommand(e.getPlayer(), "statusCommand MAIN_LEVEL set " + level);
		}
	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		return null;
	}

	public static int availableLevel = 0;

	@Override
	public int getAvailableLevel() {
		return availableLevel * 10;
	}

	@Override
	public double getAttackItemDamage(int strengthLevel) {
		return (getMaxAttackDamage() + getMinAttackDamage()) / 2;
	}

	@Override
	protected Material getMaterial() {
		return Material.WOOD_SWORD;
	}

	@Override
	public int getMaxStrengthCount() {
		return 8;
	}

	@Override
	public String[] getDetail() {
		return null;
	}

	@Override
	protected int getBaseBuyPrice() {
		return 0;
	}

}
