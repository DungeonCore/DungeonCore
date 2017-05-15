package net.l_bulb.dungeoncore.common.place.dungeon;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import net.l_bulb.dungeoncore.common.place.HolographicDisplaysManager;
import net.l_bulb.dungeoncore.common.place.PlaceBean;
import net.l_bulb.dungeoncore.common.place.PlaceInterface;
import net.l_bulb.dungeoncore.common.place.PlaceType;
import net.l_bulb.dungeoncore.dungeoncore.Main;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import lombok.Getter;
import lombok.Setter;

public class DungeonData implements PlaceInterface {

  public DungeonData(int id, String name) {
    PlaceBean placeBean = new PlaceBean();
    placeBean.setId(id);
    placeBean.setName(name);

    this.bean = placeBean;
  }

  private PlaceBean bean;

  Hologram hologram;

  @Getter
  @Setter
  boolean isShowHologram = false;

  public DungeonData(PlaceBean bean) {
    this.bean = bean;
  }

  @Override
  public int getId() {
    return bean.getId();
  }

  @Override
  public String getName() {
    return bean.getName();
  }

  @Override
  public Location getTeleportLocation() {
    return bean.getTpLocation();
  }

  @Override
  public void enable() {
    if (!isShowHologram) { return; }

    if (HolographicDisplaysManager.isUseHolographicDisplays()) {
      Location location = getEntranceLocation();
      if (location != null) {
        if (!location.getChunk().isLoaded()) {
          location.getChunk().load();
        }

        Hologram hologram = HologramsAPI.createHologram(Main.plugin, location);
        hologram.appendTextLine(ChatColor.AQUA + ChatColor.BOLD.toString() + getName());

        String difficulty = NumberUtils.isDigits(getLevel()) ? getLevel() + "レベル" : getLevel();
        hologram.appendTextLine(ChatColor.GOLD + "DIFFICULTY : " + difficulty);
        this.hologram = hologram;
      }
    }
  }

  @Override
  public void disenable() {
    if (hologram != null) {
      hologram.delete();
    }
  }

  /**
   * 対象のレベル
   *
   * @return
   */
  public String getLevel() {
    return bean.getLevel();
  }

  /**
   * ダンジョンの入口の座標
   *
   * @return
   */
  public Location getEntranceLocation() {
    return bean.getEntranceLocation();
  }

  /**
   * ダンジョンのスタート地点の座標
   *
   * @return
   */
  public Location getStartLocation() {
    return bean.getDungeonStartLocation();
  }

  @Override
  public PlaceType getPlaceType() {
    return PlaceType.DUNGEON;
  }

}
