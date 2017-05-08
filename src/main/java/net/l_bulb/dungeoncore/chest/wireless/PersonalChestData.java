package net.l_bulb.dungeoncore.chest.wireless;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PersonalChestData {
  protected PersonalChestData(int x, int y, int z, String uuid, String type) {
    super();
    this.x = x;
    this.y = y;
    this.z = z;
    this.uuid = uuid;
    this.type = type;
    playerID = UUID.fromString(uuid);
  }

  public int x;
  public int y;
  public int z;

  transient UUID playerID;
  String uuid;

  String type;

  public PersonalChestData(Player p, Location loc, String type) {
    playerID = p.getUniqueId();
    uuid = playerID.toString();

    x = loc.getBlockX();
    y = loc.getBlockY();
    z = loc.getBlockZ();

    this.type = type;
  }

  public Location getLocation() {
    return new Location(WireLessChestManager.chestWorld, x, y, z);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    result = prime * result + x;
    result = prime * result + y;
    result = prime * result + z;
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
    PersonalChestData other = (PersonalChestData) obj;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    if (x != other.x)
      return false;
    if (y != other.y)
      return false;
    if (z != other.z)
      return false;
    return true;
  }

}
