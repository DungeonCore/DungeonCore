package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import java.text.MessageFormat;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.l_bulb.dungeoncore.common.cooltime.Cooltimable;
import net.l_bulb.dungeoncore.common.cooltime.CooltimeManager;
import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.all.WeaponSkillCancel;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.player.customplayer.MagicPointManager;
import net.l_bulb.dungeoncore.util.Message;

import net.md_5.bungee.api.ChatColor;

public class WeaponSkillExecutor {
  /**
   * 右クリックした時の武器スキルの発動
   *
   * @param e
   * @param customItem
   */
  public static void executeWeaponSkillOnClick(PlayerInteractEvent e, AbstractAttackItem customItem) {
    Player player = e.getPlayer();
    // 武器を取得
    ItemStack item = e.getItem();

    // スキルの種類を取得
    WeaponSkillType type = player.isSleeping() ? WeaponSkillType.SPECIAL_SKILL : WeaponSkillType.NORMAL_SKILL;

    // 武器スキルを取得
    WeaponSkillInterface skill = getWeaponSkill(item, type);
    // 武器スキルが登録されていない場合は何もしない
    if (skill == null) { return; }

    CooltimeManager cooltimeManager = new CooltimeManager(player, new CooltimeImpl(skill), item);
    // クールタイムが残っていないか確認
    if (!cooltimeManager.canUse()) {
      cooltimeManager.sendCooltimeMessage(player);
      return;
    }

    // MP取得
    int nowMagicPoint = MagicPointManager.getNowMagicPoint(player);
    if (nowMagicPoint < skill.getNeedMagicPoint()) {
      Message.sendMessage(player, ChatColor.RED + "マジックポイントが不足しています");
      return;
    }
    boolean onExecute = skill.onClick(player, item, customItem);
    if (onExecute) {
      // メッセージを表示
      sendMessage(player, skill);
      // クールタイムをセット
      cooltimeManager.setCoolTime();
      // マジックポイントをセットする
      MagicPointManager.consumeMagicPoint(player, skill.getNeedMagicPoint());
    }
  }

  /**
   * 周囲のPlayerにメッセージを表示する
   *
   * @param player
   * @param skill
   */
  private static void sendMessage(Player p, WeaponSkillInterface skill) {
    // スキル解除の時は通知しない
    if (WeaponSkillCancel.isThisSkill(skill)) { return; }

    List<Player> players = p.getWorld().getPlayers();
    Location location = p.getLocation();
    for (Player player : players) {
      // 自分にはメッセージを表示しない
      if (player.equals(p)) {
        continue;
      }
      double x = player.getLocation().getX() - location.getX();
      double y = player.getLocation().getY() - location.getY();
      double z = player.getLocation().getZ() - location.getZ();
      if ((x * x) + (y * y) + (z * z) < 30 * 30) {
        player.sendMessage(MessageFormat.format("{0}[武器スキル] {1}{2}が{3}を発動", ChatColor.RED, ChatColor.GREEN, p.getName(), skill.getName()));
      }
    }
  }

  /**
   * 攻撃をした時、スキルを発動
   */
  public static void executeWeaponSkillOnCombat(CombatEntityEvent e) {
    // 攻撃したのがPlayerでないなら無視する
    if (e.getAttacker().getType() != EntityType.PLAYER) { return; }
    Player player = e.getAttacker();

    ItemStack item = e.getItemStack();
    CombatItemable customItem = ItemManager.getCustomItem(CombatItemable.class, item);
    if (customItem != null) {
      WeaponSkillInterface normalSkill = getWeaponSkill(item, WeaponSkillType.NORMAL_SKILL);
      // 通常スキルを発動
      if (normalSkill != null) {
        normalSkill.onCombat(player, item, customItem, e.getEnemy(), e);
      }

      WeaponSkillInterface specialSkill = getWeaponSkill(item, WeaponSkillType.SPECIAL_SKILL);
      // スペシャルスキルを発動
      if (specialSkill != null) {
        // スキルを発動
        specialSkill.onCombat(player, item, customItem, e.getEnemy(), e);
      }
    }
  }

  /**
   * ダメージを受けたとき、スキルを発動
   */
  public static void executeWeaponSkillOnDamage(EntityDamageByEntityEvent e) {
    // 攻撃したのがPlayerなら無視する
    if (e.getDamager().getType() == EntityType.PLAYER) { return; }
    // ダメージを受けたのがPlayerでないなら無視する
    if (e.getEntity().getType() != EntityType.PLAYER) { return; }

    // Playerが手に持っているアイテムを取得
    Player p = (Player) e.getEntity();
    ItemStack item = p.getItemInHand();

    CombatItemable customItem = ItemManager.getCustomItem(CombatItemable.class, item);
    if (customItem != null) {
      WeaponSkillInterface normalSkill = getWeaponSkill(item, WeaponSkillType.NORMAL_SKILL);
      // 通常スキルを発動
      if (normalSkill != null) {
        normalSkill.onDamage(p, item, e);
      }

      WeaponSkillInterface specialSkill = getWeaponSkill(item, WeaponSkillType.SPECIAL_SKILL);
      // スペシャルスキルを発動
      if (specialSkill != null) {
        // スキルを発動
        specialSkill.onDamage(p, item, e);
      }
    }
  }

  /**
   * アイテムを持ち替えたときの処理
   *
   * @param e
   */
  public static void executePlayerItemHeldEvent(PlayerItemHeldEvent e) {
    Player player = e.getPlayer();
    PlayerInventory inventory = player.getInventory();
    // 持ち替える前のアイテム
    executeOnheldItem(player, inventory.getItem(e.getPreviousSlot()), false);
    executeOnheldItem(player, inventory.getItem(e.getNewSlot()), true);
  }

  /**
   * プレイヤーが指定したアイテムを持った時の処理
   *
   * @param player
   * @param item
   * @param isOffItem 別のアイテムから指定したアイテムに持ち替えたならtrue, 指定したアイテムから別のアイテムに持ち替えたならfalse
   */
  private static void executeOnheldItem(Player player, ItemStack item, boolean isOnItem) {
    CombatItemable previousCustomItem = ItemManager.getCustomItem(CombatItemable.class, item);
    if (previousCustomItem != null) {
      WeaponSkillInterface normalSkill = getWeaponSkill(item, WeaponSkillType.NORMAL_SKILL);
      // 通常スキルを発動
      if (normalSkill != null) {
        if (isOnItem) {
          normalSkill.onHeldThisItem(player, item);
        } else {
          normalSkill.offHeldThisItem(player, item);
        }
      }

      WeaponSkillInterface specialSkill = getWeaponSkill(item, WeaponSkillType.SPECIAL_SKILL);
      // スペシャルスキルを発動
      if (specialSkill != null) {
        // スキルを発動
        if (isOnItem) {
          specialSkill.onHeldThisItem(player, item);
        } else {
          specialSkill.offHeldThisItem(player, item);
        }
      }
    }
  }

  /**
   * ItemStackから武器スキルを取得
   *
   * @param item
   * @param type
   * @return
   */
  public static WeaponSkillInterface getWeaponSkill(ItemStack item, WeaponSkillType type) {
    // 武器スキルIdを取得
    String skillId = new ItemStackNbttagAccessor(item).getSelectedWeaponSkillId(type);
    if (skillId != null) { return null; }
    return WeaponSkillFactory.getWeaponSkill(skillId);
  }

  /**
   * CoolTime情報を格納するクラス
   */
  static class CooltimeImpl implements Cooltimable {
    public CooltimeImpl(WeaponSkillInterface weaponSkillInterface) {
      this.type = weaponSkillInterface.geWeaponSkillType();
      this.cooltime = weaponSkillInterface.getCooltime() * 20;
      this.skillId = weaponSkillInterface.getId();
    }

    WeaponSkillType type;
    int cooltime;
    String skillId;

    @Override
    public int getCooltimeTick(ItemStack item) {
      return cooltime;
    }

    @Override
    public String getId() {
      if (type == WeaponSkillType.SPECIAL_SKILL) {
        return "weapon_skill_special_skill";
      } else if (type == WeaponSkillType.NORMAL_SKILL) {
        return "weapon_skill_" + skillId;
      } else {
        throw new IllegalArgumentException("不正な武器スキルタイプ：" + type);
      }
    }

  }
}
