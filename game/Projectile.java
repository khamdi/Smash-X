package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
	Vector2 position;
	private ArrayList<Vector2> points;
	private final Vector2 vect;
	private final boolean gravity;
	private final int inflictedDamage;
	private final float inflictedStun;
	private final float inflictedInvincibility;
	private Vector2 inflictedRecoil;

	/**
	 * Creates a projectile.
	 * @param position Position of the projectile.
	 * @param points Array of the hitbox points of the projectile.
	 * @param initDirection The initial vector of the projectile.
	 * @param gravity Whether the projectile is affected by the gravity.
	 * @param inflictedDamage Inflicted damage upon hit.
	 * @param inflictedStun Inflicted stun upon hit.
	 * @param inflictedInvincibility Inflicted invincibility upon hit.
	 * @param inflictedRecoil Inflicted recoil upon hit.
	 */
	public Projectile (Vector2 position, ArrayList<Vector2> points, Vector2 initDirection, boolean gravity, int inflictedDamage, float inflictedStun, float inflictedInvincibility, Vector2 inflictedRecoil) {
		this.position = position;
		this.points = points;
		vect = initDirection;
		this.gravity = gravity;
		this.inflictedDamage = inflictedDamage;
		this.inflictedStun = inflictedStun;
		this.inflictedInvincibility = inflictedInvincibility;
		this.inflictedRecoil = inflictedRecoil;
	}
	
	public ArrayList<Vector2> getPoints () {
		return points;
	}
	
	@Override
	public String toString () {
		return "Damage : " + inflictedDamage + "\n"
			+ "Stun : " + inflictedStun + "\n"
			+ "Invincibility : " + inflictedInvincibility + "\n"
			+ "Recoil : " + inflictedRecoil + "\n"
			+ "Gravity : " + ((gravity) ? "ON" : "OFF") + "\n"
			+ "Position : " + position + "\n"
			+ "Vect : " + vect;
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
		return points.parallelStream().anyMatch(e -> AttackPhase.checkPointCharacterCollision(e.cpy().add(0, -32), attackerPosition, target));
	}
	
	/**
	 * Moves the projectile.
	 */
	public void moveProjectile () {
		if (gravity)
			this.vect.add(0, - (Platformer.GRAVITY));
		position.add(vect.cpy().scl(Gdx.graphics.getDeltaTime()));
	}
	
	/**
	 * @return A copy of the projectile
	 */
	public Projectile copy () {
		return new Projectile (position.cpy(), points, vect.cpy(), gravity, inflictedDamage, inflictedStun, inflictedInvincibility, inflictedRecoil.cpy());
	}
	
	/**
	 * @return A reversed copy of the projectile
	 */
	public Projectile inversion () {
		ArrayList<Vector2> newPoints = new ArrayList<>();
		
		for (Vector2 point: points)
			newPoints.add (point.cpy().set(-point.x, point.y));
		
		return new Projectile (position.cpy().set(-position.x, position.y), newPoints, vect.cpy().set(-vect.x, vect.y), gravity, inflictedDamage, inflictedStun, inflictedInvincibility, inflictedRecoil.cpy().set(-inflictedRecoil.x, inflictedRecoil.y));
	}
	
	public void hitCharacter (Character c) {
		if (c.isInvincible() == false) {
			c.inflictDamage(inflictedDamage);
			c.setStun(inflictedStun);
			c.setInvincibility(inflictedInvincibility);
			c.addForce(inflictedRecoil);
			System.out.println("Hit !");
		}
	}
	
	public boolean checkHitAnyCharacter (ArrayList<Character> characters, Character attacker) {
		return 	characters	.stream()
							.filter(e -> (e != attacker))
							.anyMatch(e -> checkHitboxCharacterCollision(getPoints(), position, e));
	}
}
