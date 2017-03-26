package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Delay extends AttackPhase {
	/**
	 * Creates a delay phase.
	 * @param totalTime total duration of the attack.
	 * @param move the move vector applied on the character.
	 */
	public Delay (float totalTime, Vector2 move) {
		this.totalTime = totalTime;
		this.currentTime = 0;
		this.active = false;
		this.points = new ArrayList<> ();
		this.move = move;
	}
	
	boolean nextStep (boolean rightSide, Vector2 position) {
		if (hasNextStep()) {
			init ();
			currentTime += Gdx.graphics.getDeltaTime();
			return true;
		}
		reset ();
		return false;
	}
	
	@Override
	void reset () {
		currentTime = 0;
		active = false;
	}
	
	int hitCharacter (boolean rightSide, Character c) {
		return 0;
	}
}