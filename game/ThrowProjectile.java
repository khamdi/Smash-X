package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ThrowProjectile extends AttackPhase {
	final Projectile projectile;
	final ArrayList<Projectile> projectiles;
/*	final Vector2 position;
	final ArrayList<Vector2> hitboxProjectile;
	final Vector2 initDirection;
	final boolean gravity;
	final int inflictedDamage;
	final float inflictedStun;
	final float inflictedInvincibility;
	final Vector2 inflictedRecoil;
*/	
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
	public ThrowProjectile (float inflictedStun, int inflictedDamage, Vector2 inflictedRecoil, float inflictedInvincibility, float totalTime, ArrayList<Vector2> points, Vector2 move, boolean gravity, Vector2 position, Vector2 initDirection, Character character) {
		this.projectile = new Projectile (position.cpy(), points, initDirection.cpy(), gravity, inflictedDamage, inflictedStun, inflictedInvincibility, inflictedRecoil.cpy());
		this.totalTime = isPositive (totalTime);
		this.active = false;
		this.points = new ArrayList<> ();
		this.move = move;
		this.projectiles = new ArrayList<> ();
	}

	boolean nextStep (boolean rightSide, Vector2 position) {
		if (hasNextStep()) {
			init ();
			elapsedTime = Gdx.graphics.getDeltaTime();
			currentTime += elapsedTime;
			return true;
		}
		addProjectile((rightSide) ? projectile.copy() : projectile.inversion(), position);
		reset();
		return false;
	}
	
	/**
	 * Adds a projectile in the array composed of all the projectiles created by this attack.
	 * @param p : the projectile created
	 * @return true
	 */
	boolean addProjectile (Projectile p, Vector2 position) {
		p.position.add(position);
		return projectiles.add(p);
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
	void reset () {
		currentTime = 0;
		active = false;
	}
	
	int hitCharacter (boolean rightSide, Character c) {
		return 0;
	}
}
