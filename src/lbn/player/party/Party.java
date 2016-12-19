package lbn.player.party;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

 class Party {
	ArrayList<Player> pList = new ArrayList<Player>();

	protected int MAX_SIZE = 4;

	 Party(Player p) {
		pList.add(p);
	}

	 boolean addPlayer(Player p) {
		if (isExist(p)) {
			return false;
		}

		if (getSize() >= MAX_SIZE) {
			return false;
		}

		pList.add(p);
		return true;
	}

	 boolean removePlayer(Player p) {
		return pList.remove(p);
	}

	 int getSize() {
		return pList.size();
	}

	 boolean isExist(Player p) {
		return pList.contains(p);
	}

	 Player getOwner() {
		if (getSize() > 0) {
			return pList.get(0);
		}
		return null;
	}

	 List<Player> getPlayerList() {
		 return pList;
	 }

	 boolean isOwner(Player p) {
		 Player owner = getOwner();
		 return owner.getUniqueId().equals(p.getUniqueId());
	 }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pList == null) ? 0 : pList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Party other = (Party) obj;
		if (pList == null) {
			if (other.pList != null)
				return false;
		} else if (!pList.equals(other.pList))
			return false;
		return true;
	}
}
