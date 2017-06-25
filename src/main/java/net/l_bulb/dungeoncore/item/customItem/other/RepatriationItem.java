package net.l_bulb.dungeoncore.item.customItem.other;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.RightClickItemable;
import net.l_bulb.dungeoncore.util.LbnRunnable;

import net.md_5.bungee.api.ChatColor;

public class RepatriationItem extends AbstractItem implements RightClickItemable {

  @Override
  public String getItemName() {
    return "帰還玉";
  }

  @Override
  public String getId() {
    return "repatriation ";
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 500;
  }

  @Override
  public boolean excuteOnRightClick(PlayerInteractEvent e) {
    Player player = e.getPlayer();

    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(player);
      return false;
    }

    player.sendMessage(ChatColor.YELLOW + "15秒後に宿屋に帰還します。");

    // 15秒後にTPさせる
    long nowTime = System.currentTimeMillis();
    new LbnRunnable() {
      @Override
      public void run2() {
        // もしカウントダウン中に死んだら無効にする
        if (nowTime < theLowPlayer.getLastDeathTimeMillis()) {
          cancel();
          return;
        }
        player.sendMessage(ChatColor.YELLOW + "帰還まで後" + (15 - getRunCount() - 1) + "秒");
        // 最後のカウントダウンのときにTPさせる
        if (15 - getRunCount() - 1 == 0) {
          player.teleport(theLowPlayer.getRespawnLocation());
          player.playSound(player.getLocation(), Sound.PORTAL, 0.5f, 0.5f);
          portalEffect.run(player.getLocation());
          cancel();
        }
      }
    }.runTaskTimer(Main.plugin, 20 * 1, 20 * 1);
    return true;
  }

  ParticleData portalEffect = new ParticleData(ParticleType.portal, 200).setDispersion(1, 1, 1);

  @Override
  public boolean isConsumeWhenRightClick(PlayerInteractEvent event) {
    return true;
  }

  @Override
  public boolean isRemoveWhenDeath() {
    return true;
  }

  @Override
  protected Material getMaterial() {
    return Material.SLIME_BALL;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "15秒後に宿屋に帰還します。" };
  }

}
