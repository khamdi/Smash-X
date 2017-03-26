package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Buff.BuffType;

public class Shockwave extends AttackPhase {
	/**
	 * Maximum length of the shockwave.
	 */
	private final float maxLength;
	private final float maxScalPower;
	float currentLength;
	private final float growthPerSecond;
	
	/**
	 * Base vector of recoil.
	 */
	private final float baseDistRecoil;
	private final int inflictedDamage;
	private final float inflictedStun;
	private final float inflictedInvincibility;
	
	public Shockwave (float duration, Vector2 move, float maxScalPower, float maxLength, float baseDistRecoil, int inflictedDamage, float inflictedStun, float inflictedInvincibility) {
		this.totalTime = duration;
		this.currentTime = 0;
		
		this.move = move;
		this.points = new ArrayList <> ();
		this.active = false;

		this.maxScalPower = maxScalPower;
		this.maxLength = maxLength;
		this.growthPerSecond = maxLength / totalTime;
		this.currentLength = 0;
		
		this.baseDistRecoil = baseDistRecoil;
		this.inflictedDamage = inflictedDamage;
		this.inflictedInvincibility = inflictedInvincibility;
		this.inflictedStun = inflictedStun;
	}
	
	private void init (Vector2 position) {
		super.init();
		points.add(position.cpy());
	}

	@Override
	boolean nextStep(boolean rightSide, Vector2 position, CommandType command) {
		if (hasNextStep()) {
			if (active == false)
				init (position);
			active = true;
			elapsedTime = Gdx.graphics.getDeltaTime();
			currentTime += elapsedTime;
			currentLength += growthPerSecond * elapsedTime;
			return true;
		}
		reset (rightSide);
		return false;
	}
	
	@Override
	boolean checkHitboxCharacterCollision(java.util.ArrayList<Vector2> points, Vector2 attackerPosition, Character target) {
		return points.stream().anyMatch(e -> (Vector2.dst2(target.getX(), target.getY(), e.x, e.y) > currentLength * currentLength));
	}

	@Override
	void reset(boolean rightSide) {
		currentTime = 0;
		currentLength = 0;
		points.clear();
		active = false;
	}

	@Override
	int hitCharacter(boolean rightSide, Character attacker, Character target) {
		if (!target.isInvincible()) {
			float dist = (((maxLength / currentLength) > maxScalPower) ? maxScalPower : (maxLength / currentLength)) * baseDistRecoil;
			float angle = new Vector2 (target.getX() - points.get(0).x, target.getY() - points.get(0).y).angleRad();
			int damage = (int) (((((maxLength / currentLength) > maxScalPower) ? (maxLength / currentLength) : inflictedDamage) * (1 + attacker.bonusValue(BuffType.ATK))) / (1 + target.bonusValue(BuffType.DEF)));
			Vector2 scalar = new Vector2 ((float) Math.cos(angle) * dist, (float) Math.sin(angle) * dist);
		
			target.useJump(); 
			
			if ((target.getGuard() && (rightSide != target.faceRight))) {
				target.setStun(inflictedStun / 3);
				target.consumeGuard(damage);
				target.addForce(scalar.scl(1/3f));
			}
			else { 
				target.inflictDamage(damage);
				target.setStun (inflictedStun);
				target.addForce(scalar);
			}
			target.setInvincibility(inflictedInvincibility);
			target.addSpecial(Attack.SPECIALHIT);
			return Attack.SPECIALDEAL;
		}
		return 0;
	}
}
