package lbn.player.reincarnation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import lbn.api.LevelType;
import lbn.api.player.ReincarnationInterface;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.menu.MenuSelectorInterface;
import lbn.common.menu.MenuSelectorManager;
import lbn.util.ItemStackUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ReincarnationSelector implements MenuSelectorInterface{

	public static final int REINC_LEVEL = 60;

	static {
		MenuSelectorManager.regist(new ReincarnationSelector(LevelType.SWORD));
		MenuSelectorManager.regist(new ReincarnationSelector(LevelType.BOW));
		MenuSelectorManager.regist(new ReincarnationSelector(LevelType.MAGIC));
	}

	private static final String REINCARNATION_ID = "reincarnation_id";
	LevelType type;

	/**
	 * @param type 転生を行うレベル
	 * @param count 指定されたLevelTypeで転生を行った回数
	 */
	public ReincarnationSelector(LevelType type) {
		this.type = type;
	}

	@Override
	public void open(Player p) {
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
		//データがロードされていない時は開かない
		if (theLowPlayer == null) {
			TheLowPlayerManager.sendLoingingMessage(p);
			return;
		}

		//転生できないなら開かない
		if (!theLowPlayer.canReincarnation(type)) {
			p.sendMessage(ChatColor.RED + "リンカーできません。");
			return;
		}

		//インベントリを取得
		Inventory createInventory = Bukkit.createInventory(null, 9, getTitle());

		//何回目の転生か
		int count = theLowPlayer.getEachReincarnationCount(type) + 1;

		//全ての転生効果を取得
		ArrayList<ReincarnationInterface> reincarnationList = allReincarnationList();
		for (ReincarnationInterface reincarnation : reincarnationList) {
			//もし表示を許可するならアイテムをセットする
			if (reincarnation.isShow(type, count)) {
				createInventory.addItem(getViewItemStack(reincarnation, p));
			}
		}
		p.openInventory(createInventory);
	}


	ArrayList<ReincarnationInterface> reincarnationCache = null;
	/**
	 * ソート済みの全ての転生効果を取得
	 * @return
	 */
	public ArrayList<ReincarnationInterface> allReincarnationList() {
		//キャッシュがあるならキャッシュを返す
		if (reincarnationCache != null) {
			return reincarnationCache;
		}

		//全ての転生効果を取得
		Map<String, ReincarnationInterface> allReincanationMap = ReincarnationFactor.getAllReincanationMap();
		//転生効果のリスト
		ArrayList<ReincarnationInterface> reincarnationList = new ArrayList<ReincarnationInterface>(allReincanationMap.values());
		//並び替えを行う
		reincarnationList.sort(new Comparator<ReincarnationInterface>() {
			@Override
			public int compare(ReincarnationInterface o1, ReincarnationInterface o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		//キャッシュに残す
		this.reincarnationCache = reincarnationList;
		return reincarnationList;
	}

	//View用のItemStackのキャッシュ
	private static Map<ReincarnationInterface, ItemStack> viewItemCache = new HashMap<ReincarnationInterface, ItemStack>();

	/**
	 * ReincarnationInterfaceからそれに対応するView用のItemStackを取得する
	 * @param reincarnation
	 * @param p
	 * @return
	 */
	protected ItemStack getViewItemStack(ReincarnationInterface reincarnation, Player p) {
		//キャッシュから取得
		ItemStack itemStack = viewItemCache.get(viewItemCache);
		//キャッシュが存在するならそれを返す
		if (itemStack != null) {
			return itemStack;
		}

		//View用のItemStackを作成
		ItemStack item = ItemStackUtil.getItem(
				ChatColor.GREEN + reincarnation.getTitle(),		//アイテム名
				reincarnation.getMaterial(),								//素材
				(byte)reincarnation.getItemStackData()			//データ値
			);	//詳細

		//ID情報をNBTTagにセットする
		ItemStackUtil.setNBTTag(item, REINCARNATION_ID, reincarnation.getId());

		//キャッシュに保存
		viewItemCache.put(reincarnation, item);
		return item;
	}

	@Override
	public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
		String nbtTag = ItemStackUtil.getNBTTag(item, REINCARNATION_ID);
		//NBTTagがないなら転生を行わない
		if (nbtTag == null || nbtTag.isEmpty()) {
			return;
		}

		//転生を取得
		ReincarnationInterface reincarnationInterface = ReincarnationFactor.getReincarnationInterface(nbtTag);

		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
		//Playerデータがロードされていないなら閉じる
		if (theLowPlayer == null) {
			p.closeInventory();
			TheLowPlayerManager.sendLoingingMessage(p);
			return;
		}
		//転生できないなら閉める
		if (!theLowPlayer.canReincarnation(type)) {
			p.closeInventory();
			p.sendMessage(ChatColor.RED + "リンカーできませんでした。");
			return;
		}

		//転生を行う
		if (!theLowPlayer.doReincarnation(reincarnationInterface, type)) {
			p.sendMessage(ChatColor.RED + "リンカーできませんでした。");
		}
		p.closeInventory();
	}

	@Override
	public String getTitle() {
		return "reincarnation " + type.toString().toLowerCase();
	}
}
