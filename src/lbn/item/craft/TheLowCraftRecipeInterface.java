package lbn.item.craft;

import lbn.item.ItemLoreToken;

public interface TheLowCraftRecipeInterface {

	/**
	 * 材料を追加する
	 * @param itemid
	 * @param amount
	 */
	public abstract void addMaterial(String itemid, int amount);

	/**
	 *一覧に表示されるLoreを取得
	 * @return
	 */
	public abstract ItemLoreToken getViewLore();


	/**
	 * インスタンスを取得する
	 * @param mainItemId
	 * @return
	 */
	public static TheLowCraftRecipeInterface getInstance(String mainItemId) {
		if (mainItemId == null) {
			return new TheLowCraftRecipeWithMaterial();
		}
		return new TheLowCraftRecipeWithMainItem(mainItemId);
	}
}