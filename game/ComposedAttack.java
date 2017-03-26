package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class ComposedAttack extends AbstractAttacks {
	/**
	 * Creates a Composed Attack
	 * @param attackPhases the list composed of attack phases.
	 */
	public ComposedAttack (AttackPhase... attackPhases) {
		phases = new ArrayList<> ();
		for (AttackPhase attackPhase : attackPhases) {
			phases.add(attackPhase);
		}
		indexPhase = 0;
		active = false;
	}
	
	/**
	 * @param rightSide : true if the attack is regular, false if the attack is reversed.
	 */
	public void init (boolean rightSide) {
		if (active == false)
			this.rightSide = rightSide;
		active = true;
	}
	
	@Override
	public boolean isConditional () {
		return false;
	}
	
	@Override
	public String toString () {
		return "Phase " + (indexPhase + 1) + ": " + phases.get(indexPhase).toString();
	}
	
	/**
	 * @return whether the attack has a next step.
	 */
	public boolean hasNextStep () {
		return (indexPhase < phases.size());
	}
	
	/**
	 * Applies the next step of the composed attack if the composed attack is not finished.
	 * @return whether the attack is finished.
	 */
	public boolean nextStep (Vector2 position) {
		if (hasNextStep ()) {
			active = true;
			if (phases.get(indexPhase).nextStep(this.rightSide, position) == false) {
				indexPhase++;
				return nextStep(position);
			}
			else
				return true;
		}
		reset ();
		return false;
	}
}
