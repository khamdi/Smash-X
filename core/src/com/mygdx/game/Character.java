package com.mygdx.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BonusOnMap.BonusType;
import com.mygdx.game.Buff.BuffType;

public class Character {
	protected static final int DASH = 0;
	protected static final int PROJECTILE = 1;
	protected static final int JABS1 = 2;
	protected static final int JABS2 = 3;
	protected static final int JABS3 = 4;
	protected static final int ATTACKSIDE = 5;
	protected static final int ATTACKUP = 6;
	protected static final int ATTACKDOWN = 7;
	protected static final int SPE25 = 8;
	protected static final int SPE50 = 9;
	protected static final int SPE100 = 10;
	protected static final int SPECIALMOVE = 11;
	
	
	private static final float ACCELERATIONMAX = 1.5f;
	
	/**
	 * Maximum special points of the character.
	 */
	public static final int SPECIALMAX = 100;
	
	/**
	 * Maximum guard points of the character.
	 */
	public static final int GUARDMAX = 200;
	
	/**
	 * Recovered guard points of the character.
	 */
	public static final int GUARDRECOVER = 1;
	
	/**
	 * Current special energy of the character.
	 */
	int specialPoints;
	
	/**
	 * Current guard of the character.
	 */
	int guardPoints;
	
	protected Animator walkAnimation;
	protected Animator hitAnimation;
	
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
	 * Invincibility time left of the character.
	 */
	protected float invisibility;
	
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
	protected boolean guard;
	protected boolean faceRight;

	protected CommandType command;
	
	/* To set in arrays : Special composed attacks, then normal attacks */
	protected ArrayList<Attacks> attacks; /* SpNeutral, SpUp, SpDown, GrNeutral, GrSide, GrUp, GrDown, Air, Move */
	protected int activatedAttack;
	
	ArrayList<ThrowProjectile> projectiles; /* Array of all throwable projectiles */
	
	LinkedList<Buff> buffs;
	
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

		
	public Character (float x, float y,float xHitbox, float hitboxWidth, float hitboxHeight, int healthMax, int startSpecial, float weight, float speed, int nbJumpMax, boolean flying) {
		this.position = new Vector2 (isNotNegative (x), isNotNegative (y));
		this.healthMax = isNotNegative (healthMax);
		this.health = this.healthMax;
		this.specialPoints = startSpecial;
		this.guardPoints = GUARDMAX;
		
		
		this.hitbox = new Rectangle(xHitbox, y, hitboxWidth, hitboxHeight);
		
		this.weight = isNotNegative(weight);
		
		this.stun = 0f;
		this.invincibility = 0f;
		this.invisibility = 0f;
		
		this.moving = false;
		this.onFloor = false;

		this.speed = isNotNegative ( speed );

		this.faceRight = true;
		
		this.nbJumpMax = isNotNegative (nbJumpMax);
		this.nbJumpLeft = this.nbJumpMax;
		
		this.jumpCapacity = BASICJUMPCAPACITY;
		
		/* Test */
		this.attacks = new ArrayList<> ();
		
		projectiles = new ArrayList<>();
		
		buffs = new LinkedList<>();

		vectDir = new Vector2();
		
		this.direction = 3;
		this.flying = flying;
	}
	
	public LinkedList<Buff> getBuff(){
		return this.buffs;
	}
	
	/**
	 * Set the character on the floor.
	 */
	public void setOnFloor(){
		this.onFloor = true;
	}
	
	CommandType getCommandType () {
		return command;
	}
	
	public int numberOfProjectiles () {
		return projectiles.stream().map(e -> e.projectilesThrown.size()).reduce(0, Integer::sum);
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
		float limitMax = ACCELERATIONMAX * (1 + bonusValue(BuffType.SPD));
		if (this.vectDir.x < limitMax * this.speed){
			vectDir.add(speed, 0);
		}
		else
			this.vectDir.x = limitMax * this.speed;
		setFaceRight();
	 	direction = 3;
	}
	
	/**
	 * Add a left-oriented vector to the move vector.
	 */
	public void moveLeft(){
		float limitMax = ACCELERATIONMAX * (1 + bonusValue(BuffType.SPD));
		if (this.vectDir.x > limitMax * -this.speed){
			vectDir.add(-speed, 0);
		}
		else
			this.vectDir.x = limitMax * -this.speed;
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
	 * Set the character's invisibility to a certain time.
	 * @param time : The set time in seconds.
	 */
	public void setInvisibility (float time) {
		invisibility = time;
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

	public float getSpecial() {
		return specialPoints;
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
	
	public boolean getGuard () {
		return guard;
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
	 * @return Whether the character is INVISIBLE.
	 */
	public boolean isInvisible() {
		return (this.invisibility > 0);
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
		 	vectDir.add(0, jumpCapacity * (1 + bonusValue(BuffType.SPD )));
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
		this.specialPoints += special;
		
		if (this.specialPoints > SPECIALMAX)
			this.specialPoints = SPECIALMAX;
	}

	/**
	 * Makes the guard switch to hold.
	 * @param hold
	 */
	public boolean guard (boolean hold) {
		if (guardPoints > 0) {
			if (guard = hold) {
				guardPoints--;
				return true;
			}
		}
		else {
			guard = false;
		}
		return false;
	}
	
	public int recoverGuard () {
		if ((guardPoints += GUARDRECOVER) > GUARDMAX)
			guardPoints = GUARDMAX;
		return guardPoints;
	}
	
	public void consumeGuard (int amount) {
		if (guard && (guardPoints -= amount) < 0)
			guardPoints = 0;
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
	 * Decreases the invisibility time by a certain time.
	 * @param time : The time subtracted from the invisibility time in milliseconds.
	 */
	public void coolInvisibility (float time) {
		invisibility -= time;
		if (invisibility < 0)
			invisibility = 0;
	}
	
	/**
	 * Make the character lose health.
	 * @param inflictedDamage : Lost health
	 */
	public void inflictDamage (int inflictedDamage) {
		health -= inflictedDamage;
	}
	/*
	void cancelAttack () {
		attacks.get (activatedAttack).getCurrentPhase().reset ();
		attacks.get (activatedAttack).reset ();
	}
	*/
	
	
	
	
	protected static float isNotNegative (float number){
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
	
	public void heal(int healValue){
		this.health += healValue;
		
		if (health>this.healthMax)
			health = healthMax;
	}
	
	public void addSpecialPoint(int sp){
		this.specialPoints += sp;
		if (this.specialPoints > SPECIALMAX)
			this.specialPoints = SPECIALMAX;
	}

	public void checkIfPickUpBonus(BonusOnMap bonus){
		BonusType type = null;
		
		if(	Collisions.checkHitboxesCollision(this.hitbox, bonus.getHitBox()) ){
			System.out.println("J'ai chopp� un bonus de type : " + (type = bonus.getBonusType()) );
			bonus.setOutOfTime();
			
			if (type == BonusType.HP_H || type == BonusType.HP_M || type == BonusType.HP_L){
				this.heal( bonus.getHealthValue(type) );
				System.out.println("Guerison de: " + bonus.getHealthValue(type) );
			}
			else if (type == BonusType.SP_H || type == BonusType.SP_M || type == BonusType.SP_L){
				this.addSpecialPoint( bonus.getSpecialValue(type) );
				System.out.println("Sp�cial de :  " + bonus.getSpecialValue(type) );
			}
			else{ /*if(type == BonusType.ATK_L || type == BonusType.ATK_M || type == BonusType.ATK_H)*/
				Buff bufftmp = new Buff( Buff.whichBuffType(type), Buff.whichBuffValue(type) );
				this.buffs.removeIf( e -> e.equals(bufftmp));
				this.buffs.addFirst ( bufftmp);
				
//				this.buffs.addFirst( new Buff( Buff.whichBuffType(type), Buff.whichBuffValue(type) ) );
			}
		}
	}
	
	public void generateBonusOnMapIfIsDead(ArrayList<BonusOnMap> bonuses, Random randGen){
		if (isDead()){
			bonuses.add( new BonusOnMap(this.getPosition().add(0, this.getHitbox().height), 
					new Vector2(randGen.nextInt(4000) - 2000, 3000 ), BonusType.values()[randGen.nextInt(BonusType.values().length)]));
		}
			
	}
	
	public float bonusValue (BuffType type) {
		return buffs.stream().filter(e -> e.isOfType(type)).map(e -> e.getValue()).reduce(0f, Float::sum);
	}
	
	public void displayStats (ShapeRenderer shapeRenderer,CameraHandler camHandler, OrthographicCamera camera, boolean boolHitbox, boolean isPlayer1, boolean isPlayer2 ) {
		Vector2 origin = new Vector2();
		float barWidth;
		float barHeight;
		float rightOrLeft;
		
		if(isPlayer1){
			barWidth = (Gdx.graphics.getWidth()*0.4f) * camera.zoom;
			barHeight = 12 * camera.zoom;
			origin.set(camera.position.x -( camHandler.getEffectiveViewPortWidth()/2), camera.position.y + ( camHandler.getEffectiveViewPortHeight()/2) - barHeight);
			rightOrLeft = 1;
		}
		else if(isPlayer2){
			barWidth = -(Gdx.graphics.getWidth()*0.4f) * camera.zoom;
			barHeight = 12 * camera.zoom;
			origin.set(camera.position.x +( camHandler.getEffectiveViewPortWidth()/2), camera.position.y + ( camHandler.getEffectiveViewPortHeight()/2) - barHeight);
			rightOrLeft = -1;
		}
		else{
			barWidth = this.hitbox.width ;
			barHeight = 20 ;
			origin.set(hitbox.x, hitbox.y + hitbox.height + 50);
			rightOrLeft = 1;
		}
		
		/* Central point which correspond to the coordinates of the characters */
//		shapeRenderer.setColor(Color.PINK);
//		shapeRenderer.circle(position.x, position.y, 10);		
		
		
		/* Life */
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(origin.x, origin.y, barWidth, barHeight);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(origin.x , origin.y , (float) health / healthMax * barWidth, barHeight);
		
		/* Special */
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(origin.x, origin.y - (barHeight), (getHitbox().width), 15);
		shapeRenderer.setColor(Color.CYAN);
		shapeRenderer.rect(origin.x, origin.y - (barHeight), (float) specialPoints / Character.SPECIALMAX * barWidth, barHeight);
		
		/* Guard */
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(origin.x, origin.y - (barHeight * 2), (getHitbox().width), 15);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.rect(origin.x, origin.y - (barHeight * 2), (float) guardPoints / Character.GUARDMAX * barWidth, barHeight);
		
		/* Move vector */
//		shapeRenderer.setColor(Color.BLACK);
//		shapeRenderer.rectLine(new Vector2(getPosition().cpy()), new Vector2(getPosition().x + getVectDir().cpy().x ,getPosition().y + getVectDir().cpy().y) , 10);
		
		
//		/*  hitboxes */
		if(boolHitbox){
			shapeRenderer.setColor(Color.FOREST);
			shapeRenderer.rect(getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);
		}
//		
	}
	
	
	
	public void displayCharacter(ShapeRenderer shapeRenderer, SpriteBatch batch){
////		/*  hitboxes */
//		shapeRenderer.setColor(Color.FOREST);
//		shapeRenderer.rect(getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);
//		shapeRenderer.end();
//		shapeRenderer.begin(ShapeType.Filled);
		if(!isInvisible()){
	//		/* Character animation */
			batch.begin();
			TextureRegion frame;
			if (getVectDir().x != 0f || getVectDir().y != 0f) {
	//			batch.draw(walkAnimation[getDirection()].nextFrame(), getHitbox().x, getY());	
				frame = walkAnimation.nextFrame();
							
	//			batch.draw(walkAnimation[0].firstFrame(), getHitbox().x-(185), getY());
	
	
			} 
			else {
	//			batch.draw(walkAnimation[getDirection()].firstFrame(), getHitbox().x, getY());
				frame = walkAnimation.firstFrame();
			}	
			
			if(isFacingRight() == false){
				frame.flip(true, false);
				batch.draw(frame, getX() - frame.getRegionWidth()/2, getY());	
				frame.flip(true, false);
			}
			else
				batch.draw(frame, getX() - frame.getRegionWidth()/2, getY());	
			
			batch.end();
		}
//		else{
//			shapeRenderer.begin(ShapeType.Filled);
//			shapeRenderer.setColor(Color.PINK);
//			shapeRenderer.rect(getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);
//			
//			
//			/* Life */
//			shapeRenderer.setColor(Color.BLACK);
//			shapeRenderer.rect(getX() - (getHitbox().width/2), getY() + getHitbox().height + 70, (getHitbox().width), 15);
//			shapeRenderer.setColor(Color.GREEN);
//			shapeRenderer.rect(getX() - (getHitbox().width/2), getY() + getHitbox().height + 70, (float) health / healthMax * (getHitbox().width), 15);
//			
//			shapeRenderer.end();
//		}
	}
	
	void regeneration (){
		this.health = healthMax;
		this.guardPoints = GUARDMAX;
	}
	
	
	
	public void drawCharacterHit (SpriteBatch batch) {
		TextureRegion frame = hitAnimation.nextFrame();
		
		batch.begin ();
		if (!isFacingRight())
			frame.flip(true, false);
		batch.draw(frame, getX() - frame.getRegionWidth()/2, getY());
		if (!isFacingRight())
			frame.flip(true, false);
		batch.end ();
	}
	
}
