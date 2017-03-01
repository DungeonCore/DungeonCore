package lbn.player.magicstoneOre;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class MagicStoneOreScheduler {
	
	public static void resetMagicOres(boolean bool){
		if(bool){
			System.out.println("鉱石　再配置");
			for(Location location: MagicStoneFactor.magicStoneOres.keySet()) {
			
				Block locationBlock = location.getBlock();
				Material material = MagicStoneFactor.magicStoneOres.get(location).getMaterial();
			
				if(locationBlock.getType() != material) {
					locationBlock.setType(material);
				}
			
			}
		}
		
	}
}
