package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Character {
	protected static final int PROJECTILE = 0;
	protected static final int JABS1 = 1;
	protected static final int JABS2 = 2;
	protected static final int JABS3 = 3;
	protected static final int ATTACKSIDE = 4;
	protected static final int ATTACKUP = 5;
	protected static final int ATTACKDOWN = 6;
	protected static final int SPE25 = 7;
	protected static final int SPE50 = 8;
	protected static final int SPE100 = 9;
	
	
	private static final float ACCELERATIONMAX = 1.5f;
	
	/**
	 * Maximum special points of the character.
	 */
	public static final int SPECIALMAX = 100;
	
	/**
	 * Current special energy of the character.
	 */
	int special;
	
	/**
	 * Coordinates of the character.
	 */
	protected Vector2 position;
	
	/**
	 * Lateral speed of the character.
	 */
	protected final float speed;
	
	/**
	 * Hitbox of the character.
	 */
	protected Rectangle hitbox;
	
	/**
	 * Max HP of the character.
	 */
	protected final int healthMax;
	
	/**
	 * Current health of the character.
	 */
	protected int health;
	
	/**
	 * The weight of the character.
	 * It is used in the fall speed calculation.
	 */
	protected final float weight;
		
	/**
	 * Stun time left of the character.
	 */
	protected float stun;
	
	/**
	 * Invincibility time left of the character.
	 */
	protected float invincibility;
	
	/**
	 * The move vector of the character.
	 */
	protected Vector2 vectDir;
	
	/**
	 * The jump height of the character.
	 */
	private float jumpCapacity;
	
	/**
	 * The maximum number of jumps of the character.
	 */
	protected int nbJumpMax;
	
	/**
	 * The number of jumps left of the character.
	 */
	private int nbJumpLeft;
	
	protected boolean moving;
	
	protected int direction;
	
	private boolean onFloor;

	protected boolean flying;
	
	protected boolean faceRight;
	
	/* To set in arrays : Special composed attacks, then normal attacks */
	protected ArrayList<Attacks> attacks; /* SpNeutral, SpUp, SpDown, GrNeutral, GrSide, GrUp, GrDown, Air, Move */
	protected int activatedAttack;
	
	ArrayList<ThrowProjectile> projectiles; /* Array of all throwable projectiles */

	
	/**
	 * Creates a character.
	 * @param x x position of the character
	 * @param y y position of the character
	 * @param healthMax maximum health of the character
	 * @param startSpecial amount of special points given at the creation of the character.
	 * @param weight weight of the character, 1 is standard
	 * @param nbJumpMax maximum number of jumps in a row of the character
	 */
	static float BASICSPEED = 1500;
	static float BASICJUMPCAPACITY = 2500f;

	public Character (float x, float y, float hitboxWidth, float hitboxHeight, int healthMax, int startSpecial, float weight, int nbJumpMax, boolean flying) {
		this.position = new Vector2 (isNotNegative (x), isNotNegative (y));
		this.healthMax = isNotNegative (healthMax);
		this.health = this.healthMax;
		this.special = startSpecial;
	
		this.hitbox = new Rectangle(x - hitboxWidth/2, y, hitboxWidth, hitboxHeight);
		
		this.weight = isNotNegative(weight);
		
		this.stun = 0f;
		this.invincibility = 0f;
		
		this.moving = false;
		this.onFloor = false;

		this.speed = isNotNegative (BASICSPEED * (1/weight) );

		this.faceRight = true;
		
		this.nbJumpMax = isNotNegative (nbJumpMax);
		this.nbJumpLeft = this.nbJumpMax;
		
		this.jumpCapacity = BASICJUMPCAPACITY;
		
		/* Test */
		this.attacks = new ArrayList<> ();
		
		projectiles = new ArrayList<>();

		vectDir = new Vector2();
		
		this.direction = 3;
		this.flying = flying;
	}
	
	
	/**
	 * Set the character on the floor.
	 */
	public void setOnFloor(){
		this.onFloor = true;
	}
	
	/**
	 * Set the character on the air.
	 */
	public void setOnAir() {
		this.onFloor = false;
	}
	
	public boolean isOnFloor() {
		return this.onFloor;
	}
	
	public boolean isOnAir() {
		return (!this.onFloor);
	}
	
	
	/**
	 * Add a right-oriented vector to the move vector.
	 */
	public void moveRight(){
		if (this.vectDir.x < ACCELERATIONMAX * this.speed){
			vectDir.add(speed, 0);
		}
		setFaceRight();
	 	direction = 3;
	}
	
	/**
	 * Add a left-oriented vector to the move vector.
	 */
	public void moveLeft(){
		if (this.vectDir.x > ACCELERATIONMAX * -this.speed){
			vectDir.add(-speed, 0);
		}
		setFaceLeft();
	 	direction = 1;
	}
	
	/**
	 * Set the character to make him face right.
	 */
	public void setFaceRight(){
		this.faceRight = true;
	}
	
	/**
	 * Set the character to make him face left.
	 */
	public void setFaceLeft(){
		this.faceRight = false;
	}
	
	/**
	 * Set the character's position.
	 */
	public void setPosition (float x, float y) {
		this.position.set(isNotNegative(x), isNotNegative(y));
	}
	
	/**
	 * Set the character's directional vector..
	 */
	public void setVectDir(float x, float y){
		this.vectDir.set(x, y);
	}	
	
	/**
	 * Set the character's directional vector..
	 */
	public void setVectDir(Vector2 vectDir){
		this.vectDir.set(vectDir);
	}
	
	/**
	 * Set the character's x coordinate.
	 */
	public void setX(float x){
		this.position.set(isNotNegative(x), position.y);
	}
	
	/**
	 * Set the character's y coordinate.
	 */
	public void setY(float y){
		this.position.set(position.x, isNotNegative(y));
	}
	
	public void setMoving (boolean moving){
		this.moving = moving;
	}
	
	/**
	 * Set the character's invincibility to a certain time.
	 * @param time : The set time in seconds.
	 */
	public void setInvincibility (float time) {
		this.invincibility = time;
	}
	
	/**
	 * Set the character's stun to a certain time.
	 * @param time : The set time in seconds.
	 */
	public void setStun (float time) {
		stun = time;
	}
	
	/**
	 * @return The character's x coordinate.
	 */
	public float getX() {
		return position.x;
	}

	/**
	 * @return The character's y coordinate.
	 */
	public float getY() { 
		return position.y;
	}
	
	/**
	 * @return The character's position.
	 */
	public Vector2 getPosition() {
		return position;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public int getHealthMax() {
		return healthMax;
	}
	
	public int getHealth() {
		return health;
	}
	
	public float getWeight() {
		return weight;
	}
	
	
	public int getNbJumpLeft() {
		return nbJumpLeft;
	}
	
	public int getNbJumpMax() {
		return nbJumpMax;
	}
	
	/**
	 * Reset the number of jumps left of the character.
	 */
	public void resetJump() {
		nbJumpLeft = this.nbJumpMax;
	}
	
	public Vector2 getVectDir() {
		return vectDir;
	}
	
	public int getActivatedAttack () {
		return activatedAttack;
	}
	
	public Rectangle getHitbox(){
		return hitbox;
	}
	
	
	public boolean isMoving () {
		return moving;
	}
	
	public boolean isFacingRight() {
		return this.faceRight;
	}
	
	public boolean isDead(){
		return (this.health <= 0);
	}
	
	/**
	 * @return Whether the character is stunned.
	 */
	public boolean isStunned () {
		return (this.stun > 0);
	}
	
	/**
	 * @return Whether the character is invincible.
	 */
	public boolean isInvincible() {
		return (this.invincibility > 0);
	}
	
	/**
	 * Make the character jump if it is possible, i.e. the number of jumps left is positive.
	 */
	public void jump() {
		if(nbJumpLeft > 0){
/*			System.out.println("ON PEUT SAUTER de " + jumpCapacity); */
			this.nbJumpLeft --;
			this.setOnAir();
			this.blockVerticalMove();
		 	vectDir.add(0, jumpCapacity);
		}
	}
	
	public void useJump () {
		if (nbJumpLeft > 0) {
			this.nbJumpLeft --;
			this.setOnAir();
		}
	}
	
	/**
	 * Add a vector to the move vector.
	 * @param x : The x coordinate of the vector.
	 * @param y : The y coordinate of the vector.
	 */
	public void addForce (float x, float y) {
		vectDir.add (x, y);
	}
	
	public void addSpecial (int special) {
		this.special += special;
		
		if (this.special > SPECIALMAX)
			this.special = SPECIALMAX;
	}
	
	/**
	 * Add a vector to the move vector.
	 * @param vect : The vector to add.
	 */
	public void addForce (Vector2 vect) {
		vectDir.add(vect);
	}
	
	/**
	 * Add a gravity vector to the move vector.
	 */
	public void affectGravity () {
		if (!flying)
			vectDir.add (0, -Platformer.GRAVITY * weight);
	}

	public void standUp () {
		vectDir.add (0, Platformer.GRAVITY * weight);
	}
	
	/**
	 * Set the move vector to a null vector.
	 */
	public void immobilizeCharacter () {
		vectDir.setZero();
	}
	
	/**
	 * Set the x coordinate of the move vector to 0.
	 */
	public void blockHorizontalMove(){
		vectDir.x = 0;
	}
	
	
	/**
	 * Set the y coordinate of the move vector to 0.
	 */
	public void blockVerticalMove(){
		vectDir.y = 0;
	}
	
	/**
	 * Make the character move.
	 * It actualizes the coordinates of the character.
	 */
	public void moveCharacter () {
		position.add(vectDir.cpy().scl(Gdx.graphics.getDeltaTime())); 
		hitbox.x = position.x - (hitbox.width/2);
		hitbox.y = position.y;
	}
	
	/**
	 * Decreases the stun time by a certain time.
	 * @param time : The time subtracted from the stun time in milliseconds.
	 */
	public void coolStun (float time) {
		stun -= time;
		if (stun < 0)
			stun = 0;
	}
	
	/**
	 * Decreases the invincibility time by a certain time.
	 * @param time : The time subtracted from the invincibility time in milliseconds.
	 */
	public void coolInvincibility (float time) {
		invincibility -= time;
		if (invincibility < 0)
			invincibility = 0;
	}
	
	/**
	 * Make the character lose health.
	 * @param inflictedDamage : Lost health
	 */
	public void inflictDamage (int inflictedDamage) {
		health -= inflictedDamage;
	}
	
	void cancelAttack () {
		attacks.get (activatedAttack).getCurrentPhase().reset ();
		attacks.get (activatedAttack).reset ();
	}
	
	private float isNotNegative (float number){
		if (number < 0)
			throw new IllegalArgumentException(number + "can't be negative.");
		return number;
	}
	
	protected int isNotNegative (int number) {
		if (number < 0) {
			throw new IllegalArgumentException(number + "can't be negative.");
		}
		return number;
	}
	
	public void displayCharacter(ShapeRenderer shapeRenderer, SpriteBatch batch, Animator[] walkAnimation){
		/* Character animation */
		batch.begin();
		if (getVectDir().x != 0f || getVectDir().y != 0f) {
			batch.draw(walkAnimation[getDirection()].nextFrame(), getHitbox().x, getY());	
		} 
		else {
			batch.draw(walkAnimation[getDirection()].firstFrame(), getHitbox().x, getY());
		}	
		batch.end();
		
		/* Central point which correspond to the coordinates of the characters */
		shapeRenderer.setColor(Color.PINK);
		shapeRenderer.circle(getPosition().x, getPosition().y, 10);		
		
		
		/* Life */
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(getX() - (getHitbox().width/2), getY() - 30, (getHitbox().width), 15);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(getX() - (getHitbox().width/2), getY() - 30, (float) health / healthMax * (getHitbox().width), 15);
		
		/* Special */
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(getX() - (getHitbox().width/2), getY() - 50, (getHitbox().width), 15);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(getX() - (getHitbox().width/2), getY() - 50, (float) special / Character.SPECIALMAX * (getHitbox().width), 15);
		
		/* Move vector */
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rectLine(new Vector2(getPosition().cpy()), new Vector2(getPosition().x + getVectDir().cpy().x ,getPosition().y + getVectDir().cpy().y) , 10);
		
		
		/*  hitboxes */
		shapeRenderer.setColor(Color.FOREST);
		shapeRenderer.rect(getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);
	}
	
}
