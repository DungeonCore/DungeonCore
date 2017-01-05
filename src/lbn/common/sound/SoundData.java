package lbn.common.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundData {
	public SoundData(String id, Sound sound, float val, float pitch) {
		this.id = id;
		this.sound = sound;
		this.val = val;
		this.pitch = pitch;
	}

	public SoundData(String id, Sound sound) {
		this(id, sound, 1, 1);
	}

	String id;
	Sound sound;
	float val;
	float pitch;

	public void playSoundAllPlayer(Location l) {
		l.getWorld().playSound(l, sound, val, pitch);
	}

	public void playSoundOnePlayer(Location l, Player p) {
		p.playSound(l, sound, val, pitch);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SoundData && ((SoundData)obj).id != null) {
			return ((SoundData)obj).id.equals(id);
		}
		return false;
	}

}
