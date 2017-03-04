package lbn.dungeon.contents.item.sword;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.event.player.PlayerKillEntityEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.item.attackitem.SpreadSheetWeaponData;
import lbn.item.itemAbstract.SwordItem;
import lbn.item.itemInterface.EntityKillable;
import lbn.item.itemInterface.StrengthChangeItemable;
import lbn.item.strength.StrengthOperator;
import lbn.player.ItemType;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DebugSword extends SwordItem implements StrengthChangeItemable, EntityKillable{
	public DebugSword() {
		super(new SpreadSheetWeaponDataForDebug());
	}

	int availableLevel = 0;

	@Override
	public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {
		super.onPlayerStrengthFinishEvent(event);

		availableLevel = event.getLevel();
	}

	@Override
	public int getAvailableLevel() {
		return availableLevel * 10;
	}

	@Override
	public void excuteOnRightClick(PlayerInteractEvent e) {
		super.excuteOnRightClick(e);

		ItemStack item = e.getItem();

		availableLevel = StrengthOperator.getLevel(item);

		e.getPlayer().sendMessage("ランク１５　攻撃力:" + getAttackItemDamage(0));
	}

	@Override
	public void onCombatEntity(PlayerCombatEntityEvent e) {
		super.onCombatEntity(e);
		e.getPlayer().sendMessage("debug sword:onCombatEntity");
	}

	@Override
	public void onKillEvent(PlayerKillEntityEvent e) {
		e.getPlayer().sendMessage("debug sword:onKillEvent");
	}
}

class SpreadSheetWeaponDataForDebug extends SpreadSheetWeaponData {
	public SpreadSheetWeaponDataForDebug() {
	}

	@Override
	public String getId() {
		return "debug_sword";
	}

	@Override
	public String getName() {
		return "デバック用ソード";
	}

	@Override
	public int getRank() {
		return 15;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.SWORD;
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Material.WOOD_SWORD);
	}
}
