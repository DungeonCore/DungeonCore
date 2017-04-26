package lbn.dungeon.contents.item.shootbow;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.event.player.PlayerKillEntityEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.item.customItem.attackitem.SpreadSheetWeaponData;
import lbn.item.customItem.itemAbstract.BowItem;
import lbn.item.itemInterface.EntityKillable;
import lbn.item.itemInterface.StrengthChangeItemable;
import lbn.item.system.strength.StrengthOperator;
import lbn.player.ItemType;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DebugBow extends BowItem implements StrengthChangeItemable, EntityKillable{

	public DebugBow() {
		super(new SpreadSheetWeaponDataForDebug());
	}

	@Override
	public String getItemName() {
		return "デバック弓";
	}

	@Override
	public String getId() {
		return "DebugBow";
	}

	@Override
	public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {
		super.onPlayerStrengthFinishEvent(event);

		availableLevel = event.getLevel();
	}

	@Override
	public void excuteOnLeftClick(PlayerInteractEvent e) {
		super.excuteOnLeftClick(e);

		e.getPlayer().sendMessage("ランク１５ ダメージ：" + getAttackItemDamage(0));

		ItemStack item = e.getItem();
		int level = StrengthOperator.getLevel(item);
		availableLevel = level;

		if (e.getPlayer().isSneaking()) {
			Bukkit.dispatchCommand(e.getPlayer(), "statusCommand MAIN_LEVEL set " + level);
		}
	}

	public static int availableLevel = 0;

	@Override
	public int getAvailableLevel() {
		return availableLevel * 10;
	}

	@Override
	public void onCombatEntity(PlayerCombatEntityEvent e) {
		super.onCombatEntity(e);
		e.getPlayer().sendMessage("debug bow:onCombatEntity");
	}

	@Override
	public void onKillEvent(PlayerKillEntityEvent e) {
		e.getPlayer().sendMessage("debug bow:onKillEvent");
	}
}

class SpreadSheetWeaponDataForDebug extends SpreadSheetWeaponData {
	public SpreadSheetWeaponDataForDebug() {
	}

	@Override
	public String getId() {
		return "debug_bow";
	}

	@Override
	public String getName() {
		return "デバック用弓";
	}

	@Override
	public int getRank() {
		return 15;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.BOW;
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Material.BOW);
	}
}
