package lbn.player.status.mainStatus;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import lbn.player.status.IStatusDetail;
import lbn.player.status.IStatusManager;
import lbn.player.status.PlayerStatus;
import lbn.player.status.StatusAddReason;
import lbn.player.status.bowStatus.BowStatusManager;
import lbn.player.status.magicStatus.MagicStatusManager;
import lbn.player.status.swordStatus.SwordStatusManager;
import lbn.util.ItemStackUtil;

public class MainStatusManager implements IStatusManager{

	static MainStatusManager manager = new MainStatusManager();

	public static MainStatusManager getInstance() {
		return manager;
	}

	private MainStatusManager() {
	}

	@Override
	public String getManagerName() {
		return "MAIN LEVEL";
	}

	@Override
	public int getExp(OfflinePlayer p) {
		return 0;
	}

	@Override
	public int getLevel(OfflinePlayer p) {
		return (SwordStatusManager.getInstance().getLevel(p) + BowStatusManager.getInstance().getLevel(p) + MagicStatusManager.getInstance().getLevel(p)) / 3;
	}

	@Override
	public int getMaxLevel() {
		return 160;
	}

	@Override
	public void addExp(OfflinePlayer target, int value, StatusAddReason reason) {
	}

	@Override
	public void setLevel(OfflinePlayer target, int value) {
	}

	@Override
	public PlayerStatus getStatus(OfflinePlayer target) {
		return new PlayerStatus(target, manager);
	}

	@Override
	public IStatusDetail getDetail() {
		return new MainStatusDetail();
	}

	@Override
	public int getViewRowSize() {
		return 1;
	}

	@Override
	public int getExpByLevel(int nextLevel, int[] otherLevelList) {
		return 0;
	}

	@Override
	public ItemStack getLevelViewIcon(int viewIndex, OfflinePlayer p, PlayerStatus status) {
		ItemStack item = null;
		if (status.getLevel() >= (viewIndex - 1) * 10) {
			item = new ItemStack(getDetail().getViewIconMaterial());
			ItemStackUtil.setDispName(item,  "レベル" + viewIndex * 10 + ChatColor.GREEN + "  (UNLOCKED)");
			setViewIconLore(item, viewIndex, status);
		} else {
			item = new ItemStack(Material.STONE_BUTTON);
			ItemStackUtil.setDispName(item,  "レベル" + viewIndex * 10 +  ChatColor.RED + "  (LOCKED)");
			setViewIconLore(item, viewIndex, status);
		}
		return item;
	}

	protected void setViewIconLore(ItemStack item, int viewIndex, PlayerStatus status) {
		ArrayList<String> lore = new ArrayList<String>();
		for (int i = 1; i <= 10; i++) {
			int level = (viewIndex - 1) * 10 + i;

			ChatColor color = null;
			//ex)3列目の4項目目(24レベルの場所)　→　(3-1)*10 + 4 が　現在のレベルよりも大きい場合
			//すなわちLOCKされている場所
			if (level > status.getLevel()) {
				color = ChatColor.DARK_GRAY;
			} else {
				color = ChatColor.LIGHT_PURPLE;
			}
			String[] detailByLevel = getDetail().getDetailByLevel(level);

			String detail = "未実装 Comming soon";
			if (detailByLevel.length > 0) {
				detail = detailByLevel[0];
			}
			String join = StringUtils.join(new Object[]{color , "レベル" , Integer.toString(level) , "  ", detail});
			lore.add(join);
		}

		ItemStackUtil.addLore(item, lore.toArray(new String[0]));
	}

}

