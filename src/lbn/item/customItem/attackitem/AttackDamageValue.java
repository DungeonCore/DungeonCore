package lbn.item.customItem.attackitem;

import lbn.player.ItemType;

public class AttackDamageValue {
//	public static double minDamage(ItemType type, int level) {
//		return 0;
//	}
//	public static double maxDamage(ItemType type, int level) {
//		return 0;
//	}


	/**
	 * 与えられた戦闘負荷から攻撃力を取得する
	 * @param combatLoad
	 * @param availableLevel
	 * @return
	 */
	public static double getAttackDamageValue(double combatLoad, int availableLevel) {
		combatLoad = Math.max(combatLoad, 0);
		return levelMobHp[availableLevel] / combatLoad;
	}

	/**
	 * 戦闘負荷を取得
	 * @param weaponLevel
	 * @param type
	 * @return
	 */
	public static double getCombatLoad(int weaponLevel, ItemType type) {
		weaponLevel = Math.max(weaponLevel, 1);
		weaponLevel = Math.min(weaponLevel, 20);

		if (type == ItemType.BOW) {
			return bowCombatLoad[weaponLevel - 1];
		} else {
			return swordCombatLoad[weaponLevel - 1];
		}
	}

	static double[] swordCombatLoad = {
		5.6,
		5.4,
		5.2,
		5,
		4.8,
		4.6,
		4.4,
		4.2,
		4,
		3.8,	//10
		3.6,
		3.4,
		3.2,
		3,
		2.8,
		2.6,
		2.4,
		2.2,
		2,
		1.8
	};
	static double[] bowCombatLoad = {
		4,
		3.83,
		3.66,
		3.49,
		3.32,
		3.15,
		2.98,
		2.81,
		2.64,
		2.47,//10
		2.3,
		2.13,
		1.96,
		1.79,
		1.62,
		1.45,
		1.28,
		1.11,
		0.94,
		0.77
	};

	static double[] levelMobHp = {
		29,
		29.624,
		30.08051417,
		30.82738,
		31.8045841,
		32.98575016,
		34.35976722,
		35.92400806,
		37.68130045,
		39.6384196,
		41.80529595,
		44.19459873,
		46.82153575,
		49.70378876,
		52.86154055,
		56.31756987,
		60.09740043,
		64.22949659,
		68.74550224,
		73.68052145,
		79.0734417,
		84.96730148,
		91.40970512,
		98.45328876,
		106.156242,
		114.582891,
		123.804349,
		133.8992417,
		144.9545158,
		157.0663402,
		170.3411093,
		184.8965614,
		200.8630247,
		218.3848051,
		237.6217341,
		258.7508927,
		281.968535,
		307.4922324,
		335.5632652,
		366.4492918,
		400.447327,
		437.8870663,
		479.1345986,
		524.5965515,
		574.7247231,
		630.0212575,
		691.0444286,
		758.4151076,
		832.8239939,
		915.0397048,
		1005.917825,
		1106.411034,
		1217.580444,
		1340.608295,
		1476.812166,
		1627.660905,
		1794.79247,
		1980.033927,
		2185.423868,
		2413.23755,
		2666.015084,
		2946.593066,
		3258.14006,
		3604.196431,
		3988.719057,
		4416.131529,
		4891.380544,
		5419.999242,
		6008.178382,
		6662.846327,
		7391.758964,
		8203.600803,
		9108.098676,
		10116.14963,
		11239.96484,
		12493.2315,
		13891.29517,
		15451.36491,
		17192.7444,
		19137.09221,
		21308.715,
		23734.89787,
		26446.27678,
		29477.25818,
		32866.49232,
		36657.40686,
		40898.80885,
		45645.56384,
		50959.3621,
		56909.58354
	};
}
