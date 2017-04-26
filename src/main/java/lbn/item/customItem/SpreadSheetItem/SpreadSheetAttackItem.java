package lbn.item.customItem.SpreadSheetItem;

import java.util.Map.Entry;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.item.customItem.attackitem.AttackDamageValue;
import lbn.item.customItem.attackitem.SpreadSheetWeaponData;
import lbn.item.itemInterface.CraftItemable;
import lbn.item.itemInterface.StrengthChangeItemable;
import lbn.item.system.craft.TheLowCraftRecipeInterface;
import lbn.item.system.lore.ItemLoreToken;
import lbn.item.system.lore.LoreLine;
import lbn.item.system.strength.StrengthOperator;
import lbn.player.ItemType;
import lbn.util.JavaUtil;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public abstract class SpreadSheetAttackItem extends AbstractAttackItem implements StrengthChangeItemable, CraftItemable{
	protected SpreadSheetWeaponData data;
	public SpreadSheetAttackItem(SpreadSheetWeaponData data) {
		this.data = data;
	}

	/**
	 *	同じレベルのMobを倒すのにかかる攻撃回数
	 * @return
	 */
	public double getCombatLoad() {
		return AttackDamageValue.getCombatLoad(getWeaponLevel(), getAttackType());
	}

	@Override
	public String getItemName() {
		return data.getName();
	}

	@Override
	public String getId() {
		return data.getId();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return Math.max(getWeaponLevel() * 100, 10);
	}

	@Override
	public void onCombatEntity(PlayerCombatEntityEvent e) {

	}

	@Override
	public short getMaxDurability(ItemStack e) {
		if (data.getMaxDurability() != -1) {
			return data.getMaxDurability();
		} else {
			return getMaterial().getMaxDurability();
		}
	}

	@Override
	public ItemLoreToken getStandardLoreToken() {
		ItemLoreToken loreToken = super.getStandardLoreToken();
		//通常ダメージ
		loreToken.addLore(LoreLine.getLoreLine("ダメージ", JavaUtil.round(getAttackItemDamage(0) - getMaterialDamage(), 2)));
		return loreToken;
	}

	@Override
	public double getAttackItemDamage(int strengthLevel) {
		double combatLoad = getCombatLoad();
		if (JavaUtil.isRandomTrue((int) getCriticalHitRate(strengthLevel))) {
			combatLoad -= getMinusCombatLoadForCritical();
		}
		double attackDamageValue = AttackDamageValue.getAttackDamageValue(combatLoad, getAvailableLevel());
		return attackDamageValue * data.getDamageParcent();
	}

	/**
	 * クリティカル時、減算される戦闘負荷量
	 * @return
	 */
	public double getMinusCombatLoadForCritical() {
		return 0.3;
	}

	/**
	 * 強化レベルによって変化するLoreを取得
	 */
	public void setStrengthDetail(int level, ItemLoreToken loreToken) {
		if (level != 0) {
			loreToken.addLore(LoreLine.getLoreLine("クリティカル率", getCriticalHitRate(level) + "%"));
		}

		double normalDamage = AttackDamageValue.getAttackDamageValue(getCombatLoad(), getAvailableLevel());
		double criticalDamage = AttackDamageValue.getAttackDamageValue(getCombatLoad() - getMinusCombatLoadForCritical() , getAvailableLevel());

		if (criticalDamage > normalDamage) {
			loreToken.addLore(LoreLine.getLoreLine("クリティカル追加ダメージ", JavaUtil.round(criticalDamage - normalDamage, 2)));
		}
	}

	/**
	 * クリティカル確率
	 * @param level
	 * @return
	 */
	public double getCriticalHitRate(int level) {
		return 1.5 * level;
	}

	@Override
	public int getAvailableLevel() {
		return data.getAvailableLevel();
	}

	@Override
	protected int getSkillLevel() {
		return data.getSkillLevel();
	}

	@Override
	public int getWeaponLevel() {
		return data.getRank();
	}

	@Override
	protected Material getMaterial() {
		return data.getItemStack().getType();
	}

	@Override
	public String[] getDetail() {
		return data.getDetail();
	}

	@Override
	public int getMaxSlotCount() {
		return data.getMaxSlot();
	}

	@Override
	public int getDefaultSlotCount() {
		return data.getDefaultSlot();
	}

	@Override
	abstract public ItemType getAttackType();

	@Override
	public int getMaxStrengthCount() {
		return 10;
	}

	@Override
	protected ItemStack getItemStackBase() {
		return data.getItemStack().clone();
	}

	@Override
	public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {
		if (!event.isSuccess()) {
			int level = event.getNextLevel();
			//+6までの強化の時は失敗しても+0にしないで元にもどす
			if (level <= 6) {
				ItemStack item = event.getItem();
				StrengthOperator.updateLore(item, Math.max(0, level - 1));
				event.setItem(item);
			}
		}
	}

	@Override
	public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {
	}

	TheLowCraftRecipeInterface recipe;

	@Override
	public TheLowCraftRecipeInterface getCraftRecipe() {
		if (recipe == null) {
			TheLowCraftRecipeInterface recipe = TheLowCraftRecipeInterface.createNewInstance(data.getMainCraftMaterial());
			//素材を追加する
			for (Entry<String, Integer> entry : data.getCraftItem().entrySet()) {
				recipe.addMaterial(entry.getKey(), entry.getValue());
			}
			this.recipe = recipe;
		}
		return recipe;
	}


}
