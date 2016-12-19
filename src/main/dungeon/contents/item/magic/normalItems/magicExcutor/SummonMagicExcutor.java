package main.dungeon.contents.item.magic.normalItems.magicExcutor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import main.dungeon.contents.mob.zombie.NormalMagicSummonZombie;
import main.item.itemInterface.MagicExcuteable;
import main.item.strength.StrengthOperator;
import main.mob.AbstractMob;
import main.mob.SummonPlayerManager;

public class SummonMagicExcutor implements MagicExcuteable{

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
//		return (int) (10 * availableLevel / 10.0 * 3) - strengthLevel;
		return 15;
	}

	@Override
	public boolean isShowMessageIfUnderCooltime() {
		return true;
	}

	@Override
	public void excuteMagic(Player p, PlayerInteractEvent e) {
		Location added = p.getLocation().add(p.getLocation().getDirection().multiply(3).setY(0));
		Zombie spawn = getSummonMob().spawn(added);
		SummonPlayerManager.addSummon(spawn, p);
	}

	protected AbstractMob<Zombie> getSummonMob() {
		return new NormalMagicSummonZombie(availableLevel, strengthLevel);
	}
}
