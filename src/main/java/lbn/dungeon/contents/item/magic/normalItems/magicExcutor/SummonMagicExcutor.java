package lbn.dungeon.contents.item.magic.normalItems.magicExcutor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import lbn.dungeon.contents.mob.zombie.NormalMagicSummonZombie;
import lbn.item.itemInterface.MagicExcuteable;
import lbn.item.system.strength.StrengthOperator;
import lbn.mob.AbstractMob;
import lbn.mob.SummonPlayerManager;

public class SummonMagicExcutor implements MagicExcuteable {

  int availableLevel;
  int strengthLevel;

  ItemStack item;

  String id;

  public SummonMagicExcutor(int availableLevel, ItemStack item, String id) {
    this.availableLevel = availableLevel;
    this.strengthLevel = StrengthOperator.getLevel(item);
    this.item = item;
    this.id = id;
  }

  @Override
  public int getCooltimeTick(ItemStack item) {
    return 12 * 20;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public ItemStack getItem() {
    return item;
  }

  @Override
  public int getNeedMagicPoint() {
    // return (int) (10 * availableLevel / 10.0 * 3) - strengthLevel;
    return 15;
  }

  @Override
  public boolean isShowMessageIfUnderCooltime() {
    return true;
  }

  @Override
  public void excuteMagic(Player p, PlayerInteractEvent e) {
    Location added = p.getLocation();
    Zombie spawn = getSummonMob().spawn(added);
    SummonPlayerManager.addSummon(spawn, p);
  }

  protected AbstractMob<Zombie> getSummonMob() {
    return new NormalMagicSummonZombie(availableLevel, strengthLevel);
  }
}
