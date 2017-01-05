package lbn.player.appendix;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import lbn.player.appendix.appendixObject.AbstractPlayerAppendix;
import lbn.player.appendix.appendixObject.SumPlayerAppendix;

import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

class PlayerAppendexMap extends HashMap<UUID, SumPlayerAppendix> {
	private static final long serialVersionUID = 8518517389962217280L;

	private HashMultimap<UUID, AbstractPlayerAppendix> multiMap = HashMultimap.create();

	/**
	 * 対象のプレイヤーのSumAppendixを取得する
	 */
	@Override
	public SumPlayerAppendix get(Object paramObject) {
		UUID id = null;
		if (paramObject instanceof Player) {
			id = ((Player) paramObject).getUniqueId();
		} else if (paramObject instanceof UUID) {
			id = (UUID) paramObject;
		}

		if (id == null) {
			throw new NullPointerException("param is not allowed null");
		}

		SumPlayerAppendix sumPlayerAppendix = super.get(id);
		//まだ存在しなかったら追加する
		if (sumPlayerAppendix == null) {
			sumPlayerAppendix = new SumPlayerAppendix();
			put(id, sumPlayerAppendix);
		}
		return sumPlayerAppendix;
	}

	/**
	 * 対象のプレイヤーのすべてのappendixを削除する
	 */
	@Override
	public SumPlayerAppendix remove(Object paramObject) {
		UUID id = null;
		if (paramObject instanceof Player) {
			id = ((Player) paramObject).getUniqueId();
		} else if (paramObject instanceof UUID) {
			id = (UUID) paramObject;
		}

		if (id == null) {
			throw new NullPointerException("param is not allowed null");
		}
		//multimapから削除
		multiMap.removeAll(id);

		//sumを更新する
		return get(id).clear();
	}

	@Override
	public void clear() {
		multiMap.clear();

		Set<UUID> idList = keySet();
		for (UUID id : idList) {
			get(id).clear();
		}
	}

//	@Deprecated
//	@Override
//	public boolean remove(Object paramObject1, Object paramObject2) {
//		return super.remove(paramObject1, paramObject2);
//	}

	/**
	 * 対象のプレイヤーにappendixを追加する
	 * @param id
	 * @param appendix
	 * @return
	 */
	public SumPlayerAppendix addApendix(UUID id, AbstractPlayerAppendix appendix) {
		boolean containsEntry = multiMap.containsEntry(id, appendix);
		//同種のappendixが存在する場合は古いものを削除する
		if (containsEntry) {
			AbstractPlayerAppendix oldAppendix = getAppendixByName(id, appendix.getName());
			removeAppendix(id, oldAppendix);
		}
		multiMap.put(id, appendix);
		return get(id).addApendix(appendix);
	}

	/**
	 *  対象のプレイヤーの対象のappendixを取り除く
	 * @param p
	 * @param appendix
	 * @return
	 */
	public SumPlayerAppendix removeAppendix(UUID id, AbstractPlayerAppendix appendix) {
		//現在削除する予定のAppendix
		AbstractPlayerAppendix removeAppendex = null;

		//現在削除予定のAppendixを取得する
		Set<AbstractPlayerAppendix> set = multiMap.get(id);
		for (AbstractPlayerAppendix nowAppendix : set) {
			if (nowAppendix.equals(appendix)) {
				removeAppendex = nowAppendix;
			}
		}

		//起こりえないが念のため
		if (removeAppendex == null) {
			new RuntimeException("appendix is not allowed null!!").printStackTrace();
			removeAppendex = appendix;
		}

		//multimapから削除
		multiMap.remove(id, appendix);
		//sumから削除
		return get(id).removeApendix(removeAppendex);
	}

	/**
	 * 追加されているappendixから指定された名前と同じ名前のappendixを取得
	 * @param p
	 * @param appendixName
	 * @return
	 */
	public AbstractPlayerAppendix getAppendixByName(UUID id, String appendixName) {
		//対象のAppendixを取得
		Collection<AbstractPlayerAppendix> collection = multiMap.get(id);
		for (AbstractPlayerAppendix abstractPlayerAppendix : collection) {
			if (abstractPlayerAppendix.equals(appendixName)) {
				return abstractPlayerAppendix;
			}
		}
		return null;
	}

	public Serializable getSavingData() {
		HashMultimap<UUID, AbstractPlayerAppendix> map = HashMultimap.create();
		for (UUID id : multiMap.keySet()) {
			for (AbstractPlayerAppendix appendix : multiMap.get(id)) {
				if (appendix.isSavingData()) {
					map.put(id, appendix);
				}
			}
		}
		return map;
	}

	/**
	 * multimapをセットする
	 * @param multiMap
	 */
	public void setMultiMap(HashMultimap<UUID, AbstractPlayerAppendix> multiMap) {
		this.multiMap = multiMap;

		clear();
		//playerごとに処理する
		for (UUID id : multiMap.keySet()) {
			for (AbstractPlayerAppendix appendix : multiMap.get(id)) {
				addApendix(id, appendix);
			}
		}
	}
}
