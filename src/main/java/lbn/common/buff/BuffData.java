package lbn.common.buff;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BuffData {
	private String id;
	private PotionEffectType effect;
	private int tick;
	private int level = 1;

	public BuffData(String id, PotionEffectType effect, int second, int level) {
		this.id = id;
		this.effect = effect;
		this.tick = second * 20;
		if (level < 1) {
			this.level = 1;
		}
		this.level--;
	}

	public String getId() {
		return id;
	}

	public PotionEffectType getPotionEffectType() {
		return effect;
	}

	public int getTick() {
		return tick;
	}

	public int getLevel() {
		return level;
	}

	public void addBuff(LivingEntity e) {
		if (e.isValid()) {
			e.addPotionEffect(new PotionEffect(effect, tick, level), false);
		}
	}

}
