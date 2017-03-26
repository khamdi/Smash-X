package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public interface Attacks {
	/**
	 * @return Whether the attack is being used.s
	 */
	boolean isActive ();
	/**
	 * @return Whether the algorithm of the attack is conditional or a regular one.
	 */
	boolean isConditional ();
	
	public float getTriggerStun ();

	public TextureRegion getCurrentFrame ();
	
	/**
	 * Activates the attack.
	 * @param rightSide
	 */
	void init (boolean rightSide);
	/**
	 * @return whether the attack is finished.
	 */
	boolean hasNextStep ();
	/**
	 * Proceeds to the next step of the attack if the attack isn't finished.
	 * @param position the attacker's position.
	 * @return whether the attack is finished.
	 */
	boolean nextStep (Vector2 position, CommandType commandType);
	/**
	 * Resets the attack. Mostly used at the end of the attack.
	 */
	void reset ();
	
	/**
	 * @return the move vector of the current attack phase.
	 */
	Vector2 move ();
	/**
	 * @return the current attack phase.
	 */
	AttackPhase getCurrentPhase ();
}
