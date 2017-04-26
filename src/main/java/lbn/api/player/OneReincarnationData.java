package lbn.api.player;

import lbn.api.LevelType;

/**
 * 一回分の転生データを保持するクラス
 */
public class OneReincarnationData {
	/**
	 * @param reincarnationInterface 転生効果
	 * @param levelType 何のLevelTypeの転生か
	 * @param count このLevelTypeで何回目の転生か
	 */
	public OneReincarnationData(ReincarnationInterface reincarnationInterface, LevelType levelType, int count) {
		this.reincarnationInterface = reincarnationInterface;
		this.count = count;
		this.levelType = levelType;
	}

	ReincarnationInterface reincarnationInterface;
	int count;
	LevelType levelType;

	/**
	 * 転生効果を取得
	 * @return
	 */
	public ReincarnationInterface getReincarnationInterface() {
		return reincarnationInterface;
	}

	/**
	 * このレベルタイプで何回目の転生かを取得
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * なんのレベルタイプで転生したかを取得
	 * @return
	 */
	public LevelType getLevelType() {
		return levelType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + ((levelType == null) ? 0 : levelType.hashCode());
		result = prime * result + ((reincarnationInterface == null) ? 0 : reincarnationInterface.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OneReincarnationData other = (OneReincarnationData) obj;
		if (count != other.count)
			return false;
		if (levelType != other.levelType)
			return false;
		if (reincarnationInterface == null) {
			if (other.reincarnationInterface != null)
				return false;
		} else if (!reincarnationInterface.equals(other.reincarnationInterface))
			return false;
		return true;
	}

}
