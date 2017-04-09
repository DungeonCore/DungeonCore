package lbn.item.customItem.SpreadSheetItem;

import java.util.Map.Entry;

import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.item.customItem.armoritem.AbstractArmorItem;
import lbn.item.customItem.armoritem.SpreadSheetArmorData;
import lbn.item.system.craft.TheLowCraftRecipeInterface;
import lbn.item.system.lore.ItemLoreToken;
import lbn.item.system.lore.LoreLine;
import lbn.item.system.strength.StrengthOperator;

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
	public double getOtherArmorPoint(double damage, Player me, EntityDamageEvent e, boolean isBoss, LivingEntity mob) {
		return 0;
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
	public void setStrengthDetail(int level, ItemLoreToken loreToken) {
		loreToken.addLore(LoreLine.getLoreLine("最大耐久力", (data.getMaxDurability() + getAddDurability(level))));
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
	protected ItemStack getItemStackBase() {
		return data.getItemStack().clone();
	}

}
