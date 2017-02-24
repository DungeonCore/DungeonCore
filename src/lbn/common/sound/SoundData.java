package lbn.common.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundData {
	public SoundData(Sound sound, float val, float pitch) {
		this.sound = sound;
		this.val = val;
		this.pitch = pitch;
	}

	public SoundData(Sound sound) {
		this(sound, 1, 1);
	}

	Sound sound;
	float val;
	float pitch;

	public void playSoundAllPlayer(Location l) {
		l.getWorld().playSound(l, sound, val, pitch);
	}

	public void playSoundOnePlayer(Location l, Player p) {
		p.playSound(l, sound, val, pitch);
	}
}
