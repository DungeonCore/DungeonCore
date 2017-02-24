package lbn.item.itemInterface;

import lbn.common.event.player.PlayerCombatEntityEvent;

import org.bukkit.event.player.PlayerDropItemEvent;

public interface CombatItemable extends RightClickItemable, EquipItemable{
	/**
	 * Entityを攻撃した時
	 * @param e
	 */
	void onCombatEntity(PlayerCombatEntityEvent e);

	/**
	 * アイテムを捨てる時
	 * @param e
	 */
	void onPlayerDropItemEvent(PlayerDropItemEvent e);
}
