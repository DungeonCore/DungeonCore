package lbn.item.setItem;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SetItemInterface {
	public HashMap<SetItemPartsType, SetItemParts> getFullSetItem();

	public SetItemParts getRandomSetItem();

	 public SetItemParts getSetItem(SetItemPartsType parts);

	 public void doRutine(Player p, ItemStack[] itemStacks) ;

	public boolean isWearSetItem(Player p);

	public ItemStack[] getWearedSetItem(Player p);

	public void startJob(Player p, ItemStack ...item);

	public void endJob(Player p);

	public List<String> getLore();

	public String getName();
}
