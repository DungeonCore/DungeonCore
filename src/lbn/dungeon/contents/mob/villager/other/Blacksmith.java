package lbn.dungeon.contents.mob.villager.other;

import java.util.HashSet;
import java.util.List;

import lbn.common.menu.MenuSelecor;
import lbn.mob.mob.abstractmob.villager.AbstractVillager;
import lbn.util.LbnRunnable;
import lbn.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Blacksmith extends AbstractVillager{

	static HashSet<Player> sayList = new HashSet<Player>();

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		e.setCancelled(true);
		if (damager.getType() == EntityType.PLAYER) {
			final Player p = (Player) damager;
			MenuSelecor.open(p, "blacksmith menu");
		}
	}

	@Override
	protected List<String> getMessage(Player p, LivingEntity mob) {
		return null;
	}

	@Override
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		e.setCancelled(true);
		Player p = e.getPlayer();
		if (sayList.contains(p)) {
			return;
		}

		sayList.add(p);

		final int periodTick = 30;

		new LbnRunnable() {
			@Override
			public void run2() {
				switch ((int)getAgeTick()) {
				case 0:
					Message.sendMessage(p, "俺を左クリックすれば" + ChatColor.GREEN + "アイテムの強化" + ChatColor.WHITE + "が行える。");
					return;
				case periodTick:
					Message.sendMessage(p, "強化可能なアイテムを" + ChatColor.RED + "赤いガラス" + ChatColor.WHITE +"のどちらかの隣に置くんだ。");
					return;
				case periodTick * 2:
					Message.sendMessage(p, "そうすれば" + ChatColor.RED + "赤いガラス" + ChatColor.WHITE +"に強化のための情報が表示される。");
					return;
				case periodTick * 3:
					Message.sendMessage(p, "強化素材は強化後、自動で" + ChatColor.GREEN + "インベントリから削除される。");
					return;
				case periodTick * 4:
					Message.sendMessage(p, "強化に失敗すると武器のレベルは" + ChatColor.GREEN + "0" + ChatColor.WHITE + "に戻るぞ。");
					p.sendMessage("");
					return;
				default:
					break;
				}
				if (isElapsedTick(periodTick * 7)) {
					sayList.remove(p);
					cancel();
				}
			}
		}.runTaskTimer((long) (periodTick));

	}

	@Override
	public String getName() {
		return "Mr.Blacksmith(鍛冶屋)";
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	@Override
	public Location getLocation() {
		return new Location(Bukkit.getWorld("world"), 124, 67, 854);
	}

}
