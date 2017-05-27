package net.l_bulb.dungeoncore.item.customItem.itemAbstract;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.SpreadSheetAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.SpreadSheetWeaponData;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillExecutor;
import net.l_bulb.dungeoncore.item.itemInterface.MeleeAttackItemable;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

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

    Player player = (Player) owner;
    if (!isAvilable(player)) {
      sendNotAvailableMessage(player);
      e.setCancelled(true);
      return;
    }
  }

  @Override
  public ItemType getAttackType() {
    return ItemType.SWORD;
  }

  @Override
  public void excuteOnLeftClick(PlayerInteractEvent e) {}

}
