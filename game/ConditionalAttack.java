package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

/**
 * A conditional attack works like a if statement for attacks.<br>
 * It is created with three attacks : the first one is the conditional attack, and the others are activated as following :<br>
 * <li> If the conditional attack hits a character, the second attack is activated.
 * <li> Otherwise the third attack is activated.<br>
 * <br>
 * It allows miscellaneous forms of cool, stylish attacks.<br>
 * It has a form of a composed attack, since it is composed of several attacks.
 * <br>
 * 
 * @author nakaze
 */
public class ConditionalAttack extends AbstractAttacks {
	boolean hit;
	
	public ConditionalAttack (AttackPhase conditional, AttackPhase onhit, AttackPhase missed) {
		phases = new ArrayList <> ();
		phases.add(conditional);
		phases.add(onhit);
		phases.add(missed);
	}
	
	@Override
	public boolean isConditional() {
		return true;
	}
	
	/**
	 * @param rightSide : true if the attack is regular, false if the attack is reversed.
	 */
	public void init (boolean rightSide) {
		if (active == false) {
			this.rightSide = rightSide;
			hit = false;
		}
		active = true;
	}

	@Override
	public boolean hasNextStep() {
		return (indexPhase < phases.size() - ((hit) ? 1 : 0));
	}
	
	public void activateOnHit () {
		hit = true;
	}
	
	@Override
	public boolean nextStep (Vector2 position) {
		if (hasNextStep ()) {
			active = true;
			if (phases.get(indexPhase).nextStep(this.rightSide, position) == false) {
				if (!hit)
					indexPhase += 2;
				else
					indexPhase++;
				
				if (hasNextStep()) {
					phases.get(indexPhase).nextStep(this.rightSide, position);
					return true;
				}
			}
			else
				return true;
		}
		reset ();
		return false;
	}
	
	@Override
	public void reset () {
		super.reset();
		hit = false;
	}
}
