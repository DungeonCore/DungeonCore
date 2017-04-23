package lbn.dungeon.contents.item.other.strengthBase;

import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.dungeon.contents.slotStone.other.MagicStoneJade;
import lbn.item.ItemInterface;
import lbn.item.system.strength.StrengthOperator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StrengthBaseJade extends StrengthBaseItem{
	@Override
	public String getItemName() {
		return "魔法石ジェイドの欠片";
	}

	@Override
	public int getMaxStrengthCount() {
		return 6;
	}

	@Override
	public String getId() {
		return "strength_base_jade";
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return 200 * StrengthOperator.getLevel(item);
	}

	@Override
	protected Material getMaterial() {
		return Material.REDSTONE;
	}

	@Override
	protected ItemInterface getLastStrengthResultItem() {
		return new MagicStoneJade();
	}

	@Override
	public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {

	}

}
