package lbn.item.attackitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.event.player.PlayerRightShiftClickEvent;
import lbn.item.AbstractItem;
import lbn.item.itemInterface.AvailableLevelItemable;
import lbn.item.itemInterface.CombatItemable;
import lbn.item.itemInterface.RightClickItemable;
import lbn.item.itemInterface.Strengthenable;
import lbn.item.slot.slot.EmptySlot;
import lbn.item.strength.StrengthOperator;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mob.attribute.Attribute;
import lbn.mob.attribute.AttributeNormal;
import lbn.util.ItemStackUtil;
import lbn.util.JavaUtil;
import lbn.util.Message;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractAttackItem extends AbstractItem implements Strengthenable, AvailableLevelItemable, RightClickItemable, CombatItemable{
	public boolean isAvilable(Player player) {
		//クリエイティブなら使えるようにする
		if (player.getGameMode() == GameMode.CREATIVE) {
			return true;
		}

		//Playerインスタンスを取得
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
		if (theLowPlayer == null) {
			return false;
		}
		int level = theLowPlayer.getLevel(getLevelType());
		if (getAvailableLevel() > level) {
			return false;
		}
		return true;
	}

	static AttributeNormal attributeNormal = new AttributeNormal();
	public Attribute getAttribute() {
		return attributeNormal;
	}

	@Override
	public void excuteOnRightClick(PlayerInteractEvent e) {
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
		int available = getAvailableLevel();
		if (available < 10) {
			return 5;
		} else if (available < 20) {
			return 6;
		} else if (available < 30) {
			return 7;
		} else if (available < 40) {
			return 8;
		} else if (available < 50) {
			return 9;
		} else if (available < 60) {
			return 10;
		} else if (available < 70) {
			return 11;
		} else if (available < 80) {
			return 12;
		} else if (available < 90) {
			return 12;
		} else if (available < 100) {
			return 13;
		} else if (available < 110) {
			return 13;
		} else if (available < 120) {
			return 14;
		} else if (available < 120) {
			return 14;
		} else {
			return 15;
		}
	}

	double a = 0.15;
	double b = -1;
	double c = -1;

	/**
	 * 武器のダメージを取得 (武器本体のダメージも含まれます)
	 * @param p
	 * @param get_money_item
	 * @return
	 */
	public double getAttackItemDamage(int strengthLevel) {
		if (b == -1) {
			b = (- a * 0 * 0 + a * getMaxStrengthCount() * getMaxStrengthCount() + getMinAttackDamage() - getMaxAttackDamage()) / (0 - getMaxStrengthCount());
		}
		if (c == -1) {
			c = getMinAttackDamage();
		}
		double damage = a * strengthLevel * strengthLevel + b * strengthLevel + c;

		return damage;
	}

	abstract protected double getNormalDamage();

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
		//属性の説明を追加
		Attribute attribute = getAttribute();
		if (!attribute.isNonAttribute()) {
			ItemStackUtil.addLore(item, ChatColor.RED + Message.getMessage("属性特攻:{0}", attribute.getName()));
		}

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

	protected void sendNotAvailableMessage(Player p) {
		Message.sendMessage(p, Message.CANCEL_USE_ITEM_BY_LEVEL, getAttackType().getLevelType().getName(), getAvailableLevel());
	}

	protected double getMaxAttackDamage() {
		return getAttackType().getMaxDamage(getAvailableLevel());
	}

	protected double getMinAttackDamage() {
		return getAttackType().getMinDamage(getAvailableLevel());
	}

	final public String[] getStrengthDetail(int level) {
		//追加ダメージを記載
		String[] detail = getStrengthDetail2(level);

		ArrayList<String> lores;
		if (detail == null) {
			lores = new ArrayList<String>();
		} else {
			lores = new ArrayList<String>(Arrays.asList(detail));
		}

		double dispAddDamane = JavaUtil.round(getAttackItemDamage(level) - getNormalDamage(), 2);
		lores.add(Message.getMessage(Message.ADD_DAMAGE_DISP, (dispAddDamane >= 0 ? "+" : "")  + dispAddDamane));

		if (!getAttribute().isNonAttribute()) {
			lores.add(Message.getMessage("{0}に対してダメージ+{1}%", getAttribute().getName(), getAttributeAddParcentDamage(level)));
		}

		return lores.toArray(new String[0]);
	}

	/**
	 * 属性で追加されるダメージ
	 * @param level
	 * @return
	 */
	protected double getAttributeAddParcentDamage(int level) {
		if (getAttribute().isNonAttribute()) {
			return 0;
		}

		int maxStrengthCount = getMaxStrengthCount();
		//maxが+200%
		return 200.0 * level / maxStrengthCount;
	}

	abstract protected String[] getStrengthDetail2(int level);

	@Override
	public int getBuyPrice(ItemStack item) {
		int availableLevel = Math.min(getAvailableLevel(), 70);
		return (int) (getBaseBuyPrice()  + availableLevel * 5 + ((StrengthOperator.getLevel(item) + 1) * 200));
	}

	abstract protected int getBaseBuyPrice();

	@Override
	public void onCombatEntity(PlayerCombatEntityEvent e) {
		LivingEntity enemy = e.getEnemy();
		AbstractMob<?> mob = MobHolder.getMob(enemy);
		//属性のダメージを与える
		if (mob != null && mob.getAttribute().isSame(getAttribute())) {
			e.setDamage(e.getDamage() * (1 + getAttributeAddParcentDamage(e.getAttackItem().getStrengthLevel()) / 100));
			return;
		}
	}

}
