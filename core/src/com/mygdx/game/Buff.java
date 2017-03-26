package com.mygdx.game;

import com.mygdx.game.BonusOnMap.BonusType;

public class Buff {

	enum BuffType {
		ATK, DEF, SPD;
	}

	BuffType type;
	float buffValue;
	float timeLeft;
	float timeAnimation;

	static float CONSTTIMEBUFF = 10; // seconde

	public Buff(BuffType type, float buffValue) {
		this.timeLeft = CONSTTIMEBUFF;
		this.type = type;
		this.buffValue = buffValue;
		this.timeAnimation = 0f;
	}

	public void coolBuffTime(float delta) {
		this.timeLeft -= delta;
		this.timeAnimation += delta;
	}

	public void synchronize(BuffType type) {
		if (this.isOfType(type)) {
			timeAnimation = 0f;
		}
	}

	public boolean isOutOfTime() {
		return (timeLeft <= 0);
	}

	static public BuffType whichBuffType(BonusType bonusOnMapType) {
		if (bonusOnMapType == BonusType.ATK_L || bonusOnMapType == BonusType.ATK_M || bonusOnMapType == BonusType.ATK_H)
			return BuffType.ATK;

		else if (bonusOnMapType == BonusType.DEF_L || bonusOnMapType == BonusType.DEF_M
				|| bonusOnMapType == BonusType.DEF_H)
			return BuffType.DEF;

		else
			return BuffType.SPD;
	}

	static public float whichBuffValue(BonusType bonusOnMapType) {

		if (bonusOnMapType == BonusType.ATK_L || bonusOnMapType == BonusType.DEF_L
				|| bonusOnMapType == BonusType.SPD_L) {
			return 0.10f;
		}

		else if (bonusOnMapType == BonusType.ATK_M || bonusOnMapType == BonusType.DEF_M
				|| bonusOnMapType == BonusType.SPD_M) {
			return 0.25f;
		}

		return 0.50f;
	}

	public boolean isOfType(BuffType buffType) {
		return (this.type == buffType);
	}

	public float getValue() {
		return buffValue;
	}

	public void resetTimeLeft() {
		this.timeLeft = CONSTTIMEBUFF;
	}

	@Override
	public String toString() {
		return ("Ce bonus est de type: " + this.type + " avec une valeur de +: " + this.buffValue + " Il lui reste :"
				+ this.timeLeft + " secondes");
	}

	@Override
	public boolean equals(Object o) {

		if (o instanceof Buff) {
			Buff b = (Buff) o;
			return (this.type.equals(b.type) && this.buffValue == b.buffValue);
		}
		return false;
	}

}
