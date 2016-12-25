package lbn.money.galion;

public enum GalionEditReason {
	mob_drop(true),
	penalty(true),
	consume_shop(true),
	consume_strength(true),
	command(true),
	get_money_item(true),
	quest_reword(true),
	system(false);

	boolean isPrintMessageLog;

	private GalionEditReason(boolean isPrintMessageLog) {
		this.isPrintMessageLog = isPrintMessageLog;
	}

	public boolean isPrintMessageLog() {
		return isPrintMessageLog;
	}
}
