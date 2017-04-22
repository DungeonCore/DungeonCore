package lbn.item.customItem.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.dungeoncore.Main;
import lbn.item.customItem.AbstractItem;
import lbn.item.itemInterface.MoneyItemable;
import lbn.money.GalionEditReason;
import lbn.util.ItemStackUtil;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public final class GalionItem extends AbstractItem implements MoneyItemable {

	/**
	 * Instance cache
	 */
	private static Map<Integer, GalionItem> cache = new WeakHashMap<>();

	/**
	 * Get instance of GalionItem.
	 *
	 * @param galions
	 *            value
	 * @return instance
	 */
	public static GalionItem getInstance(int galions) {
		Integer key = Integer.valueOf(galions);
		if (cache.containsKey(key)) {
			return cache.get(key);
		}
		GalionItem item = new GalionItem(galions);
		cache.put(key, item);
		return item;
	}

	private GalionItem(int galions) {
		this.galions = galions;
	}

	public GalionItem(ItemStack stack) {
		List<String> lore = ItemStackUtil.getLore(stack);
		for (String string : lore) {
			if (string.contains("+") && string.contains("galions")) {
				String replace = string.replace("+", "").replace("galions", "");
				this.galions = Integer.parseInt(ChatColor.stripColor(replace).trim());
			}
		}
	}

	/**
	 * Immutable
	 */
	private int galions = 0;

	@Override
	public String getItemName() {
		return ChatColor.GOLD + "Money";
	}

	@Override
	public String getId() {
		return "galions";
	}

	@Override
	protected Material getMaterial() {
		return Material.GOLD_INGOT;
	}

	@Override
	public String[] getDetail() {
		return new String[] { "+ " + galions + " galions" };
	}

	public int getGalions() {
		return galions;
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return getGalions();
	}

	/**
	 * 金のインゴットを消してお金を加算させる
	 * 
	 * @param player
	 */
	public void applyGalionItem(Player player) {
		if (player.getGameMode() == GameMode.CREATIVE) {
			return;
		}

		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
		if (theLowPlayer == null) {
			return;
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				HashMap<Integer, ? extends ItemStack> all = player.getInventory().all(getMaterial());
				ArrayList<Integer> indexList = new ArrayList<Integer>();
				for (Entry<Integer, ? extends ItemStack> entry : all.entrySet()) {
					if (isThisItem(entry.getValue())) {
						int galions = new GalionItem(entry.getValue()).getGalions();
						indexList.add(entry.getKey());
						theLowPlayer.addGalions(galions * entry.getValue().getAmount(),
								GalionEditReason.get_money_item);
					}
				}
				for (Integer integer : indexList) {
					player.getInventory().clear(integer);
				}
			}
		}.runTaskLater(Main.plugin, 2);
	}

}
