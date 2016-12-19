package lbn.player.status;

public enum StatusAddReason {
	monster_drop(true),
	system(false),
	commad(true);

	boolean isPrintMessageLog;

	StatusAddReason(boolean isPrintMessageLog) {
		this.isPrintMessageLog = isPrintMessageLog;
	}

	public boolean isPrintMessageLog() {
		return isPrintMessageLog;
	}
}
