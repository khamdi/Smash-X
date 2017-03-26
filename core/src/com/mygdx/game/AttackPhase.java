package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public abstract class AttackPhase {
	protected float totalTime;
	protected float currentTime;
	protected float elapsedTime;
	protected boolean active;
	protected ArrayList<Vector2> points;
	protected Vector2 move;
	protected boolean activationStart;
	
	/**
	 * @return The total duration of the attack in seconds.
	 */
	float getTotalTime () {
		return totalTime;
	}

	/**
	 * Initializes the attack phase.
	 */
	void init () {
		active = true;
		activationStart = true;
	}
	
	/**
	 * Activates the attack stun.
	 */
	float activateStunTrigger () {
		if (activationStart) {
			activationStart = false;
			return totalTime;
		}
		return 0f;
	}
	
	/**
	 * @return Whether the attack is active.
	 */
	boolean isActive () {
		return active;
	}
	
	/**
	 * Checks whether the attack has a next step, i.e. whether the attack is at its final step.
	 * @return Whether the attack is finished.
	 */
	boolean hasNextStep () {
		return (currentTime + Gdx.graphics.getDeltaTime() < totalTime);
	}
	
	float isPositive (float number) {
		if (number <= 0f)
			throw new IllegalArgumentException ("Positive number needed.");
		return number;
	}
	
	/**
	 * Returns whether the character given contains the point also given.
	 * As the position of the attack hitbox is related to the attacker's, this function also needs the attacker's position.
	 * @param point the tested point
	 * @param attackerPosition position of the one who attacks
	 * @param target the targeted character
	 * @return whether the character is hit.
	 */
	static boolean checkPointCharacterCollision (Vector2 point, Vector2 attackerPosition, Character target) {
		if (target.getHitbox().contains(point.cpy().add(attackerPosition).add(0f, 32))) {
			return true;
		}
		return false;
	}

	/**
	 * Return whether the character given contains any point of the attack hitbox.
	 * As the position of the attack hitbox is related to the attacker's, this function also needs the attacker's position.
	 * @param points array of the hitbox points
	 * @param attackerPosition the position of the one who attacks
	 * @param target the targeted character
	 * @return
	 */
	boolean checkHitboxCharacterCollision (ArrayList<Vector2> points, Vector2 attackerPosition, Character target) {
		return points.parallelStream().anyMatch(e -> AttackPhase.checkPointCharacterCollision(e, attackerPosition, target));
	}
	
	/**
	 * Calculates the damage dealt upon attack.
	 * @param critical true if the attack has to be critical
	 * @param base Base damage of the attack
	 * @return The final amount of damage dealt
	 */
	int criticalDamage (boolean critical, int base) {
		return (int) ((critical) ? base * ComposedAttack.CRITDAMAGE : base);
	}
	
	/**
	 * Proceeds to the next step of the attack if it has a next step.
	 * @param rightSide : true if the attack is regular, false if the attack is reversed.
	 * @param position : position of the attacker
	 * @return Whether the attack was finished.
	 */
	abstract boolean nextStep (boolean rightSide, Vector2 position, CommandType command);
	
	/**
	 * Reset the attack.
	 */
	abstract void reset (boolean rightSide);
	
	/**
	 * Affect the attack's effects on the character if the character is not invincible.
	 * @param rightSide : true if the attack is regular, false if the attack is reversed.
	 * @param c : the attacked character.
	 */
	abstract int hitCharacter (boolean rightSide, Character attacker, Character target);
}

