package net.l_bulb.dungeoncore.quest;

import java.text.MessageFormat;
import java.util.List;

import net.l_bulb.dungeoncore.quest.questData.PlayerQuestSession;
import net.l_bulb.dungeoncore.quest.questData.PlayerQuestSessionManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuestViewIcon {
  Player p;
  PlayerQuestSession questSession;

  public QuestViewIcon(Player p) {
    this.p = p;
    questSession = PlayerQuestSessionManager.getQuestSession(p);
  }

  static final String INFO = ChatColor.GREEN + "[INFO]";

  public static boolean isQuestIconItem(ItemStack itemStack) {
    if (itemStack.getType() == Material.WRITTEN_BOOK || itemStack.getType() == Material.BOOK) {
      List<String> lore = ItemStackUtil.getLore(itemStack);
      if (lore.contains(ChatColor.BLACK + "quest_viewer_item")) { return true; }
    }
    return false;
  }

  public ItemStack getItemStack(Quest quest) {
    QuestProcessingStatus status = questSession.getProcessingStatus(quest);

    ItemStack itemStack;
    if (quest.isMainQuest()) {
      itemStack = new ItemStack(Material.WRITTEN_BOOK);
      // アイテム名追加
      ItemStackUtil.setDispName(itemStack, MessageFormat.format("{0}{1}{2}[メインクエスト]", ChatColor.GOLD, quest.getName(), ChatColor.RED));
    } else {
      itemStack = new ItemStack(Material.BOOK);
      // アイテム名追加
      ItemStackUtil.setDispName(itemStack, ChatColor.LIGHT_PURPLE + quest.getName());
    }
    // loreを追加
    ItemStackUtil.addLore(itemStack, INFO);
    for (String string : quest.getQuestDetail()) {
      ItemStackUtil.addLore(itemStack, MessageFormat.format(" {0}{1}", ChatColor.WHITE, string));
    }
    ItemStackUtil.addLore(itemStack, "");
    ItemStackUtil.addLore(itemStack, ChatColor.GREEN + "[クリア条件]");
    ItemStackUtil.addLore(itemStack, MessageFormat.format(" {0}{1}", ChatColor.WHITE, quest.getComplateCondition()));
    ItemStackUtil.addLore(itemStack, ChatColor.BLACK + "quest_viewer_item");
    ItemStackUtil.addLore(itemStack, ChatColor.GREEN + "[進行状況]");
    ItemStackUtil.addLore(itemStack, MessageFormat.format(" {0}{1}", ChatColor.WHITE, quest.getCurrentInfo(p)));
    if (status == QuestProcessingStatus.PROCESS_END && quest.getEndVillagerId() != null) {
      ItemStackUtil.addLore(itemStack, MessageFormat.format(" {0}{1}", ChatColor.WHITE, quest.getEndVillagerId() + "のところへ報告へ行こう"));
    }

    // 破棄の許可・不許可を追加
    if (quest.isMainQuest()) {
      ItemStackUtil.addLore(itemStack, ChatColor.DARK_GRAY + "メインクエストは破棄できません。");
    } else if (!quest.canDestory()) {
      ItemStackUtil.addLore(itemStack, ChatColor.DARK_GRAY + "このクエストは破棄できません。");
    } else {
      ItemStackUtil.addLore(itemStack, ChatColor.DARK_GRAY + "破棄する場合はこの本を捨ててください。");
    }
    ChatColor c = p.getGameMode() == GameMode.CREATIVE ? ChatColor.WHITE : ChatColor.BLACK;
    ItemStackUtil.addLore(itemStack, c + ItemStackUtil.getLoreForIdLine(quest.getId()));
    return itemStack;
  }

  public Quest getQuest(ItemStack item) {
    return QuestManager.getQuestById(ItemStackUtil.getId(item));
  }
}
