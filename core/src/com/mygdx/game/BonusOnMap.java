package com.mygdx.game;

 import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BonusOnMap {
	enum BonusType {
		ATK_L,
		ATK_M,
		ATK_H,
		
		DEF_L,
		DEF_M,
		DEF_H,
		
		SPD_L,
		SPD_M,
		SPD_H,
		
		HP_L,
		HP_M,
		HP_H,
		
		SP_L,
		SP_M,
		SP_H;
	}

	private Vector2 position;
	private Rectangle hitBox;
	private Vector2 vectDir;
	
//	boolean isOnFloor;
	
	private float timeOnMap;
	private final BonusType type;
	
	static float CONSTTIMEONMAP = 10; //seconde
	static float CONSTWIDTH = 128;
	static float CONSTHEIGHT = 128;
	static float CONSTWEIGHT = 2;

	static private TextureRegion[]	frames;
	static private boolean			loadFrames = false;
	private Animator[] animations;
	private Animator animation;
	
	public BonusOnMap(Vector2 position,Vector2 vectDir, BonusType type) {
		this.position = position;
		hitBox = new Rectangle(position.x - (CONSTWIDTH/2), position.y , CONSTWIDTH, CONSTHEIGHT);
		
		loadFrames();
		animations = new Animator (20, 15, "Bonus/Sprite.png", 1/60f).createAnimations(15);
		
		this.type = type;
		this.timeOnMap = 0;
		this.vectDir = vectDir;
//		isOnFloor = false;
		this.animation = getAnimations(type);
	}
	
	private void loadFrames () {
		if (!loadFrames) {
			frames = Animator.getFrames(20, 15, "Bonus/Sprite.png");
			loadFrames = true;
		}
	}
	
	public int getHealthValue(BonusType type){
		if (type == BonusType.HP_L)
			return 20;
		
		else if (type == BonusType.HP_M)
			return 50;
		
		else
			return 100;
	}
	
	public int getSpecialValue(BonusType type){
		if (type == BonusType.SP_L)
			return 20;
		
		else if (type == BonusType.SP_M)
			return 50;
		
		else
			return 100;
	}
	
	public Animator getAnimations (BonusType type) {
		if (type == BonusType.ATK_L)
			return animations[0];
		if (type == BonusType.ATK_M)
			return animations[1];
		if (type == BonusType.ATK_H)
			return animations[2];
		if (type == BonusType.DEF_L)
			return animations[3];
		if (type == BonusType.DEF_M)
			return animations[4];
		if (type == BonusType.DEF_H)
			return animations[5];
		if (type == BonusType.SPD_L)
			return animations[6];
		if (type == BonusType.SPD_M)
			return animations[7];
		if (type == BonusType.SPD_H)
			return animations[8];
		if (type == BonusType.HP_L)
			return animations[9];
		if (type == BonusType.HP_M)
			return animations[10];
		if (type == BonusType.HP_H)
			return animations[11];
		if (type == BonusType.SP_L)
			return animations[12];
		if (type == BonusType.SP_M)
			return animations[13];
		if (type == BonusType.SP_H)
			return animations[14];
		throw new IllegalArgumentException ("This animation doesn't exist.");
	}
	
	public TextureRegion getFrame () {
		return animation.nextFrame();
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public void setOutOfTime(){
		this.timeOnMap = CONSTTIMEONMAP;
	}
	
	public Rectangle getHitBox(){
		return hitBox;
	}
	
	public Vector2 getVectDir(){
		return vectDir;
	}
	
	public float getTimeOnMap(){
		return timeOnMap;
	}
	
	public BonusType getBonusType(){
		return type;
	}
	
	public void coolTimeOnMap(float delta) {
		this.timeOnMap += delta;
	}
	
	
	public boolean isOutOfTime(){
		return (timeOnMap >= CONSTTIMEONMAP);
	}
	
//	
//	public void setOnFloor(){
//		isOnFloor = true;
//	}
//	
	public void blockHorizontalMove(){
		vectDir.set(0, vectDir.y);
	}
	
	public void moveBonusOnMap () {
		this.position.add(vectDir.cpy().scl(Gdx.graphics.getDeltaTime())); 
		hitBox.x = position.x - (hitBox.width/2);
		hitBox.y = position.y;
	}
	
	public void affectGravity () {
			vectDir.add (0, -Platformer.GRAVITY * CONSTWEIGHT);
	}
	
	public void displayBonus(ShapeRenderer shapeRenderer, SpriteBatch batch){
		shapeRenderer.begin(ShapeType.Filled);
		
		if (type == BonusType.ATK_H || type == BonusType.ATK_M || type == BonusType.ATK_L)
			shapeRenderer.setColor(Color.RED);
		
		else 	if (type == BonusType.DEF_H || type == BonusType.DEF_M || type == BonusType.DEF_L)
			shapeRenderer.setColor(Color.GREEN);
		
		else 	if (type == BonusType.SPD_H || type == BonusType.SPD_M || type == BonusType.SPD_L)
			shapeRenderer.setColor(Color.BLUE);

//		shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		
		shapeRenderer.end();
		
		batch.begin();
		
		batch.draw(getFrame(), hitBox.x, hitBox.y);
		
		batch.end();
	}
}