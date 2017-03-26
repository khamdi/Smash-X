package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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
	
	private static	ArrayList<TextureRegion[]> frames;
	private static 	boolean	loadImages = false;
	private	ArrayList<Animator> animations;
	
	private	static 	boolean	created = false;
	
	public static IA createIA (float x, float y, IAType type, Level lvl){	

		float hitboxWidth = whichHitBoxWidth(type);
		float hitboxHeight = whichHitBoxHeight(type);
		float xHitbox = x - (hitboxWidth/2);

		int healthMax = whichHealth(type, lvl);
		float weight = whichWeight(type);
		boolean fly = isFlyer(type);
		
		float speed = isNotNegative ( whichIASpeed(type, lvl));

		
		return new IA (x, y , xHitbox, hitboxWidth, hitboxHeight, healthMax, weight, speed, 0, fly, type, lvl  );
		
	}
	
	
	
	public IA(float x, float y,float xHitbox, float hitboxWidth, float hitboxHeight, int healthMax, float weight, float speed, int nbJumpMax, boolean flying, IAType type, Level lvl ) {
		super(x, y, xHitbox, hitboxWidth, hitboxHeight, healthMax, 0, weight, speed, 0, flying);
		loadAllImages();
		loadAllAnimations();
		
		this.type = type;
		this.lvl = lvl;
		this.isAttacking = false;
		
		ComposedAttack a = IAAttack(type, lvl);
		this.attacks.add(a);
		this.command = new CommandType(1);
		this.walkAnimation = whichAnimationMove(type, lvl);
		this.hitAnimation = whichAnimationHit (type, lvl);
	}
	
	static private float whichWeight (IAType type){
		if (type == IAType.Pinguin){
			return 2f; 
		}
		else if (type == IAType.Shield){
			return 1.5f; 
		}
		else if (type == IAType.Swordsman){
			return 1.5f; 
		}
			return 2;
	}
	
	public static float whichIASpeed(IAType type, Level lvl){
		float speedTestEasy = 200f;
		float speedTestMedium = 400f;
		float speedTestHard = 500f;
		
		if( type == IAType.Swordsman){
			if (lvl == Level.Easy){
				return speedTestEasy;
			}
			else if (lvl == Level.Medium){
				return speedTestMedium;
			}
			
			else if (lvl == Level.Hard){
				return speedTestHard;
			}
		}
		
		else if( type == IAType.Shield){
			if (lvl == Level.Easy){
				return speedTestEasy;
			}
			else if (lvl == Level.Medium){
				return speedTestMedium;
			}
			
			else if (lvl == Level.Hard){
				return speedTestHard;
			}
		}
		
		else if( type == IAType.Trader){
			if (lvl == Level.Easy){
				return speedTestEasy;
			}
			else if (lvl == Level.Medium){
				return speedTestMedium;
			}
			
			else if (lvl == Level.Hard){
				return speedTestHard;
			}
		}
		
		else if( type == IAType.Pinguin){
			if (lvl == Level.Easy){
				return speedTestEasy;
			}
			else if (lvl == Level.Medium){
				return speedTestMedium;
			}
			
			else if (lvl == Level.Hard){
				return speedTestHard;
			}
		}
			
		return 0f;
	}
	
	private void loadAllImages () {
		if (!loadImages) {
			frames = new ArrayList<>();
			
			frames.add(Animator.getFrames(6, 1, "Characters/IA/Linux/I/linuxI.png"));
			frames.add(Animator.getFrames(6, 1, "Characters/IA/Linux/II/linuxII.png"));
			frames.add(Animator.getFrames(6, 1, "Characters/IA/Linux/III/linuxIII.png"));
			frames.add(Animator.getFrames(10, 1, "Characters/IA/Windows/I/windows_ATK_I.png"));
			frames.add(Animator.getFrames(10, 1, "Characters/IA/Windows/II/windows_ATK_II.png"));
			
			frames.add(Animator.getFrames(10, 1, "Characters/IA/Windows/III/windows_ATK_III.png"));
			frames.add(Animator.getFrames(9, 1, "Characters/IA/Android/I/android_ATK_I.png"));
			frames.add(Animator.getFrames(9, 1, "Characters/IA/Android/II/android_ATK_II.png"));
			frames.add(Animator.getFrames(9, 1, "Characters/IA/Android/III/android_ATK_III.png"));
			frames.add(Animator.getFrames(7, 2, "Characters/IA/Apple/I/apple_ATK_I.png"));
			
			frames.add(Animator.getFrames(7, 2, "Characters/IA/Apple/II/apple_ATK_II.png"));
			frames.add(Animator.getFrames(7, 2, "Characters/IA/Apple/III/apple_ATK_III.png"));
			frames.add(Animator.getFrames(6, 1, "Characters/IA/Linux/I/linuxI.png"));
			frames.add(Animator.getFrames(6, 1, "Characters/IA/Linux/II/linuxII.png"));
			frames.add(Animator.getFrames(6, 1, "Characters/IA/Linux/III/linuxIII.png"));
			
			frames.add(Animator.getFrames(10, 1, "Characters/IA/Windows/I/windows_MOV_I.png"));
			frames.add(Animator.getFrames(10, 1, "Characters/IA/Windows/II/windows_MOV_II.png"));
			frames.add(Animator.getFrames(10, 1, "Characters/IA/Windows/III/windows_MOV_III.png"));
			frames.add(Animator.getFrames(10, 2, "Characters/IA/Android/I/android_MOV_I.png"));
			frames.add(Animator.getFrames(10, 2, "Characters/IA/Android/II/android_MOV_II.png"));
			
			frames.add(Animator.getFrames(10, 2, "Characters/IA/Android/III/android_MOV_III.png"));
			frames.add(Animator.getFrames(10, 1, "Characters/IA/Apple/I/apple_MOV_I.png"));
			frames.add(Animator.getFrames(10, 1, "Characters/IA/Apple/II/apple_MOV_II.png"));
			frames.add(Animator.getFrames(10, 1, "Characters/IA/Apple/III/apple_MOV_III.png"));
			
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/PingouinHitI.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/PingouinHitII.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/PingouinHitIII.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/WindowsHitI.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/WindowsHitII.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/WindowsHitIII.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/AndroidHitI.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/AndroidHitII.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/AndroidHitIII.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/AppleHitI.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/AppleHitII.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/IA/Hit/AppleHitIII.png"));
			
			frames.add(Animator.getFrames(8, 2, "Projectiles/Apple.png"));
			loadImages = true;
		}
	}
	
	private Animator whichAnimationAttack (IAType type, Level level) {
		if (type == IAType.Pinguin){
			if (level == Level.Easy)		return animations.get(0);
			else if (level == Level.Medium)	return animations.get(1);
			return animations.get(2); 
		}
		else if (type == IAType. Shield){
			if (level == Level.Easy)		return animations.get(3);
			else if (level == Level.Medium)	return animations.get(4);
			return animations.get(5);
		}
		else if (type == IAType.Swordsman){
			if (level == Level.Easy)		return animations.get(6);
			else if (level == Level.Medium)	return animations.get(7);
			return animations.get(8);
		}
		if (level == Level.Easy)		return animations.get(9);
		else if (level == Level.Medium)	return animations.get(10);
		return animations.get(11);
	} 
	
	void loadAllAnimations () {
		animations = new ArrayList<>();
		
		animations.add(new Animator(1/60f, frames.get(0)));
		animations.add(new Animator(1/60f, frames.get(1)));
		animations.add(new Animator(1/60f, frames.get(2)));
		animations.add(new Animator(1/60f, frames.get(3)));
		animations.add(new Animator(1/60f, frames.get(4)));
			
		animations.add(new Animator(1/60f, frames.get(5)));
		animations.add(new Animator(1/60f, frames.get(6)));
		animations.add(new Animator(1/60f, frames.get(7)));
		animations.add(new Animator(1/60f, frames.get(8)));
		animations.add(new Animator(1/60f, frames.get(9)));
		
		animations.add(new Animator(1/60f, frames.get(10)));
		animations.add(new Animator(1/60f, frames.get(11)));
		animations.add(new Animator(1/60f, frames.get(12)));
		animations.add(new Animator(1/60f, frames.get(13)));
		animations.add(new Animator(1/60f, frames.get(14)));
		
		animations.add(new Animator(1/60f, frames.get(15)));
		animations.add(new Animator(1/60f, frames.get(16)));
		animations.add(new Animator(1/60f, frames.get(17)));
		animations.add(new Animator(1/60f, frames.get(18)));
		animations.add(new Animator(1/60f, frames.get(19)));
		animations.add(new Animator(1/60f, frames.get(20)));
		animations.add(new Animator(1/60f, frames.get(21)));
		animations.add(new Animator(1/60f, frames.get(22)));
		animations.add(new Animator(1/60f, frames.get(23)));	
		animations.add(new Animator(1/60f, frames.get(24)));
		animations.add(new Animator(1/60f, frames.get(25)));
		animations.add(new Animator(1/60f, frames.get(26)));
		animations.add(new Animator(1/60f, frames.get(27)));
		animations.add(new Animator(1/60f, frames.get(28)));
		animations.add(new Animator(1/60f, frames.get(29)));
		animations.add(new Animator(1/60f, frames.get(30)));
		animations.add(new Animator(1/60f, frames.get(31)));
		animations.add(new Animator(1/60f, frames.get(32)));
		animations.add(new Animator(1/60f, frames.get(33)));
		animations.add(new Animator(1/60f, frames.get(34)));
		animations.add(new Animator(1/60f, frames.get(35)));
		animations.add(new Animator(1/60f, frames.get(36)));
	}
	
	private Animator whichAnimationMove (IAType type, Level level) {
		if (type == IAType.Pinguin){
			if (level == Level.Easy)		return animations.get(12);
			else if (level == Level.Medium)	return animations.get(13);
			return animations.get(14); 
		}
		else if (type == IAType. Shield){
			if (level == Level.Easy)		return animations.get(15);
			else if (level == Level.Medium)	return animations.get(16);
			return animations.get(17);
		}
		else if (type == IAType.Swordsman){
			if (level == Level.Easy)		return animations.get(18);
			else if (level == Level.Medium)	return animations.get(19);
			return animations.get(20);
		}
		if (level == Level.Easy)		return animations.get(21);
		else if (level == Level.Medium)	return animations.get(22);
		return animations.get(23);
	}
	
	private Animator whichAnimationHit (IAType type, Level level) {
		if (type == IAType.Pinguin){
			if (level == Level.Easy)		return animations.get(24);
			else if (level == Level.Medium)	return animations.get(25);
			return animations.get(26); 
		}
		else if (type == IAType. Shield){
			if (level == Level.Easy)		return animations.get(27);
			else if (level == Level.Medium)	return animations.get(28);
			return animations.get(29);
		}
		else if (type == IAType.Swordsman){
			if (level == Level.Easy)		return animations.get(30);
			else if (level == Level.Medium)	return animations.get(31);
			return animations.get(32);
		}
		if (level == Level.Easy)		return animations.get(33);
		else if (level == Level.Medium)	return animations.get(34);
		return animations.get(35);
	}
	
	static private int whichHitBoxWidth (IAType type){
		if (type == IAType.Pinguin){
			return 300; 
		}
		else if (type == IAType. Shield){
			return 320; 
		}
		else if (type == IAType.Swordsman){
			return 200; 
		}
			return 250;
	}
	
	static private int whichHitBoxHeight (IAType type){
		if (type == IAType.Pinguin){
			return 256; 
		}
		else if (type == IAType. Shield){
			return 350; 
		}
		else if (type == IAType.Swordsman){
			return 350; 
		}
			return 400;
	}
	
	static public void IASpawnSegment(Random randGen, ArrayList<Character> characters, int xStartSegment, int widthSegment, int yPixelSpawn, int widthTile){
		int nbSpawn = randGen.nextInt(4) + 2;
		int xSpawn = 0;
		
		IAType type = null;
		Level lvl = null;
		
//		System.out.println("On doit cr�er un total de " + nbSpawn + " ennemies ");
		
		
		for (int i = 0; i < nbSpawn; i++){
			xSpawn = ((xStartSegment + randGen.nextInt(widthSegment - 1) ) * widthTile);
			type = IAType.values()[randGen.nextInt(IAType.values().length)];
			lvl = Level.values()[randGen.nextInt(Level.values().length)];
			
//			System.out.println("On a cr�e un: " + type + " de niveau " + lvl + " en " 	+ xSpawn+ " , " + yPixelSpawn );
			
			characters.add( createIA(xSpawn, yPixelSpawn, type, lvl));

		}
	}
	
	private ComposedAttack IAAttack(IAType type, Level lvl) {
		ArrayList<Vector2> points =  new ArrayList<>();
		switch (type){
			case Pinguin :
				points.add (new Vector2 (-135, 60));
				points.add (new Vector2 (-60,  60));
				points.add (new Vector2 (0,    60));
				points.add (new Vector2 (60,   60));
				points.add (new Vector2 (135,  60));
				
				points.add (new Vector2 (-135, 130));
				points.add (new Vector2 (135,  130));
				
				points.add (new Vector2 (-135, 200));
				points.add (new Vector2 (-60,  200));
				points.add (new Vector2 (0,    200));
				points.add (new Vector2 (60,   200));
				points.add (new Vector2 (135,  200));
				
				return new ComposedAttack (
								whichAnimationAttack(type, lvl),
								new Attack (0.5f, whichDamage(type, lvl), new Vector2 (50, 0), 0.2f, 0, 0, new Vector2(), new Vector2 (), 0.1f,  points)
							);
			case Swordsman :
				points.add (new Vector2 (200,  100));
				points.add (new Vector2 (220,  100));
				points.add (new Vector2 (240,  100));
				points.add (new Vector2 (260, 100));
				points.add (new Vector2 (280, 100));
				points.add (new Vector2 (300, 100));
				points.add (new Vector2 (320, 100));
				return new ComposedAttack (
								whichAnimationAttack(type, lvl),
								new Attack (0.5f, whichDamage(type, lvl), new Vector2 (10, 50), 0.3f,  (float)(Math.PI) / 2f, -(float) (Math.PI) / 6f, new Vector2 (0, 0), new Vector2 (0, 0), 0.3f, points),
								new Delay (0.2f, new Vector2(0, 0))
							);
			case Shield :
				points.add (new Vector2 (175, 60));
				points.add (new Vector2 (175, 90));
				points.add (new Vector2 (175, 120));
				points.add (new Vector2 (175, 150));
				points.add (new Vector2 (175, 180));
				points.add (new Vector2 (175, 210));
				return new ComposedAttack (
						whichAnimationAttack(type, lvl),
						new Attack (1.3f, whichDamage(type, lvl), new Vector2 (1500, 1500), 0.3f, 0, 0, new Vector2 (15, 0), new Vector2 (0, 0), 0.3f, points),
						new Delay (0.5f, new Vector2(0, 0))
				);
			case Trader :
				points.add(new Vector2 (0,0));
				points.add(new Vector2(10, 10));
				points.add(new Vector2(-10, 10));
				points.add(new Vector2(-10, -10));
				points.add(new Vector2(10, -10));
				ArrayList<Projectile> p = new ArrayList<>();
				p.add(new Projectile (animations.get(36), new Vector2 (30, 32), points, new Vector2 (780, 1500),
						true, 4, 0.2f, 0.1f, new Vector2 (10, 100), 5f, true, null));
				this.projectiles.add(
						new ThrowProjectile(p, 0.3f, new Vector2(0, 0))
				);
				
				return new ComposedAttack (
							whichAnimationAttack(type, lvl),
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
	
	
	public void TrackAndMove(Vector2 posPlayer){
		if ( Math.abs( (this.getX() - posPlayer.x)) < 3500) {
			if (getIAType() == IAType.Pinguin){
				pinguinTrackAndMove(posPlayer);
			}
			else if (getIAType() == IAType.Swordsman || getIAType() == IAType.Shield){
				swordmanShieldTrackAndMove(posPlayer);
			}
			
			else if (getIAType() == IAType.Trader){
				traderTrackAndMove(posPlayer);
			}
		}
		
	}
	public void pinguinTrackAndMove(Vector2 posPlayer){
		int distanceX = 750;
		int distanceY = 750;
		
		int amplitude = 50;
		float force = 25;
		
		this.activatedAttack = 0;
		attacks.get(activatedAttack).init(this.faceRight);
		
		if (!isAttacking){
			if (this.getX() < posPlayer.x - distanceX){
				this.moveRight();
			}
			else if (this.getX() >= posPlayer.x + distanceX){
				this.moveLeft();
			}
			else
				this.blockHorizontalMove();
		
			if (this.getY() < posPlayer.y + distanceY - amplitude){
				this.addForce(0, force);
			}
			else if (this.getY() >= posPlayer.y + distanceY + amplitude){
				this.addForce(0, -force);
			}
			else{
				this.blockVerticalMove();
				isAttacking = true;
			} 
		}
		else{
			this.activatedAttack = 0;
			attacks.get(activatedAttack).init(this.faceRight);
			this.addForce(0, -25);
			if (this.getX() < posPlayer.x)
				this.addForce(25,0);
			
			else
				this.addForce(-25,0);
			
			if (this.getY() < posPlayer.y + amplitude)
				isAttacking = false;
		}
	}
	
	public void traderTrackAndMove (Vector2 posPlayer){
		int attackDistance = 1000;
		int intervalAmplitude = 50;
		
		float deltaIAPlayer = this.getX() - posPlayer.x;
		
		if (this.getX() < posPlayer.x - attackDistance){
			if (isStunned() == false){
				this.moveRight();
			}
		}
		else if (this.getX() > posPlayer.x + attackDistance){
			if (isStunned() == false)
				this.moveLeft();
		}
		else if (Math.abs(deltaIAPlayer) < attackDistance - intervalAmplitude){
			if (deltaIAPlayer < 0)
				this.moveLeft();
			else
				this.moveRight();
		}
		else{
			this.blockHorizontalMove();
			if (deltaIAPlayer < 0)
				this.setFaceRight();
			else
				this.setFaceLeft();
			
			this.activatedAttack = 0;
			this.attacks.get(activatedAttack).init (this.faceRight);
		}
	}
	
	public void swordmanShieldTrackAndMove (Vector2 posPlayer){
		int attackDistance = 225;
		if (this.getX() < posPlayer.x - attackDistance){
			this.moveRight();
		}
		else if (this.getX() > posPlayer.x + attackDistance){
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
	
	@Override
	public String toString() {
		return " type : " +  this.type + " (" + this.getX() + "," + this.getY() + ") level : " + this.lvl + " health : " + this.health + " speed : " + this.speed;
	}
}
