package lbn.chest;

import java.util.HashMap;

import lbn.api.player.TheLowPlayer;
import lbn.dungeoncore.Main;
import lbn.mob.customMob.BossMobable;
import lbn.util.LbnRunnable;
import lbn.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class BossChest extends SpletSheetChest {

	HashMap<Player, Inventory> rewordInventoryMap = new HashMap<>();

	public BossChest(SpletSheetChest chest) {
		super(null, chest.contentLoc, chest.refuelTick, chest.moveLoc, chest.minItemCount, chest.maxItemCount, 25,
				chest.random);
	}

	Material m = Material.AIR;
	byte data = 0;

	/**
	 * チェストを設置する
	 * 
	 * @param e
	 * @return 設置後のチェストの座標
	 */
	public Location setChest(BossMobable e) {
		if (moveLoc != null) {
			for (Player p : rewordInventoryMap.keySet()) {
				// もし途中でログアウトとかしていればテレポートしない
				if (p.equals(Bukkit.getPlayerExact(p.getName()))) {
					teleportPlayer(p.getName());
				}
			}
		}

		// chestじゃなければ何も設置しない
		BlockState state = contentLoc.getBlock().getState();
		if (!(state instanceof Chest)) {
			return null;
		}

		Location chestLocation = getChestLocation(e.getEntity());
		// 実際にチェストを設置する
		setChest(chestLocation, e);

		return chestLocation;
	}

	/**
	 * 指定された場所に実際にチェストを設置する
	 * 
	 * @param chestLocation
	 */
	@SuppressWarnings("deprecation")
	protected void setChest(Location chestLocation, BossMobable e) {
		CustomChestManager.registChest(getChestLocation(e.getEntity()).getBlock().getLocation(), this);
		// 全ての人のインベントリをセット
		for (TheLowPlayer p : e.getCombatPlayer()) {
			if (p != null) {
				Player onlinePlayer = p.getOnlinePlayer();
				if (onlinePlayer != null) {
					rewordInventoryMap.put(onlinePlayer, getNewInventory(onlinePlayer));
				}
			}
		}

		// もとのブロックを取得
		m = chestLocation.getBlock().getType();
		data = chestLocation.getBlock().getData();
		// チェストを設置
		chestLocation.getBlock().setType(Material.CHEST);

		// 一定時間後にチェストを消す
		new LbnRunnable() {
			@Override
			public void run2() {
				runIfServerEnd();
			}

			protected void runIfServerEnd() {
				CustomChestManager.removeChest(chestLocation);
				// チャンクがロードされてなかったらロードする
				if (!chestLocation.getChunk().isLoaded()) {
					chestLocation.getChunk().load();
				}
				// ブロックを元に戻す
				chestLocation.getBlock().setType(m);
				chestLocation.getBlock().setData(data);
				m = Material.AIR;
				data = 0;
			};
		}.runTaskLater(Main.plugin, 20 * getTeleportTime());
	}

	protected Location getChestLocation(LivingEntity e) {
		return e.getLocation();
	}

	@Override
	public void removeChest(Location chestLocation) {
		if (chestLocation.getBlock().getType().equals(Material.CHEST)) {
			chestLocation.getBlock().setType(Material.AIR);
		}
	}

	@Override
	protected int getTeleportTime() {
		return moveSec;
	}

	@Override
	public void open(Player p, Block block, PlayerInteractEvent e) {
		if (!rewordInventoryMap.containsKey(p)) {
			Message.sendMessage(p, "あなたはボスとの戦闘に参加してないためこのチェストを開けることが出来ません。");
			return;
		}
		p.openInventory(rewordInventoryMap.get(p));
	}

	@Override
	public void setRefule(SpletSheetChest chest) {
		// とりあえず今は何もしない
	}
}
