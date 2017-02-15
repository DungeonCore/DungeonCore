package lbn.item.attackitem.weaponSkill;

import lbn.item.Cooltimable;
import lbn.item.CooltimeManager;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.player.ItemType;
import lbn.player.customplayer.MagicPointManager;
import lbn.util.ItemStackUtil;
import lbn.util.Message;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MobSkillExecutor {
	public static void executeMobSkillOnClick(PlayerInteractEvent e, AbstractAttackItem customItem) {
		Player player = e.getPlayer();

		//武器スキルを取得
		WeaponSkillInterface skill = getWeaponSkill(e.getItem());
		//武器スキルが登録されていない場合は何もしない
		if (skill == null) {
			return;
		}

		CooltimeManager cooltimeManager = new CooltimeManager(e, new CooltimeImpl(customItem.getAttackType(), skill.getCooltime()));
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

		//スキルを発動
		skill.onClick(player, e.getItem(), customItem);
		//クールタイムをセット
		cooltimeManager.setCoolTime();
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
