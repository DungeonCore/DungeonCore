package main.util.particle;

import net.minecraft.server.v1_8_R1.EnumParticle;


//https://gist.github.com/thinkofdeath/5110835#file-gistfile1-txt-L14
public enum ParticleType {
	hugeexplosion(EnumParticle.EXPLOSION_HUGE),
	largeexplode(EnumParticle.EXPLOSION_LARGE),
	fireworksSpark(EnumParticle.FIREWORKS_SPARK),
	bubble(EnumParticle.WATER_BUBBLE),
	suspended,
	depthsuspend(EnumParticle.SUSPENDED_DEPTH), //奈落
	townaura(EnumParticle.TOWN_AURA), //奈落
	crit,
	magicCrit(EnumParticle.CRIT_MAGIC),
	smoke(EnumParticle.SMOKE_NORMAL),
	mobSpell(EnumParticle.SPELL_MOB),
	mobSpellAmbient(EnumParticle.SPELL_MOB_AMBIENT),
	spell(EnumParticle.SPELL),
	instantSpell(EnumParticle.SPELL_INSTANT),
	witchMagic(EnumParticle.SPELL_WITCH),
	note,
	portal,
	enchantmenttable(EnumParticle.ENCHANTMENT_TABLE),
	explode(EnumParticle.EXPLOSION_NORMAL),
	flame,
	lava,
	footstep,
	splash(EnumParticle.WATER_SPLASH),
	largesmoke(EnumParticle.SMOKE_LARGE),
	cloud,
	reddust(EnumParticle.REDSTONE),
	snowballpoof(EnumParticle.SNOWBALL),
	dripWater(EnumParticle.DRIP_WATER),
	dripLava(EnumParticle.DRIP_LAVA),
	snowshovel(EnumParticle.SNOW_SHOVEL),
	slime,
	heart,
	angryVillager(EnumParticle.VILLAGER_ANGRY),
	happyVillager(EnumParticle.VILLAGER_HAPPY),
	water_wake,
	barrier,
	water_drop;

	EnumParticle enumType;

	private ParticleType() {
		enumType = EnumParticle.valueOf(toString().toUpperCase());
	}

	private ParticleType(EnumParticle enumType) {
		this.enumType = enumType;
	}

	public static ParticleType getType(String data) {
		try {
			return valueOf(data.toLowerCase());
		} catch (Exception e) {
			return null;
		}
	}

	public EnumParticle getEnumType() {
		return enumType;
	}
}

