package lbn.player.appendix.appendixObject;

import lbn.item.setItem.SetItemPartsType;

public class SetItemAppendix extends AbstractPlayerAppendix{
	private static final long serialVersionUID = 2654840015346727719L;

	String name;

	public SetItemAppendix(SetItemPartsType type) {
		name = "SET_ITEM_" + type;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setAttack(double attack) {
		this.attack = attack;
	}

	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void setStunPercent(double stunPercent) {
		this.stunRate = stunPercent;
	}

	public void setStunTick(int stunTick) {
		this.stunTick = stunTick;
	}

	public void setFireDamagePassageRate(double fireDamagePassageRate) {
		this.fireDamagePassageRate = fireDamagePassageRate;
	}

	@Override
	public boolean isSavingData() {
		return false;
	}


}
