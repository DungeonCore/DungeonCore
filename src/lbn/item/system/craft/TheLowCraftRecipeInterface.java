package lbn.item.system.craft;

import java.util.Map;

import lbn.item.ItemInterface;

import org.bukkit.entity.Player;

public interface TheLowCraftRecipeInterface {

	/**
	 * 材料を追加する
	 * @param itemid
	 * @param amount
	 */
	public abstract void addMaterial(String itemid, int amount);

	/**
	 * 必要素材を取得する。もし登録された素材が不正、もしくはクラフトできないならnullを返す
	 * @return
	 */
	public abstract Map<ItemInterface, Integer> getMaterialMap();

	/**
	 * 新しいインスタンスを生成する
	 * @param mainItemId
	 * @return
	 */
	public static TheLowCraftRecipeInterface createNewInstance(String mainItemId) {
		if (mainItemId == null) {
			return new TheLowCraftRecipeWithMaterial();
		}
		return new TheLowCraftRecipeWithMainItem(mainItemId);
	}

	/**
	 * クラフトに使うもとのアイテムを取得する。存在しないならnull
	 * @return
	 */
	public ItemInterface getMainItem();

	/**
	 * メインアイテムが存在するならTRUE
	 * @return
	 */
	public boolean hasMainItem();


	/**
	 * 指定されたPlayerが全てのクラフト素材を持っているか確認する
	 * @param p
	 * @return
	 */
	public boolean hasAllMaterial(Player p);
}