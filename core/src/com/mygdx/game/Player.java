package com.mygdx.game;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character {	
	public enum CharacterType {
		Ninja,
		Warrior,
		Mage;
	}
	
	int roundWon;
	
	private int nbPressedJab;
	
	private boolean dashLeft;
	private boolean dashRight;
	
	private CharacterType type;

	private float time;
	private Animator guardAnimation;
	
	private static ArrayList<TextureRegion[]> frames;
	private static boolean createFrames = false;
	private ArrayList<Animator> animations;

//	private static ArrayList<ArrayList<Attacks>> attacklist;
//	private static boolean createAttacks = false;
	
	public static Player createPlayer(float x, float y, int cmd, CharacterType type){
		float speed = 0;
		float xHitbox = 0;
		float hitboxWidth = 0;
		float hitboxHeight = 0;
		float weight = 0;
		int startSpecial = 100;
		
		int healthMax = 400;
		int nbJumpMax = 2;
	
		
		if (type == CharacterType.Ninja){
			healthMax = 400;
			hitboxHeight = 205f;
			hitboxWidth =125f;
			weight = 1.5f;
		}
		
		else if (type == CharacterType.Mage){
			healthMax = 400;
			hitboxHeight = 205f;
			hitboxWidth =125f;
			weight = 1.5f;
		}
		
		else{
			healthMax = 400;
			hitboxHeight = 205f;
			hitboxWidth = 125f;
			weight = 1.5f;
		}
		
		xHitbox = x-hitboxWidth;
		speed = isNotNegative ( (BASICSPEED*1.5f) /(weight) );
		
		return new Player(x, y, xHitbox, hitboxWidth, hitboxHeight, healthMax, weight, speed, startSpecial, nbJumpMax, cmd, type);
	}
	
	
	public Player(float x, float y, float xHitbox, float hitboxWidth, float hitboxHeight, int healthMax, float weight, float speed,  int startSpecial, int nbJumpMax,  int cmd, CharacterType type) {
		super(x, y, xHitbox, hitboxWidth, hitboxHeight, healthMax, startSpecial, weight, speed, nbJumpMax,  false);
		
		loadAllImages();
		loadAllAnimations();
//		loadAllAttacks();
		
		this.command = new CommandType(cmd);
		this.type = type;
		this.attacks = this.playerAttacks(type);
		this.nbPressedJab = 0;
		this.roundWon = 0;
		this.dashLeft = false;
		this.dashRight = false;
		this.walkAnimation = walkAnimation(type); 
		this.guardAnimation = guardAnimation(type);
		this.hitAnimation = hitAnimation(type);
	}
	
	private void loadAllImages () {
		if (!createFrames) {
			frames = new ArrayList<>();
		
			frames.add(Animator.getFrames(10, 1, "Characters/Ninja/walkanimationninja.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/Ninja/Ninja - GuardStance.png"));
			frames.add(Animator.getFrames(1, 1, "Characters/Ninja/Hit.png"));
			frames.add(Animator.getFrames(5, 1, "Projectiles/Shuriken.png"));
			frames.add(Animator.getFrames(3, 1, "Projectiles/Fireball.png"));
			frames.add(Animator.getFrames(11, 1, "Projectiles/smoke.png"));
			frames.add(Animator.getFrames(6, 1, "Characters/Ninja/Ninja---Dash.png"));
			frames.add(Animator.getFrames(5, 1, "Characters/Ninja/ShurikenThrow.png"));
			frames.add(Animator.getFrames(10, 1, "Characters/Ninja/Ninja---Jabp1.png"));
			frames.add(Animator.getFrames(15, 1, "Characters/Ninja/Ninja---Jabp2.png"));
			frames.add(Animator.getFrames(10, 1, "Characters/Ninja/Ninja---Jabp3.png"));
			frames.add(Animator.getFrames(11, 1, "Characters/Ninja/Ninja---FrontAttack2.png"));
			frames.add(Animator.getFrames(7, 1, "Characters/Ninja/Ninja---UpAttack.png"));
			frames.add(Animator.getFrames(12, 1, "Characters/Ninja/Ninja---DownAttack.png"));
			frames.add(Animator.getFrames(10, 2, "Characters/Ninja/Ninja---Katon.png"));
			frames.add(Animator.getFrames(8, 5, "Characters/Ninja/Raikiri.png"));
			frames.add(Animator.getFrames(8, 8, "Characters/Ninja/GhostBlade.png"));
			
			createFrames = true;
		}
	}
	
	private void loadAllAnimations () {
		animations = new ArrayList<>();
		/* Ninja */
		/* Walk animation */
		animations.add(new Animator(1/30f, frames.get(0)));
		/* Guard */
		animations.add(new Animator(1/30f, frames.get(1)));
		/* Hit */
		animations.add(new Animator(1/30f, frames.get(2)));
		
		/* Projectiles */
		animations.add(new Animator (1/60f, frames.get(3)));
		animations.add(new Animator (1/20f, frames.get(4)));
		animations.add(new Animator (1/20f, frames.get(5)));
		/* Attacks */
		animations.add(new Animator(1/60f, frames.get(6)));
		animations.add(new Animator(1/60f, frames.get(7)));
		animations.add(new Animator(1/60f, frames.get(8)));
		animations.add(new Animator(1/60f, frames.get(9)));
		animations.add(new Animator(1/60f, frames.get(10)));
		animations.add(new Animator(1/30f, frames.get(11)));
		animations.add(new Animator(1/30f, frames.get(12)));
		animations.add(new Animator(1/60f, frames.get(13)));
		
		/* Special */
		animations.add(new Animator(1/60f, frames.get(14)));
		animations.add(new Animator(1/60f, frames.get(15)));
		animations.add(new Animator(1/60f, frames.get(16)));
	}
/*	
	private void loadAllAttacks () {
		if (!createAttacks) {
			attacklist = new ArrayList<>();
			attacklist.add(playerAttacks(CharacterType.Ninja));
			// Others
			createAttacks = true;
		}
	}

	private ArrayList<Attacks> getAttacks (CharacterType type) {
		switch (type) {
			case Ninja : return attacklist.get(0);
			case Mage : break;
			case Warrior : break;
		}
		return null;
	}
*/	
	private Animator walkAnimation (CharacterType type) {
		switch (type) {
			case Ninja : return animations.get(0);
			case Mage : break;
			case Warrior : break;
		}
		return null;
	}
	
	private Animator guardAnimation (CharacterType type) {
		switch (type) {
			case Ninja : return animations.get(1);
			case Mage : break;
			case Warrior : break;
		}
		return null;
	}
	
	private Animator hitAnimation (CharacterType type) {
		switch (type) {
			case Ninja : return animations.get(2);
			case Mage : break;
			case Warrior : break;
		}
		return null;
	}
	
	private ArrayList<Attacks> playerAttacks(CharacterType type) {
		ArrayList<Attacks> attacks = new ArrayList<>();
		switch (type){
			case Ninja :
				ArrayList<Vector2> pointsDash = new ArrayList<>();
				pointsDash.add(new Vector2(32, 32));
				attacks.add(
						new ComposedAttack (
									animations.get(6),
									new Delay (0.1f, new Vector2(750, 0)),
									new Delay (5/60f, new Vector2(0,0))
								)
						);
				
				/*Projectiles Shuriken*/
				ArrayList<Vector2> pointsShuriken = new ArrayList<>();
				pointsShuriken.add(new Vector2(30, 100));
				pointsShuriken.add(new Vector2(-30, 100));
				pointsShuriken.add(new Vector2(-30, 20));
				pointsShuriken.add(new Vector2(30, 20));
				
				ArrayList<Projectile> p = new ArrayList<>();
				p.add(new Projectile (animations.get(3), new Vector2 (30, 32), pointsShuriken, new Vector2 (1000, 0), false, 4, 0.2f, 0.1f, new Vector2 (25, 25), 2f, true, null));
				ThrowProjectile shuriken =  new ThrowProjectile(p, 5/60f, new Vector2 ());
				projectiles.add(shuriken);

				attacks.add(
						new ComposedAttack (
								animations.get(7), 
								shuriken
							)
						);
				
				/*Jabs Ninja Manche*/
				ArrayList<Vector2> pointsJabsNinja1 = new ArrayList<>();
				pointsJabsNinja1.add(new Vector2(70, 18));
				pointsJabsNinja1.add(new Vector2(70, 33));
				pointsJabsNinja1.add(new Vector2(70, 48));
				pointsJabsNinja1.add(new Vector2(70, 63));
				pointsJabsNinja1.add(new Vector2(85, 18));
				pointsJabsNinja1.add(new Vector2(85, 33));
				pointsJabsNinja1.add(new Vector2(85, 48));
				pointsJabsNinja1.add(new Vector2(110, 48));
				attacks.add(
						new ComposedAttack (
							animations.get(8),
							new Attack (0.4f, 1, new Vector2 (25, 0), 0.25f, (float) (Math.PI) / 12f, (float) (Math.PI) / 6f, new Vector2 (0, 0), new Vector2 (0, 0), 1/6f, pointsJabsNinja1)
//							new Delay (0.2f, new Vector2(0, 0))
						)
					);
				
				/*Jabs Ninja Coup épée*/
				ArrayList<Vector2> pointsJabsNinja2 = new ArrayList<>();
				pointsJabsNinja2.add (new Vector2 (80, 38));
				pointsJabsNinja2.add (new Vector2 (100, 38));
				pointsJabsNinja2.add (new Vector2 (120, 38));
				pointsJabsNinja2.add (new Vector2 (140, 38));
				pointsJabsNinja2.add (new Vector2 (160, 38));
				pointsJabsNinja2.add (new Vector2 (180, 38));
				pointsJabsNinja2.add (new Vector2 (200, 38));
				
				attacks.add(
							new ComposedAttack (
								animations.get(9),
//								new Attack (0.4f, 4, new Vector2 (50, 0), 0.25f, (float)Math.PI / 6, (float)Math.PI / 2, new Vector2 (0, 0), new Vector2 (0, 0), 0.15f, pointsJabsNinja2),
								new Attack (0.4f, 4, new Vector2 (50, 0), 0.25f, (float)Math.PI / 2, 0, new Vector2 (0, 0), new Vector2 (0, 0), 1/4f, pointsJabsNinja2)
//								new Delay (0.2f, new Vector2(0, 0))
							)
						);
				
				/*Jabs Ninja Coup de pied*/
				
				ArrayList<Vector2> pointsKick= new ArrayList<>();
				pointsKick.add (new Vector2 (20, 20));
				pointsKick.add (new Vector2 (40, 20));
				pointsKick.add (new Vector2 (60, 20));
				pointsKick.add (new Vector2 (80, 20));
				pointsKick.add (new Vector2 (100, 20));
				attacks.add(
						new ComposedAttack (
								animations.get(10),
								new Delay (5f/60, new Vector2(0, 0)),
								new Attack(0.2f, 3, new Vector2 (25, 25), 0.3f, -(float)(Math.PI) / 2, 0, new Vector2 (0, 0), new Vector2 (0, 0), 5f/60, pointsKick)
//								new Delay (0.2f, new Vector2(0, 0))
							)
						);
				
				/*Coté corps a corps Estoc*/
				ArrayList<Vector2> pointsEstoc = new ArrayList<>();
				pointsEstoc.add (new Vector2 (80, 38));
				pointsEstoc.add (new Vector2 (100, 38));
				pointsEstoc.add (new Vector2 (120, 38));
				pointsEstoc.add (new Vector2 (140, 38));
				pointsEstoc.add (new Vector2 (160, 38));
				
//				attacks.add(
//					new ConditionalAttack(new Attack (0.2f, 5, new Vector2 (25, 0), 0.1f, 7f * (float)(Math.PI) / 6f, -(float) (Math.PI) / 6f, new Vector2 (0, 0), new Vector2 (0, 0), 0.2f, pointsEstoc),
//							new Attack(0.4f, 1, new Vector2 (15, 300), 0.1f, -1/6f * (float) (Math.PI), 1/3f * (float) (Math.PI), new Vector2 (0, 0), new Vector2 (15, 50), 0.2f, pointsEstoc),
//							new Delay(0.2f, new Vector2 (0,0))
//					)	
//				);
				
				attacks.add(
					new ComposedAttack (
						animations.get(11),
						new Delay (1/6f, new Vector2(0, 0)),
						new Attack (0.4f, 6, new Vector2 (50, 0), 0.25f, 0, 0, new Vector2 (30, 0), new Vector2 (0, 0), 1/6f, pointsEstoc)
						)
					);
				
				
				/*Haut corps a corps coup épée*/
				attacks.add( 
						new ComposedAttack (
								animations.get(12),
								new Attack (0.3f, 5, new Vector2 (25, 0), 0.3f, 2f * (float)(Math.PI) / 3f, -(float) (Math.PI) / 6f, new Vector2 (0, 0), new Vector2 (0, 0), 7/30f, pointsEstoc)
//								new Delay (0.5f, new Vector2(0, 0))
							)
						);
				
				/*Bas corps a corps Balayette*/
				attacks.add(
						new ComposedAttack (
								animations.get(13),
								new Attack(0.2f, 3, new Vector2 (25, 25), 0.3f, -(float)(Math.PI) / 2, 0, new Vector2 (0, 0), new Vector2 (0, 0), 0.2f, pointsKick)
//								new Delay (0.2f, new Vector2(0, 0))
							)
						);
				
				/*Spe 25 Katon*/
				ArrayList<Vector2> pointsKaton = new ArrayList<>();
				pointsKaton.add (new Vector2 (10, 120));
				pointsKaton.add (new Vector2 (50, 120));
				pointsKaton.add (new Vector2 (90, 120));
				pointsKaton.add (new Vector2 (27, 152));
//				pointsKaton.add (new Vector2 (35, 35));
				pointsKaton.add (new Vector2 (72, 152));
				pointsKaton.add (new Vector2 (27, 83));
//				pointsKaton.add (new Vector2 (35, -35));
				pointsKaton.add (new Vector2 (72, 83));
				
				ArrayList<Projectile> sp = new ArrayList<>();
				sp.add(new Projectile (animations.get(4), new Vector2 (30, 32), pointsKaton, new Vector2 (600, 0), false, 10, 0.5f, 0.3f, new Vector2 (300, 25), 5f, false, null));
				ThrowProjectile katon =  new ThrowProjectile(sp, 0.3f, new Vector2 ());
				projectiles.add(katon);
				attacks.add(
						new ComposedAttack (
								animations.get(14),
								katon,   
								new Delay (0.5f, new Vector2(0, 0))
							)
						);
				
				/*Spe 50 Raikiri 1/4 ecrant ? */
				ArrayList<Vector2> pointsRaikiri = new ArrayList<>();
				pointsRaikiri.add (new Vector2 (30, 38));
				pointsRaikiri.add (new Vector2 (45, 38));
				pointsRaikiri.add (new Vector2 (60, 38));
				
				attacks.add( 
						new ComposedAttack (
							animations.get(15),
							new Delay (8/60f, new Vector2(0, 0)),
							new Attack (0.5f, 20, new Vector2 (25, 0), 0.3f, 0, 0, new Vector2 (25, 0), new Vector2 (250, 0), 1/2f, pointsRaikiri)
//							new Delay (0.5f, new Vector2(0, 0))
						)
					);
				
				/*Spe 100 Slash Annimation ? */
				ArrayList<Vector2> pointsSlash = new ArrayList<>();
				pointsSlash.add (new Vector2 (125, 48));
				pointsSlash.add (new Vector2 (165, 48));
				pointsSlash.add (new Vector2 (205, 48));
				pointsSlash.add (new Vector2 (142, 80));
//				pointsSlash.add (new Vector2 (125, 100));
				pointsSlash.add (new Vector2 (187, 80));
				pointsSlash.add (new Vector2 (142, 11));
//				pointsSlash.add (new Vector2 (125, -10));
				pointsSlash.add (new Vector2 (187, 11));
				
				attacks.add(
						new ComposedAttack (
							animations.get(16),
							new Delay (1f/6, new Vector2()),
							new Delay (1f/6, new Vector2(250,0)),
//							Projetile 
							new Attack (0.3f, 10, new Vector2(), 1/30f, 0f, 0f, new Vector2(), new Vector2(), 1/3f, pointsSlash),
//							final attack.
							new Delay (8/60f, new Vector2()),
							new Attack (0.5f, 20, new Vector2(45, 10), 0.3f, 0, 0, new Vector2(15, 0), new Vector2(25,0), 2/30f, pointsSlash),
							new Delay (4/30f, new Vector2())
						)
					);
				
				/*Special Move teleportation*/
				ArrayList<Vector2> pointTeleportation = new ArrayList<>();
				pointTeleportation.add(new Vector2 (38, 38));
				
				ArrayList<Projectile> fumer = new ArrayList<>();
				Animator smoke = animations.get(5);
				fumer.add(
						new Projectile (smoke, new Vector2(), new ArrayList<Vector2>(), new Vector2(), false, 0, 0f, 0f, new Vector2(), 1f/2, false, null)
				);
				ThrowProjectile tmp = new ThrowProjectile(fumer, 1f/60, new Vector2());
				projectiles.add(tmp);
				attacks.add( 
					new ComposedAttack (
						animations.get(0), 
						tmp,
						new Delay (0.5f, new Vector2(300, 0)),
						tmp
					)
				);
				break;
				
			case Warrior :
			
				
			case Mage :
//					/*Projectile Boule élémentaire*/
//				ArrayList<Vector2> pointsElementale = new ArrayList<>();
//				pointsElementale.add(new Vector2(38, 38));
//				pointsElementale.add(new Vector2(48, 48));
//				pointsElementale.add(new Vector2(28, 48));
//				pointsElementale.add(new Vector2(28, 28));
//				pointsElementale.add(new Vector2(48, 28));
//				
//				/*gestion du chargement des attaques*/
//				ArrayList<Projectile> p1 = new ArrayList<>();
//				p1.add(new Projectile (new Vector2 (30, 32), pointsElementale, new Vector2 (300, 0), false, 3, 0.2f, 0.1f, new Vector2 (10, 100), 3f, true, null));
//				ThrowProjectile elementale =  new ThrowProjectile(p1, 0.3f, new Vector2 ());
//				projectiles.add(elementale);
//				attacks.add(
//						new ComposedAttack (
//								elementale
//							)
//						);
//				
//				/*Jabs Mage Boule Feu */
//				ArrayList<Vector2> pointsJabsMage1_2 = new ArrayList<>();
//				pointsJabsMage1_2.add (new Vector2 (68, 38));
//				pointsJabsMage1_2.add (new Vector2 (113, 38));
//				pointsJabsMage1_2.add (new Vector2 (158, 38));
//				pointsJabsMage1_2.add (new Vector2 (85, 65));
////				pointsJabsMage1.add (new Vector2 (45, 45));
//				pointsJabsMage1_2.add (new Vector2 (102, 65));
//				pointsJabsMage1_2.add (new Vector2 (85, 11));
////				pointsJabsMage1.add (new Vector2 (45, -45));
//				pointsJabsMage1_2.add (new Vector2 (140, 11));
//				attacks.add(
//						new ComposedAttack(
//							new Attack (0.4f, 2, new Vector2 (25, 0), 0.25f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 0), 0.25f, pointsJabsMage1_2),
//							new Delay (0.2f, new Vector2(0, 0))
//						)
//					);
//				
//				/*Jabs Mage Boule Electrique*/
//				attacks.add(
//						new ComposedAttack(
//							new Attack (0.4f, 2, new Vector2 (25, 0), 0.25f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 0), 0.25f, pointsJabsMage1_2),
//							new Delay (0.2f, new Vector2(0, 0))
//						)
//					);
//				
//				/*Jabs Mage Pique de glace */
//				ArrayList<Vector2> pointsJabsMage3 = new ArrayList<>();
//				pointsJabsMage3.add (new Vector2 (100, -20));
//				pointsJabsMage3.add (new Vector2 (120, -20));
//				pointsJabsMage3.add (new Vector2 (140, -20));
//				pointsJabsMage3.add (new Vector2 (110, 0));
//				pointsJabsMage3.add (new Vector2 (130, 0));
//				pointsJabsMage3.add (new Vector2 (120, 0));
//				pointsJabsMage3.add (new Vector2 (120, 20));
//				pointsJabsMage3.add (new Vector2 (120, 35));
//				pointsJabsMage3.add (new Vector2 (120, 50));
//				attacks.add(
//						new ComposedAttack(
//							new Attack (0.4f, 4, new Vector2 (0, 50), 0.25f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 0), 0.25f, pointsJabsMage3),
//							new Delay (0.2f, new Vector2(0, 0))
//						)
//					);
//				/*attaque côté Explosion attaque a condition*/
//				attacks.add(new ComposedAttack());
//				
//				/*Attaque Haut Hélicoptère*/
//				ArrayList<Vector2> pointsChopper = new ArrayList<>();
//				pointsChopper.add(new Vector2(-20, 40));
//				pointsChopper.add(new Vector2(-15, 40));
//				pointsChopper.add(new Vector2(-10, 40));
//				pointsChopper.add(new Vector2(-5, 40));
//				pointsChopper.add(new Vector2(0, 40));
//				pointsChopper.add(new Vector2(5, 40));
//				pointsChopper.add(new Vector2(10, 40));
//				pointsChopper.add(new Vector2(15, 40));
//				pointsChopper.add(new Vector2(20, 40));
//				attacks.add(
//						new ComposedAttack (
//								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
//								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
//								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
//								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
//								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
//								new Attack (0.2f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
//								new Delay(0.5f, new Vector2())
//							)
//						);
//				
//				/*Corps a Corps bas Soin*/
//				
//				/*Special 25 YOU SHALL NOT PASS*/
//				
//				/*Special 50 Invocation*/
//				
//				/*Special 100 PVC | Genkidama*/
//				ArrayList<Vector2> pointsGenkidama = new ArrayList<>();
//				pointsGenkidama.add(new Vector2());
//				pointsGenkidama.add(new Vector2());
				
				break;
				
			default : 
				throw new IllegalArgumentException("This player does not exist");
		}
		
		return attacks;
	}
	
	private void inputSpecial(){
		this.immobilizeCharacter();
		if(Gdx.input.isKeyPressed(command.up)) {
			System.out.println("Special up");
			if (consumeSpecial(50)) {
				activatedAttack = SPE50;
		 		attacks.get(activatedAttack).init (this.faceRight);
			}
		}
		else if(Gdx.input.isKeyPressed(command.down) ) {
//			System.out.println("Special down ");
			if (consumeSpecial(100)) {
				activatedAttack = SPE100;
				attacks.get(activatedAttack).init (this.faceRight);
			}
		}
		else { /* Default */
//			System.out.println("Special neutral");

			if (consumeSpecial (25)) {
				activatedAttack = SPE25;
		 		attacks.get(activatedAttack).init (this.faceRight);
		 		
			}
		}
	}
	
	private void inputAttack(){
		this.immobilizeCharacter();
		if(Gdx.input.isKeyPressed(command.up)) {
//			System.out.println("Attack up.");
			activatedAttack = ATTACKUP;
		 	attacks.get(activatedAttack).init (this.faceRight);
		 	this.nbPressedJab = 0;
		}
		else if(Gdx.input.isKeyPressed(command.down) ) {
//			System.out.println("Attack down.");
			activatedAttack = ATTACKDOWN;
		 	attacks.get(activatedAttack).init (this.faceRight);
		 	
		 	
		 	this.nbPressedJab = 0;
		}
		else if(Gdx.input.isKeyPressed(command.left) || Gdx.input.isKeyPressed(command.right)) {
//			System.out.println("Attack side.");
			activatedAttack = ATTACKSIDE;
		 	attacks.get(activatedAttack).init (this.faceRight);
		 	this.nbPressedJab = 0;
		}
		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.I) ){
//			System.out.println("OKKKKKKKKKKKKKKKKk");
//			setInvisibility(3f);
//		}
		
		
		else { /* Default */
			this.nbPressedJab ++;
			if (this.nbPressedJab == 1)
				activatedAttack = JABS1;
			
			else if (this.nbPressedJab == 2)
				activatedAttack = JABS2;
			
			else{
				activatedAttack = JABS3;
				this.nbPressedJab = 0;
			}
//			System.out.println("Attack neutral.");
		 	attacks.get(activatedAttack).init (this.faceRight);
		}
	}
	
	private void inputMove() {
		time -= Gdx.graphics.getDeltaTime();
		boolean isMoving = false;
		
		if (time < 0){
			this.dashLeft = false;
			this.dashRight = false;
		}
		
		
		if (Gdx.input.isKeyJustPressed(command.left) && this.dashLeft && isOnFloor()){
			 if (time > 0){
				 this.dashLeft = false;
				 
//				 System.out.println("Dash");
				 activatedAttack = DASH;
				 attacks.get(activatedAttack).init (false);

				 this.useJump();
			 }
		 }
		else if(Gdx.input.isKeyPressed(command.left)){
			 this.moveLeft();
			 this.nbPressedJab = 0;
			 
			 if (!this.dashLeft && !isMoving){
				 this.dashLeft = true; 
				 time = 0.2f;
			 }
			 isMoving = true;
			 
			 this.dashRight = false;
		 }
		 
		if (Gdx.input.isKeyJustPressed(command.specialMove)){
//			setStun(0.5f);
			setInvisibility(0.6f);
			setInvincibility(0.5f);
			activatedAttack = SPECIALMOVE;
			attacks.get(activatedAttack).init(faceRight);
			
		}
		
		if (Gdx.input.isKeyJustPressed(command.right) && this.dashRight && isOnFloor()){
			 if (time > 0){
				 this.dashRight = false;
				 
//				 System.out.println("Dash");
				 activatedAttack = DASH;
				 attacks.get(activatedAttack).init (true);
				 this.useJump();
				 
			 }
			 this.dashRight = true;
		 }
		 
		 
		else if(Gdx.input.isKeyPressed(command.right)){
			 this.moveRight();
			 this.nbPressedJab = 0;
			 isMoving = true;
		 
			 if (!this.dashRight){
				 this.dashRight = true; 
				 time = 0.5f;
			 }
			 
			 this.dashLeft = false;
		 }
		 if(Gdx.input.isKeyJustPressed(command.jump)){
			 this.jump();
			 this.nbPressedJab = 0;
			 isMoving = true;
			 
			 this.dashLeft = false;
			 this.dashRight = false;
		 }
		 if(Gdx.input.isKeyPressed(command.down)){
			 this.affectGravity ();
			 this.nbPressedJab = 0;
			 isMoving = true;
		 
			 this.dashLeft = false;
			 this.dashRight = false;
		 }
		 if (!isMoving)
			 this.blockHorizontalMove();
	}

	public void input() {
		if (!isStunned()) {
			if (this.isOnFloor()){
				if (!guard(Gdx.input.isKeyPressed(command.guard))) {
					if (Gdx.input.isKeyPressed(command.special)) {
						this.nbPressedJab = 0;
						this.inputSpecial();
					}
					else if (Gdx.input.isKeyJustPressed(command.attack)) {
						this.inputAttack();
					}
					else if (Gdx.input.isKeyPressed(command.projectile)){
//						System.out.println("Projectile.");
						this.immobilizeCharacter();
						activatedAttack = PROJECTILE;
						attacks.get(activatedAttack).init (this.faceRight);	
						this.nbPressedJab = 0;
					}
					else {
						recoverGuard ();
						this.inputMove();
					}
				}
				else {
					this.immobilizeCharacter();
				}
			}
			else if (Gdx.input.isKeyPressed(command.projectile)){
//				System.out.println("Projectile.");
				activatedAttack = PROJECTILE;
				attacks.get(activatedAttack).init (this.faceRight);	
				this.nbPressedJab = 0;
			}
			else {
				recoverGuard ();
				this.inputMove();
			}
		}
	}
	
	private boolean consumeSpecial (int requirement) {
		if (specialPoints >= requirement) {
			 specialPoints -= requirement;
			 return true;
		}
		return false;
	}
	
	public float whichHitboxWidth(CharacterType playerType){

		if (playerType == CharacterType.Ninja){
			return 125f;
		}
		
		else if (playerType == CharacterType.Mage){
			return 125f;
		}
		return 125f;
	}
	
	public float whichHitboxHeight(CharacterType playerType){

		if (playerType == CharacterType.Ninja){
			return 205f;
		}
		
		else if (playerType == CharacterType.Mage){
			return 205f;
		}
		return 205f;
	}
	public float whichWeight(CharacterType playerType){

		if (playerType == CharacterType.Ninja){
			return 1.5f;
		}
		
		else if (playerType == CharacterType.Mage){
			return 1.5f;
		}
		return 1.5f;
	}
	
	static Player isWinner(Player p1, Player p2){
		if (p1.health <= 0)
			return p2;
		return p1;
	}
	/**
	 * Draw the player in guard stance.
	 * @param batch Unopened batch
	 */
	public void drawGuardStance (SpriteBatch batch) {
		TextureRegion frame = guardAnimation.nextFrame();
		
		batch.begin ();
		if (!isFacingRight())
			frame.flip(true, false);
		batch.draw(frame, getX() - frame.getRegionWidth()/2, getY());
		if (!isFacingRight())
			frame.flip(true, false);
		batch.end ();
	}
}
