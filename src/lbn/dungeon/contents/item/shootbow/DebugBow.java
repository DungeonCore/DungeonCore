package lbn.dungeon.contents.item.shootbow;

import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.itemAbstract.BowItem;
import lbn.item.strength.StrengthOperator;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DebugBow extends BowItem{

	@Override
	public String getItemName() {
		return "デバック弓";
	}

	@Override
	public String getId() {
		return "DebugBow";
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return null;
	}

	@Override
	protected void excuteOnLeftClick2(PlayerInteractEvent e) {
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
	public int getMaxStrengthCount() {
		return 8;
	}

	@Override
	protected String[] getDetail() {
		return null;
	}

	@Override
	public void excuteOnProjectileHit(ProjectileHitEvent e, ItemStack bow) {

	}

	@Override
	protected void excuteOnShootBow2(EntityShootBowEvent e) {
	}

	@Override
	protected int getBaseBuyPrice() {
		return 0;
	}

}
