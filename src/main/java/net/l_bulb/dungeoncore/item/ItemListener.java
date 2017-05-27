package net.l_bulb.dungeoncore.item;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import net.l_bulb.dungeoncore.NbtTagConst;
import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerBreakMagicOreEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerKillEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerSetStrengthItemResultEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerStrengthFinishEvent;
import net.l_bulb.dungeoncore.common.projectile.ProjectileManager;
import net.l_bulb.dungeoncore.item.customItem.armoritem.ArmorBase;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillExecutor;
import net.l_bulb.dungeoncore.item.itemInterface.BowItemable;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.itemInterface.EntityKillable;
import net.l_bulb.dungeoncore.item.itemInterface.EquipItemable;
import net.l_bulb.dungeoncore.item.itemInterface.FoodItemable;
import net.l_bulb.dungeoncore.item.itemInterface.LeftClickItemable;
import net.l_bulb.dungeoncore.item.itemInterface.MagicPickaxeable;
import net.l_bulb.dungeoncore.item.itemInterface.MeleeAttackItemable;
import net.l_bulb.dungeoncore.item.itemInterface.RightClickItemable;
import net.l_bulb.dungeoncore.item.itemInterface.StrengthChangeItemable;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.item.slot.magicstone.CombatSlot;
import net.l_bulb.dungeoncore.item.slot.magicstone.KillSlot;
import net.l_bulb.dungeoncore.mob.LastDamageManager;
import net.l_bulb.dungeoncore.mob.LastDamageMethodType;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class ItemListener implements Listener {

  @EventHandler
  public void onClick(PlayerInteractEvent e) {
    Action action = e.getAction();

    Player player = e.getPlayer();
    ItemStack item = player.getItemInHand();

    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
      RightClickItemable clickItem = ItemManager.getCustomItem(RightClickItemable.class, item);
      if (clickItem != null) {
        clickItem.excuteOnRightClick(e);
      }
    } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
      LeftClickItemable clickItem = ItemManager.getCustomItem(LeftClickItemable.class, item);
      if (clickItem != null) {
        clickItem.excuteOnLeftClick(e);
      }
    }
  }

  @EventHandler
  public void onShootBow(EntityShootBowEvent event) {
    ItemStack bow = event.getBow();
    BowItemable bowItem = ItemManager.getCustomItem(BowItemable.class, bow);
    if (bowItem != null) {
      bowItem.excuteOnShootBow(event);
    }
  }

  @EventHandler
  public void onLunchProjectile(ProjectileLaunchEvent e) {
    Projectile entity = e.getEntity();

    // 打った瞬間、手に持ったものからProjectileIDを取得
    ProjectileSource shooter = entity.getShooter();
    if (!(shooter instanceof LivingEntity)) { return; }
    ItemStack itemInHand = ((LivingEntity) shooter).getEquipment().getItemInHand();
    // custom itemでないなら何もしない
    BowItemable item = ItemManager.getCustomItem(BowItemable.class, itemInHand);
    if (item == null) { return; }
    ProjectileManager.onLaunchProjectile(entity, item, itemInHand);
  }

  /**
   * CustomItemの剣でダメージを与えた時にEventを発火させる
   *
   * @param e
   */
  @EventHandler(priority = EventPriority.LOW)
  public void onDamage(EntityDamageByEntityEvent e) {
    // ダメージを受けたのが生き物でないなら何もしない
    if (!e.getEntityType().isAlive()) { return; }

    // ダメージを与えたのがPlayerじゃないならTRUE
    if (e.getDamager().getType() != EntityType.PLAYER) { return; }
    Player damager = (Player) e.getDamager();
    // 手に持っているアイテムを取得
    ItemStack itemInHand = damager.getEquipment().getItemInHand();

    if (itemInHand != null) {
      // custom itemを取得
      ItemInterface customItem = ItemManager.getCustomItem(itemInHand);
      if (customItem == null) { return; }

      // 攻撃したアイテムが剣ならイベントを発動させる
      if (customItem.getAttackType() == ItemType.SWORD) {
        // イベントを発動させる
        CombatEntityEvent callEvent = new CombatEntityEvent(damager, e.getDamage(DamageModifier.BASE), (AbstractAttackItem) customItem,
            itemInHand, true, (LivingEntity) e.getEntity()).callEvent();
        e.setDamage(DamageModifier.BASE, callEvent.getDamage());
      }

      // MeleeAttackItemableならEventを発火させる
      MeleeAttackItemable attackItem = ItemManager.getCustomItem(MeleeAttackItemable.class, itemInHand);
      if (attackItem != null) {
        attackItem.excuteOnMeleeAttack(itemInHand, (LivingEntity) e.getDamager(), (LivingEntity) e.getEntity(), e);
      }
    }
  }

  /**
   * CustomItemでダメージを与えた時のEvent
   *
   * @param e
   */
  @EventHandler(priority = EventPriority.LOWEST)
  public void onAttackDamage(CombatEntityEvent e) {
    // last damageを登録する
    if (e.getAttacker().getType() == EntityType.PLAYER) {
      LastDamageManager.addData((Player) e.getAttacker(), e.geLastDamageMethodType(), e.getEnemy());
    }
  }

  /**
   * 防具の処理
   *
   * @param e
   */
  @EventHandler
  public void onDamageAll(EntityDamageEvent e) {
    EntityType entityType = e.getEntityType();
    if (entityType != EntityType.PLAYER) { return; }
    ArmorBase.onArmor(e);
  }

  @EventHandler
  public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {
    ItemStack item = event.getItem();
    StrengthChangeItemable customItem = ItemManager.getCustomItem(StrengthChangeItemable.class, item);
    if (customItem != null) {
      customItem.onSetStrengthItemResult(event);
    }
  }

  @EventHandler
  public void onPlayerStrengthItemEvent(PlayerStrengthFinishEvent event) {
    ItemStack item = event.getItem();
    StrengthChangeItemable customItem = ItemManager.getCustomItem(StrengthChangeItemable.class, item);
    if (customItem != null) {
      customItem.onPlayerStrengthFinishEvent(event);
    }
  }

  @EventHandler
  public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
    ItemStack item = e.getItem();
    FoodItemable customItem = ItemManager.getCustomItem(FoodItemable.class, item);
    if (customItem != null) {
      customItem.onPlayerItemConsumeEvent(e);
    }
  }

  @EventHandler
  public void onDeath(EntityDeathEvent e) {
    // 死んだのが敵でなければ何もしない
    if (!LivingEntityUtil.isEnemy(e.getEntity())) { return; }

    LivingEntity entity = e.getEntity();
    EntityDamageEvent lastDamageCause = entity.getLastDamageCause();

    // 最後に攻撃した攻撃者を取得
    LastDamageMethodType lastDamageMethod = LastDamageManager.getLastDamageAttackType(entity);
    Player player = LastDamageManager.getLastDamagePlayer(entity);
    // 攻撃者が記録されていないなら何もしない
    if (lastDamageMethod == null || player == null) { return; }

    // 倒すときに使ったアイテム
    ItemStack item = null;

    // 最後に攻撃をしたEntityを取得
    Entity damager = ((EntityDamageByEntityEvent) lastDamageCause).getDamager();

    if (lastDamageMethod == LastDamageMethodType.SWORD || lastDamageMethod == LastDamageMethodType.BOW) {
      // 攻撃をしたのが生き物なら
      if (damager.getType().isAlive()) {
        // 手に持っているアイテムで倒したと判断
        item = player.getItemInHand();
      } else {
        // 攻撃したのがProjectileならその情報からItemを取得
        item = ProjectileManager.getItemStack(damager);
      }
    }

    // 攻撃につかったアイテムが不明な時は無視する
    if (item == null) { return; }

    ItemInterface customItem = ItemManager.getCustomItem(item);
    // カスタムアイテムでないなら何もしない
    if (customItem == null) { return; }

    // もしLastDamageManagerの攻撃手段と、使ったアイテムの攻撃手段が異なるならエラーにする
    if (LastDamageMethodType.fromAttackType(customItem.getAttackType()) != lastDamageMethod) {
      new RuntimeException("Player:" + player.getName() + "がモンスターを倒した際にエラーが発生しました。(manager:" + lastDamageMethod + "と item:"
          + LastDamageMethodType.fromAttackType(customItem.getAttackType())
          + "が一致しません").printStackTrace();
      ;
    }

    PlayerKillEntityEvent playerKillEntityEvent = new PlayerKillEntityEvent(player, entity, item);
    playerKillEntityEvent.callEvent();
  }

  @EventHandler
  public void onCombat(CombatEntityEvent e) {
    // 攻撃したのがPlayerでないなら何もしない
    if (e.getAttacker().getType() != EntityType.PLAYER) { return; }

    CombatItemable combatItem = ItemManager.getCustomItem(CombatItemable.class, e.getItemStack());
    // 戦闘用アイテムでないなら何もしない
    if (combatItem == null) { return; }

    CustomWeaponItemStack2 instance = combatItem.getCombatAttackItemStack(e.getItemStack());
    List<SlotInterface> useSlot = instance.getUseSlot();

    for (SlotInterface slot : useSlot) {
      if (slot instanceof CombatSlot) {
        ((CombatSlot) slot).onCombat(e, (Player) e.getAttacker());
      }
    }
    // 武器スキルを実行
    WeaponSkillExecutor.executeWeaponSkillOnCombat(e);
  }

  @EventHandler
  public void onKill(PlayerKillEntityEvent e) {
    CombatItemable combatItem = ItemManager.getCustomItem(CombatItemable.class, e.getItem());
    // 戦闘用アイテムでないなら何もしない
    if (combatItem == null) { return; }

    CustomWeaponItemStack2 combatAttackItemStack = combatItem.getCombatAttackItemStack(e.getItem());

    List<SlotInterface> useSlot = combatAttackItemStack.getUseSlot();
    for (SlotInterface slot : useSlot) {
      if (slot instanceof KillSlot) {
        ((KillSlot) slot).onKill(e);
      }
    }

    EntityKillable customItem = ItemManager.getCustomItem(EntityKillable.class, e.getItem());
    if (customItem != null) {
      customItem.onKillEvent(e);
    }
  }

  @EventHandler
  public void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {
    ItemStack item = e.getItem();
    // もし指定したアイテムDamageItemableでないなら無視
    EquipItemable customItem = ItemManager.getCustomItem(EquipItemable.class, item);
    if (customItem == null) { return; }

    // 現在の耐久を取得
    short customDurability = ItemStackUtil.getNBTTagShort(item, NbtTagConst.THELOW_DURABILITY);
    // 耐久値を更新
    customDurability += e.getDamage();

    // 設定した固有の最大耐久
    int customMaxDurability = customItem.getMaxDurability(e.getItem());
    // 実際の素材の最大耐久
    short itemMaxDurability = item.getType().getMaxDurability();

    // 耐久をセットする
    ItemStackUtil.setNBTTag(item, NbtTagConst.THELOW_DURABILITY, customDurability);

    short ItemDurability = (short) (itemMaxDurability * customDurability / customMaxDurability);
    // 耐久をセットする
    item.setDurability(ItemDurability);

    // アイテムの耐久がまだ残っているならここで耐久をセットしたので何もしない
    if (ItemDurability < itemMaxDurability) {
      // 耐久をここでセットしたのでEvent後はセットしない
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onCombatEntity(CombatEntityEvent e) {
    // もし指定したアイテムDamageItemableでないなら無視
    ItemInterface itemInterface = e.getCustomItem();

    // 戦闘用のアイテムか確認
    if (ItemManager.isImplemental(CombatItemable.class, itemInterface)) {
      ((CombatItemable) itemInterface).onCombatEntity(e);
    }
  }

  @EventHandler
  public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
    // もし指定したアイテムDamageItemableでないなら無視
    CombatItemable customItem = ItemManager.getCustomItem(CombatItemable.class, e.getItemDrop().getItemStack());
    if (customItem == null) { return; }
    customItem.onPlayerDropItemEvent(e);
  }

  @EventHandler
  public void onPlayerBreakMagicOreEvent(PlayerBreakMagicOreEvent e) {
    Player player = e.getPlayer();
    // 所持しているアイテムを取得
    ItemStack itemInHand = player.getItemInHand();

    // 登録されているアイテム以外なら鉱石を掘らせない
    MagicPickaxeable customItem = ItemManager.getCustomItem(MagicPickaxeable.class, itemInHand);
    if (customItem == null) {
      e.setCancelled(true);
    } else {
      customItem.onPlayerBreakMagicOreEvent(e);
    }
  }
}
