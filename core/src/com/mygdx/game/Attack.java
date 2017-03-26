package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Buff.BuffType;

public class Attack extends AttackPhase {	
	private final float inflictedStun;
	private final int inflictedDamage;
	private final Vector2 inflictedRecoil;
	private final float inflictedInvincibility;
	
	private float angle;
	private final float initAngle;
	private final float rotateAnglePerSecond;
	
	private Vector2 totalTranslation;
	private final Vector2 translation;
	
	public static final int SPECIALDEAL = 2;
	public static final int SPECIALHIT = 1;
	
	/**
	 * Create an attack.
	 * @param inflictedStun Inflicted stun upon hit.
	 * @param inflictedDamage Inflicted damage upon hit.
	 * @param inflictedRecoil Inflicted recoil upon hit. 
	 * @param inflictedInvincibility Inflicted invincibility upon hit.
	 * @param initAngle Start angle of the attack.
	 * @param endAngle End angle of the attack. It should be equal to initAngle if there are no rotation.
	 * @param translation Translation vector of the points during the attack.
	 * @param move Move vector of the character during the attack.
	 * @param totalTime Total duration of the attack.
	 * @param points Array of the hitbox points.
	 */
	
	public Attack (float inflictedStun, int inflictedDamage, Vector2 inflictedRecoil, float inflictedInvincibility, float initAngle, float endAngle, Vector2 translation, Vector2 move, float totalTime, ArrayList<Vector2> points) {
		this.inflictedStun = inflictedStun;
		this.inflictedDamage = inflictedDamage;
		this.inflictedRecoil = inflictedRecoil;
		this.inflictedInvincibility = inflictedInvincibility;
		
		this.totalTime = isPositive (totalTime);
		this.currentTime = 0;
		this.activationStart = false;
		
		this.angle = 0;
		this.initAngle = initAngle;
		this.rotateAnglePerSecond = (endAngle - initAngle) / totalTime;
		
		this.translation = translation.scl(1/totalTime);
		this.totalTranslation = new Vector2 ();
		this.move = move;
		
		this.points = points;
	}
	
	/**
	 * Rotate all the points by endAngle.
	 * @param rightSide : true if the attack is regular, false if the attack is reversed.
	 */
	private void rotateAttack (boolean rightSide) {
		angle += ((rightSide) ? 1 : -1) * rotateAnglePerSecond * elapsedTime;
		points.forEach(e -> e.rotateRad (((rightSide) ? 1 : -1) * rotateAnglePerSecond * elapsedTime));
	}
	
	/**
	 * Rotate all the points of the attack at the start of the attack.
	 * @param rightSide : true if the attack is regular, false if the attack is reversed.
	 */
	private void initRotateAttack (boolean rightSide) {
		angle += (rightSide) ? initAngle : (- initAngle);
		points.forEach(e -> inversion(e, rightSide).rotateRad ((rightSide) ? initAngle : - initAngle));
	}
	
	private Vector2 inversion (Vector2 point, boolean rightSide) {
		if (!rightSide)
			point.set(-point.x, point.y);
		return point;
	}
	
	private void init (boolean rightSide) {
		initRotateAttack (rightSide);
		super.init();
	}
	
	/**
	 * Translate all the points by translation;
	 * @param rightSide : true if the attack is regular, false if the attack is reversed.
	 */
	private void translateAttack(boolean rightSide) {
		Vector2 translate = translation.cpy().rotateRad(((rightSide) ? 1 : -1) * rotateAnglePerSecond * currentTime).scl(((rightSide) ? 1 : -1) * elapsedTime);
		totalTranslation.add(translate);
		points.forEach(e -> e.add(translate));
	}
	
	boolean nextStep (boolean rightSide, Vector2 position, CommandType command) {
		if (hasNextStep()) {
			if (isActive() == false)
				init (rightSide);
			elapsedTime = Gdx.graphics.getDeltaTime();
			currentTime += elapsedTime;
			translateAttack (rightSide);
			rotateAttack (rightSide);
			return true;
		}
		reset (rightSide);
		return false;
	}
	
	void reset (boolean rightSide) {
		points.forEach(e -> inversion(e.add(totalTranslation.cpy().scl(-1)).rotateRad (-angle), rightSide));
//		points.forEach(e -> e.add(totalTranslation.cpy().scl(-1)).rotateRad (-angle));
		totalTranslation.setZero();
		angle = 0;
		this.currentTime = 0;
		active = false;
	}
	
	int hitCharacter (boolean rightSide, Character attacker, Character target) {
		int damage = (int) (inflictedDamage * (1 + attacker.bonusValue(BuffType.ATK)) / (1 + target.bonusValue(BuffType.DEF)));
		if (!target.isInvincible()) {
			target.useJump();
//			System.out.println(((rightSide == target.isFacingRight()) ? "Critical " : "") + "Hit !");
			if ((target.getGuard() && (rightSide != target.faceRight))) {
				target.setStun(inflictedStun / 3);
				target.addForce((rightSide) ? inflictedRecoil.cpy().scl(1/3f) : inflictedRecoil.cpy().set(-inflictedRecoil.x, inflictedRecoil.y).scl(1/3f));
				target.consumeGuard(damage);
				System.out.println("GUARD SUCCESS");
			}
			else { 
				target.addForce((rightSide) ? inflictedRecoil : inflictedRecoil.cpy().set(-inflictedRecoil.x, inflictedRecoil.y));
				target.inflictDamage (criticalDamage((rightSide == target.isFacingRight()), damage));
				target.setStun (inflictedStun);
			}
			target.setInvincibility(inflictedInvincibility);
			target.addSpecial (SPECIALHIT);
			return SPECIALDEAL;
		}
		return 0;
	}
	
	@Override
	public String toString () {
		return "Damage: " + inflictedDamage + "\n"
				+ "Stun: " + inflictedStun + "\n"
				+ "Recoil: " + inflictedRecoil + "\n"
				+ "RotateAnglePerSecond: " + rotateAnglePerSecond + "\n"
				+ "Translation: " + translation + "\n"
				+ "Move: " + move + "\n"
				+ "Attack duration: " + totalTime;
	}
}