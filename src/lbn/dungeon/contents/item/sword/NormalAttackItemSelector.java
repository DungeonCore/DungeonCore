package lbn.dungeon.contents.item.sword;

import java.util.ArrayList;
import java.util.Collection;

import lbn.dungeon.contents.item.magic.normalItems.NormalMagicItem;
import lbn.dungeon.contents.item.shootbow.NormalBowWrapper;
import lbn.item.ItemInterface;
import lbn.item.attackitem.SpecialAttackItemSelector;
import lbn.item.itemInterface.SpecialAttackItemable;
import lbn.mob.attribute.Attribute;
import lbn.mob.attribute.AttributeNormal;
import lbn.player.ItemType;

public class NormalAttackItemSelector extends SpecialAttackItemSelector{

	public NormalAttackItemSelector(ItemType type) {
		super(new SpecialAttackItemableForNormal(type));
	}

	@Override
	protected String getRarityStart() {
		return "lowest";
	}

}

class SpecialAttackItemableForNormal implements SpecialAttackItemable {
	protected SpecialAttackItemableForNormal(ItemType type) {
		this.type = type;
		switch (type) {
		case SWORD:
			this.itemList = NormalSwordWrapper.getAllNormalItem();
			break;
		case MAGIC:
			this.itemList = NormalMagicItem.getAllItem();
			break;
		case BOW:
			this.itemList = NormalBowWrapper.getAllNormalItem();
			break;
		default:
			this.itemList = new ArrayList<ItemInterface>();
			break;
		}
	}

	ItemType type;
	Collection<ItemInterface> itemList;


	@Override
	public String getSpecialName() {
		return "COMMON " + getAttackType();
	}

	@Override
	public Collection<ItemInterface> getAllItem() {
		return itemList;
	}

	@Override
	public int getRank() {
		return 0;
	}

	@Override
	public String getId() {
		return "common " + getAttackType();
	}

	@Override
	public ItemType getAttackType() {
		return type;
	}

	@Override
	public Attribute getAttribute() {
		return AttributeNormal.getInstance();
	}

}
