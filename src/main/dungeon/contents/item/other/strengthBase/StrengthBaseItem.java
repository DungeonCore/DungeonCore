package main.dungeon.contents.item.other.strengthBase;

import main.common.event.player.PlayerSetStrengthItemResultEvent;
import main.dungeon.contents.strength_template.ChangeStrengthItemTemplate;
import main.dungeon.contents.strength_template.StrengthTemplate;
import main.item.AbstractItem;
import main.item.ItemInterface;
import main.item.itemInterface.StrengthChangeItemable;
import main.util.Message;

import org.bukkit.inventory.ItemStack;

public abstract class StrengthBaseItem extends AbstractItem implements StrengthChangeItemable{

	@Override
	public String[] getStrengthDetail(int level) {
		return null;
	}


	@Override
	public StrengthTemplate getStrengthTemplate() {
		return new ChangeStrengthItemTemplate(getItem(), 1000, 70);
	}

	@Override
	public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {
		int nextLevel = event.getNextLevel();
		if (getMaxStrengthCount() == nextLevel) {
			ItemStack newItem = getLastStrengthResultItem().getItem();
			event.setItem(newItem);
		}
	}

	protected abstract ItemInterface getLastStrengthResultItem();

	@Override
	protected String[] getDetail() {
		return Message.getMessage("このアイテムを{0}回強化に成功すると, [{1}]になります。", getMaxStrengthCount(), getLastStrengthResultItem().getSimpleName()).split(",");
	}

}
