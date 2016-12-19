package main.player.party;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class PartyManager {

	static HashMap<Player, Party> playerPartyList = new HashMap<Player, Party>();

	/**
	 * partyViewを取得
	 * @param p
	 * @return
	 */
	public static PartyView getParty(Player p) {
		Party party = playerPartyList.get(p);
		return new PartyView(party);
	}

	/**
	 * パーティーにプレイヤーを追加
	 * @param p
	 * @param partyView
	 * @return
	 */
	public static boolean addPlayer(Player p, PartyView partyView) {
		if (containsParty(p)) {
			return false;
		}
		Player owner = partyView.getOwner();
		//Partyを取得
		Party party = playerPartyList.get(owner);

		if (!party.addPlayer(p)) {
			return false;
		}
		playerPartyList.put(p, party);
		return true;
	}

	/**
	 * もしどこかのパーティーに属しているならTRUE
	 * @param p
	 * @return
	 */
	public static boolean containsParty(Player p) {
		return playerPartyList.containsKey(p);
	}

	/**
	 * 新しいPartyを取得する
	 * @param p
	 * @return
	 */
	public static boolean createParty(Player p) {
		if (containsParty(p)) {
			return false;
		}
		Party party = new Party(p);
		playerPartyList.put(p, party);
		return true;
	}

	/**
	 * パーティーから人を削除する
	 * @param p
	 * @return
	 */
	public static boolean remove(Player p) {
		if (!containsParty(p)) {
			return false;
		}

		Party party = playerPartyList.get(p);
		if (party.isOwner(p)) {
			deleteParty(new PartyView(party));
		} else {
			party.removePlayer(p);
			playerPartyList.remove(p);
		}
		return true;
	}

	public static void deleteParty(PartyView partyView) {
		Player owner = partyView.getOwner();
		Party party = playerPartyList.get(owner);

		for (Player p : party.getPlayerList()) {
			playerPartyList.remove(p);
		}
	}
}
