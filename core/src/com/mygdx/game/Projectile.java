package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	private float currentTime;
	private final float totalTime;
	private boolean hit;
	private final boolean destroyOnContact;
	private final Projectile next;
	private final Animator frames;
	
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
	 * @param totalTime Total time in the throw state.
	 * @param destroyOnContact If true, the projectile won't be destroyed on contact.
	 * @param nextProjectile Next projectile created on contact.
	 */
	public Projectile (Animator frames, Vector2 position, ArrayList<Vector2> points, Vector2 initDirection, boolean gravity, int inflictedDamage, float inflictedStun, float inflictedInvincibility, Vector2 inflictedRecoil, float totalTime, boolean destroyOnContact, Projectile nextProjectile) {
		this.position = position;
		this.points = points;
		vect = initDirection;
		this.gravity = gravity;
		this.inflictedDamage = inflictedDamage;
		this.inflictedStun = inflictedStun;
		this.inflictedInvincibility = inflictedInvincibility;
		this.inflictedRecoil = inflictedRecoil;
		this.totalTime = totalTime;
		currentTime = 0;
		hit = false;
		this.destroyOnContact = destroyOnContact;
		next = nextProjectile;
		this.frames = frames;
	}
	
	public ArrayList<Vector2> getPoints () {
		return points;
	}
	
	public boolean hasHit() {
		return hit;
	}
	
	public Projectile getNext () {
		return next;
	}
	
	public boolean movingRight () {
		return vect.x > 0;
	}
	
	public TextureRegion getFrame () {
		return frames.nextFrame();
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
	
	boolean ifDestroy () {
		if ((currentTime > totalTime) || hit) {
			frames.firstFrame();
			return true;
		}
		return false;
	}
	
	void hit () {
		hit = ((destroyOnContact) ? true : hit);
	}
	
	/**
	 * Moves the projectile.
	 */
	public void moveProjectile () {
		float elapsedTime = Gdx.graphics.getDeltaTime();
		if (gravity)
			this.vect.add(0, - (Platformer.GRAVITY));
		position.add(vect.cpy().scl(elapsedTime));
		currentTime += elapsedTime;
	}
	
	/**
	 * @return A copy of the projectile
	 */
	public Projectile copy () {
		return new Projectile (frames, position.cpy(), points, vect.cpy(), gravity, inflictedDamage, inflictedStun, inflictedInvincibility, inflictedRecoil.cpy(), totalTime, destroyOnContact, (next != null) ? next.copy() : null);
	}
	
	public Projectile modAngleDirection (float angle) {
		vect.set(	(float) (Math.cos (angle) * vect.x - Math.sin (angle) * vect.y), 
					(float) (Math.sin (angle) * vect.x + Math.cos (angle) * vect.y));
		return this;
	}
	
	/**
	 * @return A reversed copy of the projectile
	 */
	public Projectile inversion () {
		ArrayList<Vector2> newPoints = new ArrayList<>();
		
		for (Vector2 point: points)
			newPoints.add (point.cpy().set(-point.x, point.y));
		
		return new Projectile (frames, position.cpy().set(-position.x, position.y), newPoints, vect.cpy().set(-vect.x, vect.y), gravity, inflictedDamage, inflictedStun, inflictedInvincibility, inflictedRecoil.cpy().set(-inflictedRecoil.x, inflictedRecoil.y), totalTime, destroyOnContact, (next != null) ? next.inversion() : null);
	}
	
	public void hitCharacter (Character c) {
		if (c.isInvincible() == false) {
			if ((c.getGuard() && (((vect.x > 0) == (!c.faceRight)) || (vect.x < 0) == (c.faceRight)))) {
				c.setStun(inflictedStun / 3);
				c.addForce(inflictedRecoil.cpy().scl(1/3f));
				c.consumeGuard(inflictedDamage);
			}
			else { 
				c.addForce(inflictedRecoil);
				c.inflictDamage (inflictedDamage);
				c.setStun (inflictedStun);
			}
			c.setInvincibility(inflictedInvincibility);
//			c.addSpecial (Attack.SPECIALHIT);
//			System.out.println("Hit !");
			if (destroyOnContact)
				hit = true;
		}
	}
	
	public boolean checkHitAnyCharacter (ArrayList<Character> characters, Character attacker) {
		return 	characters	.stream()
							.filter(e -> (e != attacker))
							.anyMatch(e -> checkHitboxCharacterCollision(getPoints(), position, e));
	}
}
