package lbn.dungeon.contents.item.armor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lbn.common.event.ChangeStrengthLevelItemEvent;
import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.dungeon.contents.strength_template.BeneArmorStrengthTemplate;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemInterface;
import lbn.item.armoritem.ArmorMaterial;
import lbn.item.armoritem.BeneEffectManager;
import lbn.item.armoritem.BeneEffectType;
import lbn.item.strength.StrengthOperator;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class BeneCommonAromor extends CommonArmor implements Beneable{
	public BeneCommonAromor(Material m) {
		super(m);
	}

	public static List<ItemInterface> getAllArmor() {
		return Arrays.asList(
				new BeneCommonAromor(Material.LEATHER_HELMET),
				new BeneCommonAromor(Material.LEATHER_CHESTPLATE),
				new BeneCommonAromor(Material.LEATHER_LEGGINGS),
				new BeneCommonAromor(Material.LEATHER_BOOTS),
				new BeneCommonAromor(Material.GOLD_HELMET),
				new BeneCommonAromor(Material.GOLD_CHESTPLATE),
				new BeneCommonAromor(Material.GOLD_LEGGINGS),
				new BeneCommonAromor(Material.GOLD_BOOTS),
				new BeneCommonAromor(Material.CHAINMAIL_HELMET),
				new BeneCommonAromor(Material.CHAINMAIL_CHESTPLATE),
				new BeneCommonAromor(Material.CHAINMAIL_LEGGINGS),
				new BeneCommonAromor(Material.CHAINMAIL_BOOTS),
				new BeneCommonAromor(Material.IRON_HELMET),
				new BeneCommonAromor(Material.IRON_CHESTPLATE),
				new BeneCommonAromor(Material.IRON_LEGGINGS),
				new BeneCommonAromor(Material.IRON_BOOTS),
				new BeneCommonAromor(Material.DIAMOND_HELMET),
				new BeneCommonAromor(Material.DIAMOND_CHESTPLATE),
				new BeneCommonAromor(Material.DIAMOND_LEGGINGS),
				new BeneCommonAromor(Material.DIAMOND_BOOTS)
		);
	}

	@Override
	public String getItemName() {
		return "Bene " + super.getItemName();
	}

	@Override
	public String getId() {
		return "bene_" + super.getId();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return (int) (super.getBuyPrice(item) * 1.2);
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return new BeneArmorStrengthTemplate(getArmorMaterial());
	}

	@Override
	public int getMaxStrengthCount() {
		return 5;
	}

	Random rnd = new Random();

	@Override
	public String[] getStrengthDetail(int level) {
		if (level == 0) {
			return new String[]{BeneEffectType.BENE_EFFECT_UNKNOW.getLine(0)};
		} else {
			return null;
		}
	}

	@Override
	protected List<String> getBaseDefanceDetail() {
		int base = 2;
		int boss = 4;
		switch (getArmorMaterial()) {
		case DIAMOND:
			base++;
			boss++;
		case IRON:
			base++;
			boss++;
		case CHAINMAIL:
			base++;
			boss++;
		case GOLD:
			base++;
			boss++;
		case LEATHER:
			base++;
			boss++;
		default:
			break;
		}

		return Arrays.asList(Message.getMessage("防御力: ● × {0}", base), Message.getMessage("ボスに対しての防御力: ● × {0}", boss));
	}

	@Override
	public double getStrengthDamageCuteParcent(Player me, EntityDamageEvent e,
			ItemStack armor, boolean isArmorCutDamage, boolean isBoss,
			LivingEntity mob) {
		if (!isArmorCutDamage) {
			return 0;
		}

		ArmorMaterial material = getArmorMaterial();
		if (material == null) {
			return 0;
		}

		//最大強化分だけカットする
		if (!isBoss) {
			return material.getStrengthTotalDamageCut();
		} else {
			return material.getStrengthBossTotalDamageCut();
		}
	}

	@Override
	protected String[] getDetail() {
		return new String[]{"Bene装備", "ランダムで特殊効果が付与されます"};
	}

	@Override
	public boolean isDispList() {
		return true;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		item.addUnsafeEnchantment(Enchantment.DURABILITY, getDurabilityLevel(super.getMaxStrengthCount()));
		return item;
	}

	@Override
	public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {

	}

	@Override
	public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {
		ArrayList<BeneEffectType> beneEffectList = BeneEffectManager.getBeneEffectList(ItemStackUtil.getLore(event.getItem()));

		//unknownが付いている時はランダムで付与する
		if (beneEffectList.contains(BeneEffectType.BENE_EFFECT_UNKNOW)) {
			List<String> newLore = ItemStackUtil.getLore(event.getItem());
			//全ての強化情報を削除
			StrengthOperator.removedStrengthLore(newLore);
			int beneSize = 1;
			int nextInt = rnd.nextInt(100);
			if (nextInt < 80) {
				beneSize = 1;
			} else if (nextInt < 95){
				beneSize = 2;
			} else {
				beneSize = 3;
			}
			ArrayList<String> newEffectLore = BeneEffectManager.getNewEffectLore(beneSize);
			//強化情報(Bene)を付与
			StrengthOperator.addStrengthLore(newEffectLore, newLore);

			//Loreを更新
			ItemStackUtil.setLore(event.getItem(), newLore);
		}
	}

	@Override
	public void onChangeStrengthLevelItemEvent(
			ChangeStrengthLevelItemEvent event) {
		List<String> newLore = ItemStackUtil.getLore(event.getAfter());
		//beforeのbeneを取得
		ArrayList<BeneEffectType> beforeBeneList = BeneEffectManager.getBeneEffectList(ItemStackUtil.getLore(event.getBefore()));

		//bene情報が付いている時はつける
		if (!beforeBeneList.isEmpty()) {
			//全ての強化情報を削除
			StrengthOperator.removedStrengthLore(newLore);
			//強化情報(Bene)を付与
			StrengthOperator.addStrengthLore(BeneEffectManager.getBeneLore(beforeBeneList, event.getNextLevel()), newLore);
		}


		ItemStackUtil.setLore(event.getAfter(), newLore);
	}

	@Override
	public void extraDamageCut(Player me, EntityDamageEvent e,
			ItemStack armor, boolean isArmorCutDamage, boolean isBoss,
			LivingEntity mob) {
		ArrayList<BeneEffectType> beneEffectList = BeneEffectManager.getBeneEffectList(ItemStackUtil.getLore(armor));
		for (BeneEffectType beneEffectType : beneEffectList) {
			beneEffectType.execute(me, e, armor, isArmorCutDamage, isBoss, mob, StrengthOperator.getLevel(armor));
		}
	}
}
