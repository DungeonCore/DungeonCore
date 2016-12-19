package lbn.command;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lbn.common.menu.MenuSelecor;
import lbn.common.menu.SelectRunnable;
import lbn.dungeon.contents.item.key.KeyItemable;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.itemAbstract.BowItem;
import lbn.item.itemAbstract.MagicItem;
import lbn.item.itemAbstract.SwordItem;
import lbn.item.itemInterface.ArmorItemable;
import lbn.item.itemInterface.AvailableLevelItemable;
import lbn.item.setItem.SetItemParts;
import lbn.item.slot.SlotInterface;
import lbn.util.DungeonLog;
import lbn.util.ItemStackUtil;

public class CommandGiveItem implements CommandExecutor{

	public static boolean initFlg = false;

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		if (paramCommandSender instanceof Player && paramArrayOfString.length == 0) {
			if (count != ItemManager.getAllItem().size()) {
				init();
			} else if (initFlg) {
				init();
			}
			MenuSelecor.open((Player) paramCommandSender, "item nemu");
		} else {
			giveItem(paramCommandSender, paramArrayOfString);
		}
		return true;

	}

	//使わないので一旦コメントアウト
//	private void openInv(Player paramCommandSender) {
//		List<ItemInterface> allItem = ItemManager.getAllItem();
//
//		Collections.sort(allItem, new Comparator<ItemInterface>() {
//			@Override
//			public int compare(ItemInterface o1, ItemInterface o2) {
//				return Double.compare(getOrder(o1), getOrder(o2));
//			}
//			HashMap<ItemInterface, Double> cache = new HashMap<ItemInterface, Double>();
//
//			double getOrder(ItemInterface item) {
//				if (cache.containsKey(item)) {
//					return cache.get(item);
//				}
//
//				double rtn;
//				if (item instanceof SwordItem) {
//					rtn =  1;
//				} else if (item instanceof BowItem) {
//					rtn =  2;
//				} else if (item instanceof MagicItem) {
//					rtn =  3;
//				} else if (item instanceof KeyItemable) {
//					rtn =  4;
//				} else if (item instanceof SetItemParts) {
//					rtn =  5;
//				} else if (item instanceof ArmorItemable) {
//					rtn =  7;
//				} else if (item instanceof SlotInterface) {
//					rtn =  8;
//				} else if (item.isQuestItem()) {
//					rtn =  99;
//				} else {
//					rtn =  1000;
//				}
//				cache.put(item, rtn);
//				return rtn;
//			}
//		});
//
//
//
//		Inventory createInventory = Bukkit.createInventory(null, (int)((allItem.size() / 9) + 1) * 9);
//		for (ItemInterface iItem : allItem) {
//			createInventory.addItem(iItem.getItem());
//		}
//		paramCommandSender.openInventory(createInventory);
//	}

	private void giveItem(CommandSender sender, String[] paramArrayOfString) {
		if (paramArrayOfString.length == 0) {
			return;
		}

		Player target = null;
		if (sender instanceof Player) {
			target = (Player) sender;
		}

		String itemId = "";

		Player playerExact = Bukkit.getPlayerExact(paramArrayOfString[0]);

		int startIndex = 0;
		if (playerExact != null) {
			target = playerExact;
			startIndex = 1;
		}

		for (int i = startIndex; i < paramArrayOfString.length; i++) {
			itemId += paramArrayOfString[i] + " ";
		}
		itemId = itemId.trim();

		ItemInterface customItemById = ItemManager.getCustomItemById(itemId);
		if (customItemById != null && target != null) {
			target.getInventory().addItem(customItemById.getItem());
		}
	}

	static int count = 0;
	static {
		init();
	}

	protected static void init() {
		DungeonLog.printDevelopln("item list init!!");
		MenuSelecor menuSelecor = new MenuSelecor("item nemu");
		HashMap<Integer, TreeSet<ItemInterface>> allItem = new HashMap<Integer, TreeSet<ItemInterface>>();

		count = 0;
		//アイテムをグループごとに分類分けする
		for (ItemInterface item : ItemManager.getAllItem()) {
			if (!item.isDispList()) {
				continue;
			}
			int rtn;
			if (item instanceof AbstractAttackItem) {
				rtn =  1;
			} else if (item instanceof SetItemParts) {
				rtn =  2;
			} else if (item instanceof ArmorItemable) {
				rtn =  2;
			} else if (item instanceof SlotInterface) {
				rtn =  8;
			} else if (item instanceof KeyItemable) {
				rtn =  99;
			} else if (item.isQuestItem()) {
				rtn =  99;
			} else {
				rtn =  1000;
			}
			if (!allItem.containsKey(rtn)) {
				TreeSet<ItemInterface> treeSet = getTreeSet();
				allItem.put(rtn, treeSet);
			}

			TreeSet<ItemInterface> treeSet = allItem.get(rtn);
			treeSet.add(item);
			count++;
		}


		ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
		ItemStackUtil.setDispName(itemStack, "武器");
		menuSelecor.addMenu(itemStack, 9, getRun(allItem.get(1)));

		ItemStack itemStack2 = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStackUtil.setDispName(itemStack2, "装備");
		menuSelecor.addMenu(itemStack2, 11, getRun(allItem.get(2)));

		ItemStack itemStack3 = new ItemStack(Material.INK_SACK);
		itemStack3.setDurability((short) 10);
		ItemStackUtil.setDispName(itemStack3, "魔法石");
		menuSelecor.addMenu(itemStack3, 13, getRun(allItem.get(8)));

		ItemStack itemStack4 = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemStackUtil.setDispName(itemStack4, "鍵・クエストアイテム");
		menuSelecor.addMenu(itemStack4, 15, getRun(allItem.get(99)));

		ItemStack itemStack5 = new ItemStack(Material.FEATHER);
		ItemStackUtil.setDispName(itemStack5, "その他");
		menuSelecor.addMenu(itemStack5, 17, getRun(allItem.get(1000)));
		MenuSelecor.regist(menuSelecor);

		initFlg = false;
	}

	protected static TreeSet<ItemInterface> getTreeSet() {
		TreeSet<ItemInterface> treeSet = new TreeSet<ItemInterface>(new Comparator<ItemInterface>() {
			@Override
			public int compare(ItemInterface o1, ItemInterface o2) {
				//まずはアイテムの種類ごと
				double order1 = getOrder(o1);
				double order2 = getOrder(o2);
				if (order1 != order2) {
					return Double.compare(order1, order2);
				}

				//アイテムのレベルごと
				int level1 = - 1;
				int level2 = - 1;
				if (o1 instanceof AvailableLevelItemable) {
					level1 = ((AvailableLevelItemable) o1).getAvailableLevel();
				}
				if (o2 instanceof AvailableLevelItemable) {
					level2 = ((AvailableLevelItemable) o2).getAvailableLevel();
				}
				if (level1 != level2) {
					return Integer.compare(level1, level2);
				}

				//魔法石の場合は特別
				if (o1 instanceof SlotInterface && o2 instanceof SlotInterface) {
					if (((SlotInterface)o1).getSlotType() != ((SlotInterface)o2).getSlotType()) {
						return ((SlotInterface)o1).getSlotType().compareTo(((SlotInterface)o2).getSlotType());
					}

					if (((SlotInterface)o1).getLevel().getSucessPer() != ((SlotInterface)o2).getLevel().getSucessPer()) {
						return Double.compare(((SlotInterface)o2).getLevel().getSucessPer(), ((SlotInterface)o1).getLevel().getSucessPer());
					}
				}

				return o1.getId().compareTo(o2.getId());
			}
			HashMap<ItemInterface, Double> cache = new HashMap<ItemInterface, Double>();

			double getOrder(ItemInterface item) {
				if (cache.containsKey(item)) {
					return cache.get(item);
				}

				double rtn;
				if (item instanceof SwordItem) {
					rtn =  1;
				} else if (item instanceof BowItem) {
					rtn =  2;
				} else if (item instanceof MagicItem) {
					rtn =  3;
				} else if (item instanceof KeyItemable) {
					rtn =  4;
				} else if (item instanceof SetItemParts) {
					rtn =  5;
				} else if (item instanceof ArmorItemable) {
					rtn =  7;
				} else if (item instanceof SlotInterface) {
					rtn =  8;
				} else if (item.isQuestItem()) {
					rtn =  99;
				} else {
					rtn =  1000;
				}
				cache.put(item, rtn);
				return rtn;
			}
	});
		return treeSet;
	}

	protected static SelectRunnable getRun(Set<ItemInterface> items) {
		return new SelectRunnable() {
			@Override
			public void run(Player p) {
				Inventory createInventory = Bukkit.createInventory(null, (int)(((items.size())/ 9) + 2) * 9, "item list");
				for (ItemInterface iItem : items) {
					createInventory.addItem(iItem.getItem());
				}
				createInventory.setItem(createInventory.getSize() - 1, getBackItem());
				p.openInventory(createInventory);
			}
		};
	}

	protected static ItemStack getBackItem() {
		return ItemStackUtil.getItem("メニューに戻る", Material.CHEST);
	}

	public static void onClick(InventoryInteractEvent e) {
		Inventory inventory = e.getInventory();
		String name = inventory.getName();
		if (!"item list".equals(name)) {
			return;
		}

		if (e instanceof InventoryClickEvent) {
			if (getBackItem().equals(((InventoryClickEvent) e).getCurrentItem())) {
				e.setCancelled(true);
				Player p = (Player) e.getWhoClicked();
				MenuSelecor.open(p, "item nemu");
			}
		} else if (e instanceof InventoryDragEvent) {
			if (getBackItem().equals(((InventoryDragEvent) e).getOldCursor()) || getBackItem().equals(((InventoryDragEvent) e).getCursor())) {
				e.setCancelled(true);
				Player p = (Player) e.getWhoClicked();
				MenuSelecor.open(p, "item nemu");
			}

		}

	}
}
