package lbn.player.customplayer;

import java.util.ArrayList;

import lbn.api.LevelType;
import lbn.api.player.OneReincarnationData;
import lbn.api.player.ReincarnationInterface;

/**
 * ある一人のPlayerの転生データを管理するためのクラス
 *
 */
public class PlayerReincarnationData {

	//剣の転生データ
	ArrayList<OneReincarnationData> swordReincarnationData = new ArrayList<OneReincarnationData>();
	//魔法の転生データ
	ArrayList<OneReincarnationData> magicReincarnationData = new ArrayList<OneReincarnationData>();
	//弓の転生データ
	ArrayList<OneReincarnationData> bowReincarnationData = new ArrayList<OneReincarnationData>();

	/**
	 * 転生を行うときのデータを追加
	 * @param reincarnationInterface
	 * @param levelType
	 * @return 今回追加されたOneReincarnationData
	 */
	public OneReincarnationData addReincarnation(ReincarnationInterface reincarnationInterface, LevelType levelType) {
		//現在の転生回数を取得
		int nowReincarnationCount = getNowReincarnationCount(levelType);
		//転生データを作成
		OneReincarnationData oneReincarnationData = new OneReincarnationData(reincarnationInterface, levelType, nowReincarnationCount + 1);
		//データを追加
		getDataMap(levelType).add(oneReincarnationData);

		return oneReincarnationData;
	}

	/**
	 * 現在何回転生を行ったのかを取得
	 * @return
	 */
	public int getNowReincarnationCount(LevelType levelType) {
		ArrayList<OneReincarnationData> dataMap = getDataMap(levelType);
		return dataMap.size();
	}

	/**
	 * 対応するレベルタイプに対応したMapを取得
	 * @param levelType
	 * @return
	 */
	protected ArrayList<OneReincarnationData> getDataMap(LevelType levelType) {
		switch (levelType) {
		case SWORD:
			return swordReincarnationData;
		case MAGIC:
			return magicReincarnationData;
		case BOW:
			return bowReincarnationData;
		default:
			throw new RuntimeException("invalied level type for reincaranation:" + levelType);
		}
	}

	/**
	 * 指定したレベルタイプに対応した転生データを取得
	 * @param levelType
	 * @return
	 */
	public ArrayList<OneReincarnationData> getOneReincarnationDataList(LevelType levelType) {
		try {
			return getDataMap(levelType);
		} catch (RuntimeException e) {
			return null;
		}
	}

	/**
	 * 転生データを再設定する
	 */
	public void reapplayReincarnation() {
		//TODO
	}
}

