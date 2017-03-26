package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character {	
	public enum CharacterType {
		Ninja,
		Warrior,
		Mage;
	}
	
	private int nbPressedJab;
	
	private CharacterType type;
	
	private CommandType command;
	
	public Player(float x, float y,float hitboxWidth, float hitboxHeight, int healthMax, float weight,  int startSpecial, int nbJumpMax,  int cmd, CharacterType type) {
		super(x, y, hitboxWidth, hitboxHeight, healthMax, startSpecial, weight, nbJumpMax,  false);
		
		this.command = new CommandType(cmd);
		this.type = type;
		this.attacks = this.PlayerAttacks(type);
		this.nbPressedJab = 0;
	}
	
	private ArrayList<Attacks> PlayerAttacks(CharacterType type) {
		ArrayList<Attacks> attacks = new ArrayList<>();
		switch (type){
			case Ninja :
				/*Projectiles Shuriken*/
				ArrayList<Vector2> pointsShuriken = new ArrayList<>();
				pointsShuriken.add(new Vector2(0, 0));
				pointsShuriken.add(new Vector2(10, 10));
				pointsShuriken.add(new Vector2(-10, 10));
				pointsShuriken.add(new Vector2(-10, -10));
				pointsShuriken.add(new Vector2(10, -10));
				
				ThrowProjectile shuriken =  new ThrowProjectile(0.2f, 4, new Vector2 (10, 100), 0.1f, 0.3f, pointsShuriken, new Vector2 (), false, new Vector2 (30, 32), new Vector2 (300, 0), this);
				projectiles.add(shuriken);
				attacks.add(
						new ComposedAttack (
								shuriken
							)
						);
				
				/*Jabs Ninja Manche*/
				ArrayList<Vector2> pointsJabsNinja1 = new ArrayList<>();
				pointsJabsNinja1.add(new Vector2(30, 0));
				pointsJabsNinja1.add(new Vector2(45, 0));
				pointsJabsNinja1.add(new Vector2(60, 0));
				attacks.add(
						new ComposedAttack(
							new Attack (0.4f, 1, new Vector2 (25, 0), 0.25f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 0), 0.25f, pointsJabsNinja1),
							new Delay (0.2f, new Vector2(0, 0))
						)
					);
				
				/*Jabs Ninja Coup épée*/
				ArrayList<Vector2> pointsJabsNinja2 = new ArrayList<>();
				pointsJabsNinja2.add (new Vector2 (30, 0));
				pointsJabsNinja2.add (new Vector2 (45, 0));
				pointsJabsNinja2.add (new Vector2 (60, 0));
				pointsJabsNinja2.add (new Vector2 (75, 0));
				
				attacks.add(
							new ComposedAttack(
								new Attack (0.4f, 4, new Vector2 (50, 0), 0.25f, (float)Math.PI / 2, 0, new Vector2 (0, 0), new Vector2 (0, 0), 0.25f, pointsJabsNinja2),
								new Delay (0.2f, new Vector2(0, 0))
							)
						);
				
				/*Jabs Ninja Coup de pied*/
				ArrayList<Vector2> pointsJabsNinja3 = new ArrayList<>();
				pointsJabsNinja3.add (new Vector2 (15, 0));
				pointsJabsNinja3.add (new Vector2 (30, 0));
				pointsJabsNinja3.add (new Vector2 (45, 0));
				attacks.add(
						new ComposedAttack(
								 new Attack(0.4f, 2, new Vector2 (25, 25), 0.3f, -(float)(Math.PI) / 6, -(float) (Math.PI) / 6, new Vector2 (0, 0), new Vector2 (0, 0), 0.1f, pointsJabsNinja3),
								 new Delay (0.2f, new Vector2(0, 0))
							)
						);
				
				/*Coté corps a corps Estoc*/
				ArrayList<Vector2> pointsEstoc = new ArrayList<>();
				pointsEstoc.add (new Vector2 (30, 0));
				pointsEstoc.add (new Vector2 (45, 0));
				pointsEstoc.add (new Vector2 (60, 0));
				pointsEstoc.add (new Vector2 (75, 0));
				
				attacks.add(
					new ConditionalAttack(new Attack (0.2f, 5, new Vector2 (25, 0), 0.1f, 7f * (float)(Math.PI) / 6f, -(float) (Math.PI) / 6f, new Vector2 (0, 0), new Vector2 (0, 0), 0.2f, pointsEstoc),
							new Attack(0.4f, 1, new Vector2 (15, 300), 0.1f, -1/6f * (float) (Math.PI), 1/3f * (float) (Math.PI), new Vector2 (0, 0), new Vector2 (15, 50), 0.2f, pointsEstoc),
							new Delay(0.2f, new Vector2 (0,0))
					)	
				);
				/*
				attacks.add(
							new ComposedAttack(
								new Attack (0.4f, 6, new Vector2 (50, 0), 0.25f, 0, 0, new Vector2 (30, 0), new Vector2 (0, 0), 0.25f, pointsEstoc),
								new Delay (0.5f, new Vector2(0, 0))
							)
						);
				*/
				
				/*Haut corps a corps coup épée*/
				attacks.add( 
						new ComposedAttack(
								new Attack (0.3f, 5, new Vector2 (25, 0), 0.3f, 7f * (float)(Math.PI) / 6f, -(float) (Math.PI) / 6f, new Vector2 (0, 0), new Vector2 (0, 0), 0.3f, pointsEstoc),
								new Delay (0.5f, new Vector2(0, 0))
							)
						);
				
				/*Bas corps a corps Balayette*/
				ArrayList<Vector2> pointsBalayette = new ArrayList<>();
				pointsBalayette.add (new Vector2 (15, 0));
				pointsBalayette.add (new Vector2 (30, 0));
				pointsBalayette.add (new Vector2 (45, 0));
				attacks.add(
						new ComposedAttack(
								 new Attack(0.2f, 3, new Vector2 (25, 25), 0.3f, -(float)(Math.PI) / 6, -(float) (Math.PI) / 6, new Vector2 (15, 0), new Vector2 (0, 0), 0.1f, pointsBalayette),
								 new Delay (0.2f, new Vector2(0, 0))
							)
						);
				
				/*Spe 25 Katon*/
				ArrayList<Vector2> pointsKaton = new ArrayList<>();
				pointsKaton.add (new Vector2 (0, 0));
				pointsKaton.add (new Vector2 (35, 0));
				pointsKaton.add (new Vector2 (70, 0));
				pointsKaton.add (new Vector2 (17, 17));
//				pointsKaton.add (new Vector2 (35, 35));
				pointsKaton.add (new Vector2 (52, 17));
				pointsKaton.add (new Vector2 (17, -17));
//				pointsKaton.add (new Vector2 (35, -35));
				pointsKaton.add (new Vector2 (52, -17));
				
				ThrowProjectile katon = new ThrowProjectile(0.5f, 10, new Vector2 (25, 25), 0.3f, 0.3f, pointsKaton, new Vector2 (), false, new Vector2 (30, 32), new Vector2 (300, 0), this);
				projectiles.add(katon);
				
				attacks.add(
						new ComposedAttack(
								katon,   
								new Delay (0.5f, new Vector2(0, 0))
							)
						);
				
				/*Spe 50 Raikiri 1/4 ecrant ? */
				ArrayList<Vector2> pointsRaikiri = new ArrayList<>();
				pointsRaikiri.add (new Vector2 (30, 0));
				pointsRaikiri.add (new Vector2 (45, 0));
				pointsRaikiri.add (new Vector2 (60, 0));
				
				attacks.add( 
						new ComposedAttack(
								new Delay (0.3f, new Vector2(0, 0)),
								new Attack (0.5f, 20, new Vector2 (25, 0), 0.3f, 0, 0, new Vector2 (25, 0), new Vector2 (250, 0), 0.3f, pointsRaikiri),
								new Delay (0.5f, new Vector2(0, 0))
							)
						);
				
				/*Spe 100 Slash Annimation ? */
				
				return attacks;
				
			case Warrior :
			
				
			case Mage :
					/*Projectile Boule élémentaire*/
				ArrayList<Vector2> pointsElementale = new ArrayList<>();
				pointsElementale.add(new Vector2(0, 0));
				pointsElementale.add(new Vector2(10, 10));
				pointsElementale.add(new Vector2(-10, 10));
				pointsElementale.add(new Vector2(-10, -10));
				pointsElementale.add(new Vector2(10, -10));
				/*gestion du chargement des attaques*/
				ThrowProjectile elementale =  new ThrowProjectile(0.2f, 3, new Vector2 (10, 100), 0.1f, 0.3f, pointsElementale, new Vector2 (), false, new Vector2 (30, 32), new Vector2 (300, 0), this);
				projectiles.add(elementale);
				attacks.add(
						new ComposedAttack (
								elementale
							)
						);
				
				/*Jabs Mage Boule Feu */
				ArrayList<Vector2> pointsJabsMage1_2 = new ArrayList<>();
				pointsJabsMage1_2.add (new Vector2 (30, 0));
				pointsJabsMage1_2.add (new Vector2 (75, 0));
				pointsJabsMage1_2.add (new Vector2 (120, 0));
				pointsJabsMage1_2.add (new Vector2 (47, 27));
//				pointsJabsMage1.add (new Vector2 (45, 45));
				pointsJabsMage1_2.add (new Vector2 (102, 27));
				pointsJabsMage1_2.add (new Vector2 (47, -27));
//				pointsJabsMage1.add (new Vector2 (45, -45));
				pointsJabsMage1_2.add (new Vector2 (102, -27));
				attacks.add(
						new ComposedAttack(
							new Attack (0.4f, 2, new Vector2 (25, 0), 0.25f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 0), 0.25f, pointsJabsMage1_2),
							new Delay (0.2f, new Vector2(0, 0))
						)
					);
				
				/*Jabs Mage Boule Electrique*/
				attacks.add(
						new ComposedAttack(
							new Attack (0.4f, 2, new Vector2 (25, 0), 0.25f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 0), 0.25f, pointsJabsMage1_2),
							new Delay (0.2f, new Vector2(0, 0))
						)
					);
				
				/*Jabs Mage Pique de glace */
				ArrayList<Vector2> pointsJabsMage3 = new ArrayList<>();
				pointsJabsMage3.add (new Vector2 (100, -20));
				pointsJabsMage3.add (new Vector2 (120, -20));
				pointsJabsMage3.add (new Vector2 (140, -20));
				pointsJabsMage3.add (new Vector2 (110, 0));
				pointsJabsMage3.add (new Vector2 (130, 0));
				pointsJabsMage3.add (new Vector2 (120, 0));
				pointsJabsMage3.add (new Vector2 (120, 20));
				pointsJabsMage3.add (new Vector2 (120, 35));
				pointsJabsMage3.add (new Vector2 (120, 50));
				attacks.add(
						new ComposedAttack(
							new Attack (0.4f, 4, new Vector2 (0, 50), 0.25f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 0), 0.25f, pointsJabsMage3),
							new Delay (0.2f, new Vector2(0, 0))
						)
					);
				/*attaque côté Explosion attaque a condition*/
				attacks.add(new ComposedAttack());
				
				/*Attaque Haut Hélicoptère*/
				ArrayList<Vector2> pointsChopper = new ArrayList<>();
				pointsChopper.add(new Vector2(-20, 40));
				pointsChopper.add(new Vector2(-15, 40));
				pointsChopper.add(new Vector2(-10, 40));
				pointsChopper.add(new Vector2(-5, 40));
				pointsChopper.add(new Vector2(0, 40));
				pointsChopper.add(new Vector2(5, 40));
				pointsChopper.add(new Vector2(10, 40));
				pointsChopper.add(new Vector2(15, 40));
				pointsChopper.add(new Vector2(20, 40));
				attacks.add(
						new ComposedAttack (
								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
								new Attack (0.1f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
								new Attack (0.2f, 1, new Vector2 (0, 15), 0.3f, 0, 0, new Vector2 (0, 0), new Vector2 (0, 15), 0.2f, pointsChopper),
								new Delay(0.5f, new Vector2())
							)
						);
				
				/*Corps a Corps bas Soin*/
				
				/*Special 25 YOU SHALL NOT PASS*/
				
				/*Special 50 Invocation*/
				
				/*Special 100 PVC*/
				
				return attacks;
				
			default : 
				throw new IllegalArgumentException("This player does not exist");
		}
	}
	
	
	
	private void inputSpecial(){
		if(Gdx.input.isKeyPressed(command.up)) {
			System.out.println("Special up");
			if (haveEnough (special, 50)) {
				activatedAttack = SPE50;
//				setStun (attacks.get(activatedAttack).getTotalTime());
				special -= 50;
		 		attacks.get(activatedAttack).init (this.faceRight);
			}
		}
		else if(Gdx.input.isKeyPressed(command.down) ) {
			System.out.println("Special down ");
			if (haveEnough (special, 100)) {
				special -= 100;
				activatedAttack = SPE100;
//				setStun (1f);
			}
		}
		else { /* Default */
			System.out.println("Special neutral");

			System.out.println(special + " " + haveEnough(special, 25));
			if (haveEnough (special, 25)) {
				activatedAttack = SPE25;
				special -= 25;
//				setStun (attacks.get(activatedAttack).getTotalTime());
		 		attacks.get(activatedAttack).init (this.faceRight);
		 		
			}
		}
	}
	
	private void inputAttack(){
		if(Gdx.input.isKeyPressed(command.up)) {
			System.out.println("Attack up.");
			activatedAttack = ATTACKUP;
//			setStun (attacks.get(activatedAttack).getTotalTime());
		 	attacks.get(activatedAttack).init (this.faceRight);
		 	this.nbPressedJab = 0;
		}
		else if(Gdx.input.isKeyPressed(command.down) ) {
			System.out.println("Attack down.");
			activatedAttack = ATTACKDOWN;
//			setStun (attacks.get(activatedAttack).getTotalTime());
		 	attacks.get(activatedAttack).init (this.faceRight);
		 	this.nbPressedJab = 0;
		}
		else if(Gdx.input.isKeyPressed(command.left) || Gdx.input.isKeyPressed(command.right)) {
			System.out.println("Attack side.");
			activatedAttack = ATTACKSIDE;
//			setStun (attacks.get(activatedAttack).getTotalTime());
		 	attacks.get(activatedAttack).init (this.faceRight);
		 	this.nbPressedJab = 0;
		}
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
			System.out.println("Attack neutral.");
//		 	setStun (attacks.get(activatedAttack).getTotalTime());
		 	attacks.get(activatedAttack).init (this.faceRight);
		}
	}
	
	private void inputMove() {
		boolean isMoving = false;
		 if(Gdx.input.isKeyPressed(command.left)){
			 this.moveLeft();
			 this.nbPressedJab = 0;
			 isMoving = true;
		 }
		 if(Gdx.input.isKeyPressed(command.right)){
			 this.moveRight();
			 this.nbPressedJab = 0;
			 isMoving = true;
		 }
		 if(Gdx.input.isKeyJustPressed(command.jump)){
			 this.jump();
			 this.nbPressedJab = 0;
			 isMoving = true;
		 }
		 if(Gdx.input.isKeyPressed(command.down)){
			 this.affectGravity ();
			 this.nbPressedJab = 0;
			 isMoving = true;
		 }
		 if (!isMoving)
			 this.blockHorizontalMove();
		 
	}

	public void input() {
		if (isStunned() == false) {
			if (this.isOnFloor()){
				if (Gdx.input.isKeyPressed(command.special)) {
					this.nbPressedJab = 0;
					this.inputSpecial();
				}
				else if (Gdx.input.isKeyJustPressed(command.attack)) {
					this.inputAttack();
				}		
				else if (Gdx.input.isKeyPressed(command.projectile)){
					System.out.println("Projectile.");
					activatedAttack = 0;	
				 	attacks.get(activatedAttack).init (this.faceRight);	
				 	this.nbPressedJab = 0;
				}
			}
			this.inputMove();
		}
	}
	
	private boolean haveEnough (int points, int requirement) {
		return (points >= requirement);
	}
}
