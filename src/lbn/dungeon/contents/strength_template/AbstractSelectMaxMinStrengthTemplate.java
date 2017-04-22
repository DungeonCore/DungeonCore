package lbn.dungeon.contents.strength_template;

public abstract class AbstractSelectMaxMinStrengthTemplate implements StrengthTemplate {
	int maxStrengthCount;

	public AbstractSelectMaxMinStrengthTemplate(int maxStrengthCount) {
		this.maxStrengthCount = maxStrengthCount;
	}

	@Override
	public int successChance(int level) {
		int l = maxStrengthCount - 1;

		level = (level - 1);

		double a = getA();
		double b = (a * l * l + getSuccessRateLevel0() - getSuccessRateLevelMax()) / (-1.0 * l);
		double c = getSuccessRateLevel0();
		return Math.min(100, (int) (a * level * level + b * level + c) + 1);
	}

	abstract protected double getSuccessRateLevel0();

	abstract protected double getSuccessRateLevelMax();

	protected double getA() {
		return 0.1;
	}
}
