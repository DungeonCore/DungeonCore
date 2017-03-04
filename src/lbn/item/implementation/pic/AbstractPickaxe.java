package lbn.item.implementation.pic;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lbn.common.event.player.PlayerBreakMagicOreEvent;
import lbn.item.AbstractItem;
import lbn.item.itemInterface.MagicPickaxeable;
import lbn.player.magicstoneOre.MagicStoneOreType;
import lbn.util.ItemStackUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.inventory.ItemStack;

public abstract class AbstractPickaxe extends AbstractItem implements MagicPickaxeable{

	private static final String MAGIC_PICKAXE_LEVEL = "magic_pickaxe_level";

	/**
	 * ピッケルの素材の日本語名
	 * @return
	 */
	abstract String getMaterialName();

	@Override
	public String getItemName() {
		return getMaterialName() + "のピッケル";
	}

	@Override
	public String getId() {
		return "magic_" + getMaterial().toString().toLowerCase();
	}

	Random random = new Random();
	@Override
	public void onPlayerBreakMagicOreEvent(PlayerBreakMagicOreEvent e) {
		if (!canDestory(e.getBrokenType())) {
			e.setCancelled(true);
			return;
		}
		ItemStack useItem = e.getUseItem();
		//次のレベルを取得
		short nextLevel = (short) (getPickLevel(useItem) + 1);

		//ラピスの個数処理
		if (e.getBrokenType() == MagicStoneOreType.LAPIS_ORE) {
			//個数をセットする
			if (getLapisCount(nextLevel) > 1) {
				int nextInt = random.nextInt(getLapisCount(nextLevel) - 1) + 1;
				e.getAcquisition().setAmount(nextInt);
			} else {
				e.getAcquisition().setAmount(1);
			}
		}

		//最大レベル未満の時はレベルを追加する
		if (nextLevel < getMaxLevel()) {
			updatePickLevel(useItem, nextLevel);
		} else {
			//次のピッケルがない場合は最大レベルまでしか追加しない
			if (getNextPickAxe() == null) {
				updatePickLevel(useItem, getMaxLevel());
				return;
			}
			//進化後のピッケルをセットする
			ItemStack nextPickAxe = getNextPickAxe().getItem();
			e.getPlayer().setItemInHand(nextPickAxe);
			//メッセージを送信する
			e.getPlayer().sendMessage(ChatColor.GREEN + "ピッケルが" + getNextPickAxe().getItemName() + "に進化した。");
		}
	}

	abstract public int getLapisCount(short nextLevel);

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		//0レベルにセットする
		updatePickLevel(item, (short) 0);
		return item;
	}

	/**
	 * 進化時のピッケルを取得する
	 * @return
	 */
	abstract public AbstractPickaxe getNextPickAxe();

	/**
	 * このアイテムの最大レベルを取得
	 * @return
	 */
	abstract public short getMaxLevel();

	abstract public boolean canDestory(MagicStoneOreType type);

	/**
	 * ピッケルのレベルを取得
	 * @param item
	 * @return
	 */
	public static short getPickLevel(ItemStack item) {
		short nbtTagShort = ItemStackUtil.getNBTTagShort(item, MAGIC_PICKAXE_LEVEL);
		return nbtTagShort;
	}

	/**
	 * ピッケルのレベルを設定する
	 * @param item
	 * @param level
	 */
	public void updatePickLevel(ItemStack item, short level) {
		//NBTTagを設定
		ItemStackUtil.setNBTTag(item, MAGIC_PICKAXE_LEVEL, level);

		List<String> lore = ItemStackUtil.getLore(item);
		Iterator<String> iterator = lore.iterator();
		//ピッケルレベルのLoreを削除する
		while (iterator.hasNext()) {
			String next = iterator.next();
			if (next.contains("ピッケルレベル ")) {
				iterator.remove();
			}
		}
		//レベルを記載
		lore.add(ChatColor.GREEN + "ピッケルレベル " + level + "/" + getMaxLevel());

		ItemStackUtil.setLore(item, lore);
	}

	@Override
	protected List<String> getAddDetail() {
		List<String> addDetail = super.getAddDetail();
		if (getNextPickAxe() != null) {
			addDetail.add(getMaxLevel() + "レベルで" + getNextPickAxe().getItemName() + "に進化する");
		}
		if (getLapisCount((short) 0) != 1) {
			addDetail.add("一定確率でラピスを" + getLapisCount((short) 0) + "個まで取得");
		}
		return addDetail;
	}

	@Override
	protected ItemStack getItemStackBase() {
		return ItemStackUtil.getItemStackByCommand("/give @p minecraft:" + getGiveItemId() + " 1 0 {Unbreakable:1,HideFlags:8,CanDestroy:[\"minecraft:coal_ore\",\"minecraft:iron_ore\",\"minecraft:lapis_ore\",\"minecraft:redstone_ore\",\"minecraft:gold_ore\",\"minecraft:diamond_ore\", \"minecraft:lit_redstone_ore\"]}");
	}

	/**
	 * Giveコマンドに使うIDを取得
	 * @return
	 */
	abstract public String getGiveItemId();

}
