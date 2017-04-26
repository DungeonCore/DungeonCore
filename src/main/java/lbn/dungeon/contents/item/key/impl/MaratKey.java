package lbn.dungeon.contents.item.key.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MaratKey extends Fragment {

  public MaratKey() {
    super(1);
  }

  @Override
  public String getItemName() {
    return "Marat's Key";
  }

  @Override
  public String getLastLine(Player p, String[] params) {
    return "";
  }

  @Override
  protected Material getMaterial() {
    return Material.TRIPWIRE_HOOK;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "Marat Assembly hallで使用可能", "x:630 y:12 z:168" };
  }

}
