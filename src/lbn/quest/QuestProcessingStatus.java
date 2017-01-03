package lbn.quest;

public enum QuestProcessingStatus {
	NONE(false),
	NOT_START(false),
	PROCESSING(true),
	PROCESS_END(true);

	private QuestProcessingStatus(boolean isDoing) {
		this.isDoing = isDoing;
	}

	private boolean isDoing;

	public boolean isDoing() {
		return isDoing;
	}

}
