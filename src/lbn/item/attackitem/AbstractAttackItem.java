package lbn.item.attackitem;

import java.util.ArrayList;
import java.util.List;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.event.player.PlayerRightShiftClickEvent;
import lbn.item.AbstractItem;
import lbn.item.attackitem.weaponSkill.WeaponSkillSelector;
import lbn.item.itemInterface.AvailableLevelItemable;
import lbn.item.itemInterface.CombatItemable;
import lbn.item.itemInterface.LeftClickItemable;
import lbn.item.itemInterface.RightClickItemable;
import lbn.item.itemInterface.Strengthenable;
import lbn.item.slot.slot.EmptySlot;
import lbn.util.ItemStackUtil;
import lbn.util.JavaUtil;
import lbn.util.Message;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractAttackItem extends AbstractItem implements Strengthenable, AvailableLevelItemable, RightClickItemable, LeftClickItemable,  CombatItemable{
	/**
	 * この武器が使用可能ならTRUE
	 * @param player
	 * @return
	 */
	protected boolean isAvilable(Player player) {
		//クリエイティブなら使えるようにする
		if (player.getGameMode() == GameMode.CREATIVE) {
			return true;
		}

		//Playerインスタンスを取得
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
		if (theLowPlayer == null) {
			TheLowPlayerManager.sendLoingingMessage(player);
			return false;
		}
		//レベルを確認
		int level = theLowPlayer.getLevel(getLevelType());
		if (getAvailableLevel() > level) {
			return false;
		}
		return true;
	}

	@Override
	public void excuteOnLeftClick(PlayerInteractEvent e) {

		//レベルを確認
		Player player = e.getPlayer();
		if (!isAvilable(player)) {
			sendNotAvailableMessage(player);
			e.setCancelled(true);
			return;
		}
		//選択メニューを開く
		if (player.isSneaking()) {
			new WeaponSkillSelector(getAttackType(), getSkillLevel()).open(player);
			e.setCancelled(true);
		}
	}

	@Override
	public void excuteOnRightClick(PlayerInteractEvent e) {

		//レベルを確認
		Player player = e.getPlayer();
		if (!isAvilable(player)) {
			sendNotAvailableMessage(player);
			e.setCancelled(true);
			return;
		}

		ItemStack item = e.getItem();
		if (player.isSneaking()) {
			new PlayerRightShiftClickEvent(player, item).callEvent();
		}
	}

	@Override
	public LevelType getLevelType() {
		return getAttackType().getLevelType();
	}

	/**
	 * スキルレベルを取得
	 * @return
	 */
	abstract protected int getSkillLevel();

	/**
	 * デフォルトのスロットの数
	 * @return
	 */
	public int getDefaultSlotCount() {
		return 1;
	}

	/**
	 * デフォルトのスロットの数
	 * @return
	 */
	public int getMaxSlotCount() {
		return 3;
	}

	@Override
	public int getMaxStrengthCount() {
		return 9;
	}

	/**
	 * 武器のダメージを取得 (武器本体のダメージも含まれます)
	 * @param p
	 * @param get_money_item
	 * @return
	 */
	public double getAttackItemDamage(int strengthLevel) {
		//キャッシュになければ作成
		if (damageCache == -1) {
			double minDamage = getAttackType().getMinDamage(getAvailableLevel());
			double maxDamage = getAttackType().getMaxDamage(getAvailableLevel());
			damageCache = minDamage + (maxDamage - minDamage) / 20.0 * getCraftLevel();
		}

		//クリティカルなら1.25倍にする
		if (JavaUtil.isRandomTrue((int) getCriticalHitRate(strengthLevel))) {
			return damageCache * 1.25;
		} else {
			return damageCache;
		}
	}

	double damageCache = -1;

	/**
	 * クラフトレベルを取得する
	 * @return
	 */
	abstract public int getCraftLevel();

	@Override
	public boolean isShowItemList() {
		return true;
	}

	/**
	 * この武器のアイテムのデフォルトの攻撃力を取得
	 * @return
	 */
	abstract protected double getMaterialDamage();

	@Override
	protected List<String> getAddDetail() {
		List<String> lore = super.getAddDetail();
		//使用可能レベル
		lore.add(Message.getMessage("使用可能：{0}{1}以上", getAttackType().getLevelType().getName(), getAvailableLevel()));
		lore.add("最大SLOT数：" + getMaxSlotCount() + "個");
		return lore;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();

		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(ChatColor.GREEN + "[SLOT]");
		EmptySlot slot = new EmptySlot();
		for (int i = 0; i < getDefaultSlotCount(); i++) {
			arrayList.add (StringUtils.join(new Object[]{slot.getNameColor(), "    ■ ", slot.getSlotName(), ChatColor.BLACK, "id:", slot.getId()}));
		}
		arrayList.add("");

		ItemStackUtil.addLore(item, arrayList.toArray(new String[0]));
		return item;
	}

	/**
	 * 使用不可のときに表示するメッセージ
	 * @param p
	 */
	protected void sendNotAvailableMessage(Player p) {
		Message.sendMessage(p, Message.CANCEL_USE_ITEM_BY_LEVEL, getAttackType().getLevelType().getName(), getAvailableLevel());
	}

	/**
	 * 強化レベルによって変化するLoreを取得
	 */
	public String[] getStrengthDetail(int level) {
		if (level == 0) {
			return new String[0];
		}
		return new String[]{"クリティカル率:" + getCriticalHitRate(level) + "%"};
	}

	/**
	 * クリティカル確率
	 * @param level
	 * @return
	 */
	public double getCriticalHitRate(int level) {
		return 1.5 * level;
	}

	@Override
	public void onCombatEntity(PlayerCombatEntityEvent e) {
	}
}
