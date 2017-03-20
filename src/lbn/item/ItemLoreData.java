package lbn.item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import lbn.util.ItemStackUtil;

import org.bukkit.inventory.ItemStack;


public class ItemLoreData {
	ItemStack item;

	ArrayList<String> beforeDetail = new ArrayList<>();

	ArrayList<String> afterDetail = new ArrayList<>();

	TreeMap<String, ItemLoreToken> loreMap = new TreeMap<String, ItemLoreToken>(new ComparatorImplemention());

	public ItemLoreData() {
	}

//	public static void main(String[] args) {
//		String[] aa = new String[]{"§8id:level60Bow", "", "§a[機能性能]§ctitle", "    §e最大強化 ： §6+11", "    §e使用可能 ： §6弓レベル60以上", "    §eスキルレベル ：§661レベル", "    §e耐久値 ： §6384", "", "", "§a[強化性能]§ctitle", "    §eADD:ダメージ §6+34.9", "", "§a[SLOT]  §b最大3個", "§f    ■ 空のスロット§0id:epty", ""};
//		List<String> asList = Arrays.asList(aa);
//		new ItemLoreData(asList);
//	}

	public ItemLoreData(ItemStack stack) {
		this(ItemStackUtil.getLore(stack));
	}

	public ItemLoreData(List<String> lore) {
		LorePoint point = LorePoint.BEFORE;

		ItemLoreToken nowToken = null;

		for (String line : lore) {
			switch (point) {
			case BEFORE:
				//タイトルでない場合は値を保持しておく
				if (!ItemLoreToken.isTitle(line)) {
					beforeDetail.add(line);
					break;
				}
				//タイトルであった場合はLoreTokenとする
				point = LorePoint.LORE;
			case LORE:
				if (nowToken == null) {
					//タイトルだった場合はLoreTokenを生成する
					if (ItemLoreToken.isTitle(line)) {
						nowToken = new ItemLoreToken(line, false);
					//タイトルでなければ次に進む
						break;
					} else if (line.equals("")) {
						//空白の時はミスかもしれないので何もしない
						break;
					} else {
						point = LorePoint.AFTER;
					}
				} else {
					//空白の場合はLoreToken終了とする
					if (line.equals("")) {
						loreMap.put(nowToken.getTitle(), nowToken);
						nowToken = null;
					} else {
						nowToken.addLoreAsOriginal(line);
					}
					break;
				}
			case AFTER:
				afterDetail.add(line);
			default:
				break;
			}

		}
		//一番最後が空文字ならそれを削除する
		if (beforeDetail.size() != 0) {
			if (beforeDetail.get(beforeDetail.size() - 1).equals("")) {
				beforeDetail.remove(beforeDetail.size() - 1);
			}
		}
		//一番最後が空文字ならそれを削除する
		if (afterDetail.size() != 0) {
			if (afterDetail.get(afterDetail.size() - 1).equals("")) {
				afterDetail.remove(afterDetail.size() - 1);
			}
		}
	}

	public void addBefore(String line) {
		beforeDetail.add(line);
	}

	public void addBefore(List<String> line) {
		beforeDetail.addAll(line);
	}

	public void setBefore(List<String> line) {
		beforeDetail = new ArrayList<String>(line);
	}

	public void addAfter(String line) {
		afterDetail.add(line);
	}

	public void addAfter(List<String> line) {
		afterDetail.addAll(line);
	}

	public void setAfter(List<String> line) {
		afterDetail = new ArrayList<String>(line);
	}

	/**
	 * Loreを取得する
	 * @param item
	 */
	public List<String> getLore() {
		List<String> lore = new ArrayList<String>();
		lore.addAll(beforeDetail);
		for (Entry<String, ItemLoreToken> entry : loreMap.entrySet()) {
			lore.addAll(entry.getValue().getLore());
			lore.add("");
		}
		lore.addAll(afterDetail);

		return lore;
	}

	/**
	 * Loreを追加する
	 * @param loreToken
	 */
	public void addLore(ItemLoreToken loreToken) {
		loreMap.put(loreToken.getTitle(), loreToken);
	}

	enum LorePoint {
		BEFORE, LORE, AFTER ;
	}

	class ComparatorImplemention implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			int index1 = getIndex(o1);
			int index2 = getIndex(o2);

			if (index1 == index2) {
				return o1.compareTo(o2);
			}
			return index1 - index2;
		}
	}

	public int getIndex(String value) {
		if (value.contains(ItemLoreToken.TITLE_STANDARD)) {
			return 1;
		} else if (value.contains(ItemLoreToken.TITLE_STRENGTH)) {
			return 2;
		} else if (value.contains(ItemLoreToken.TITLE_SLOT)) {
			return 20;
		}
		return 10;
	}
}
