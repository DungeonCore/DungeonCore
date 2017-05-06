package lbn.item.slot;

/**
 * 魔法石の種類
 *
 */
public enum SlotType {
	EMPTY, //空の魔法石
	UNAVAILABLE, //使用不可の魔法石
	NORMAL("魔法石の装着に成功しました", "魔法石の装着に失敗しました"), //通常の魔法石
	ADD_EMPTY("空のスロットの追加に成功しました", "空のスロットの追加に失敗しました"),//空の魔法石を追加
	REMOVE_UNAVAILABLE("使用不可のスロットを取り除きました", "使用不可のスロットを取り除くのに失敗しました"),//使用不可の魔法石を削除する
	ADDUNKNOW,//不明
	;

	private String successComment;
	private String failureComment;

	private SlotType(String successComment, String failureComment) {
		this.successComment = successComment;
		this.failureComment = failureComment;
	}

	private SlotType() {
		this("成功しました", "失敗しました");
	}

	/**
	 * 装着成功時のコメントを取得する
	 * @return
	 */
	public String getSuccessComment() {
		return successComment;
	}

	/**
	 * 装着失敗時のコメントを取得する
	 * @return
	 */
	public String getFailureComment() {
		return failureComment;
	}
}
