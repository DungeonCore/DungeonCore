package lbn.item.strength;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.dungeoncore.Main;
import lbn.item.ItemManager;
import lbn.item.itemInterface.Strengthenable;
import lbn.money.galion.GalionManager;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

public class StrengthLaterRunnable extends BukkitRunnable{
	 public StrengthLaterRunnable(CraftingInventory top, InventoryClickEvent e) {
		this.top = top;
		this.e = e;
	}

	CraftingInventory top;
	 InventoryClickEvent e;

	@Override
	public void run() {
		ItemStack item1 = top.getItem(4);
		ItemStack item2 = top.getItem(6);

		ItemStack strengthItem = null;
		if (StrengthOperator.allowWithStrength(item1) && StrengthOperator.canStrength(item2))  {
			//item2を強化するとき
			strengthItem = item2;
		} else if (StrengthOperator.allowWithStrength(item2)&& StrengthOperator.canStrength(item1))  {
			//item1を強化するとき
			strengthItem = item1;
		}

		//強化対象のアイテム以外がセットされている場合は何もしない
		if (strengthItem == null) {
			top.setItem(5, StrengthTableOperation.redGlass);
			return;
		}
		int nowLevel = StrengthOperator.getLevel(strengthItem);

		//完成形のアイテム
		ItemStack item = StrengthOperator.getItem(getCloneItem(strengthItem), nowLevel + 1);

		//完成形のアイテムが存在しないなら何もしない
		if (item == null) {
			return;
		}
		//個数は必ず1
		item.setAmount(1);

		Strengthenable customItem = ItemManager.getCustomItem(Strengthenable.class, item);
		if (customItem == null) {
			//起こりえないが念のため
			return;
		}

		StrengthTemplate template = customItem.getStrengthTemplate();
		//最大レベルを上回っているかどうか確認
		if (nowLevel + 1 > customItem.getMaxStrengthCount()) {
			top.setItem(5, getMaxLevelRedGlass());
			return;
		}

		//強化素材のチェック
		MaterialCheck materialCheck = new MaterialCheck((Player) e.getWhoClicked(), template, nowLevel + 1);

		ItemStack updateRedGlass = getUpdateRedGlass(template, StrengthOperator.getLevel(item), materialCheck);
		top.setItem(5, updateRedGlass);

		//材料が足りない時は強化できない
		if (!materialCheck.canStrength()) {
			return;
		}

		PlayerSetStrengthItemResultEvent event = new PlayerSetStrengthItemResultEvent((Player) e.getWhoClicked(), item, nowLevel + 1);
		Bukkit.getServer().getPluginManager().callEvent(event);

		//アイテムをセットする
		top.setResult(event.getItem());

		//メタデータをいれておく
		ItemStack clone = strengthItem.clone();
		clone.setAmount(1);
		e.getWhoClicked().setMetadata("material_strength_item", new FixedMetadataValue(Main.plugin, clone));
		e.getWhoClicked().setMetadata("next_strength_level", new FixedMetadataValue(Main.plugin, nowLevel + 1));
		//完成形を表示させる
		new BukkitRunnable() {
			@Override
			public void run() {
				((Player)e.getWhoClicked()).updateInventory();
			}
		}.runTaskLater(Main.plugin, 2);
	}

	protected ItemStack getCloneItem(ItemStack strengthItem) {
		ItemStack clone = strengthItem.clone();

		if (strengthItem.getType() == Material.BOW || ItemStackUtil.isSword(strengthItem)) {
			clone.setDurability((short) 0);
		} else {
			clone.setDurability(strengthItem.getDurability());
		}
		return clone;
	}

	protected ItemStack getUpdateRedGlass(StrengthTemplate template, int nextLevel, MaterialCheck materialCheck) {
		ItemStack clone = StrengthTableOperation.redGlass.clone();

		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "・success");
		lore.add(ChatColor.GREEN.toString() + "   - " + template.successChance(nextLevel) + "%");

		if (materialCheck.getNeedItems().size() != 0) {
			lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "・need item");
			for (String item : materialCheck.getNeedItems()) {
				lore.add(item);
			}
		}
		lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "・need money");
		lore.add(materialCheck.needMoney);

		lore.add("");
		lore.add(Message.getMessage(ChatColor.GRAY + "[You have {0} Galions]", materialCheck.nowMoney));
		ItemStackUtil.setLore(clone, lore);

		return clone;
	}

	protected ItemStack getMaxLevelRedGlass() {
		ItemStack clone = StrengthTableOperation.redGlass.clone();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RED.toString() + ChatColor.BOLD +"これ以上強化できません");
		ItemStackUtil.setLore(clone, lore);
		return clone;
	}
}

class MaterialCheck {
	StrengthTemplate template;

	boolean canStrength = true;

	ArrayList<String> needItems = new ArrayList<String>();

	String needMoney = "";

	int nowMoney = 0;

	public MaterialCheck(Player p, StrengthTemplate template, int level) {
		this.template = template;

		ItemStack[] strengthMaterials = template.getStrengthMaterials(level);

		if (template.getStrengthMaterials(level) != null) {
			for (ItemStack itemStack : strengthMaterials) {
				if (p.getInventory().containsAtLeast(itemStack, itemStack.getAmount())) {
					needItems.add(getNeedItem(ChatColor.GREEN, itemStack));
				} else {
					canStrength = false;
					needItems.add(getNeedItem(ChatColor.RED, itemStack));
				}
			}
		}

		int strengthGalions = template.getStrengthGalions(level);
		ChatColor color;
		if (GalionManager.getGalion(p) >= strengthGalions) {
			color = ChatColor.GREEN;
		} else {
			canStrength = false;
			color = ChatColor.RED;
		}
		needMoney = Message.getMessage(p, "{0}   - {1} Galions", color, strengthGalions);

		nowMoney = GalionManager.getGalion(p);
	}

	public boolean canStrength() {
		return canStrength;
	}

	public ArrayList<String> getNeedItems() {
		return needItems;
	}

	private String getNeedItem(ChatColor c, ItemStack item) {
		if (ItemStackUtil.getName(item).isEmpty()) {
			return c + "   - " + ChatColor.stripColor(item.getType() + "  ×" + item.getAmount());
		} else {
			return c + "   - " + ChatColor.stripColor(ItemStackUtil.getName(item) + "  ×" + item.getAmount());
		}
	}
}
