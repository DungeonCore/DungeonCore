package lbn.player.status;

import lbn.api.player.TheLowPlayer;
import lbn.player.status.detail.BowStatusDetail;
import lbn.player.status.detail.MagicStatusDetail;
import lbn.player.status.detail.MainStatusDetail;
import lbn.player.status.detail.SwordStatusDetail;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class StatusViewerInventory {
	public static final String TITLE = "STATUS VIEWER";

	static final ItemStack VIEWER_LINE = new ItemStack(Material.STAINED_GLASS_PANE);
	static {
		VIEWER_LINE.getData().setData((byte) 4);
		VIEWER_LINE.setDurability((short) 4);
	}

	public static Inventory getStatusView(TheLowPlayer p) {
		Inventory viewer = Bukkit.createInventory(null, 9 * 5, TITLE);

		// Mainレベル
		setViewerItem(viewer, 0, new MainStatusDetail(p));

		// 線
		for (int index = 9; index < 18; index++) {
			viewer.setItem(index, VIEWER_LINE);
		}

		// 剣レベル
		setViewerItem(viewer, 2, new SwordStatusDetail(p));
		// 弓レベル
		setViewerItem(viewer, 3, new BowStatusDetail(p));
		// 魔法レベル
		setViewerItem(viewer, 4, new MagicStatusDetail(p));

		return viewer;
	}

	/**
	 * Viewer(インベントリに表示するアイテムをセットする)
	 * 
	 * @param viewer
	 *            対象のインベントリ
	 * @param row
	 *            行番号
	 * @param detail
	 *            詳細クラス
	 */
	public static void setViewerItem(Inventory viewer, int row, IStatusDetail detail) {
		for (int index = 0; index < 9; index++) {
			// 0番目
			if (index == 0) {
				viewer.setItem(index + row * 9, detail.getTitleViewIcon());
			} else {
				viewer.setItem(index + row * 9, detail.getLevelViewIcon(index));
			}
		}
	}
}
