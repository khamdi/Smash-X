package com.mygdx.game;

import java.util.ArrayList;
import java.util.Date;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.IA.IAType;
import com.mygdx.game.IA.Level;
import com.mygdx.game.Map;
import com.mygdx.game.Player.CharacterType;

public class Platformer extends ApplicationAdapter {
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private CameraHandler camHandler;

	ArrayList<Character> characters;
	IA ia;
	private Animator animator;
	private Animator[] walkAnimation;

	Rectangle rec;
	
	Map map; 

	private long lastClickTime;

	static float BASICHITBOXDIMENSIONS = 256;
	
	
	static float GRAVITY = 35f;
	
	static int NBJUMP = 2;
	
	int numPerso = 0;
	boolean rule = false;
	private Character currentPlayer;
	
	@Override
	public void create() {
		lastClickTime = 0;
		

		characters = new ArrayList<>();
		

		
		characters.add( new Player (1000, 1000, BASICHITBOXDIMENSIONS, BASICHITBOXDIMENSIONS, 400, 1.2f,  100, NBJUMP,  3, CharacterType.Mage) );
		
		characters.add (new Player (2000, 4500, BASICHITBOXDIMENSIONS, BASICHITBOXDIMENSIONS, 400, 0.5f, 100, NBJUMP, 4, CharacterType.Ninja) );
		
		characters.add( new IA (1500f, 625f, BASICHITBOXDIMENSIONS, BASICHITBOXDIMENSIONS, 300, 1f, IAType.Pinguin, Level.Hard));
		characters.add(new IA (1500f, 625f, BASICHITBOXDIMENSIONS, BASICHITBOXDIMENSIONS, 100, 1f, IAType.Trader, Level.Hard));
		
		currentPlayer = characters.get(1);
		
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = 4f;
		camera.update();
		
		camHandler = new CameraHandler();
		
		map = new Map(shapeRenderer, batch, camera, 2000 , currentPlayer.getWeight(), currentPlayer.getNbJumpMax());
		map.generateMap();

		animator = new Animator(9, 4, "character.png", 1f / 9f);
		walkAnimation = animator.createAnimations(4);
	}
	
	@Override
	public void render() {
		Character currentPlayer = characters.get(1);
		Date time = new Date();
		camHandler.inputCamera(this.camera);		
		camHandler.cameraGestion(this.camera, this.map, this.characters, this.shapeRenderer, this.batch);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isTouched() && (time.getTime() - lastClickTime > 100)) {
			lastClickTime = time.getTime();
			map.generateMap();
			currentPlayer.setX(map.getWidthTile() * 3);
			currentPlayer.setY(( map.getHeightTile() * (float)map.getHeightMap() ) * (2f/3f));
		}

	
		map.updateMap( (int) map.pointAdressInMap(currentPlayer.getPosition()).x );


		for (Character perso: characters){
			if (perso instanceof Player){
				Player p = (Player) perso;
				p.input();
			}
			else if (perso instanceof IA){
				IA i = (IA) perso;
				if (i.getIAType() == IAType.Pinguin)
					i.pinguinMove(currentPlayer.position);
			}	
			perso.affectGravity();
		}
		IA i = (IA) characters.get(3);
		i.characterIAtrack2(currentPlayer.position);
		
		display();
		Collisions.checkCharacterCharacterCollision(characters.get(0), currentPlayer);
		Collisions.checkHorizontalCollision(characters, map);
		Collisions.checkVerticalCollision(characters, map);
			
		
		for (Character perso: characters) {

			perso.moveCharacter();
			perso.coolStun(Gdx.graphics.getDeltaTime());
			
			if (perso.isInvincible())
				perso.coolInvincibility (Gdx.graphics.getDeltaTime());
		}
	}
	
	public void display(){
		int inc = 0;
			
		map.displayWhiteBackground(camera);
		
		shapeRenderer.begin(ShapeType.Filled);
		
		for(Character perso: characters) {
			
			perso.displayCharacter(shapeRenderer, batch, walkAnimation);

			/* Attacks */
			shapeRenderer.setColor(Color.RED);
			
			if (perso.attacks.get(perso.getActivatedAttack()).isActive()) {
				if (perso.getActivatedAttack() >= 0 && perso.getActivatedAttack() <= 8) { /* It's a test because I'm too lazy to create other attacks atm */
					Attacks attack = perso.attacks.get(perso.getActivatedAttack());
					
					if (attack.nextStep(perso.getPosition())) {
						if (!perso.isStunned()) {
							try {
								System.out.println(perso.stun);
								perso.setStun(attack.getTriggerStun());
							}
							catch (IllegalStateException e){
								System.out.println(e.getMessage());
							}
						}
						
						attack.getCurrentPhase().points.forEach(e -> shapeRenderer.circle(e.x + perso.getX(), e.y + perso.getY() + 32, 5));
						
						if (attack.isConditional() && characters.stream()
								.filter(e -> (e != perso) && (attack.getCurrentPhase().checkHitboxCharacterCollision(attack.getCurrentPhase().points, perso.getPosition(), e)))
								.findFirst()
								.isPresent())
							((ConditionalAttack) attack).activateOnHit();
						
						characters	.stream()
									.filter(e -> (e != perso) && attack.getCurrentPhase().checkHitboxCharacterCollision(attack.getCurrentPhase().points, perso.getPosition(), e))
									.forEach(e -> perso.addSpecial(attack.getCurrentPhase().hitCharacter(perso.isFacingRight(), e)));
						
						Collisions.checkAttackMapCollision(map, attack.getCurrentPhase().points, perso.getPosition());
						
						perso.addForce(perso.attacks.get(perso.getActivatedAttack()).move());
					}
				}
				else {
					System.out.println("Attack #" + perso.getActivatedAttack() + " activated.");
				}
			}	
			
			/* Projectiles */
			for (ThrowProjectile tothrow : perso.projectiles) {
				for (Projectile projectile : tothrow.projectiles) {
					characters	.stream()
								.filter(e -> (e != perso) && projectile.checkHitboxCharacterCollision(projectile.getPoints(), projectile.position, e))
								.forEach(e -> projectile.hitCharacter(e));
					
					projectile	.getPoints()
								.stream()
								.forEach(e -> shapeRenderer.circle(projectile.position.x + e.x, projectile.position.y + e.y, 5));	
				}
				tothrow.projectiles.removeIf(e -> e.checkHitAnyCharacter(characters, perso) || Collisions.checkProjectileMapCollision(map, e.getPoints(), e.position));
				tothrow.projectiles.forEach(e -> e.moveProjectile());
			}
			
			
		}
		shapeRenderer.end();
		
		map.displayGraphicMap(camera);
		map.displayGrid(camera);
	}
	
}
