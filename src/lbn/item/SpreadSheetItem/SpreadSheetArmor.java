package lbn.item.SpreadSheetItem;

import java.util.Map.Entry;

import lbn.common.event.ChangeStrengthLevelItemEvent;
import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.item.armoritem.AbstractArmorItem;
import lbn.item.armoritem.SpreadSheetArmorData;
import lbn.item.craft.TheLowCraftRecipeInterface;
import lbn.item.strength.StrengthOperator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class SpreadSheetArmor extends AbstractArmorItem{
	SpreadSheetArmorData data;
	public SpreadSheetArmor(SpreadSheetArmorData data) {
		this.data = data;
	}

	@Override
	public double getArmorPointForNormalMob() {
		return data.getArmorPointNormalMob();
	}

	@Override
	public double getArmorPointForBossMob() {
		return data.getArmorPointBoss();
	}

	@Override
	public double getModifiedDamage(double damage, Player me, EntityDamageEvent e, boolean isBoss, LivingEntity mob) {
		return damage;
	}

	@Override
	public short getMaxDurability(ItemStack e) {
		return (short) (data.getMaxDurability() + getAddDurability(StrengthOperator.getLevel(e)));
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
		return data.getPrice();
	}

	@Override
	public int getAvailableLevel() {
		return data.getAvailableLevel();
	}


	@Override
	public String[] getStrengthDetail(int level) {
		return new String[]{"最大耐久値 ： " + ChatColor.GOLD + (data.getMaxDurability() + getAddDurability(level))};
	}

	@Override
	public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {
		if (!event.isSuccess()) {
			int level = event.getLevel();
			//+6までの強化の時は失敗しても+0にしないで元にもどす
			if (level <= 6) {
				ItemStack item = event.getItem();
				StrengthOperator.updateLore(item, Math.max(0, level - 1));
			}
		}
	}

	TheLowCraftRecipeInterface recipe;

	@Override
	public TheLowCraftRecipeInterface getCraftRecipe() {
		if (recipe == null) {
			TheLowCraftRecipeInterface recipe = TheLowCraftRecipeInterface.getInstance(data.getMainCraftMaterial());
			//素材を追加する
			for (Entry<String, Integer> entry : data.getCraftItem().entrySet()) {
				recipe.addMaterial(entry.getKey(), entry.getValue());
			}
			this.recipe = recipe;
		}
		return recipe;
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
	public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {

	}

	@Override
	public void onChangeStrengthLevelItemEvent(ChangeStrengthLevelItemEvent event) {

	}

	@Override
	protected ItemStack getItemStackBase() {
		return data.getItemStack().clone();
	}

}
