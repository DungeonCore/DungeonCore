package lbn.item.customItem.attackitem;

import java.util.ArrayList;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.event.player.PlayerRightShiftClickEvent;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.customItem.AbstractItem;
import lbn.item.customItem.attackitem.weaponSkill.WeaponSkillSelector;
import lbn.item.itemInterface.CombatItemable;
import lbn.item.itemInterface.LeftClickItemable;
import lbn.item.itemInterface.Strengthenable;
import lbn.item.lore.ItemLoreToken;
import lbn.item.slot.slot.EmptySlot;
import lbn.util.ItemStackUtil;
import lbn.util.JavaUtil;
import lbn.util.Message;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractAttackItem extends AbstractItem implements Strengthenable, CombatItemable, LeftClickItemable{
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

	static WeaponStrengthTemplate strengthTemplate = new WeaponStrengthTemplate();
	@Override
	public StrengthTemplate getStrengthTemplate() {
		return strengthTemplate;
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
	 * 武器のダメージを取得 (武器本体のダメージも含まれます)
	 * @param p
	 * @param strengthLevel 強化レベル
	 * @return
	 */
	public double getAttackItemDamage(int strengthLevel) {
		//キャッシュになければ作成
		if (damageCache == -1) {
			double minDamage = getAttackType().getMinDamage(getAvailableLevel());
			double maxDamage = getAttackType().getMaxDamage(getAvailableLevel());
			damageCache = minDamage + (maxDamage - minDamage) / 20.0 * (getCraftLevel() + 1);
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
	public double getMaterialDamage() {
		double vanillaDamage = ItemStackUtil.getVanillaDamage(getMaterial());
		return vanillaDamage;
	}

	@Override
	public ItemLoreToken getStandardLoreToken() {
		ItemLoreToken loreToken = super.getStandardLoreToken();
		//使用可能レベル
		loreToken.addLore(Message.getMessage("使用可能 ： {2}{0}{1}以上", getAttackType().getLevelType().getName(), getAvailableLevel(), ChatColor.GOLD));
		loreToken.addLore("スキルレベル ： " + ChatColor.GOLD + getSkillLevel() + "レベル");
		//武器は耐久とレベルが関係ないのでnullでも問題ない
		loreToken.addLore("耐久値 ： " + ChatColor.GOLD + getMaxDurability(null));
		return loreToken;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		//SLOTを追加
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(ChatColor.GREEN + "[SLOT]  " + ChatColor.AQUA + "最大" + getMaxSlotCount() + "個" + ItemLoreToken.TITLE_TAG);
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
		String[] lore;
		if (level == 0) {
			lore = new String[1];
		} else {
			lore = new String[2];
			lore[1] = "クリティカル率 " + ChatColor.GOLD + getCriticalHitRate(level) + "%";
		}
		double dispAddDamane = JavaUtil.round(getAttackItemDamage(level) - getMaterialDamage(), 2);
		lore[0] = Message.getMessage(Message.ADD_DAMAGE_DISP, (dispAddDamane >= 0 ? "+" : "")  + dispAddDamane, ChatColor.GOLD);

		return lore;
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
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		//スニーク状態でないなら無視
		if (!player.isSneaking()) {
			return;
		}
		//捨てるのをキャンセル
		e.setCancelled(true);

		//レベルが足りないなら何もしない
		if (isAvilable(player)) {
			//選択メニューを開く
			new WeaponSkillSelector(getAttackType(), getSkillLevel()).open(player);
		} else {
			sendNotAvailableMessage(player);
		}
	}
}