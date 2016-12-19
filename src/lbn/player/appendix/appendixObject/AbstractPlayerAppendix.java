package lbn.player.appendix.appendixObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayerAppendix implements Serializable{
	private static final long serialVersionUID = 1419175991945872989L;

	protected float speed = 0;
	protected double attack = 0;
	protected double projetileAttack = 0;
	protected double maxHealth = 0;
	protected double stunRate = 0;
	protected int stunTick = 0;
	protected double fireDamagePassageRate = 1;
	protected int maxMagicPoint = 100;

	abstract public String getName();

	public int hashCode() {
		return getName().hashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof AbstractPlayerAppendix) {
			return getName().equals(((AbstractPlayerAppendix) obj).getName());
		}

		if (obj instanceof String) {
			return getName().equals(obj);
		}

		return false;
	}

	public float getAddedWalkSpeed() {
		return speed;
	}

	public double getAddedAtackPoint() {
		return attack;
	}

	public double getAddedMaxHealth() {
		return maxHealth;
	}

	/**
	 * 1だと抵抗なし、0だとダメージなし
	 * @return
	 */
	public double getFireDamagePassageRate() {
		return Math.max(fireDamagePassageRate, 0);
	}

	public double getAddedStunRate() {
		return stunRate;
	}

	public int getAddedStunTick() {
		return stunTick;
	}

	public int getMaxMp() {
		return maxMagicPoint;
	}

	public List<String> getView() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("最大体力:" + getAddedMaxHealth());
		list.add("攻撃力:" + getAddedAtackPoint());
		list.add("歩く速さ:" + getAddedWalkSpeed());
		list.add("気絶確率:" + getAddedStunRate() + "%");
		list.add("気絶時間:" + getAddedStunTick() + "tick");
		list.add("最大MP:" + getMaxMp() + "MP");
		return list;
	}

	abstract public boolean isSavingData();
}

