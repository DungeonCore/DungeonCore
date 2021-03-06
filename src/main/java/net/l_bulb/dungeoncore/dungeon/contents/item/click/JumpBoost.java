package net.l_bulb.dungeoncore.dungeon.contents.item.click;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.item.customItem.itemAbstract.RightClickItem;
import net.l_bulb.dungeoncore.player.PlayerChecker;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;
import net.l_bulb.dungeoncore.util.Message;

public class JumpBoost extends RightClickItem {
  @Override
  public String getItemName() {
    return "Jumping Feather";
  }

  @Override
  protected boolean excuteOnRightClick2(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    Location location = player.getLocation();
    location.setY(0);
    Material type = location.getBlock().getType();
    if (type == Material.LAPIS_BLOCK) {
      Message.sendMessage(player, "この場所はこのアイテムが使えないエリアです");
      return false;
    }

    // このアイテムは使えません
    if (PlayerChecker.isNormalPlayer(player)) { return false; }

    player.setVelocity(new Vector(0, 2, 0));
    player.getWorld().playSound(player.getLocation(), Sound.BAT_LOOP, 1, 3);
    new ParticleData(ParticleType.crit, 100).setDispersion(0.5, 0.5, 0.5).run(player.getLocation());

    LivingEntityUtil.setNoFallDamage(player);

    return true;
  }

  @Override
  protected Material getMaterial() {
    return Material.FEATHER;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "右クリックをすることで", "真上に飛べます。" };
  }

  @Override
  public String getId() {
    return getItemName().toLowerCase();
  }

  @Override
  protected boolean isConsumeWhenUse() {
    return true;
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 2000;
  }

}
