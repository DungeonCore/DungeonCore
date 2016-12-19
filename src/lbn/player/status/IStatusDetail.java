package lbn.player.status;

import org.bukkit.Material;


public interface IStatusDetail {
	public String[] getIndexDetail();

	public String[] getDetailByLevel(int level);

	public String getDisplayName();

	public Material getViewIconMaterial();
}
