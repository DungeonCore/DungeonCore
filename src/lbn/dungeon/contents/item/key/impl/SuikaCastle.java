package lbn.dungeon.contents.item.key.impl;

import lbn.dungeon.contents.item.key.AbstractKeyItem;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SuikaCastle extends AbstractKeyItem{
	public SuikaCastle(String no, String loc) {
		this.no = no;
		this. loc = loc;
	}

	protected String no;

	protected String loc;

	@Override
	public String getItemName() {
		return ChatColor.RED + "Va Rometo key " + no;
	}

	@Override
	protected Material getMaterial() {
		return Material.TRIPWIRE_HOOK;
	}

	@Override
	protected String[] getDetail() {
		return new String[]{"Va Rometoで使用可能", loc};
	}

	public static SuikaCastle[] getAllKey() {
		return new SuikaCastle[]{getKey1(), getKey2(), getKey3(), getKey4()};
	}

	public static SuikaCastle getKey1() {
		return new SuikaCastle("Ⅰ", "x:1238 y:66 z:764");
	}
	public static SuikaCastle getKey2() {
		return new SuikaCastle("Ⅱ", "x:1238 y:67 z:884");
	}
	public static SuikaCastle getKey3() {
		return new SuikaCastle("Ⅲ", "x:1238 y:36 z:925");
	}
	public static SuikaCastle getKey4() {
		return new SuikaCastle("Ⅳ", "x:1238 y:36 z:887");
	}

	@Override
	public void onClick(PlayerInteractEvent e, String[] lines, ItemStack item) {
		String[] split = lines[3].split(",");
		if (split.length != 3) {
			return;
		}

		Location loc = null;
		try {
			double x = Double.parseDouble(split[0]);
			double y = Double.parseDouble(split[1]);
			double z = Double.parseDouble(split[2]);
			Player p = e.getPlayer();
			loc = new Location(p.getWorld(), x, y, z);
			new ParticleData(ParticleType.portal, 50).run(loc);
			p.playSound(loc, Sound.PORTAL_TRAVEL, 1, 1);
			//TPする
			p.teleport(loc);

			//アイテムを減らせる
			ItemStack itemInHand = p.getItemInHand();
			if (itemInHand.getAmount() == 1) {
				p.setItemInHand(new ItemStack(Material.AIR));
			} else {
				itemInHand.setAmount(itemInHand.getAmount() - 1);
				p.setItemInHand(itemInHand);
			}
		} catch (NumberFormatException ex) {
			Player player = e.getPlayer();
			if (player.isOp()) {
				player.sendMessage(ChatColor.RED + "座標が不正です");
			}
			return;
		}

	}

	@Override
	public String getLastLine(Player p, String[] params) {
		try  {
			double x = Double.parseDouble(params[0]);
			double y = Double.parseDouble(params[1]);
			double z = Double.parseDouble(params[2]);

			return StringUtils.join(new Object[]{(int)x, "," ,(int)y, ",", (int)z});
		} catch (NumberFormatException e) {
			p.sendMessage("座標は実数を指定してください。");
			return null;
		}
	}

	@Override
	protected String getDungeonName() {
		return null;
	}

	@Override
	protected Location getDungeonLocation() {
		return null;
	}

	@Override
	public boolean isDispList() {
		return false;
	}

}
