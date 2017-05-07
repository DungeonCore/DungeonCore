package lbn.common.place;

import org.bukkit.Location;

public interface PlaceInterface {
  public int getId();

  public String getName();

  public Location getTeleportLocation();

  public void enable();

  public void disenable();

  public PlaceType getPlaceType();
}
