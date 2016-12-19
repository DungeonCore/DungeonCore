package main.dungeon.contents.item.sword;

import java.util.ArrayList;
import java.util.Collection;

import main.dungeon.contents.item.magic.normalItems.NormalMagicItem;
import main.dungeon.contents.item.shootbow.NormalBowWrapper;
import main.item.ItemInterface;
import main.item.attackitem.SpecialAttackItemSelector;
import main.item.itemInterface.SpecialAttackItemable;
import main.mob.attribute.Attribute;
import main.mob.attribute.AttributeNormal;
import main.player.AttackType;

public class NormalAttackItemSelector extends SpecialAttackItemSelector{

	public NormalAttackItemSelector(AttackType type) {
		super(new SpecialAttackItemableForNormal(type));
	}

	@Override
	protected String getRarityStart() {
		return "lowest";
	}

}

class SpecialAttackItemableForNormal implements SpecialAttackItemable {
	protected SpecialAttackItemableForNormal(AttackType type) {
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

	AttackType type;
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
	public AttackType getAttackType() {
		return type;
	}

	@Override
	public Attribute getAttribute() {
		return AttributeNormal.getInstance();
	}

}
