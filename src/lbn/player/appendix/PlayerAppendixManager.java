package lbn.player.appendix;

import java.util.UUID;

import lbn.player.appendix.appendixObject.AbstractPlayerAppendix;
import lbn.util.InOutputUtil;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

public class PlayerAppendixManager {
	public static float DEFAULT_WALK_SPEED = 0.2f;
	public static double DEFAULT_MAX_HEALTH = 20;

	public static final String FILE_NAME = "player_appendix.dat";

	private static PlayerAppendexMap playerAppendixMap = new PlayerAppendexMap();

	/**
	 * 全てのappendixを取り除く
	 * @param p
	 */
	public static void clearAppendix(Player p) {
		playerAppendixMap.remove(p);
		updatePlayerStatus(p);
	}

	/**
	 * appendixを追加する
	 * @param p
	 * @param appendix
	 */
	public static void addAppendix(OfflinePlayer p, AbstractPlayerAppendix appendix) {
		playerAppendixMap.addApendix(p.getUniqueId(), appendix);
		Player player = p.getPlayer();
		if (player != null) {
			updatePlayerStatus(player);
		}
	}

	/**
	 * 指定したappendixを取り除く
	 * @param p
	 * @param appendix
	 */
	public static void removeAppendix(Player p, AbstractPlayerAppendix appendix) {
		if (appendix == null) {
			return;
		}
		playerAppendixMap.removeAppendix(p.getUniqueId(), appendix);
		updatePlayerStatus(p);
	}

	/**
	 * 指定した名前のappendixを取り除く
	 * @param p
	 * @param appendixName
	 */
	public static void removeAppendix(Player p, String appendixName) {
		AbstractPlayerAppendix appendex = playerAppendixMap.getAppendixByName(p.getUniqueId(), appendixName);
		if (appendex != null) {
			removeAppendix(p, appendex);
			updatePlayerStatus(p);
		}
	}

	public static AbstractPlayerAppendix getPlayerAppendix(Player p) {
		return playerAppendixMap.get(p);
	}

	@SuppressWarnings("unchecked")
	public static void load() {
		Object inputStream = InOutputUtil.inputStream(FILE_NAME);
		if (inputStream == null) {
			return;
		}
		playerAppendixMap.setMultiMap((HashMultimap<UUID, AbstractPlayerAppendix>) inputStream);
	}

	public static void save() {
		InOutputUtil.outputStream(playerAppendixMap.getSavingData(), FILE_NAME);
	}

	/**
	 * プレイヤーの状態をその場で更新する必要があるなら更新する
	 * @param p
	 */
	public static void updatePlayerStatus(Player p) {
		AbstractPlayerAppendix playerAppendix = getPlayerAppendix(p);
		//歩くスピード
		p.setWalkSpeed(DEFAULT_WALK_SPEED + playerAppendix.getAddedWalkSpeed());
		//最大体力
		p.setMaxHealth(DEFAULT_MAX_HEALTH + playerAppendix.getAddedMaxHealth());
	}

}

