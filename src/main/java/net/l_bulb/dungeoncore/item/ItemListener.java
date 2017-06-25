package net.l_bulb.dungeoncore.item;

import java.util.Map;

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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerBreakMagicOreEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerDeathInDungeonEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerKillEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerSetStrengthItemResultEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerStrengthFinishEvent;
import net.l_bulb.dungeoncore.common.projectile.ProjectileManager;
import net.l_bulb.dungeoncore.item.customItem.armoritem.ArmorBase;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.specialDamage.SpecialType;
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
import net.l_bulb.dungeoncore.item.nbttag.CustomWeaponItemStack;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.item.slot.SlotType;
import net.l_bulb.dungeoncore.item.slot.magicstone.CombatSlot;
import net.l_bulb.dungeoncore.item.slot.magicstone.KillSlot;
import net.l_bulb.dungeoncore.mob.LastDamageManager;
import net.l_bulb.dungeoncore.mob.LastDamageMethodType;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.player.PlayerChecker;
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
        boolean useFlg = clickItem.excuteOnRightClick(e);
        // アイテムの効果が発動し、消費するなら手持ちのアイテムを１つ消費する
        if (useFlg && clickItem.isConsumeWhenRightClick(e)) {
          ItemStackUtil.consumeItemInHand(player);
        }
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
        if (!callEvent.isCancel()) {
          e.setDamage(DamageModifier.BASE, callEvent.getDamage());
        } else {
          e.setCancelled(true);
        }
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
   * 最終ダメージをセットする
   *
   * @param e
   */
  @EventHandler
  public void onAttackDamage(CombatEntityEvent e) {
    // last damageを登録する
    LastDamageManager.onDamage(e.getEnemy(), e.getAttacker(), e.geLastDamageMethodType(), e.getItemStack());
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

    // 最後に攻撃した攻撃者を取得
    LastDamageMethodType lastDamageMethod = LastDamageManager.getLastDamageAttackType(entity);
    Player player = LastDamageManager.getLastDamagePlayer(entity);
    ItemStack item = LastDamageManager.getLastDamageItem(entity);
    // 攻撃者が記録されていないなら何もしない
    if (lastDamageMethod == null || player == null) { return; }

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

    ItemStackNbttagAccessor nbtTagSetter = new ItemStackNbttagAccessor(e.getItemStack());

    for (SlotInterface slot : nbtTagSetter.getGetAllSlotList(SlotType.NORMAL)) {
      if (slot instanceof CombatSlot) {
        ((CombatSlot) slot).onCombat(e, e.getAttacker());
      }
    }
    // 武器スキルを実行
    WeaponSkillExecutor.executeWeaponSkillOnCombat(e);

    // 特定の対象に対して武器固有のダメージを加算させる
    CustomWeaponItemStack instance = CustomWeaponItemStack.getInstance(e.getItemStack(), (CombatItemable) e.getCustomItem());
    Map<SpecialType, Double> specialTypeList = instance.getSpecialTypeList();
    specialTypeList.keySet().stream().filter(t -> t.getIsTarget().test(e.getEnemy())).map(specialTypeList::get)
        .forEach(t -> e.setDamage(e.getDamage() * specialTypeList.get(t)));
  }

  @EventHandler
  public void onKill(PlayerKillEntityEvent e) {
    CombatItemable combatItem = ItemManager.getCustomItem(CombatItemable.class, e.getItem());
    // 戦闘用アイテムでないなら何もしない
    if (combatItem == null) { return; }

    ItemStackNbttagAccessor nbtTagSetter = new ItemStackNbttagAccessor(e.getItem());
    for (SlotInterface slot : nbtTagSetter.getGetAllSlotList(SlotType.NORMAL)) {
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
    ItemStackNbttagAccessor nbttagSetter = new ItemStackNbttagAccessor(item);

    // もし指定したアイテムDamageItemableでないなら無視
    EquipItemable customItem = ItemManager.getCustomItem(EquipItemable.class, item);
    if (customItem == null) { return; }

    // 現在の耐久を取得
    short customDurability = nbttagSetter.getNowDurability();
    // 耐久値を更新
    customDurability += e.getDamage();

    // 設定した固有の最大耐久
    int customMaxDurability = nbttagSetter.getMaxDurability();
    // 実際の素材の最大耐久
    short itemMaxDurability = item.getType().getMaxDurability();

    // 耐久をセットする
    nbttagSetter.setNowDurability(customDurability);

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

  /**
   * Playerがダンジョンで死んだ時アイテムを削除する
   *
   * @param e
   */
  @EventHandler
  public void onPlayerDeathInDungeonEvent(PlayerDeathInDungeonEvent e) {
    Player player = e.getPlayer();
    if (PlayerChecker.isNonNormalPlayer(player)) { return; }

    PlayerInventory inv = player.getInventory();
    ItemStack[] contents = inv.getContents();
    for (int i = 0; i < contents.length; i++) {
      ItemInterface customItem = ItemManager.getCustomItem(contents[i]);
      if (customItem == null) {
        continue;
      }
      if (!customItem.isRemoveWhenDeath()) {
        continue;
      }
      inv.clear(i);
    }
  }
}
