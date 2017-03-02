package lbn.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import net.minecraft.server.v1_8_R1.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.Joiner;

public class ItemStackUtil {
	public static final String SOUL_BOUND = "soulbound";

	public static void addSoulBound(ItemStack item) {
		if (item == null) {
			return;
		}
		if (!isSoulBound(item)) {

			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = itemMeta.getLore();

			if (lore == null) {
				lore = new ArrayList<String>();
			}
			lore.add(SOUL_BOUND);
			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);
		}
	}

	public static boolean isSoulBound(ItemStack item) {
		if (item == null) {
			return false;
		}

		ItemMeta itemMeta = item.getItemMeta();

		if (itemMeta == null) {
			return false;
		}

		List<String> lore = itemMeta.getLore();

		if (lore == null) {
			return false;
		}

		for (String str : lore) {
			if (str.contains(SOUL_BOUND)) {
				return true;
			}
		}

		return false;
	}

	public static ItemMeta getItemMeta(ItemStack item) {
		if (item == null) {
			return null;
		}

		ItemMeta itemMeta = item.getItemMeta();
		if (itemMeta == null) {
			return null;
		}

		return itemMeta;
	}

	public static List<String> getLore(ItemStack item) {
		if (item == null) {
			return new ArrayList<>();
		}

		ItemMeta itemMeta = getItemMeta(item);
		if (itemMeta == null) {
			return new ArrayList<>();
		}

		List<String> lore = itemMeta.getLore();
		if (lore == null) {
			return new ArrayList<>();
		}

		return lore;
	}

	public static List<String> getLore(ItemMeta meta) {
		if (meta == null) {
			return new ArrayList<>();
		}

		List<String> lore = meta.getLore();
		if (lore == null) {
			return new ArrayList<>();
		}

		return lore;
	}

	public static void setDispName(ItemStack item, String name) {
		ItemMeta itemMeta = getItemMeta(item);
		if (itemMeta == null) {
			return;
		}

		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
	}

	public static String getName(ItemStack item) {
		ItemMeta itemMeta = getItemMeta(item);
		if (itemMeta == null) {
			return "";
		}

		String name = itemMeta.getDisplayName();
		if (name == null) {
			return "";
		} else {
			return name;
		}
	}

	public static void setLore(ItemStack item, List<String> lore) {
		ItemMeta itemMeta = getItemMeta(item);
		if (itemMeta == null) {
			return;
		}
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
	}

	public static void addLore(ItemStack item, String... lore) {
		ItemMeta itemMeta = getItemMeta(item);
		if (itemMeta == null) {
			return;
		}

		List<String> lore2 = getLore(itemMeta);
		lore2.addAll(Arrays.asList(lore));

		itemMeta.setLore(lore2);

		item.setItemMeta(itemMeta);
	}

	public static boolean isEmpty(ItemStack item) {
		return item == null || item.getType() == Material.AIR;
	}

	/**
	 * unsafeなエンチャントならTRUE
	 * @param itemMeta
	 * @return
	 */
	public static boolean isUnsafeEnchant(EnchantmentStorageMeta itemMeta) {
		Map<Enchantment, Integer> storedEnchants = itemMeta.getStoredEnchants();

		for (Entry<Enchantment, Integer> e : storedEnchants.entrySet()) {
			Enchantment ench = e.getKey();
			if (ench.getMaxLevel() < e.getValue() || ench.getStartLevel() > e.getValue()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSword(ItemStack item) {
		if (item == null) {
			return false;
		}

		Material type = item.getType();
		switch (type) {
		case WOOD_AXE:
		case WOOD_SWORD:
		case STONE_AXE:
		case STONE_SWORD:
		case GOLD_AXE:
		case GOLD_SWORD:
		case IRON_AXE:
		case IRON_SWORD:
		case DIAMOND_AXE:
		case DIAMOND_SWORD:
			return true;
		default:
			return false;
		}
	}

	public static void removeAll(Inventory inv, ItemStack... itemList) {
		for (ItemStack item : itemList) {
			HashMap<Integer, ? extends ItemStack> allItem = inv.all(item.getType());
			for (Entry<Integer, ? extends ItemStack> entry : allItem.entrySet()) {
				if (isThisItem(entry.getValue(), item)) {
					inv.setItem(entry.getKey(), new ItemStack(Material.AIR));
				}
			}
		}
	}

	private static boolean isThisItem(ItemStack value, ItemStack item) {
		ItemInterface customItem = ItemManager.getCustomItem(item);
		if (customItem == null) {
			return getName(item).equals(getName(value)) && value.getType() == item.getType();
		} else {
			return customItem.isThisItem(item);
		}
	}

	public static double getVanillaDamage(Material m) {
		switch (m) {
		case WOOD_AXE:
			return 3;
		case WOOD_SWORD:
			return 4;
		case STONE_AXE:
			return 4;
		case STONE_SWORD:
			return 5;
		case GOLD_AXE:
			return 3;
		case GOLD_SWORD:
			return 4;
		case IRON_AXE:
			return 5;
		case IRON_SWORD:
			return 6;
		case DIAMOND_AXE:
			return 6;
		case DIAMOND_SWORD:
			return 7;
		case BOW:
			return 7.5;
		default:
			return 0;
		}
	}

	public static String getLoreForIdLine(String id) {
		return "id:" + id;
	}

	public static String getId(ItemStack item) {
		List<String> lore = getLore(item);
		for (String string : lore) {
			string = ChatColor.stripColor(string).trim();
			if (string.startsWith("id:")) {
				return string.replace("id:", "");
			}
		}
		return null;
	}

	/**
	 * 名前からアイテムを取得
	 * @param name
	 * @return
	 */
	public static ItemStack getItemStack(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		}
		//最初はMaterialから調べる
		Material material = Material.getMaterial(name.toUpperCase());
		if (material != null) {
			return new ItemStack(material);
		}

		ItemInterface customItemById = ItemManager.getCustomItemById(name);
		if (customItemById != null) {
			return customItemById.getItem();
		}
		return null;

	}

	public static ItemStack getItem(String name, Material m, String... lore) {
		ItemStack item = new ItemStack(m);
		setDispName(item, name);
		setLore(item, Arrays.asList(lore));
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getItem(String name, Material m, byte data, String... lore) {
		ItemStack item = new ItemStack(m);
		item.setDurability(data);
		item.getData().setData(data);
		setDispName(item, name);
		setLore(item, Arrays.asList(lore));
		return item;
	}

	/**
	 * GiveコマンドからItemStackを取得する。エラー内容はコンソールに表示される
	 * @param command
	 * @return
	 */
	public static ItemStack getItemStackByCommand(String command) {
		return getItemStackByCommand(command, Bukkit.getConsoleSender());
	}

	/**
	 * GiveコマンドからItemStackを取得する。エラー内容はコンソールに表示される
	 * @param command
	 * @param sender
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getItemStackByCommand(String command, CommandSender sender) {
		command = command.trim();
		if (command.startsWith("/give ") || command.startsWith("give ")) {
			command = command.substring(command.indexOf(" ") + 1);
		}
		String[] args = command.split(" ");

		Material material = Material.matchMaterial(args[1]);
		if (material == null) {
			material = Bukkit.getUnsafe().getMaterialFromInternalName(args[1]);
		}
		if (material != null) {
			//個数は絶対に１つ
			int amount = 1;
			short data = 0;
			if (args.length >= 3) {
				if (args.length >= 4) {
					try {
						data = Short.parseShort(args[3]);
					} catch (NumberFormatException localNumberFormatException) {
					}
				}
			}
			ItemStack stack = new ItemStack(material, amount, data);
			if (args.length >= 5) {
				try {
					stack = Bukkit.getUnsafe().modifyItemStack(stack, Joiner.on(' ').join(Arrays.asList(args).subList(4, args.length)));
				} catch (Throwable t) {
					sender.sendMessage("コマンド解析中にエラーが発生しました。");
					t.printStackTrace();
					return null;
				}
			}
			return stack;
		}
		sender.sendMessage("materialが不正です。");
		return null;
	}

	/**
	 * アイテムの個数を1つ減少させる
	 * @param item
	 * @return
	 */
	public static ItemStack getDecremented(ItemStack item) {
		if (item == null) {
			return new ItemStack(Material.AIR);
		}

		if (item.getAmount() == 1) {
			return new ItemStack(Material.AIR);
		} else if (item.getAmount() > 1){
			item.setAmount(item.getAmount() - 1);
			return item;
		} else {
			return new ItemStack(Material.AIR);
		}
	}

	/**
	 * インベントリから同じアイテムのものを取得する
	 * @param inv
	 * @param item
	 * @return
	 */
	public static Map<Integer, ItemStack> allSameItems(Inventory inv, ItemStack item) {
		HashMap<Integer, ItemStack> slots = new HashMap<>();
		if (item != null) {
			ItemStack[] inventory = inv.getContents();
			for (int i = 0; i < inventory.length; i++) {
				if (item.isSimilar(inventory[i])) {
					slots.put(i, inventory[i]);
				}
			}
		}
		return slots;
	}

	public static boolean canGiveItem(Player p, ItemStack item) {
		if (!p.isOnline()) {
			return false;
		}

		//インベントリに１つ以上の空きがあるならTRUE
		if (p.getInventory().firstEmpty() != -1) {
			return true;
		}

		//最大スタック数が1の場合はこの時点でFALSE
		int maxStackSize = item.getMaxStackSize();
		if (maxStackSize != 1) {
			return false;
		}

		//stackした時にアイテムを格納できるか確認する
		Map<Integer, ItemStack> all = ItemStackUtil.allSameItems(p.getInventory(), item);
		for (ItemStack invItem : all.values()) {
			if (invItem.getAmount() + item.getAmount() <= maxStackSize) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 手に持っているアイテムを1つ消費する
	 * @param player
	 */
	public static void consumeItemInHand(Player player) {
		ItemStack itemInHand = player.getItemInHand();
		if (itemInHand == null) {
			return;
		}
		//消費させる
		if (player.getItemInHand().getAmount() <= 1) {
			player.getInventory().clear(player.getInventory().getHeldItemSlot());
		} else {
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
		}
	}

	/**
	 * NTBTagをセットする
	 * @param item
	 * @param name
	 * @param value
	 */
	public static void setNBTTag(ItemStack item, String name, String value) {
		net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		if (nmsStack.getTag() == null) {
			nmsStack.setTag(new NBTTagCompound());
		}
		nmsStack.getTag().setString(name, value);
		item.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
	}

	/**
	 * NTBTagを取得する
	 * @param item
	 * @param name
	 */
	public static String getNBTTag(ItemStack item, String name) {
		net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		if (nmsStack.getTag() == null) {
			return "";
		}
		return nmsStack.getTag().getString(name);
	}

	/**
	 * NTBTagをセットする
	 * @param item
	 * @param name
	 * @param value
	 */
	public static void setNBTTag(ItemStack item, String name, short value) {
		net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		if (nmsStack.getTag() == null) {
			nmsStack.setTag(new NBTTagCompound());
		}
		nmsStack.getTag().setShort(name, value);
		item.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
	}

	/**
	 * NTBTagを取得する
	 * @param item
	 * @param name
	 */
	public static short getNBTTagShort(ItemStack item, String name) {
		net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		if (nmsStack.getTag() == null) {
			return 0;
		}
		return nmsStack.getTag().getShort(name);
	}
}
