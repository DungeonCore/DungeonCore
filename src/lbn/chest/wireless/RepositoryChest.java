package lbn.chest.wireless;

import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.menu.MenuSelector;
import lbn.common.menu.MenuSelectorManager;
import lbn.common.menu.SelectRunnable;
import lbn.money.GalionEditReason;
import lbn.util.ItemStackUtil;
import lbn.util.Message;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RepositoryChest extends WireLessChest{

//	Location loc;
	RepositoryType type;
	public RepositoryChest(Location loc, RepositoryType type) {
//		this.loc = loc;
		this.type = type;

		if (MenuSelectorManager.contains("Repository Menu:" + type)) {
			return;
		}

		//メニューセレクトターを作成
		MenuSelector menuSelecor = new MenuSelector("Repository Menu:" + type);
		menuSelecor.addMenu(ItemStackUtil.getItem("倉庫を購入する", Material.WOOL, (byte)5,
				Message.getMessage("{0}Galionで倉庫を購入する", type.price)), 11, new SelectRunnable() {
			@Override
			public void run(Player p, ItemStack item) {
				TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
				if (theLowPlayer == null) {
					Message.sendMessage(p, ChatColor.RED + "現在Playerデータをロードしています。もう暫くお待ち下さい");
					return;
				}

				if (theLowPlayer.getGalions() > type.getPrice()) {
					//チェストを作成
					instance.createChest(p, type.getType());
					//お金を引く
					theLowPlayer.addGalions(-type.getPrice(), GalionEditReason.consume_shop);
					Message.sendMessage(p, ChatColor.GREEN + "倉庫({0})を購入しました。", type.getType());
				} else {
					Message.sendMessage(p, ChatColor.RED + "お金が足りないので購入できません。", type.getType());
				}
				p.closeInventory();
			}
		});

		menuSelecor.addMenu(ItemStackUtil.getItem("倉庫を購入しない", Material.WOOL, (byte)14,
				Message.getMessage("倉庫を購入しない")), 15, new SelectRunnable() {
			@Override
			public void run(Player p, ItemStack item) {
				p.closeInventory();
			}
		});
		menuSelecor.regist();
	}

	public RepositoryType getType() {
		return type;
	}

	WireLessChestManager instance = WireLessChestManager.getInstance();
	@Override
	public Location getContainsLocation(Player p, Block block, PlayerInteractEvent e){
		//すでに作成済みの場合はそのまま返す
		Location chestContentsLocation = instance.getChestContentsLocation(p, type.getType());
		if (chestContentsLocation != null) {
			return chestContentsLocation;
		}
		MenuSelectorManager.open(p, "Repository Menu:" + type);

		return null;
	}

	@Override
	public String getName() {
		return "RepositoryChest";
	}

	@Override
	public boolean canOpen(Player p, Block block, PlayerInteractEvent e) {
		return true;
	}
}
