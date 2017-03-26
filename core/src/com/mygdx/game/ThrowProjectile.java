package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class ThrowProjectile extends AttackPhase {
	final ArrayList<Projectile> projectiles;
	final ArrayList<Projectile> projectilesThrown;
	
	int command;
	
	private static final int MASKRIGHT = 1;
	private static final int MASKLEFT = 1 << 1;
	private static final int MASKUP = 1 << 2;
	private static final int MASKDOWN = 1 << 3;
	/**
	 * Creates a attack in which a projectile will be thrown.
	 * @param inflictedStun Inflicted stun upon hit.
	 * @param inflictedDamage Inflicted damage upon hit.
	 * @param inflictedRecoil Inflicted recoil upon hit.
	 * @param inflictedInvincibility Inflicted invincibility upon hit.
	 * @param totalTime Time needed to throw a projectile.
	 * @param points Array of hitbox points of the projectile.
	 * @param move Move vector added to the player.
	 * @param gravity Whether the projectile is affected by the gravity.
	 * @param position The position of the projectile.
	 * @param initDirection The initial direction of the projectile.
	 */
	public ThrowProjectile (ArrayList<Projectile> projectiles, float totalTime, Vector2 move) {
		this.projectiles = projectiles;
		this.totalTime = isPositive (totalTime);
		this.active = false;
		this.points = new ArrayList<> ();
		this.move = move;
		this.projectilesThrown = new ArrayList<> ();
	}
	
	private void updateCommand (boolean rightSide, CommandType cmd) {
		if (Gdx.input.isKeyPressed(cmd.right))
			command |= MASKRIGHT;
		if (Gdx.input.isKeyPressed(cmd.left))
			command |= MASKLEFT;
		if (Gdx.input.isKeyPressed(cmd.up))
			command |= MASKUP;
		if (Gdx.input.isKeyPressed(cmd.down))
			command |= MASKDOWN;
	}
	
	private boolean getFlag (int mask) {
		return (command & mask) == mask;
	}
	
	private int invertValue (boolean rightSide, int value) {
		return (rightSide) ? value : -value;
	}
	
	private int applyCommand (boolean rightSide) {
		if (getFlag(MASKRIGHT) || getFlag(MASKLEFT)) {
			if (getFlag(MASKUP))	return invertValue(rightSide, 1);
			if (getFlag(MASKDOWN))	return invertValue(rightSide,-1);
			return 0;
		}
		if (getFlag(MASKUP))	return invertValue (rightSide, 2);
		if (getFlag(MASKDOWN))	return invertValue (rightSide, -2);
		return 0;
	}

	boolean nextStep (boolean rightSide, Vector2 position, CommandType command) {
		if (hasNextStep()) {
			init ();
			updateCommand(rightSide, command);
			elapsedTime = Gdx.graphics.getDeltaTime();
			currentTime += elapsedTime;
			return true;
		}
		addProjectile((rightSide) ? projectiles.get(0).copy().modAngleDirection(applyCommand(rightSide) * (float) (Math.PI/4)) : projectiles.get(0).inversion().modAngleDirection(applyCommand(rightSide) * (float) (Math.PI/4)), position);


		reset(rightSide);
		return false;
	}
	
	/**
	 * Adds a projectile in the array composed of all the projectiles created by this attack.
	 * @param p : the projectile created
	 * @return true
	 */
	private boolean addProjectile (Projectile p, Vector2 position) {
		if (p != null) {
			p.position.add(position);
			return projectilesThrown.add(p);
		}
		return false;
	}
	
	private boolean addProjectileNext (Projectile p, Vector2 position) {
		if (p != null && p.hasHit()) {
			return addProjectile (p.getNext(), position);
		}
		return false;
	}
	
	void addAll () {
		projectilesThrown.forEach(e -> addProjectileNext(e, e.position));
	}
	
	/**
	 * Remove a projectile in the array composed of all the projectiles created by this attack.
	 * @param p : the projectile to remove
	 * @return true if this list contained the specified element.
	 */
	boolean removeProjectile (Projectile p) {
		return projectiles.remove(p);
	}
	
	@Override
	void reset (boolean rightSide) {
		currentTime = 0;
		active = false;
		command = 0;
	}
	
	int hitCharacter (boolean rightSide, Character attacker, Character target) {
		return 0;
	}
}
