package main.money.galion;

public enum GalionEditReason {
	mob_drop(true),
	penalty(true),
	consume_shop(true),
	consume_strength(true),
	command(true),
	item(true),
	system(false);

	boolean isPrintMessageLog;

	private GalionEditReason(boolean isPrintMessageLog) {
		this.isPrintMessageLog = isPrintMessageLog;
	}

	public boolean isPrintMessageLog() {
		return isPrintMessageLog;
	}
}
