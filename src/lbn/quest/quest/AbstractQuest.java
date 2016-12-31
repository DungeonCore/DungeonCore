package lbn.quest.quest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.mob.mob.abstractmob.villager.AbstractVillager;
import lbn.money.galion.GalionEditReason;
import lbn.money.galion.GalionManager;
import lbn.player.status.AbstractNormalStatusManager;
import lbn.player.status.StatusAddReason;
import lbn.player.status.bowStatus.BowStatusManager;
import lbn.player.status.magicStatus.MagicStatusManager;
import lbn.player.status.swordStatus.SwordStatusManager;
import lbn.quest.Quest;
import lbn.quest.QuestManager;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractQuest implements Quest{
	protected AbstractQuest(String id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	abstract protected void init();

	public String villagerName;

	@Override
	public void onSatisfyComplateCondtion(Player p) {
		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);
		if (villagerName != null) {

		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Quest) {
			return ((Quest) obj).getId().equals(getId());
		}
		return false;
	}

	@Override
	public String toString() {
		return getName() + "(" + getId() + ")";
	}

	@Override
	public void playCompleteSound(Player p) {
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, (float) 0.1);
	}

	@Override
	public void playDistructionSound(Player p) {
		p.playSound(p.getLocation(), Sound.VILLAGER_DEATH, 1, (float) 0.1);
	}

	@Override
	public void playStartSound(Player p) {
		p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 0.1);
	}

	boolean canDestory = true;

	@Override
	public boolean canDestory() {
		return !isMainQuest() && canDestory;
	}

	/**
	 * クエスト進展メッセージ
	 * @param player
	 */
	protected void sendProgressMessage(Player player, int needCount, int nowCount) {
		player.sendMessage(StringUtils.join(new Object[]{ChatColor.GOLD, "[Quest]", ChatColor.GREEN, getName(), "  (", nowCount, "/", needCount, ")"}));
	}

	/**
	 * クエストクリアメッセージ
	 * @param player
	 */
	protected void sendQuestComplateMessage(Player player) {
		Message.sendMessage(player, StringUtils.join(new Object[]{ChatColor.GREEN, ChatColor.BOLD + Message.getMessage(player, "クエスト達成！！ 村人のところに戻ろう")}));
	}

	String id;

	@Override
	public String getId() {
		return id;
	}

	String name;

	@Override
	public String getName() {
		return name;
	}

	String detail;
	@Override
	public String getQuestDetail() {
		return detail;
	}

	boolean isSucessBeforeQuest = false;
	String[] beforeQuestIds = null;
	Set<Quest> beforeQuests = null;
	@Override
	public Set<Quest> getBeforeQuest() {
		if (isSucessBeforeQuest) {
			return beforeQuests;
		}

		//もし前提クエストがない場合は空を返す
		if (beforeQuestIds == null) {
			beforeQuests = new HashSet<Quest>();
			isSucessBeforeQuest = true;
			return beforeQuests;
		}

		//前提クエストを空にする
		beforeQuests = new HashSet<Quest>();

		boolean existNullQuest = false;
		//IDからクエストを取得
		for (String id : beforeQuestIds) {
			//クエストが存在しない場合はNullQuestとして処理
			Quest questById = QuestManager.getQuestById(id);
			if (questById == null) {
				existNullQuest = true;
				questById = new NullQuest(id);
			}
			beforeQuests.add(questById);
		}

		//もしnullQuestが存在しない場合はクエスト取得成功にする
		if (!existNullQuest) {
			isSucessBeforeQuest = true;
		}
		return beforeQuests;
	}

	boolean isMain = false;
	@Override
	public boolean isMainQuest() {
		return isMain;
	}

	boolean isStartOverlap = false;
	@Override
	public boolean isStartOverlap() {
		return isStartOverlap;
	}

	String[] talk1 = null;

	@Override
	public String[] getTalk1() {
		return talk1;
	}

	String[] talk2 = null;
	@Override
	public String[] getTalk2() {
		return talk2;
	}

	Set<String> permiseQuest = null;

	String autoExecuteNextQuestId = null;
	Quest autoExecuteNextQuest = null;

	@Override
	public Quest getAutoExecuteNextQuest() {
		//次のクエストIDが存在しないならnullを返す
		if (autoExecuteNextQuestId == null) {
			return null;
		}

		//次のクエストIDが見つかっていないなら再検する
		if (autoExecuteNextQuest != null) {
			Quest questIfExist = getQuestIfExist(autoExecuteNextQuestId);
			//見つからなかったらnullQuestを返す
			if (questIfExist == null || questIfExist.isNullQuest()) {
				return questIfExist;
			}
			//見つかったならsetする
			autoExecuteNextQuest = questIfExist;
		}
		return autoExecuteNextQuest;
	}

	/**
	 * クエストがもし存在すればTRUE
	 * @param questId
	 * @return
	 */
	public Quest getQuestIfExist(String questId) {
		if (questId == null) {
			return null;
		}
		Quest questById = QuestManager.getQuestById(questId);
		if (questById != null) {
			return questById;
		}
		return new NullQuest(questId);
	}

	boolean isShowTitle = true;
	@Override
	public boolean isShowTitle() {
		return isShowTitle;
	}

	long coolTimeSecound = -1;
	@Override
	public long getCoolTimeSecound() {
		return coolTimeSecound;
	}

	@Override
	public boolean canFinish(Player p) {
		if (!p.isOnline()) {
			return false;
		}

		if (getRewordItem() == null) {
			return true;
		}

		ItemStack item = getRewordItem().getItem();
		//空きがあるならTRUE
		if (p.getInventory().firstEmpty() != -1) {
			Message.sendMessage(p, "インベントリに空きが無いためクエストを完了できません。");
			return true;
		}

		//最大スタック数が1の場合はこの時点でFALSE
		int maxStackSize = item.getMaxStackSize();
		if (maxStackSize != 1) {
			Message.sendMessage(p, "インベントリに空きが無いためクエストを完了できません。");
			return false;
		}

		//stackした時にアイテムを格納できるか確認する
		Map<Integer, ItemStack> all = ItemStackUtil.allSameItems(p.getInventory(), item);
		for (ItemStack invItem : all.values()) {
			if (invItem.getAmount() + item.getAmount() <= maxStackSize) {
				return true;
			}
		}
		Message.sendMessage(p, "インベントリに空きが無いためクエストを完了できません。");
		return false;
	}

	@Override
	public void giveRewardItem(Player p) {
		if (swordExe != 0) {
			AbstractNormalStatusManager instance = SwordStatusManager.getInstance();
			instance.addExp(p, swordExe, StatusAddReason.quest_reword);
		}
		if (bowExe != 0) {
			AbstractNormalStatusManager instance2 = BowStatusManager.getInstance();
			instance2.addExp(p, bowExe, StatusAddReason.quest_reword);
		}
		if (magicExe != 0) {
			AbstractNormalStatusManager instance3 = MagicStatusManager.getInstance();
			instance3.addExp(p, magicExe, StatusAddReason.quest_reword);
		}

		if (rewordMoney != 0) {
			GalionManager.addGalion(p, rewordMoney, GalionEditReason.quest_reword);
		}

		if (getRewordItem() != null) {
			p.getInventory().addItem(getRewordItem().getItem());
		}
	}

	int swordExe = 0;
	int bowExe = 0;
	int magicExe = 0;
	int rewordMoney = 0;

	String reworldItemId = null;

	public ItemInterface getRewordItem() {
		if (reworldItemId != null) {
			return ItemManager.getCustomItemById(reworldItemId);
		}
		return null;
	}

	@Override
	public boolean isNullQuest() {
		return false;
	}

	int availableMainLevel = 0;

	@Override
	public int getAvailableMainLevel() {
		return availableMainLevel;
	}

	@Override
	public List<String> getRewordText() {
		ArrayList<String> texts = new ArrayList<String>();
		if (swordExe != 0) {
			texts.add("剣レベル: +" + swordExe + "exp");
		}
		if (bowExe != 0) {
			texts.add("弓レベル: +" + bowExe + "exp");
		}
		if (magicExe != 0) {
			texts.add("魔法レベル: +" + magicExe + "exp");
		}

		if (rewordMoney != 0) {
			texts.add("お金: +" + rewordMoney + "Galions");
		}

		if (getRewordItem() != null) {
			texts.add("アイテム: " + getRewordItem().getItemName());
		}

		if (texts.isEmpty()) {
			texts.add("なし");
		}
		return texts;
	}

}
