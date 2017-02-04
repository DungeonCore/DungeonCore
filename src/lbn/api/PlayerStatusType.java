package lbn.api;

public enum PlayerStatusType {
	SWORD_ATTACK, BOW_ATTACK, MAGIC_ATTACK, MAX_MAGIC_POINT, MAX_HP;

	boolean isPercent;

	private PlayerStatusType(boolean isPercent) {
	}

	private PlayerStatusType() {
		this(false);
	}

	public boolean isPercent() {
		return isPercent;
	}

}
