package lbn.player.magicstoneOre;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class MagicStoneOreScheduler {
	
	public static void resetMagicOres(boolean bool){
		Bukkit.broadcastMessage("SUCCEFUL CALLED");
		if(bool){
			Bukkit.broadcastMessage("ARG WAS TRUE");
			for(Location location: MagicStoneFactor.locations) {
			
				Block locationBlock = location.getBlock();
				Material material = MagicStoneFactor.magicStoneOres.get(location).getMaterial();
			
				if(locationBlock.getType() != material) {
					locationBlock.setType(material);
				}
			
			}
		}
		
	}
}
