package lbn.common.citizenNpc;

import net.citizensnpcs.api.event.NPCSpawnEvent;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CitizenNpcManager {
	public static void registNpcPlayer(String name, String skin, Location loc) {
//		NPC createNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, skin);
//		createNPC.spawn(loc);
	}

	public static void NPCSpawnEvent(NPCSpawnEvent e) {
//		NPC createNPC = e.getNPC();
//		Player p = (Player)createNPC.getEntity();
//		changeName(p, "天才");
//		List<Player> players = p.getWorld().getPlayers();
//
//		//名前を変更する
//		for (Player player : players) {
//			if (isNpc(player)) {
//				return;
//			}
//			player.hidePlayer(p);
//			//1秒後に表示させる
//			new BukkitRunnable() {
//				@Override
//				public void run() {
//					player.showPlayer(p);
//				}
//			}.runTaskLater(Main.plugin, 20 * 3);
//		}
	}

	public static void changeName(Player p, String newName) {
//		CraftHumanEntity ncp = (CraftPlayer) p;
//		EntityHuman handle = ncp.getHandle();
//		try {
//			Class<EntityHuman> c = EntityHuman.class;
//			Field fld = c.getDeclaredField("bF");
//			fld.setAccessible(true);
//			GameProfile object = (GameProfile) fld.get(handle);
//			Field fld2 = GameProfile.class.getDeclaredField("name");
//			FinalFieldSetter instance = FinalFieldSetter.getInstance();
//			instance.set(object, fld2, newName);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
	}

	public static boolean isNpc(Entity entity) {
		return entity.hasMetadata("NPC");
	}
}
