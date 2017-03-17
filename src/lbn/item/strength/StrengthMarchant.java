package lbn.item.strength;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.trade.TheLowMerchant;
import lbn.common.trade.TheLowMerchantRecipe;
import lbn.common.trade.nms.MerchantRecipeListImplemention;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemManager;
import lbn.item.implementation.StrengthScrollArmor;
import lbn.item.implementation.StrengthScrollWeapon;
import lbn.item.itemInterface.Strengthenable;
import lbn.item.strength.old.StrengthOperator;
import lbn.util.ItemStackUtil;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class StrengthMarchant extends TheLowMerchant{

	//武器の強化スクロール
	private static final ItemStack WEAPON_SCROLL = new StrengthScrollWeapon().getItem();
	//防具の強化スクロール
	private static final ItemStack ARMOR_SCROLL = new StrengthScrollArmor().getItem();

	public StrengthMarchant(Player p) {
		super(p);
	}

	@Override
	protected void onSetItem(InventoryView inv) {
		ItemStack item1 = inv.getItem(0);

		Strengthenable customItem = ItemManager.getCustomItem(Strengthenable.class, item1);
		//強化出来ないアイテムなら何もしない
		if (customItem == null) {
			sendRecipeList(getInitRecipes());
			return;
		}

		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
		if (theLowPlayer == null) {
			//ロードされていない時は何もしない
			return;
		}

		//現在のレベル
		int nowLevel = StrengthOperator.getLevel(item1);
		ItemStack item2 = inv.getItem(1);

		//もし強化できるならレシピをセットする
		if (canStrength(customItem, theLowPlayer, nowLevel + 1, item2, item1)) {
			ItemStack result = item1.clone();
			//強化後のアイテムを取得する
			StrengthOperator.updateLore(result, nowLevel + 1);
			TheLowMerchantRecipe newRecipe = new TheLowMerchantRecipe(item1, customItem.getStrengthTemplate().getStrengthMaterials(nowLevel + 1), result);
			//レシピをセットする
			sendRecipeList(Arrays.asList(newRecipe));
			nowRecipeType = NowRecipeType.ITEM;
		} else {
			//初期レシピをセットする
			sendRecipeList(getInitRecipes());
			nowRecipeType = NowRecipeType.INIT;
		}
	}

	NowRecipeType nowRecipeType = NowRecipeType.INIT;

	/**
	 * 強化できるならTRUEを返す
	 * @param customItem 強化するアイテム
	 * @param theLowPlayer 強化しているPlayer
	 * @param nextLevel 強化後のレベル
	 * @param item2 強化素材アイテムとしてセットされているアイテム
	 * @return
	 */
	public boolean canStrength(Strengthenable customItem, TheLowPlayer theLowPlayer, int nextLevel, ItemStack item2, ItemStack item1) {
		//最大強化回数を取得
		if (nextLevel > customItem.getMaxStrengthCount()) {
			return false;
		}

		StrengthTemplate strengthTemplate = customItem.getStrengthTemplate();
		//お金を調べる
		if (theLowPlayer.getGalions() < strengthTemplate.getStrengthGalions(nextLevel)) {
			return false;
		}

		//強化に必要なアイテムを取得
		ItemStack strengthMaterial = strengthTemplate.getStrengthMaterials(nextLevel);

		//素材アイテムが必要なく、また素材アイテムがセットされていないなら問題なし
		if (ItemStackUtil.isEmpty(strengthMaterial) && ItemStackUtil.isEmpty(item2)) {
			return true;
		}

		//同じアイテム　かつ　個数が必要以上ある場合
		if (strengthMaterial.isSimilar(item2) && strengthMaterial.getAmount() <= item2.getAmount()) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "strength";
	}

	@Override
	public void onShowResult(TheLowMerchantRecipe recipe) {
		//ランダムにする
	}

	@Override
	public List<TheLowMerchantRecipe> getInitRecipes() {
		ArrayList<TheLowMerchantRecipe> recipes = new ArrayList<TheLowMerchantRecipe>();
		recipes.add(new TheLowMerchantRecipe(ItemStackUtil.getItem("強化したい武器", Material.IRON_SWORD, "強化する武器を置いてください"),
				WEAPON_SCROLL, ItemStackUtil.getItem("強化された武器", Material.DIAMOND_SWORD)));
		recipes.add(new TheLowMerchantRecipe(ItemStackUtil.getItem("強化したい防具", Material.IRON_CHESTPLATE, "強化する防具を置いてください"),
				ARMOR_SCROLL, ItemStackUtil.getItem("強化された防具", Material.DIAMOND_CHESTPLATE)));
		return recipes;
	}

	MerchantRecipeListImplemention nowRecipeList = null;

	@Override
	public MerchantRecipeListImplemention getNowRecipeList() {
		if (nowRecipeList == null) {
			nowRecipeList = new MerchantRecipeListImplemention(this);
			for (TheLowMerchantRecipe recipe : getInitRecipes()) {
				nowRecipeList.addTheLowRecipe(recipe);
			}
		}
		return nowRecipeList;
	}

	@Override
	protected void sendRecipeList(List<TheLowMerchantRecipe> recipeList) {
		super.sendRecipeList(recipeList);

		//一旦全て削除し、入れ直す
		if (nowRecipeList != null) {
			nowRecipeList.clear();
		} else {
			nowRecipeList = new MerchantRecipeListImplemention(this);
		}
		for (TheLowMerchantRecipe theLowMerchantRecipe : recipeList) {
			nowRecipeList.addTheLowRecipe(theLowMerchantRecipe);
		}
	}
}

enum NowRecipeType {
	INIT, ITEM
}
