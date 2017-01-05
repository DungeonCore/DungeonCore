package lbn.item.strength;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.dungeoncore.Main;
import lbn.item.ItemManager;
import lbn.item.itemInterface.Strengthenable;
import lbn.money.galion.GalionEditReason;
import lbn.money.galion.GalionManager;
import lbn.util.ItemStackUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CraeteStrengthItemResultLater extends BukkitRunnable{
	static Random rnd = new Random();

	public CraeteStrengthItemResultLater(InventoryClickEvent e) {
		this.e = e;
	}
	InventoryClickEvent e;

	boolean canStrength = false;

	@Override
	public void run() {
		ItemStack item = e.getCursor();

		if (item.getType() == Material.AIR || item == null) {
			return;
		}

		int nextLevel = StrengthOperator.getLevel(item);
		try {
			nextLevel = e.getWhoClicked().getMetadata("next_strength_level").get(0).asInt();
		} catch (Exception e) {
		}

		//前の強化に使ったアイテムは絶対に強化可能なアイテム
		Strengthenable itemInterface = ItemManager.getCustomItem(Strengthenable.class, (ItemStack)e.getWhoClicked().getMetadata("material_strength_item").get(0).value());
		StrengthTemplate template = itemInterface.getStrengthTemplate();

		Player p = (Player) e.getWhoClicked();

		//強化素材または料金が足りない場合は強化レベルを戻す
		if (!checkMaterial(p, item, template, nextLevel) || !checkGalions(p, item, template, nextLevel)) {
			//素材のアイテムに変更
			changeItem(item, e.getWhoClicked().getMetadata("material_strength_item"));
			StrengthOperator.updateLore(item, nextLevel - 1);
			((Player)e.getWhoClicked()).updateInventory();
			canStrength = true;
			return;
		}

		ItemStack[] strengthMaterials = template.getStrengthMaterials(nextLevel);
		if (strengthMaterials == null) {
			strengthMaterials = new ItemStack[0];
		}

		//強化素材を消費する
		for (ItemStack needItem : strengthMaterials) {
			consumeMaterial(p, needItem);
		}
		//お金を消費する
		GalionManager.addGalion(p, - template.getStrengthGalions(nextLevel), GalionEditReason.consume_strength);

		int chance = template.successChance(nextLevel);

		int nextInt = rnd.nextInt(100);
		boolean isSuccess = (nextInt + 1 <= chance);
		//成功
		if (isSuccess) {
			//成功の時は何もしない
			((Player)e.getWhoClicked()).sendMessage(ChatColor.BLUE + "強化に成功しました");
			((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.ANVIL_USE, 1, 1);
		//失敗
		} else {
			((Player)e.getWhoClicked()).sendMessage(ChatColor.RED + "強化に失敗しました");
			((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.ITEM_BREAK, 1f, 1f);
			changeItem(item, e.getWhoClicked().getMetadata("material_strength_item"));
			StrengthOperator.updateLore(item, 0);
			((Player)e.getWhoClicked()).updateInventory();
		}
		canStrength = true;
		PlayerStrengthFinishEvent playerStrengthItemEvent = new PlayerStrengthFinishEvent((Player)e.getWhoClicked(), chance, nextLevel, item, isSuccess);
		Bukkit.getServer().getPluginManager().callEvent(playerStrengthItemEvent);
	}

	private void changeItem(ItemStack item, List<MetadataValue> metadata) {
		if (metadata.size() == 0) {
			return;
		}

		MetadataValue metadataValue = metadata.get(0);
		ItemStack beforeItem = (ItemStack) metadataValue.value();
		item.setType(beforeItem.getType());
		item.setAmount(beforeItem.getAmount());
		item.setDurability(beforeItem.getDurability());
		item.setData(beforeItem.getData().clone());
		item.setItemMeta(beforeItem.getItemMeta().clone());
	}

	/**
	 * 指定したアイテムをインベントリの中から取り除く
	 * @param p
	 * @param needItem
	 */
	protected void consumeMaterial(Player p, ItemStack needItem) {
		int cost = needItem.getAmount();
		for (Entry<Integer, ? extends ItemStack> entry : p.getInventory().all(needItem.getType()).entrySet()) {
			if (cost <= 0) {
				break;
			}

			ItemStack value = entry.getValue();
			//名前とTypeが同じなら同じアイテムと判断
			if (equal(ItemStackUtil.getName(needItem) , ItemStackUtil.getName(value)) && needItem.getType() == value.getType()) {
				//選択したアイテムの個数がコストより小さい時はそのアイテムは全て削除する
				if (cost > value.getAmount()) {
					p.getInventory().setItem(entry.getKey(), null);
					cost = cost - value.getAmount();
				//選択したアイテムの個数がコストより大きい時はコストの分だけ個数を減らす
				} else {
					ItemStack clone = value.clone();
					clone.setAmount(value.getAmount() - cost);
					p.getInventory().setItem(entry.getKey(), clone);
					cost = 0;
				}
			}
		}
	}

	/**
	 * 強化素材が足りるか調べる
	 * @param item
	 * @param template
	 * @return
	 */
	public boolean checkMaterial(Player p, ItemStack item, StrengthTemplate template, int nextLevel) {
		ItemStack[] strengthMaterials = template.getStrengthMaterials(nextLevel);
		if (strengthMaterials != null) {
			for (ItemStack needItem : template.getStrengthMaterials(nextLevel)) {
				if (!p.getInventory().containsAtLeast(needItem, needItem.getAmount())) {
					p.sendMessage(ChatColor.RED + "強化素材が足りないため、強化できませんでした。");
					return false;
				}
			}
		}
		return true;
	}

	public boolean checkGalions(Player p, ItemStack item, StrengthTemplate template, int nextLevel) {
		int strengthGalions = template.getStrengthGalions(nextLevel);
		boolean rtn = GalionManager.getGalion(p) >= strengthGalions;
		if (!rtn) {
			p.sendMessage(ChatColor.RED + "強化料金が足りないため、強化できませんでした。");
		}
		return rtn;
	}

	public synchronized BukkitTask runTaskLater(Plugin plugin)
			throws IllegalArgumentException, IllegalStateException {
		BukkitTask runTaskLater = super.runTaskLater(plugin, 1);

		new BukkitRunnable() {
			@Override
			public void run() {
				if (!canStrength) {
					return;
				}

				//クラフト欄を初期化する
				if ((CraftingInventory)e.getView().getTopInventory() instanceof CraftingInventory) {
					StrengthTableOperation.setInitInv((CraftingInventory)e.getView().getTopInventory());
					((Player)e.getWhoClicked()).updateInventory();
				}
			}
		}.runTaskLater(Main.plugin, 3);

		return runTaskLater;
	}

	private boolean equal(String a, String b) {
		if (a == null && b == null) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}

		return a.equals(b);
	}
}
