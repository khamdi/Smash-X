package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class IA extends Character {
	public enum IAType {
		Pinguin,
		Shield,
		Swordsman,
		Trader;
	}
	
	public enum Level {
		Easy,
		Medium,
		Hard;
	}
	
	private	final 	IAType type;
	
	private final	Level lvl;
	
	private 		boolean isAttacking;
	private			boolean hasAttacked;
	
	private			float distancePlayer;
	
	public IA(float x, float y, float hitboxWidth, float hitboxHeight, float speed, float weight, IAType type, Level lvl ) {
		super(x, y, hitboxWidth, hitboxHeight, whichHealth(type, lvl), 0, weight, 0, isFlyer(type));
		
		this.type = type;
		this.lvl = lvl;
		this.isAttacking = false;
		
		ComposedAttack a = IAAttack(type, lvl);
		attacks.add(a);
	}
	
	private ComposedAttack IAAttack(IAType type, Level lvl) {
		ArrayList<Vector2> points =  new ArrayList<>();
		switch (type){
			case Pinguin :
				points.add (new Vector2 (0, 0));
				points.add (new Vector2 (10, 0));
				points.add (new Vector2 (-10, 0));
				return new ComposedAttack (
								new Attack (0.5f, whichDamage(type, lvl), new Vector2 (50, 0), 0.2f, 0, 0, new Vector2(0, 0), new Vector2 (5, Platformer.GRAVITY), 0.5f,  points),
								new Delay (1f, new Vector2 (0, 0))
							);
			case Swordsman :
				points.add (new Vector2 (30, 0));
				points.add (new Vector2 (45, 0));
				points.add (new Vector2 (60, 0));
				points.add (new Vector2 (75, 0));
				return new ComposedAttack(
								new Attack (0.5f, whichDamage(type, lvl), new Vector2 (10, 50), 0.3f, (float)(Math.PI) / 2f, -(float) (Math.PI) / 6f, new Vector2 (0, 0), new Vector2 (0, 0), 0.3f, points),
								new Delay (0.5f, new Vector2(0, 0))
							);
			case Shield :
				points.add (new Vector2 (30, 0));
				points.add (new Vector2 (30, -15));
				points.add (new Vector2 (30, 15));
				return new ComposedAttack (
								new Attack (1.3f, whichDamage(type, lvl), new Vector2 (500, 500), 0.3f, 0, 0, new Vector2 (15, 0), new Vector2 (0, 0), 0.3f, points),
								new Delay (0.5f, new Vector2(0, 0))
							);
			case Trader :
				points.add(new Vector2 (0,0));
				this.projectiles.add(new ThrowProjectile(0.2f, 4, new Vector2 (10, 100), 0.1f, 0.3f, points, new Vector2 (), true, new Vector2 (30, 32), new Vector2 (300, 500), this));
				
				return new ComposedAttack (
							projectiles.get(0)
						);
			default :
				throw new IllegalArgumentException("This enemy does not exist");
				
		}
		
	}
	
	public IAType getIAType() {
		return type;
	}
	
	static private int whichDamage (IAType type, Level lvl){
		if (type == IAType.Pinguin){
			if (lvl == Level.Easy)
				return 5; 
			else if (lvl == Level.Medium)
				return 7;
			return 10;
		}
		if (type == IAType. Shield){
			if (lvl == Level.Easy)
				return 4; 
			else if (lvl == Level.Medium)
				return 7;
			return 10;
		}
		if (type == IAType.Swordsman){
			if (lvl == Level.Easy)
				return 5; 
			else if (lvl == Level.Medium)
				return 8;
			return 12;
		}
		if (lvl == Level.Easy)
			return 4; 
		else if (lvl == Level.Medium)
			return 7;
		return 10;
	}
	
	static private int whichHealth (IAType type, Level lvl){
		if (type == IAType.Pinguin){
			if (lvl == Level.Easy)
				return 30; 
			else if (lvl == Level.Medium)
				return 45;
			return 60;
		}
		if (type == IAType. Shield){
			if (lvl == Level.Easy)
				return 50; 
			else if (lvl == Level.Medium)
				return 70;
			return 100;
		}
		if (type == IAType.Swordsman){
			if (lvl == Level.Easy)
				return 40; 
			else if (lvl == Level.Medium)
				return 55;
			return 80;
		}
		if (lvl == Level.Easy)
			return 40; 
		else if (lvl == Level.Medium)
			return 55;
		return 70;
	}
	
	static boolean isFlyer(IAType type){
		return (type == IAType.Pinguin);
	}
	
	@Override
	public String toString() {
		return " type : " +  this.type + " (" + this.getX() + "," + this.getY() + ") level : " + this.lvl + " health : " + this.health + " speed : " + this.speed;
	}
	
	
	public void pinguinMove(Vector2 posPlayer){
		if (isAttacking)  {
			if (isStunned() == false){
				if (isFacingRight())
					this.moveRight();
				else
					this.moveLeft();
				this.addForce(0, Platformer.GRAVITY);
				if (this.getY() > 625){
					this.isAttacking = false;
					this.immobilizeCharacter();
				}
			}
			else if (this.getY() <= posPlayer.y && this.hasAttacked == false){
				this.hasAttacked = true;
				this.activatedAttack = 0;
				this.attacks.get(activatedAttack).init (this.faceRight);
			}
		}
		else if (this.getX() > posPlayer.x + 250 ){
			this.moveLeft();
		}
		else if (this.getX() < posPlayer.x - 250){
			this.moveRight();
		}
		
		else {
			isAttacking = true;
			hasAttacked = false;
			if (this.getX() < posPlayer.x)
				this.setFaceRight();
			else
				this.setFaceLeft();
			this.addForce(0, -1000);
		}
	}
	
	public void characterIAtrack2 (Vector2 posPlayer){
		if (this.getX() < posPlayer.x - 325){
			if (isStunned() == false)
				this.moveRight();
		}
		else if (this.getX() > posPlayer.x + 325){
			if (isStunned() == false)
				this.moveLeft();
		}
		else if (Math.abs(this.getX() - posPlayer.x) < 325 - 50){
			if (this.getX() - posPlayer.x < 0)
				this.moveLeft();
			else
				this.moveRight();
		}
		else{
			if (this.getX() < posPlayer.x)
				this.setFaceRight();
			else
				this.setFaceLeft();
			
			this.activatedAttack = 0;
			this.attacks.get(activatedAttack).init (this.faceRight);
		}
	}
	
	public void characterIAtrack (Vector2 posPlayer){
		if (this.getX() < posPlayer.x - 70){
			this.moveRight();
		}
		else if (this.getX() > posPlayer.x + 70){
			this.moveLeft();
		}
		else{
			if (this.getX() < posPlayer.x)
				this.setFaceRight();
			else
				this.setFaceLeft();
			
			this.activatedAttack = 0;
			this.attacks.get(activatedAttack).init (this.faceRight);
		}
	}
}
