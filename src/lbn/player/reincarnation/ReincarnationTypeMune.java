package lbn.player.reincarnation;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.menu.MenuSelectorInterface;
import lbn.common.menu.MenuSelectorManager;
import lbn.util.ItemStackUtil;

public class ReincarnationTypeMune implements MenuSelectorInterface {
	static {
		MenuSelectorManager.regist(new ReincarnationTypeMune());
	}

	// 転生出来るときの表示用アイテム
	static ItemStack swordSelectItem = ItemStackUtil.getItem(ChatColor.AQUA + "剣レベルをリンカーする", Material.DIAMOND_SWORD,
			ChatColor.WHITE + "現在の剣レベルから60レベル消費し、", ChatColor.WHITE + "特殊効果を得ます。");
	static ItemStack magicSelectItem = ItemStackUtil.getItem(ChatColor.AQUA + "魔法レベルをリンカーする", Material.DIAMOND_HOE,
			ChatColor.WHITE + "現在の魔法レベルから60レベル消費し、", ChatColor.WHITE + "特殊効果を得ます。");
	static ItemStack bowSelectItem = ItemStackUtil.getItem(ChatColor.AQUA + "弓レベルをリンカーする", Material.BOW,
			ChatColor.WHITE + "現在の弓レベルから60レベル消費し、", ChatColor.WHITE + "特殊効果を得ます。");

	// 転生出来ないときの表示用アイテム
	static ItemStack swordCantSelectItem = ItemStackUtil.getItem(ChatColor.RED + "剣レベルリンカー不可", Material.COBBLESTONE,
			ChatColor.WHITE + "条件に達していないため転生できません");
	static ItemStack magicCantSelectItem = ItemStackUtil.getItem(ChatColor.RED + "魔法レベルリンカー不可", Material.COBBLESTONE,
			ChatColor.WHITE + "条件に達していないため転生できません");
	static ItemStack bowCantSelectItem = ItemStackUtil.getItem(ChatColor.RED + "弓レベルリンカー不可", Material.COBBLESTONE,
			ChatColor.WHITE + "条件に達していないため転生できません");

	@Override
	public void open(Player p) {
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
		// データがロードされていない時は開かない
		if (theLowPlayer == null) {
			TheLowPlayerManager.sendLoingingMessage(p);
			return;
		}
		// 表示用のアイテムを設置する
		Inventory createInventory = Bukkit.createInventory(null, 9 * 3, getTitle());
		// 転生できるかどうかで設置するアイテムを変える
		createInventory.setItem(11,
				theLowPlayer.canReincarnation(LevelType.SWORD) ? swordSelectItem : swordCantSelectItem);
		createInventory.setItem(13,
				theLowPlayer.canReincarnation(LevelType.MAGIC) ? magicSelectItem : magicCantSelectItem);
		createInventory.setItem(15, theLowPlayer.canReincarnation(LevelType.BOW) ? bowSelectItem : bowCantSelectItem);
		p.openInventory(createInventory);
	}

	@Override
	public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
		// データがロードされていない時は閉じる
		if (theLowPlayer == null) {
			p.closeInventory();
			TheLowPlayerManager.sendLoingingMessage(p);
			return;
		}

		// どのレベルで転生を行うか
		LevelType levelType = null;
		if (swordSelectItem.equals(item)) {
			levelType = LevelType.SWORD;
		} else if (magicSelectItem.equals(item)) {
			levelType = LevelType.MAGIC;
		} else if (bowSelectItem.equals(item)) {
			levelType = LevelType.BOW;
		}
		// 転生できるなら転生メニューを表示
		if (levelType != null) {
			new ReincarnationSelector(levelType).open(p);
		}
	}

	@Override
	public String getTitle() {
		return "reinc type nemu";
	}

}
