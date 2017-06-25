package net.l_bulb.dungeoncore.player.ability;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.l_bulb.dungeoncore.api.PlayerStatusType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.player.ItemType;

public class PlayerAbilityListener implements Listener {
  @EventHandler
  public void onDamage(CombatEntityEvent e) {
    // 攻撃したのはPlayerでないなら何もしない
    if (e.getAttacker().getType() != EntityType.PLAYER) { return; }

    ItemType attackType = e.geItemType();

    // 武器に対応するStatusのタイプを取得
    PlayerStatusType addType = null;
    PlayerStatusType multiplyType = null;
    switch (attackType) {
      case SWORD:
        addType = PlayerStatusType.ADD_SWORD_ATTACK;
        multiplyType = PlayerStatusType.MULTIPLY_SWORD_ATTACK;
        break;
      case BOW:
        addType = PlayerStatusType.ADD_BOW_ATTACK;
        multiplyType = PlayerStatusType.MULTIPLY_BOW_ATTACK;
        break;
      case MAGIC:
        addType = PlayerStatusType.ADD_MAGIC_ATTACK;
        multiplyType = PlayerStatusType.MULTIPLY_MAGIC_ATTACK;
        break;
      default:
        break;
    }

    // 対応するステータスが存在しない時は無視する
    if (addType == null) { return; }

    // Playerデータがロードされていない時は無視する
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer((Player) e.getAttacker());
    if (theLowPlayer == null) { return; }

    // 攻撃力の増加値を取得
    double addStatusData = theLowPlayer.getStatusData(addType);
    double multiplyStatusData = theLowPlayer.getStatusData(multiplyType);

    // 攻撃力を増加させる
    e.setDamage(e.getDamage() * (1 + multiplyStatusData) + addStatusData);
  }

  @EventHandler
  public void onDamaged(EntityDamageByEntityEvent e) {
    // Entityを取得
    Entity entity = e.getEntity();

    // もしEntityがPlayerでないなら無視する
    if (entity.getType() != EntityType.PLAYER) { return; }

    // Playerデータがロードされていない時は無視する
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer((Player) entity);
    if (theLowPlayer == null) { return; }

    // TODO 防御力をいじる
  }
}
