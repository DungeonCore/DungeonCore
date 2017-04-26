package lbn.dungeon.contents.item.magic;

import lbn.item.itemInterface.MagicExcuteable;
import lbn.item.system.strength.StrengthOperator;

import org.bukkit.inventory.ItemStack;

public abstract class AbstractMagicExcuter implements MagicExcuteable{

	public AbstractMagicExcuter(ItemStack item, int cooltimeTick, int needMagicPoint, String id, boolean isShowMessage) {
		this.item = item;
		this.cooltimeTick = cooltimeTick;
		this.needMagicPoint = needMagicPoint;
		this.id = id;
		this.isShowMessage = isShowMessage;
	}

	ItemStack item;
	int cooltimeTick;
	int needMagicPoint;
	String id;
	boolean isShowMessage;


	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getCooltimeTick(ItemStack item) {
		return cooltimeTick;
	}

	@Override
	public int getNeedMagicPoint() {
		return needMagicPoint;
	}

	@Override
	public boolean isShowMessageIfUnderCooltime() {
		return isShowMessage;
	}

	@Override
	public ItemStack getItem() {
		return item;
	}

	public int getStrengthLevel() {
		return StrengthOperator.getLevel(item);
	}
}
