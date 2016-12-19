package lbn.item.armoritem;


public enum ArmorMaterial {
	LEATHER(0.06, 0.04, 0.04, 0.08), GOLD(0.07, 0.06, 0.04, 0.1), CHAINMAIL(0.08, 0.08, 0.04, 0.124), IRON(0.09, 0.1, 0.04, 0.159), DIAMOND(0.1, 0.12, 0.04, 0.212);

	private ArmorMaterial(double baseDamageCut, double baseBossDamageCut,
			double strengthTotalDamageCut, double strengthBossTotalDamageCut) {
		this.baseDamageCut = baseDamageCut;
		this.baseBossDamageCut = baseBossDamageCut;
		this.strengthTotalDamageCut = strengthTotalDamageCut;
		this.strengthBossTotalDamageCut = strengthBossTotalDamageCut;
	}

	double baseDamageCut;
	double baseBossDamageCut;
	double strengthTotalDamageCut;
	double strengthBossTotalDamageCut;


	/**
	 * 強化してない状態でザコ敵に関してダメージをカットする倍率
	 * @return
	 */
	public double getBaseDamageCut() {
		return baseDamageCut;
	}

	/**
	 * 強化してない状態でボスに関してダメージをカットする倍率
	 * @return
	 */
	public double getBaseBossDamageCut() {
		return baseBossDamageCut;
	}

	/**
	 * 最大強化の時、追加でザコ敵に関してダメージをカットする割合
	 * @return
	 */
	public double getStrengthTotalDamageCut() {
		return strengthTotalDamageCut;
	}

	/**
	 * 最大強化の時、追加でボスに関してダメージをカットする割合
	 * @return
	 */
	public double getStrengthBossTotalDamageCut() {
		return strengthBossTotalDamageCut;
	}
}
