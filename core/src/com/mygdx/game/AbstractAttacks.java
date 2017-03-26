package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractAttacks implements Attacks {
	/**
	 * The coefficient value of the critical damage.
	 */
	static final float CRITDAMAGE = 1.5f;
	/**
	 * Array of phases of this attack.
	 */
	protected ArrayList<AttackPhase> phases;
	/**
	 * Index of the current phase.
	 */
	protected int indexPhase;
	/**
	 * Whether the attack is active.
	 */
	protected boolean active;
	/**
	 * Whether the attack should be played regular or reversed.
	 */
	protected boolean rightSide;
	/**
	 * Animation of the attack.
	 */
	protected Animator frames;
	
	/**
	 * @return whether the attack is active.
	 */
	public boolean isActive () {
		return active;
	}
	
	/**
	 * @return the current attack phase.
	 */
	public AttackPhase getCurrentPhase () {
		return phases.get(indexPhase);
	}
	
	public float getTriggerStun () {	
		return phases.get(indexPhase).activateStunTrigger();		
	}
	
	public TextureRegion getCurrentFrame () {
		return frames.nextFrame();
	}
	
	/**
	 * @return the move vector of the current phase.
	 */
	public Vector2 move () {
		Vector2 move = phases.get(indexPhase).move;
		return move.cpy().set(((rightSide) ? 1 : -1) * move.x, move.y);
	}
	
	/**
	 * Reset the composed attack.
	 */
	public void reset () {
		frames.firstFrame();
		indexPhase = 0;
		active = false;
	}
}
