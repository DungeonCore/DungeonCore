package lbn.item.itemInterface;

import java.util.Collection;

import lbn.item.ItemInterface;
import lbn.mob.attribute.Attribute;
import lbn.player.AttackType;

public interface SpecialAttackItemable {
	public String getSpecialName();
	public Collection<ItemInterface> getAllItem();
	public int getRank();
	public String getId();
	public AttackType getAttackType();
	public Attribute getAttribute();
}
