package main.item.itemInterface;

import java.util.Collection;

import main.item.ItemInterface;
import main.mob.attribute.Attribute;
import main.player.AttackType;

public interface SpecialAttackItemable {
	public String getSpecialName();
	public Collection<ItemInterface> getAllItem();
	public int getRank();
	public String getId();
	public AttackType getAttackType();
	public Attribute getAttribute();
}
