package main.player.status;

import main.player.status.mainStatus.MainStatusManager;
import main.util.ItemStackUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class StatusViewerInventory {
	public static final String TITLE =  "STATUS VIEWER";


	public static Inventory getStatusView(Player p) {
		IStatusManager[] managerList = new IStatusManager[]{PlayerStatusListener.managerList[0], new ViewerLine(), PlayerStatusListener.managerList[1], PlayerStatusListener.managerList[2], PlayerStatusListener.managerList[3]};
		int managerSize = 0;
		for (IStatusManager iStatusManager : managerList) {
			managerSize += iStatusManager.getViewRowSize();
		}

		Inventory viewer = Bukkit.createInventory(null, 9 * managerSize, TITLE);

		//行番号
		int row = 0;
		for (IStatusManager manager : managerList) {
			PlayerStatus status = manager.getStatus(p);
			IStatusDetail detail = manager.getDetail();

			int level = 0;
			//列番号
			for (int index = row  * 9; index < 9 * manager.getViewRowSize() + row  * 9; index++) {
				//0番目
				if (index == row  * 9) {
					if (manager instanceof MainStatusManager) {
						viewer.setItem(index, getIndexItemForMainStatus(detail, status));
					} else if (manager instanceof ViewerLine) {
						viewer.setItem(index, manager.getLevelViewIcon(index, p, status));
					} else {
						viewer.setItem(index, getIndexItem(detail, status));
					}
					level++;
				} else if (index % 9 == 0) {
					//何もしない
				} else {
					viewer.setItem(index, manager.getLevelViewIcon(level, p, status));
					level++;
				}
			}
			row += manager.getViewRowSize();
		}

		return viewer;
	}

	private static ItemStack getIndexItemForMainStatus(IStatusDetail detail,
			PlayerStatus status) {
		ItemStack itemStack = new ItemStack(Material.WOOL);
		setPinkWool(itemStack, DyeColor.GREEN);
		ItemStackUtil.addLore(itemStack, new String[]{ChatColor.GREEN + "    ・現在のレベル:" + status.getLevel(), ""});
		ItemStackUtil.setDispName(itemStack, detail.getDisplayName()  + "  "+ "レベル" + status.getLevel());
		ItemStackUtil.addLore(itemStack, detail.getIndexDetail());
		return itemStack;
	}

	private static ItemStack getIndexItem(IStatusDetail detail, PlayerStatus status) {
		ItemStack itemStack = new ItemStack(Material.WOOL);
		setPinkWool(itemStack, DyeColor.WHITE);
		ItemStackUtil.addLore(itemStack, new String[]{ChatColor.GREEN + "    ・現在のレベル:" + status.getLevel(),
				ChatColor.GREEN + "    ・現在の経験値:" + status.getExp(),
				ChatColor.GREEN + "    ・次のレベルまでの必要経験値:" + status.getRemainExp(), ""});

		ItemStackUtil.setDispName(itemStack, detail.getDisplayName()  + "  "+ "レベル" + status.getLevel());
		ItemStackUtil.addLore(itemStack, detail.getIndexDetail());
		return itemStack;
	}

	@SuppressWarnings("deprecation")
	public static void setPinkWool(ItemStack itemStack, DyeColor c) {
		Wool data = (Wool) itemStack.getData();
		data.setColor(c);
		itemStack.setData(data);
		itemStack.setDurability((short) c.getData());
	}

	@SuppressWarnings("deprecation")
	static class ViewerLine implements IStatusManager {
		@Override
		public String getManagerName() {
			return "";
		}


		@Override
		public int getExp(OfflinePlayer p) {
			return 0;
		}

		@Override
		public int getLevel(OfflinePlayer p) {
			return 0;
		}

		@Override
		public int getMaxLevel() {
			return 0;
		}

		@Override
		public void addExp(OfflinePlayer target, int value, StatusAddReason reason) {

		}

		@Override
		public void setLevel(OfflinePlayer target, int value) {

		}

		@Override
		public PlayerStatus getStatus(OfflinePlayer target) {
			return null;
		}

		@Override
		public IStatusDetail getDetail() {
			return new IStatusDetail() {

				@Override
				public Material getViewIconMaterial() {
					return Material.STAINED_GLASS_PANE;
				}

				@Override
				public String[] getIndexDetail() {
					return new String[0];
				}

				@Override
				public String getDisplayName() {
					return "";
				}

				@Override
				public String[] getDetailByLevel(int level) {
					return new String[0];
				}
			};
		}

		@Override
		public int getViewRowSize() {
			return 1;
		}

		@Override
		public int getExpByLevel(int nextLevel, int[] otherLevelList) {
			return 0;
		}

		final ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
		{
			itemStack.getData().setData((byte) 4);
			itemStack.setDurability((short) 4);
		}

		@Override
		public ItemStack getLevelViewIcon(int viewIndex, OfflinePlayer p,
				PlayerStatus status) {
			return itemStack;
		}

	}

}
