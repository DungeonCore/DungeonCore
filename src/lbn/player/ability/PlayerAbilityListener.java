package lbn.player.ability;

import lbn.api.PlayerStatusType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.player.ItemType;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerAbilityListener implements Listener{
	@EventHandler
	public void onDamage(PlayerCombatEntityEvent e) {
		ItemType attackType = e.getAttackItem().getItemType();

		//武器に対応するStatusのタイプを取得
		PlayerStatusType statusType = null;
		switch (attackType) {
		case SWORD:
			statusType = PlayerStatusType.SWORD_ATTACK;
			break;
		case BOW:
			statusType = PlayerStatusType.BOW_ATTACK;
			break;
		case MAGIC:
			statusType = PlayerStatusType.MAGIC_ATTACK;
			break;
		default:
			break;
		}

		//対応するステータスが存在しない時は無視する
		if (statusType == null) {
			return;
		}

		//Playerデータがロードされていない時は無視する
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(e);
		if (theLowPlayer == null) {
			return;
		}

		//攻撃力の増加値を取得
		double statusData = theLowPlayer.getStatusData(statusType);

		//攻撃力を増加させる
		e.setDamage(e.getDamage() + statusData);
	}

	@EventHandler
	public void onDamaged(EntityDamageByEntityEvent e) {
		//Entityを取得
		Entity entity = e.getEntity();

		//もしEntityがPlayerでないなら無視する
		if (entity.getType() != EntityType.PLAYER) {
			return;
		}

		//Playerデータがロードされていない時は無視する
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer((Player)entity);
		if (theLowPlayer == null) {
			return;
		}

		//TODO 防御力をいじる
	}
}
