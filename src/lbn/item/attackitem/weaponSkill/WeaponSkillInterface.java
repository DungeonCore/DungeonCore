package lbn.item.attackitem.weaponSkill;

import lbn.common.other.ItemStackData;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.player.ItemType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface WeaponSkillInterface {
	/**
	 * スキルレベルを取得
	 * @return
	 */
	int getSkillLevel();

	/**
	 * スキル名を取得
	 * @return
	 */
	String getName();

	/**
	 * スキルIDを取得
	 * @return
	 */
	String getId();

	/**
	 * スキルの説明を取得
	 * @return
	 */
	String[] getDetail();

	/**
	 * スキル選択後にクリックした時の処理
	 * @param p 実行者のPlayer
	 * @param item クリックしたアイテム
	 * @param customItem クリックしたアイテムのカスタムアイテム
	 */
	void onClick(Player p, ItemStack item, AbstractAttackItem customItem);

	/**
	 * スキルのクールタイムを取得
	 * @return
	 */
	int getCooltime();

	/**
	 * 消費MPを取得
	 * @return
	 */
	int getNeedMagicPoint();

	/**
	 * ItemStackDataを取得
	 * @return
	 */
	ItemStackData getItemStackData();

	/**
	 * もし指定したタイプの武器で使えるならTRUE
	 * @param type
	 * @return
	 */
	boolean canUse(ItemType type);
}
