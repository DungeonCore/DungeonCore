package lbn.quest;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lbn.util.ItemStackUtil;

public class QuestViewIcon {
	Player p;

	public QuestViewIcon(Player p) {
		this.p = p;
	}

	static final String INFO = ChatColor.GREEN + "[INFO]";

	public static boolean isQuestIconItem(ItemStack itemStack) {
		 if (itemStack.getType() == Material.WRITTEN_BOOK || itemStack.getType() == Material.BOOK){
			 List<String> lore = ItemStackUtil.getLore(itemStack);
			 if (lore.contains("quest_viewer_item")) {
				 return true;
			 }
		}
		 return false;
	}

	public ItemStack getItemStack(Quest quest) {
		ItemStack itemStack;
		if (quest.isMainQuest()) {
			itemStack = new ItemStack(Material.WRITTEN_BOOK);
			//アイテム名追加
			ItemStackUtil.setDispName(itemStack, ChatColor.GOLD + quest.getName() + ChatColor.RED + "[メインクエスト]");
		} else {
			itemStack = new ItemStack(Material.BOOK);
			//アイテム名追加
			ItemStackUtil.setDispName(itemStack, ChatColor.LIGHT_PURPLE + quest.getName());
		}
		//loreを追加
		String[] split = quest.getQuestDetail().split("。");
		ItemStackUtil.addLore(itemStack, INFO);
		for (String string : split) {
			ItemStackUtil.addLore(itemStack, " " + ChatColor.WHITE + string + "。");
		}
		ItemStackUtil.addLore(itemStack, "");
		ItemStackUtil.addLore(itemStack, ChatColor.GREEN + "[PROGRESS]");
		ItemStackUtil.addLore(itemStack, "" + ChatColor.WHITE + quest.getCurrentInfo(p));
		ItemStackUtil.addLore(itemStack, ChatColor.BLACK + "quest_viewer_item");

		//破棄の許可・不許可を追加
		if (quest.isMainQuest()) {
			ItemStackUtil.addLore(itemStack, ChatColor.DARK_GRAY + "メインクエストは破棄できません。");
		} else if (quest.canDestory()) {
			ItemStackUtil.addLore(itemStack, ChatColor.DARK_GRAY +  "このクエストは破棄できません。");
		} else {
			ItemStackUtil.addLore(itemStack, ChatColor.DARK_GRAY +  "破棄する場合はこの本を捨ててください。");
		}
		ChatColor c = p.getGameMode() == GameMode.CREATIVE ? ChatColor.WHITE : ChatColor.BLACK;
		ItemStackUtil.addLore(itemStack, c + ItemStackUtil.getLoreForIdLine(quest.getId()));
		return itemStack;
	}

	public Quest getQuest(ItemStack item) {
		return QuestManager.getQuestById(ItemStackUtil.getId(item));
	}
}
