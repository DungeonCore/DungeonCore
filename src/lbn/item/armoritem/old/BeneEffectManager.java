package lbn.item.armoritem.old;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lbn.item.strength.StrengthOperator;
import lbn.util.ItemStackUtil;

import org.bukkit.inventory.ItemStack;


public class BeneEffectManager {
	static ArrayList<BeneEffectType> typeA = new ArrayList<BeneEffectType>();
	static ArrayList<BeneEffectType> typeB = new ArrayList<BeneEffectType>();
	static ArrayList<BeneEffectType> typeC = new ArrayList<BeneEffectType>();
	static{
		typeA.add(BeneEffectType.PROTECTION_EXPLOTION);
		typeA.add(BeneEffectType.PROTECTION_DROWN);
		typeA.add(BeneEffectType.PROTECTION_FALL);
		typeA.add(BeneEffectType.PROTECTION_FIRE);
		typeA.add(BeneEffectType.PROTECTION_POISON);
		typeA.add(BeneEffectType.PROTECTION_WITHER);
		typeB.add(BeneEffectType.RESISTANCE_GIANT);
		typeB.add(BeneEffectType.RESISTANCE_PIGMAN);
		typeB.add(BeneEffectType.RESISTANCE_SKELETON);
		typeB.add(BeneEffectType.RESISTANCE_SPIDER);
		typeB.add(BeneEffectType.RESISTANCE_SKELETON);
		typeC.add(BeneEffectType.POTION_REGENERATION);
		typeC.add(BeneEffectType.POTION_RESISTANC);
		typeC.add(BeneEffectType.POTION_SLOW);
		typeC.add(BeneEffectType.POTION_SPEED);
		typeC.add(BeneEffectType.POTION_WEAKNESS);
	}

	static Random rnd = new Random();

	public static ArrayList<String> getNewEffectLore(int size) {
		ArrayList<String> lore = new ArrayList<String>();

		switch (size) {
		case 3:
			lore.add(typeC.get(rnd.nextInt(typeC.size())).getLine(0));
		case 2:
			lore.add(typeB.get(rnd.nextInt(typeB.size())).getLine(0));
		case 1:
			lore.add(typeA.get(rnd.nextInt(typeA.size())).getLine(0));
		default:
			break;
		}
		return lore;
	}

	public static ArrayList<BeneEffectType> getBeneEffectList(List<String> lore) {
		ArrayList<BeneEffectType> beneList = new  ArrayList<BeneEffectType>();
		for (String string : lore) {
			//idがあるか確認
			if (string.contains(BeneEffectType.BENE_ID)) {
				//id取得
				String beneId = string.substring(string.lastIndexOf(":") + 1).trim();
				//beneが存在すれば追加する
				BeneEffectType beneTypeById = BeneEffectType.getBeneTypeById(beneId);
				if (beneTypeById != null) {
					beneList.add(beneTypeById);
				}
			}
		}
		return beneList;
	}

	/**
	 * もとからある場合は削除する
	 * @param lore
	 * @param beneList
	 * @param strengthLevel
	 * @return
	 */
	public static ArrayList<String> getBeneLore(List<BeneEffectType> beneList, int strengthLevel) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (BeneEffectType beneEffectType : beneList) {
			arrayList.add(beneEffectType.getLine(strengthLevel));
		}
		return arrayList;
	}

	@SuppressWarnings("deprecation")
	public static void updateBeneLore(ItemStack item, List<BeneEffectType> types) {
		ArrayList<String> beneLore = BeneEffectManager.getBeneLore(types, StrengthOperator.getLevel(item));
		List<String> lore = ItemStackUtil.getLore(item);
		StrengthOperator.removedStrengthLore(lore);
		StrengthOperator.addStrengthLore(beneLore, lore);
		ItemStackUtil.setLore(item, lore);
	}

//	public static ArrayList<String> getUpdatedLore(List<String> list, int strengthLevel) {
//		//beneが書いてあるIndexのList
//		ArrayList<String> newLore = new ArrayList<String>();
//
//		for (int i = 0; i < list.size(); i++) {
//			String string = list.get(i);
//			//idがあるか確認
//			if (string.contains(BeneEffectType.BENE_ID)) {
//				//id取得
//				String beneId = string.substring(string.lastIndexOf(":")).trim();
//				//beneが存在すればレベルを更新する
//				BeneEffectType beneTypeById = BeneEffectType.getBeneTypeById(beneId);
//				if (beneTypeById != null) {
//					newLore.add(beneTypeById.getLine(strengthLevel));
//					continue;
//				}
//			}
//
//			//その他のものは変更しない
//			newLore.add(string);
//		}
//		return newLore;
//	}
}
