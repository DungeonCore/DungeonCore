package lbn.player.magicstoneOre;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;

public class MagicStoneOreScheduler {
	
	/**
	 * reload時にすべての鉱石を再配置する。
	 * @param boolean
	 */
	public static void resetMagicOres(boolean bool){
		Bukkit.getLogger().info("全ての鉱石を再配置");
		if(true){
			for(Location location: MagicStoneFactor.magicStoneOres.keySet()) {
				Block locationBlock = location.getBlock();
				Material material = MagicStoneFactor.magicStoneOres.get(location).getMaterial();
			
				locationBlock.setType(material);
			}
		}
		
	}
	
	/**
	 * ブロックの座標が魔法鉱石に登録されていたらプレイヤーのインベントリに直接鉱石を追加する。
	 * @param block
	 * @param ore
	 * @param player
	 */
	public static void giveOreItem(Block block,ItemStack ore,Player player){
		if(MagicStoneFactor.magicStoneOres.containsKey(block.getLocation())){
			long tick = MagicStoneOreType.getRespawnTickFromMaterial(block.getType());
			
			player.getInventory().addItem(ore);
			block.setType(Material.COBBLESTONE);
			magicStoneRespawnScheduler(tick*20*60, block);
		}
	}
	
	public static void magicStoneRespawnScheduler(long tick,Block b){
		new BukkitRunnable(){
			@Override
			public void run() {
				b.setType(MagicStoneFactor.magicStoneOres.get(b.getLocation()).getMaterial());
			}
			
		}.runTaskLater(Main.plugin, tick);
	}
	
}
