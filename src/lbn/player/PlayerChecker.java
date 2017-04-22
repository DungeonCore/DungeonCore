package lbn.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerChecker {
	/**
	 * 通常の遊んでいるPlayerならTRUE
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isNormalPlayer(Entity entity) {
		if (entity.getType() == EntityType.PLAYER) {
			Player p = (Player) entity;
			return p.getGameMode() == GameMode.ADVENTURE || p.getGameMode() == GameMode.SURVIVAL;
		}
		return false;
	}

	/**
	 * Playerで通常の遊んでいるPlayerでないならTRUE
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isNonNormalPlayer(Entity entity) {
		if (entity.getType() == EntityType.PLAYER) {
			return !isNormalPlayer(entity);
		}
		return false;
	}
}
