package lbn.common.other;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import lbn.dungeoncore.Main;
import lbn.player.PlayerChecker;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EndPortalOperator {
	public static void onBlockMultiPlaceEvent(BlockMultiPlaceEvent e) {
		//変わった直後だとポータルを認識できないので１tick後にする
		new BukkitRunnable() {
			@Override
			public void run() {
				subExecute(e);
			}
		}.runTaskLater(Main.plugin, 1);
	}

	public static void subExecute(BlockMultiPlaceEvent e) {
		//エンドポータルでないなら無視
		if ( e.getBlock().getType() != Material.ENDER_PORTAL_FRAME) {
			return;
		}

		if (e.getReplacedBlockStates().size() != 10) {
			return;
		}

		//全てEndPortalであることを確認する
		for (BlockState states : e.getReplacedBlockStates()) {
			if (states.getBlock().getType() != Material.ENDER_PORTAL && states.getBlock().getType() != Material.ENDER_PORTAL_FRAME) {
				return;
			}
		}

		//エンドポータルフレームのリスト
		HashSet<Block> endPortalFrameList = new HashSet<Block>();

		List<BlockFace> faces = Arrays.asList(BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST);

		//周りにあるエンドポータルフレームを取得
		for (BlockState states : e.getReplacedBlockStates()) {
			if (states.getBlock().getType() == Material.ENDER_PORTAL_FRAME) {
				continue;
			}

			Block block = states.getBlock();
			//4方向調べる
			for (BlockFace face : faces) {
				Block relative = block.getRelative(face);
				//ポータルフレームならリストに入れる
				if (relative.getType() == Material.ENDER_PORTAL_FRAME) {
					endPortalFrameList.add(relative);
				}
			}
		}
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				//ポータルを消す
				for (BlockState states : e.getReplacedBlockStates()) {
					if (states.getBlock().getType() == Material.ENDER_PORTAL) {
						//チャンクをロードする
						Chunk chunk = states.getBlock().getChunk();
						if (!chunk.isLoaded()) {
							chunk.load();
						}
						states.getBlock().setType( Material.OBSIDIAN);
					}
				}

				//エンダーアイを取り除く
				for (Block frame : endPortalFrameList) {
					//チャンクをロードする
					Chunk chunk = frame.getChunk();
					if (!chunk.isLoaded()) {
						chunk.load();
					}
					byte data = frame.getData();
					if (data >= 4) {
						frame.setData((byte) (data - 4));
					}
				}
			}
		}.runTaskLater(Main.plugin, 20 * 10);
	}

	@SuppressWarnings("deprecation")
	public static void onClick(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		Block block = e.getClickedBlock();

		//エンダーアイの処理
		if (item == null || item.getType() != Material.EYE_OF_ENDER) {
			return;
		}

		Player player = e.getPlayer();
		//Adminなら何もしない
		if (PlayerChecker.isNonNormalPlayer(player)) {
			return;
		}

		//エンダーアイで空のポータルをクリックした時のみ、キャンセルしない
		if (block != null && block.getType() == Material.ENDER_PORTAL_FRAME && block.getData() <= 3) {
//			//エンダーアイを１つ消費させる
//			ItemStackUtil.consumeItemInHand(player);
//			//エンダーアイをポータルにつける
//			block.setData((byte) (block.getData() + 4));
		}
		e.setCancelled(true);

	}
}
