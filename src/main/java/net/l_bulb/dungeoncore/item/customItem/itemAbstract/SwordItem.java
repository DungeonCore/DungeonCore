package net.l_bulb.dungeoncore.item.customItem.itemAbstract;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent_old;
import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.SpreadSheetAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.SpreadSheetWeaponData;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillExecutor;
import net.l_bulb.dungeoncore.item.itemInterface.MeleeAttackItemable;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class SwordItem extends SpreadSheetAttackItem implements MeleeAttackItemable {
  public SwordItem(SpreadSheetWeaponData data) {
    super(data);
  }

  public int rank() {
    return 0;
  }

  @Override
  public void excuteOnRightClick(PlayerInteractEvent e) {
    super.excuteOnRightClick(e);
    // レベルなどを確認する
    Player player = e.getPlayer();
    if (!isAvilable(player)) {
      sendNotAvailableMessage(player);
      e.setCancelled(true);
      return;
    }
    if (!e.getPlayer().isSneaking()) {
      // スキルを発動
      WeaponSkillExecutor.executeWeaponSkillOnClick(e, this);
    }
  }

  @Override
  public double getMaterialDamage() {
    return ItemStackUtil.getVanillaDamage(getMaterial());
  }

  @Override
  public void excuteOnMeleeAttack(ItemStack item, LivingEntity owner, LivingEntity target, EntityDamageByEntityEvent e) {
    // プレイヤーでないなら関係ない
    if (owner.getType() != EntityType.PLAYER) { return; }

    owner.sendMessage(getAttackItemDamage(0) + "");

    Player player = (Player) owner;
    if (!isAvilable(player)) {
      sendNotAvailableMessage(player);
      e.setCancelled(true);
      return;
    }

    if (LivingEntityUtil.isEnemy(target)) {
      // eventを呼ぶ
      // 相殺されるはず(e.getDamage() - getNormalDamage() )
      PlayerCombatEntityEvent_old playerCombatEntityEvent = new PlayerCombatEntityEvent_old(player, target, item,
          e.getDamage() - getMaterialDamage() + getAttackItemDamage(StrengthOperator.getLevel(item)));
      playerCombatEntityEvent.callEvent();
      // ダメージの計算を行う
      e.setDamage(playerCombatEntityEvent.getDamage());
    } else {
      e.setDamage(e.getDamage() + getAttackItemDamage(StrengthOperator.getLevel(item)) - getMaterialDamage());
    }
  }

  @Override
  public ItemType getAttackType() {
    return ItemType.SWORD;
  }

  @Override
  public void excuteOnLeftClick(PlayerInteractEvent e) {}

}
