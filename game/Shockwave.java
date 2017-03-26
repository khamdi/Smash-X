package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

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
	boolean nextStep(boolean rightSide, Vector2 position) {
		if (hasNextStep()) {
			if (active == false)
				init (position);
			active = true;
			elapsedTime = Gdx.graphics.getDeltaTime();
			currentTime += elapsedTime;
			currentLength += growthPerSecond * elapsedTime;
			return true;
		}
		reset ();
		return false;
	}
	
	@Override
	boolean checkHitboxCharacterCollision(java.util.ArrayList<Vector2> points, Vector2 attackerPosition, Character target) {
		return points.stream().anyMatch(e -> (Vector2.dst2(target.getX(), target.getY(), e.x, e.y) > currentLength * currentLength));
	}

	@Override
	void reset() {
		currentTime = 0;
		currentLength = 0;
		points.clear();
		active = false;
	}

	@Override
	int hitCharacter(boolean rightSide, Character c) {
		if (!c.isInvincible()) {
			float dist = (((maxLength / currentLength) > maxScalPower) ? maxScalPower : (maxLength / currentLength)) * baseDistRecoil;
			float angle = new Vector2 (c.getX() - points.get(0).x, c.getY() - points.get(0).y).angleRad();
			c.inflictDamage((int) (((maxLength / currentLength) > maxScalPower) ? (maxLength / currentLength) : inflictedDamage));
			c.useJump();
			c.setStun(inflictedStun);
			c.addForce((float) Math.cos(angle) * dist, (float) Math.sin(angle) * dist);
			c.setInvincibility(inflictedInvincibility);
			
			c.addSpecial(Attack.SPECIALHIT);
			return Attack.SPECIALDEAL;
		}
		return 0;
	}
}
