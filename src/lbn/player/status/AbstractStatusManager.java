package lbn.player.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lbn.common.event.player.PlayerChangeStatusExpEvent;
import lbn.common.event.player.PlayerChangeStatusLevelEvent;
import lbn.util.ItemStackUtil;


public abstract class AbstractStatusManager implements IStatusManager{
	private HashMap<UUID, Integer> expMap = new HashMap<UUID, Integer>();
	private HashMap<UUID, Integer> levelMap = new HashMap<UUID, Integer>();

	@Override
	public void addExp(OfflinePlayer p, int exp, StatusAddReason reason) {
		//加算前のレベルを取得
		int beforeLevel = getLevel(p);
		//現在の経験値を取得
		Integer integer = getExpMap().get(p.getUniqueId());
		if (integer == null) {
			integer = 0;
		}

		PlayerChangeStatusExpEvent event1 = new PlayerChangeStatusExpEvent(p, exp, this, reason);
		Bukkit.getServer().getPluginManager().callEvent(event1);
		exp = event1.getAddExp();

		//加算する
		int foodExp = integer.intValue() + exp;
		getExpMap().put(p.getUniqueId(), foodExp);

		//今のレベルを取得
		int nowLevel = getLevel(p);

		if (beforeLevel != nowLevel) {
			PlayerChangeStatusLevelEvent event = new PlayerChangeStatusLevelEvent(p, beforeLevel, nowLevel, this);
			Bukkit.getServer().getPluginManager().callEvent(event);
			levelup(p, nowLevel);
		}
	}

	@Override
	public void setLevel(OfflinePlayer p, int level) {
		int beforeLevel = getLevel(p);
		//setする
		getLevelMap().put(p.getUniqueId(), level);
		int nowLevel = getLevel(p);

		//EXPも更新する
		getExpMap().put(p.getUniqueId(), 0);

		if (beforeLevel != nowLevel) {
			PlayerChangeStatusLevelEvent event = new PlayerChangeStatusLevelEvent(p, beforeLevel, nowLevel, this);
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	@Override
	public int getExp(OfflinePlayer p) {
		UUID id = p.getUniqueId();
		Integer integer = getExpMap().get(id);
		return integer == null ? 0 : integer.intValue();
	}

	@Override
	public int getLevel(OfflinePlayer p) {
		if (!getLevelMap().containsKey(p.getUniqueId())) {
			getLevelMap().put(p.getUniqueId(), 0);
		}
		Integer integer = getLevelMap().get(p.getUniqueId());
		if (integer == null) {
			return 0;
		}
		return integer.intValue();
	}

	protected void levelup(OfflinePlayer p, int nowLevel) {
		//レベルをセットする
		getLevelMap().put(p.getUniqueId(), nowLevel);

		final Player player = p.getPlayer();
		if (player == null) {
			return;
		}
		player.sendMessage(new String[]{ChatColor.GREEN + "===" + getManagerName() +" LEVEL UP===",getManagerName() + "が" + nowLevel + "になりました。", ChatColor.GREEN + "===" + getManagerName() + " LEVEL UP==="});
	}

	protected Map<UUID, Integer> getExpMap() {
		return expMap;
	}

	protected Map<UUID, Integer> getLevelMap() {
		return levelMap;
	}

	@Override
	public int getMaxLevel() {
		return 160;
	}

	@Override
	public int getViewRowSize() {
		return 1;
	}

	@Override
	public ItemStack getLevelViewIcon(int viewIndex, OfflinePlayer p, PlayerStatus status) {
		ItemStack item = null;
		if (status.getLevel() >= (viewIndex - 1) * 10) {
			item = new ItemStack(getDetail().getViewIconMaterial());
			ItemStackUtil.setDispName(item,  "レベル" + viewIndex * 10 + ChatColor.GREEN + "  (UNLOCKED)");
			setViewIconLore(item, viewIndex, status);
		} else {
			item = new ItemStack(Material.STONE_BUTTON);
			ItemStackUtil.setDispName(item,  "レベル" + viewIndex * 10 +  ChatColor.RED + "  (LOCKED)");
			setViewIconLore(item, viewIndex, status);
		}
		return item;
	}

	protected void setViewIconLore(ItemStack item, int viewIndex, PlayerStatus status) {
		ArrayList<String> lore = new ArrayList<String>();
		for (int i = 1; i <= 10; i++) {
			int level = (viewIndex - 1) * 10 + i;

			ChatColor color = null;
			//ex)3列目の4項目目(24レベルの場所)　→　(3-1)*10 + 4 が　現在のレベルよりも大きい場合
			//すなわちLOCKされている場所
			if (level > status.getLevel()) {
				color = ChatColor.DARK_GRAY;
			} else {
				color = ChatColor.LIGHT_PURPLE;
			}
			String[] detailByLevel = getDetail().getDetailByLevel(level);

			String detail = "未実装 Comming soon";
			if (detailByLevel.length > 0) {
				detail = detailByLevel[0];
			}
			String join = StringUtils.join(new Object[]{color , "レベル" , Integer.toString(level) , "  ", detail});
			lore.add(join);
		}

		ItemStackUtil.addLore(item, lore.toArray(new String[0]));
	}

	public void remove(Player p) {
		expMap.remove(p);
		levelMap.remove(p);
	}
}

