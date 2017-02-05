package lbn.common.menu;

import java.util.Arrays;
import java.util.HashMap;

import lbn.item.repair.newrepair.RepairUi;
import lbn.item.slot.table.SlotSetTableOperation;
import lbn.item.strength.StrengthTableOperation;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuSelecor implements MenuSelectorInterface{
	public void regist() {
		MenuSelectorManager.regist(this);
	}

	protected String title;
	protected Inventory createInventory;
	public MenuSelecor(String title) {
		this.title = ChatColor.WHITE + "-- " + title + " --";
		createInventory = Bukkit.createInventory(null, 9 * 3, ChatColor.WHITE + "-- " + title + " --");
	}

	protected HashMap<ItemStack, SelectRunnable> runMap = new HashMap<ItemStack, SelectRunnable>();

	/**
	 * メニューを追加する
	 * @param item
	 * @param index
	 * @param run
	 * @return
	 */
	public MenuSelecor addMenu(ItemStack item, int index, SelectRunnable run) {
		createInventory.setItem(index, item);
		runMap.put(item, run);
		return this;
	}

	/**
	 * メニューを開く
	 * @param p
	 */
	public void open(Player p) {
		p.openInventory(createInventory);
	}

	/**
	 * アイテムを選択したときの処理
	 * @param p
	 * @param item
	 */
	public void onSelectItem(Player p, ItemStack item) {
		if (runMap.containsKey(item)) {
			runMap.get(item).run(p, item);
		}
	}

	@Override
	public String getTitle() {
		return title;
	}


	static {
		MenuSelecor menuSelecor = new MenuSelecor("blacksmith menu");
		//魔法石装着
		ItemStack itemStack = new ItemStack(Material.BEACON);
		ItemStackUtil.setDispName(itemStack, ChatColor.WHITE + "" + ChatColor.BOLD + Message.getMessage("魔法石装着"));
		ItemStackUtil.setLore(itemStack, Arrays.asList(ChatColor.GREEN + Message.getMessage("武器に魔法石を装着します。"),
				ChatColor.GREEN + Message.getMessage("魔法石と武器を装着してください。"), "",
				ChatColor.GREEN + Message.getMessage("成功確率などのが、"),
				ChatColor.GREEN + Message.getMessage("赤いガラスの部分に表示されます。"))
				);
		menuSelecor.addMenu(itemStack, 15,
		new SelectRunnable() {
			@Override
			public void run(Player p, ItemStack item) {
				SlotSetTableOperation.openSlotTable(p);
			}
		});

		//強化
		ItemStack itemStack2 = new ItemStack(Material.LAVA_BUCKET);
		ItemStackUtil.setDispName(itemStack2, ChatColor.WHITE + "" + ChatColor.BOLD + Message.getMessage("強化"));
		ItemStackUtil.setLore(itemStack2, Arrays.asList(ChatColor.GREEN + Message.getMessage("アイテムを強化します。"),
				ChatColor.GREEN + Message.getMessage("強化するアイテムを置いてください。"), "",
				ChatColor.GREEN + Message.getMessage("成功確率・強化素材などが"),
				ChatColor.GREEN + Message.getMessage("赤いガラスの部分に表示されます。")
				));
		menuSelecor.addMenu(itemStack2, 13,
				new SelectRunnable() {
			@Override
			public void run(Player p, ItemStack item) {
				StrengthTableOperation.openStrengthTable(p);
			}
		});

		ItemStack itemStack3 = new ItemStack(Material.ANVIL);
		ItemStackUtil.setDispName(itemStack3, ChatColor.WHITE + "" + ChatColor.BOLD + Message.getMessage("修理"));
		ItemStackUtil.setLore(itemStack3, Arrays.asList(Message.getMessage("アイテムの修理を行います。")));
		menuSelecor.addMenu(itemStack3, 11,
				new SelectRunnable() {
			@Override
			public void run(Player p, ItemStack item) {
				RepairUi.onOpenUi(p);
			}
		});

		MenuSelectorManager.regist(menuSelecor);
	}

}
