package lbn.item.customItem.attackitem.weaponSkill;

import java.text.MessageFormat;
import java.util.List;

import lbn.common.cooltime.Cooltimable;
import lbn.common.cooltime.CooltimeManager;
import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.item.customItem.attackitem.AttackItemStack;
import lbn.player.ItemType;
import lbn.player.customplayer.MagicPointManager;
import lbn.util.ItemStackUtil;
import lbn.util.Message;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WeaponSkillExecutor {
	/**
	 * 右クリックした時の武器スキルの発動
	 * @param e
	 * @param customItem
	 */
	public static void executeWeaponSkillOnClick(PlayerInteractEvent e, AbstractAttackItem customItem) {
		Player player = e.getPlayer();
		//武器を取得
		ItemStack item = e.getItem();

		//武器スキルを取得
		WeaponSkillInterface skill = getWeaponSkill(item);
		//武器スキルが登録されていない場合は何もしない
		if (skill == null) {
			return;
		}

		CooltimeManager cooltimeManager = new CooltimeManager(player, new CooltimeImpl(customItem.getAttackType(), skill.getCooltime()), item);
		//クールタイムが残っていないか確認
		if (!cooltimeManager.canUse()) {
			cooltimeManager.sendCooltimeMessage(player);
			return;
		}

		//MP取得
		int nowMagicPoint = MagicPointManager.getNowMagicPoint(player);
		if (nowMagicPoint < skill.getNeedMagicPoint()) {
			Message.sendMessage(player, ChatColor.RED + "マジックポイントが不足しています");
			return;
		}
		boolean onExecute = skill.onClick(player, item, customItem);
		if (onExecute) {
			//メッセージを表示
			sendMessage(player, skill);
			//クールタイムをセット
			cooltimeManager.setCoolTime();
		}
	}

	/**
	 * 周囲のPlayerにメッセージを表示する
	 * @param player
	 * @param skill
	 */
	private static void sendMessage(Player p, WeaponSkillInterface skill) {
		List<Player> players = p.getWorld().getPlayers();
		Location location = p.getLocation();
		for (Player player : players) {
			//自分にはメッセージを表示しない
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
	public static void executeWeaponSkillOnCombat(PlayerCombatEntityEvent e) {
		AttackItemStack attackItem = e.getAttackItem();
		ItemStack item = attackItem.getItem();
		Player player = e.getPlayer();
		AbstractAttackItem customItem = attackItem.getItemInterface();

		//武器スキルを取得
		WeaponSkillInterface skill = getWeaponSkill(item);
		//武器スキルが登録されていない場合は何もしない
		if (skill == null) {
			return;
		}

		//スキルを発動
		skill.onCombat(player, item, customItem, e.getEnemy(), e);
	}



	/**
	 * ItemStackから武器スキルを取得
	 * @param item
	 * @return
	 */
	public static WeaponSkillInterface getWeaponSkill(ItemStack item) {
		//武器スキルIdを取得
		String skillId = ItemStackUtil.getNBTTag(item, "weaponskill");
		if (skillId == null) {
			return null;
		}
		return WeaponSkillFactory.getWeaponSkill(skillId);
	}


	/**
	 * CoolTime情報を格納するクラス
	 */
	static class CooltimeImpl implements Cooltimable {
		public CooltimeImpl(ItemType type, int cooltime) {
			this.type = type;
			this.cooltime = cooltime * 20;
		}

		ItemType type;
		int cooltime;

		@Override
		public int getCooltimeTick(ItemStack item) {
			return cooltime;
		}

		@Override
		public String getId() {
			return "weapon_skill_" + type;
		}

	}
}
