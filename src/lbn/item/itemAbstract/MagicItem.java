package lbn.item.itemAbstract;

import lbn.item.CooltimeManager;
import lbn.item.attackitem.old.AbstractAttackItem_Old;
import lbn.item.attackitem.weaponSkill.MobSkillExecutor;
import lbn.item.itemInterface.LeftClickItemable;
import lbn.item.itemInterface.MagicExcuteable;
import lbn.item.itemInterface.RightClickItemable;
import lbn.player.ItemType;
import lbn.player.customplayer.MagicPointManager;
import lbn.util.Message;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class MagicItem extends AbstractAttackItem_Old implements RightClickItemable, LeftClickItemable{

	@Override
	final public void excuteOnLeftClick(PlayerInteractEvent e) {
		e.setCancelled(true);
		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();
		if (!isAvilable(player)) {
			sendNotAvailableMessage(player);
			return;
		}

		if (!player.isSneaking()) {
			excuteMagic(e, player, item, getLeftClickMagic(item));
		}

	}

	@Override
	final public void excuteOnRightClick(PlayerInteractEvent e) {
		super.excuteOnRightClick(e);
		e.setCancelled(true);
		Player player = e.getPlayer();

		if (!isAvilable(player)) {
			e.setCancelled(true);
			return;
		}

		if (!player.isSneaking()) {
			excuteMagic(e, player, e.getItem(), getRightClickMagic(e.getItem()));
			//スキルを発動
			MobSkillExecutor.executeMobSkillOnClick(e, this);
		}
	}

	/**
	 * 指定された魔法を発動する
	 * @param e
	 * @param player
	 * @param item
	 * @param magic
	 */
	protected void excuteMagic(PlayerInteractEvent e, Player player, ItemStack item, MagicExcuteable magic) {
		//魔法が存在しないなら何もしない
		if (magic == null) {
			return;
		}
		//クールタイムを確認
		CooltimeManager cooltime = new CooltimeManager(e, magic);
		//クールタイム中ならメッセージを表示
		if (!cooltime.canUse()) {
			if (magic.isShowMessageIfUnderCooltime()) {
				cooltime.sendCooltimeMessage(player);
			}
			return;
		}
		//マジックポイントを確認し足りなければメッセージを表示
		if (!hasMagicPoint(player, magic.getNeedMagicPoint())) {
			Message.sendMessage(player, "マジックポイントが不足しています。");
			return;
		}
		//魔法を発動
		magic.excuteMagic(player, e);
		//クールタイムをつける
		cooltime.setCoolTime();
		//マジックポイントを消費する
		MagicPointManager.consumeMagicPoint(player, magic.getNeedMagicPoint());
	}

	/**
	 * 必要のマジックポイントを持っているか
	 * @param p
	 * @param needMagicPoint
	 * @return
	 */
	protected boolean hasMagicPoint(Player p, int needMagicPoint) {
		int nowMagicPoint = MagicPointManager.getNowMagicPoint(p);
		if (needMagicPoint > nowMagicPoint) {
			Message.sendMessage(p, Message.MP_SHORTAGE);
			return false;
		}
		return true;
	}

	@Override
	public ItemType getAttackType() {
		return ItemType.MAGIC;
	}

	abstract public int getAvailableLevel();

	@Override
	protected double getMaterialDamage() {
		return 0;
	}

	abstract protected MagicExcuteable getRightClickMagic(ItemStack item);
	abstract protected MagicExcuteable getLeftClickMagic(ItemStack item);

}
