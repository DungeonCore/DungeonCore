package main.player.appendix.appendixObject;

import java.util.Collection;

public class SumPlayerAppendix extends AbstractPlayerAppendix{
	private static final long serialVersionUID = -4230194924087824240L;

	@Override
	public String getName() {
		return "SUM_APPENDIX";
	}

	@Override
	public float getAddedWalkSpeed() {
		return Math.min(super.getAddedWalkSpeed(), 0.8f);
	}

	/**
	 * appendexを追加する
	 * @param appendix
	 */
	public SumPlayerAppendix addApendix(Collection<AbstractPlayerAppendix> appendixList) {
		for (AbstractPlayerAppendix appendix : appendixList) {
			addApendix(appendix);
		}
		return this;
	}
	/**
	 * appendexを追加する
	 * @param appendix
	 */
	public SumPlayerAppendix addApendix(AbstractPlayerAppendix appendix) {
		speed += appendix.getAddedWalkSpeed();
		attack += appendix.getAddedAtackPoint();
		maxHealth += appendix.getAddedMaxHealth();
		stunRate += appendix.getAddedStunRate();
		stunTick += appendix.getAddedStunTick();

		fireDamagePassageRate *=  appendix.getFireDamagePassageRate();

		return this;
	}

	/**
	 * appendexを削除する
	 * @param appendix
	 */
	public SumPlayerAppendix removeApendix(AbstractPlayerAppendix appendix) {
		speed -= appendix.getAddedWalkSpeed();
		attack -= appendix.getAddedAtackPoint();
		maxHealth -= appendix.getAddedMaxHealth();
		stunRate -= appendix.getAddedStunRate();
		stunTick -= appendix.getAddedStunTick();

		fireDamagePassageRate /= appendix.getFireDamagePassageRate();
		return this;
	}

	/**
	 * 初期化する
	 */
	public SumPlayerAppendix clear() {
		return new SumPlayerAppendix();
	}

	@Override
	public boolean isSavingData() {
		return false;
	}
}
