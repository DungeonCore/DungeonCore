package lbn.quest;

/**
 * クエストの進行状況を判断するメソッド
 *
 */
public enum QuestProcessingStatus {
	NONE(false),//その他
	NOT_START(false),//開始していない
	PROCESSING(true),//実行中
	PROCESS_END(true);//終了条件を満たしてしるがまだ実行中

	private QuestProcessingStatus(boolean isDoing) {
		this.isDoing = isDoing;
	}

	private boolean isDoing;

	public boolean isDoing() {
		return isDoing;
	}

}
