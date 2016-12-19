package main.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import main.item.slot.SlotInterface;
import main.util.ItemStackUtil;
import main.util.JavaUtil;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class ItemManager {
	static HashMap<String, ItemInterface> allItemNameList = new HashMap<>();
	static HashMap<String, ItemInterface> allItemIdList = new HashMap<>();

	static HashMap<Class<?>, HashMap<String, ItemInterface>> allItemNameClassList = new HashMap<>();

	static HashMap<Class<?>, HashMap<String, ItemInterface>> allItemIdClassList = new HashMap<>();

	static {
		registItem(new GalionItem(0));
	}

	public static void registItem(ItemInterface[] item) {
		for (ItemInterface itemInterface : item) {
			registItem(itemInterface);
		}
	}
	/**
	 * itemを登録する
	 * @param item
	 */
	public static void registItem(ItemInterface item) {
		Set<Class<?>> interfaces = JavaUtil.getInterface(item.getClass());
		for (Class<?> clazz : interfaces) {
			if (clazz.isAssignableFrom(ItemInterface.class) || !clazz.equals(ItemInterface.class)) {
				registItem(clazz, item);
			}
		}
		//全てのアイテムを登録する
		allItemNameList.put(ChatColor.stripColor(item.getItemName()).toUpperCase(), item);
		allItemIdList.put(ChatColor.stripColor(item.getId()).toUpperCase(), item);

		if (item instanceof SlotInterface) {
			SlotManager.registSlot((SlotInterface) item);
		}
	}

	private static void registItem(Class<?> clazz, ItemInterface item) {
		if (!allItemNameClassList.containsKey(clazz)) {
			allItemNameClassList.put(clazz, new HashMap<String, ItemInterface>());
		}
		allItemNameClassList.get(clazz).put(ChatColor.stripColor(item.getItemName()).toUpperCase(), item);

		if (!allItemIdClassList.containsKey(clazz)) {
			allItemIdClassList.put(clazz, new HashMap<String, ItemInterface>());
		}
		allItemIdClassList.get(clazz).put(item.getId(), item);
	}

	/**
	 * 全てのアイテムを取得する
	 * @return
	 */
	public static List<ItemInterface> getAllItem() {
		return new ArrayList<ItemInterface>(allItemNameList.values());
	}

	/**
	 * 全てのアイテム名を取得する
	 * @return
	 */
	public static Set<String> getAllItemName() {
		return allItemNameList.keySet();
	}

	/**
	 * clazz, itemに対応したアイテムを取得する
	 * @param clazz
	 * @param item
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ItemInterface> T getCustomItem(Class<T> clazz, ItemStack item) {
		if (item == null) {
			return null;
		}

		//IDが存在するとき
		String id = ItemStackUtil.getId(item);
		if (id != null && !id.isEmpty()) {
			HashMap<String, T> hashMap = (HashMap<String, T>) allItemIdClassList.get(clazz);
			T t = hashMap.get(id);
			if (t != null) {
				return t;
			}
		}

		//名前から取得
		String name = ItemStackUtil.getName(item);
		if (name == null || name.isEmpty()) {
			return null;
		}

		//ヘルスクリスタルだけ別処理
		if (!name.contains("CRYSTAL")) {
			return null;
		}

		name = ChatColor.stripColor(name).toUpperCase();
		//interfaceが登録されていない場合は無視する
		if (!allItemNameClassList.containsKey(clazz)) {
			return null;
		}

		HashMap<String, T> hashMap2 = (HashMap<String, T>) allItemNameClassList.get(clazz);
		//+1などがついてない場合はそのまま返す
		if (hashMap2.containsKey(name)) {
			return hashMap2.get(name);
		}
		//+1などが付いている場合
		if (name.contains("+")) {
			//+1などを取り除く
			name = name.substring(0, name.indexOf("+")).trim();
			return hashMap2.get(name);
		}
		return null;
	}

	public static ItemInterface getCustomItem(ItemStack item) {
		if (item == null) {
			return null;
		}
		//IDから取るのでコメントアウト
		String id = ItemStackUtil.getId(item);
		if (id == null) {
			//IDがない場合は名前から取る
			String name = ItemStackUtil.getName(item);
			return getCustomItemByName(name);
		} else {
			return getCustomItemById(id);
		}
	}

	public static ItemInterface getCustomItemById(String id) {
		id = ChatColor.stripColor(id).toUpperCase();
		return allItemIdList.get(id);
	}

	public static ItemInterface getCustomItemByName(String name) {
		if (name.isEmpty()) {
			return null;
		}

		name = ChatColor.stripColor(name).toUpperCase();
		//+1などがついてない場合はそのまま返す
		if (allItemNameList.containsKey(name)) {
			return allItemNameList.get(name);
		}

		//+1などが付いている場合
		if (name.contains("+")) {
			//+1などを取り除く
			name = name.substring(0, name.indexOf("+")).trim();
			return allItemNameList.get(name);
		}
		return null;
	}

	public static void registItem(Collection<ItemInterface> itemList) {
		registItem(itemList.toArray(new ItemInterface[0]));
	}
}
