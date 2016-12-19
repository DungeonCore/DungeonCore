package main.util;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;

public class SoundUtil {
	public static void PlaySound(Sound s, int i, int j, int range, Entity e) {
		e.getWorld().playSound(e.getLocation(), s, i, j);
	}
}
