package main.player.appendix.appendixObject;

public class StatusKillLevelAppendix extends AbstractPlayerAppendix{
	private static final long serialVersionUID = 5670618256837773173L;

	public void addAttack(double value) {
		attack += value;
	}

	public void addWalkSpeed(double value) {
		speed += value;
	}


	public void addMaxHealth(double value) {
		maxHealth += value;
	}

	public void addStunPercent(double value) {
		stunRate += value;
	}

	public void addStunTick(int value) {
		stunTick += value;
	}

	@Override
	public String getName() {
		return "KILL LEVEL APPENDIX";
	}


	@Override
	public boolean isSavingData() {
		return false;
	}

}
