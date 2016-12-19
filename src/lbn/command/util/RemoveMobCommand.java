package lbn.command.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import lbn.util.LivingEntityUtil;

public class RemoveMobCommand implements CommandExecutor{

	//command x y z mob_name
	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		Location loc = null;

		if (paramCommandSender instanceof Player) {
			loc = ((Player) paramCommandSender).getLocation();
		} else if (paramCommandSender instanceof BlockCommandSender) {
			loc = ((BlockCommandSender) paramCommandSender).getBlock().getLocation();
		}

		double x = 0;
		double y = 0;
		double z = 0;
		String join = "";
		try {
			x = Double.parseDouble(paramArrayOfString[0]);
			y = Double.parseDouble(paramArrayOfString[1]);
			z = Double.parseDouble(paramArrayOfString[2]);
			join = StringUtils.join(Arrays.copyOfRange(paramArrayOfString, 3, paramArrayOfString.length), " ").trim();
		} catch (Exception e) {
			paramCommandSender.sendMessage("入力されたデータが違います");
			return false;
		}


		Entity spawnEntity = loc.getWorld().spawnEntity(loc, EntityType.SNOWBALL);
		List<Entity> nearbyEntities = spawnEntity.getNearbyEntities(x , y, z);
		spawnEntity.remove();

		for (Entity entity : nearbyEntities) {
			if (!LivingEntityUtil.isEnemy(entity)) {
				continue;
			}
			if (join.equalsIgnoreCase("all")) {
				entity.remove();
			} else if (join.equalsIgnoreCase(((LivingEntity)entity).getCustomName())){
				entity.remove();
			}
		}

		return true;
	}

}
