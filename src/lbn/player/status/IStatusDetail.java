package lbn.player.status;

import java.util.ArrayList;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.util.ItemStackUtil;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public abstract class IStatusDetail {
	protected int level = 0;
	protected int maxLevel = 60;
	protected TheLowPlayer p;

	public IStatusDetail(TheLowPlayer p) {
		level = p.getLevel(getLevelType());
		maxLevel = p.getMaxLevel(getLevelType());
		this.p = p;
	}

	abstract public String[] getIndexDetail();

	abstract public String[] getDetailByLevel(int level);

	abstract public String getDisplayName();

	abstract public Material getViewIconMaterial();

	abstract public LevelType getLevelType();

	public ItemStack getTitleViewIcon() {
		if (getLevelType() == LevelType.MAIN) {
			return getIndexItemForMainStatus();
		} else {
			return getIndexItem();
		}
	}

	protected ItemStack getIndexItemForMainStatus() {
		ItemStack itemStack = new ItemStack(Material.WOOL);
		setPinkWool(itemStack, DyeColor.GREEN);
		ItemStackUtil.addLore(itemStack, new String[] { ChatColor.GREEN + "    ・現在のレベル:" + level, "" });
		ItemStackUtil.setDispName(itemStack, getDisplayName() + "  " + "レベル" + level);
		ItemStackUtil.addLore(itemStack, getIndexDetail());
		return itemStack;
	}

	protected ItemStack getIndexItem() {
		LevelType levelType = getLevelType();
		ItemStack itemStack = new ItemStack(Material.WOOL);
		setPinkWool(itemStack, DyeColor.WHITE);
		ItemStackUtil.addLore(itemStack, new String[] { ChatColor.GREEN + "    ・現在のレベル:" + level + " level",
				ChatColor.GREEN + "    ・現在の経験値 : " + p.getExp(levelType) + " exp", ChatColor.GREEN + "    ・次レベルまであと"
						+ (p.getNeedExp(levelType, level + 1) - p.getExp(levelType)) + " exp必要",
				"" });
		ItemStackUtil.setDispName(itemStack, getDisplayName() + "  " + "レベル" + level);
		ItemStackUtil.addLore(itemStack, getIndexDetail());
		return itemStack;
	}

	@SuppressWarnings("deprecation")
	public static void setPinkWool(ItemStack itemStack, DyeColor c) {
		Wool data = (Wool) itemStack.getData();
		data.setColor(c);
		itemStack.setData(data);
		itemStack.setDurability((short) c.getData());
	}

	public ItemStack getLevelViewIcon(int viewIndex) {
		ItemStack item = null;

		// 指定されたViewerの場所を指すレベル
		int indexLevel = (viewIndex - 1) * 10;
		// 自分のレベル >= index * 10
		if (level > indexLevel) {
			item = new ItemStack(getViewIconMaterial());
			ItemStackUtil.setDispName(item, "STAGE" + viewIndex + ChatColor.GREEN + "  (UNLOCKED)");
			setViewIconLore(item, viewIndex);
		} else {
			// 指定された場所のレベルが最大レベルを下回っている場合はボタンを設置
			if (indexLevel < maxLevel) {
				item = new ItemStack(Material.STONE_BUTTON);
				ItemStackUtil.setDispName(item, "STAGE" + viewIndex + ChatColor.RED + "  (LOCKED)");
				setViewIconLore(item, viewIndex);
			} else {
				// 何も設置しない
			}
		}
		return item;
	}

	protected void setViewIconLore(ItemStack item, int viewIndex) {
		ArrayList<String> lore = new ArrayList<String>();
		for (int i = 1; i <= 10; i++) {
			int level = (viewIndex - 1) * 10 + i;

			ChatColor color = null;
			// ex)3列目の4項目目(24レベルの場所) → (3-1)*10 + 4 が 現在のレベルよりも大きい場合
			// すなわちLOCKされている場所
			if (level <= this.level) {
				color = ChatColor.LIGHT_PURPLE;
			} else {
				if (level <= maxLevel) {
					color = ChatColor.DARK_GRAY;
				} else {
					continue;
				}
			}
			String[] detailByLevel = getDetailByLevel(level);

			String detail = "";
			if (detailByLevel.length > 0) {
				detail = detailByLevel[0];
			}
			String join = StringUtils.join(new Object[] { color, "レベル", Integer.toString(level), "  ", detail });
			lore.add(join);
		}

		ItemStackUtil.addLore(item, lore.toArray(new String[0]));
	}
}
