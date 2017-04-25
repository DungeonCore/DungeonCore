package lbn.nametag;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TagConfig extends JavaPlugin {

	public void config() {
		FileConfiguration tagconfig = new YamlConfiguration();
		//TODO: tag.yml create
	}

	public static void setNewTag(String string) {
		//TODO: add set tag
	}

	public static void setPlayer(String string) {
		//TODO: add set player
	}
}
