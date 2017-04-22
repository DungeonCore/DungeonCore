package lbn.dungeon.contents.item.other.strengthBase;

import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.dungeon.contents.strength_template.ChangeStrengthItemTemplate;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemInterface;
import lbn.item.customItem.AbstractItem;
import lbn.item.itemInterface.StrengthChangeItemable;
import lbn.item.system.lore.ItemLoreToken;
import lbn.util.Message;

import org.bukkit.inventory.ItemStack;

public abstract class StrengthBaseItem extends AbstractItem implements StrengthChangeItemable {

	@Override
	public void setStrengthDetail(int level, ItemLoreToken loreToken) {
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
	public String[] getDetail() {
		return Message.getMessage("このアイテムを{0}回強化に成功すると, [{1}]になります。", getMaxStrengthCount(),
				getLastStrengthResultItem().getSimpleName()).split(",");
	}

}
