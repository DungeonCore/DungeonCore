package net.l_bulb.dungeoncore.item.customItem.attackitem.old;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillExecutor;
import net.l_bulb.dungeoncore.item.itemInterface.BowItemable;
import net.l_bulb.dungeoncore.item.itemInterface.LeftClickItemable;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BowItemOld extends AbstractAttackItem_Old implements ItemInterface, BowItemable, LeftClickItemable {
  @Override
  protected Material getMaterial() {
    return Material.BOW;
  }

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e,
      ItemStack item, LivingEntity owner, LivingEntity target) {}

  abstract protected void excuteOnShootBow2(EntityShootBowEvent e);

  @Override
  public void excuteOnShootBow(EntityShootBowEvent e) {
    LivingEntity entity = e.getEntity();
    if (entity.getType() == EntityType.PLAYER) {
      if (!isAvilable((Player) entity)) {
        sendNotAvailableMessage((Player) entity);
        e.setCancelled(true);
        return;
      }
    }
    excuteOnShootBow2(e);
  }

  @Override
  public void excuteOnLeftClick(PlayerInteractEvent e) {
    // レベルなどを確認する
    Player player = e.getPlayer();
    if (!isAvilable(player)) {
      sendNotAvailableMessage(player);
      e.setCancelled(true);
      return;
    }
    excuteOnLeftClick2(e);

    if (!player.isSneaking()) {
      // スキルを発動
      WeaponSkillExecutor.executeWeaponSkillOnClick(e, this);
    }
  }

  abstract protected void excuteOnLeftClick2(PlayerInteractEvent e);

  @Override
  public ItemType getAttackType() {
    return ItemType.BOW;
  }

  @Override
  abstract public int getAvailableLevel();

  @Override
  public double getMaterialDamage() {
    return ItemStackUtil.getVanillaDamage(getMaterial());
  }
}
