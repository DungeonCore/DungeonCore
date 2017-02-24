package lbn.dungeon.contents.item.magic;

import lbn.dungeon.contents.mob.zombie.AbstractSummonZombie;
import lbn.dungeon.contents.strength_template.NormalWeaponStrengthTemplate;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.attackitem.old.MagicItemOld;
import lbn.item.itemInterface.MagicExcuteable;
import lbn.mob.SummonPlayerManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MagicStick extends MagicItemOld{
	@Override
	public String getItemName() {
		return "MAGIC_STICK";
	}

	@Override
	protected Material getMaterial() {
		return Material.BLAZE_ROD;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"右クリック:ゾンビを一体召喚", "左クリック:炎魔法"};
	}

	@Override
	public int getAvailableLevel() {
		return 10;
	}

	@Override
	public String getId() {
		return "magic stick";
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return new NormalWeaponStrengthTemplate(10, getMaxStrengthCount());
	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		return null;
	}

	@Override
	protected MagicExcuteable getRightClickMagic(ItemStack item) {
		return new AbstractMagicExcuter(item, 20 * 3, 5, getId() + "_r", false) {
			@Override
			public void excuteMagic(Player p, PlayerInteractEvent e) {
				Location location = e.getPlayer().getLocation();
				Location added = location.add(location.getDirection().multiply(3).setY(0));

				//zombieを召喚
				AbstractSummonZombie customZombie = new AbstractSummonZombie();
				final Zombie zombie = customZombie.spawn(added);
				zombie.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));

				SummonPlayerManager.addSummon(zombie, e.getPlayer());
			}
		};
	}

	@Override
	protected MagicExcuteable getLeftClickMagic(ItemStack item) {
		return new AbstractMagicExcuter(item, 5, 5, getId() + "_l", false) {

			@Override
			public void excuteMagic(Player p, PlayerInteractEvent e) {
				Player player = e.getPlayer();
				Fireball launchProjectile = player.launchProjectile(Fireball.class);
				launchProjectile.setIsIncendiary(false);
				launchProjectile.setShooter(player);
				player.getWorld().playSound(player.getLocation(), Sound.BLAZE_HIT, 1, 0.5f);
			}
		};
	}

	@Override
	protected int getBaseBuyPrice() {
		return 200;
	}

}
