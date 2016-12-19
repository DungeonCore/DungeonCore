package lbn.player.party;

import java.util.List;

import org.bukkit.entity.Player;

public class PartyView {
	Party party;

	public PartyView(Party party) {
		this.party = party;
	}

	public Player getOwner() {
		return party.getOwner();
	}

	public List<Player> getPlayerList() {
		return party.getPlayerList();
	}

	public String getName() {
		return getOwner().getName() + "'s Party";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PartyView) {
			return party.equals(((PartyView) obj).party);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return party.hashCode();
	}
}
