package com.mygdx.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BonusOnMap.BonusType;
import com.mygdx.game.IA.IAType;
import com.mygdx.game.IA.Level;
import com.mygdx.game.Map;
import com.mygdx.game.Player.CharacterType;

public class Platformer extends ApplicationAdapter {
	private boolean boolHitbox = false;
	private boolean boolBack = true;
	private boolean boolGrid = false;
	
	private BitmapFont font;
	private float roundTimeMax = 100;
	private float roundTime = roundTimeMax;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private CameraHandler camHandler;

	ArrayList<Character> characters;
	ArrayList<BonusOnMap> bonuses;
	IA ia;

	Rectangle rec;
	
	Map map; 
	boolean isMapGenerated = false;
	
	private long lastClickTime;

	static float BASICHITBOXDIMENSIONS = 150;
	
	static float BASICHITBOXWIDTH = 125;
	static float BASICHITBOXHEIGHT = 205;
	
	static int BASICMAPWIDTH = 500;
	
	private boolean mode;
	private boolean modeAventure;
	private boolean modeCombat;
	
	private boolean gameEnd;
	
	private Rectangle recAventure;
	private Rectangle recCombat;
	
	private static int widthButton;
	private static int heightButton;
	
	private TextureRegion texturButtonAv;
	private TextureRegion texturButtonCo;
	
	static float GRAVITY = 35f;
	
	static int NBJUMP = 2;
	static float WEIGHTTEST = 1.5f;
	
	int numPerso = 0;
	boolean rule = false;
	
	private Character player1 = null;
	private Character player2 = null;
	
	
	private Random randGen;
	private boolean randomSeed = false;
	
	@Override
	public void create() {
		lastClickTime = 0;
		
		mode = false;
		modeAventure = false;
		modeCombat = false;
		
		
		
		gameEnd = true;
		
		recAventure = new Rectangle();
		recCombat = new Rectangle();
		
		randGen = new java.util.Random(randomSeed ? (new Date()).getTime() : 1);

		characters = new ArrayList<>();
		bonuses = new ArrayList<>();

		texturButtonAv = new TextureRegion( new Texture("Modes/ModeAventure.png") );
		
		texturButtonCo = new TextureRegion(new Texture("Modes/ModeCombat.png") );
		
		widthButton = texturButtonAv.getRegionWidth();
		heightButton = texturButtonAv.getRegionHeight();
		
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = 1.8f;
		camera.update();
		
		camHandler = new CameraHandler(camera);
		shapeRenderer.setProjectionMatrix( camera.combined );

	}
	
	public void updateWorld(){
		int xPerso = (int) map.pointAdressInMap(this.player1.getPosition()).x;
		
//		System.out.println("width = " + map.getWidthSegment());
//		System.out.println("generationNotFinished = " + map.isGenerationNotFinished());
		if( camHandler.getXCamRightEdge() /*xPerso*/ >  map.getXSegmentStart() && map.isGenerationNotFinished()) {
			map.updateMap( randGen );
			IA.IASpawnSegment(randGen, characters, map.getXSegmentStart(), map.getWidthSegment(), (int)(map.getAltitudeSegment()*map.getHeightTile()), (int)map.getWidthTile() );
		}
		

	}
	
	public void AdventureMode(){
		System.out.println("Il y a : "  + (characters.size() -1) + " ennemies" );

		if( !isMapGenerated){
			characters.add ( Player.createPlayer (1000, 4500, 1, CharacterType.Ninja) );
			player1 = characters.get(0);
			map = new Map(shapeRenderer, batch, camera, BASICMAPWIDTH , player1.getWeight(), player1.getNbJumpMax());
			map.generateMap(randGen);
			map.generateBackground(randGen);
			isMapGenerated = true;
			
			characters.add(IA.createIA(map.getWidthTile() * 10, 5000, IAType.Trader, Level.Medium));
//			characters.add(IA.createIA(map.getWidthTile() * 10, 5000, IAType.Trader, Level.Hard));

		}
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		
		Date time = new Date();
		camHandler.inputCamera();		
		camHandler.cameraGestion(this.map, this.player1, this.shapeRenderer, this.batch);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isTouched() && (time.getTime() - lastClickTime > 100)) {
			
			lastClickTime = time.getTime();
			
			this.characters.clear();
			this.characters.add(Player.createPlayer (2000, 4500, 1, CharacterType.Ninja) );
			player1 = characters.get(0);
			
			
			map.generationRestart();
			map.generateMap(randGen);
			map.generateBackground(randGen);
			this.player1.setX(map.getWidthTile() * 3);
			this.player1.setY(( map.getHeightTile() * (float)map.getHeightMap() ) * (2f/3f));
			

		}
	
		updateWorld();
		display();
		
		for (Character perso: characters){
			if (perso instanceof Player){
				Player p = (Player) perso;
				p.input();
			}
			
			else if (perso instanceof IA){
				IA i = (IA) perso;
				i.TrackAndMove(this.player1.getPosition());
			}
			perso.affectGravity();
		}
		
		for(BonusOnMap bonus: bonuses){
			bonus.affectGravity();
		}
		
		/*------------------------------COLLISIONS---------------------------------------*/
		boolean fallDown;
//		Pour chaque perso on check ses collisions horizontales puis verticales
		for(Character character: characters){
			
			Collisions.checkHorizontalCollision(character.getHitbox(), character.getVectDir(), map, shapeRenderer);
			fallDown= (character.getVectDir().y < 0)?true:false;
			if (Collisions.checkVerticalCollision(character.getHitbox(), character.getVectDir(), map, shapeRenderer) ){
				if(fallDown  && character.isOnAir()){
					character.setStun(0.1f);
					character.resetJump();
					character.setOnFloor();
				}
			}
			else
				character.setOnAir();
		}
		
		/*Physique des bonus*/
		for(BonusOnMap bonus: bonuses){
			Collisions.checkHorizontalCollision(bonus.getHitBox(), bonus.getVectDir(), map, shapeRenderer);
			if( Collisions.checkVerticalCollision(bonus.getHitBox(), bonus.getVectDir(), map, shapeRenderer) ){
				bonus.blockHorizontalMove();
			}

		}
		
		Collisions.checkAllVerticalCollisionCharacterCharacter(characters);
		Collisions.checkAllHorizontalCollisionCharacterCharacter(characters);
		
		/*On check si player1 ramasse un bonus*/
		bonuses.forEach(e -> player1.checkIfPickUpBonus( e ) );
		
		
		/*------------------------------MOUVEMENTS---------------------------------------*/

		
		
//		Pour chaque perso, on le fait suivre son vecteur directeur et on descend son stun et invincibilit�.
		for (Character perso: characters) {
			perso.moveCharacter();
			
			if (perso.isStunned() )
				perso.coolStun(deltaTime);
			
			if (perso.isInvincible())
				perso.coolInvincibility (deltaTime);
			
			if (perso.isInvisible())
				perso.coolInvisibility(deltaTime);
			
		}
		
//		System.out.println (
//				"ATK:" + (1 + player1.bonusValue(BuffType.ATK)) +
//				"DEF:" + (1 + player1.bonusValue(BuffType.DEF)) +
//				"SPD:" + (1 + player1.bonusValue(BuffType.SPD))
//		);
		
		for(BonusOnMap bonus: bonuses){
			bonus.moveBonusOnMap();
			bonus.coolTimeOnMap(deltaTime);
		}
		characters.forEach(e -> e.generateBonusOnMapIfIsDead(bonuses, randGen));
		
		/*------------------------------SUPPRIME LES MORTS ---------------------------------------*/

		if (player1.isDead())
			gameEnd = true;
		
		characters.removeIf(e -> e.isDead());
		
		bonuses.removeIf(e -> e.isOutOfTime());
		player1.getBuff().removeIf(e -> e.isOutOfTime());

		player1.getBuff().forEach(e -> e.coolBuffTime(deltaTime));
//		player1.getBuff().forEach(e -> System.out.println( e.toString()));
//		System.out.println( "Il y a en action " + player1.getBuff().size() + " buffs " ) ;
		
//		for(Buff b: player1.getBuff()){
//			b.coolTimeOnMap(deltaTime);
//			System.out.println(b.toString());
//		}
		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8) )
//			bonuses.add( new BonusOnMap(player1.getPosition().cpy().add(0, player1.getHitbox().height), 
//					new Vector2(randGen.nextInt(4000) - 2000,randGen.nextInt(2000) + 1000 ),
//					/*BonusType.ATK_H*/ BonusType.values()[randGen.nextInt(BonusType.values().length)] ));
//		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.H) ){
//			if ( boolHitbox )
//				boolHitbox = false;
//			else
//				boolHitbox = true;
//		}
//		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.B) ){
//			if ( boolBack )
//				boolBack = false;
//			else
//				boolBack = true;
//		}
//		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.G) ){
//			if ( boolGrid )
//				boolGrid = false;
//			else
//				boolGrid = true;
//		}
		
	}

	
	public void CombatMode(){
		roundTime -= Gdx.graphics.getDeltaTime()/2;

		if( !isMapGenerated){
			characters.add ( Player.createPlayer (2000, 4500, 4, CharacterType.Ninja) );
			player1 = characters.get(0);
			
			characters.add ( Player.createPlayer (4000, 4500, 3, CharacterType.Ninja) );
			characters.get(1).setFaceLeft();
			player2 = characters.get(1);
		
			map = new Map(shapeRenderer, batch, camera, 60 , player1.getWeight(), player1.getNbJumpMax());
			
			map.generateArena0();
			
			map.generateBackground(randGen);
			isMapGenerated = true;
			
		}
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		
//		camHandler.inputCamera();		
		camHandler.cameraGestionCombatMode(map, player1, player2, shapeRenderer, batch);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	
		display();
		
		for (Character perso: characters){
			if (perso instanceof Player){
				Player p = (Player) perso;
				p.input();
			}
			
			perso.affectGravity();
		}
		
		
		/*------------------------------COLLISIONS---------------------------------------*/
		boolean fallDown;
//		Pour chaque perso on check ses collisions horizontales puis verticales
		for(Character character: characters){
			
			Collisions.checkHorizontalCollision(character.getHitbox(), character.getVectDir(), map, shapeRenderer);
			fallDown= (character.getVectDir().y < 0)?true:false;
			if (Collisions.checkVerticalCollision(character.getHitbox(), character.getVectDir(), map, shapeRenderer) ){
				if(fallDown  && character.isOnAir()){
					character.setStun(0.1f);
					character.resetJump();
					character.setOnFloor();
				}
			}
			else
				character.setOnAir();
		}
		
		
		Collisions.checkAllVerticalCollisionCharacterCharacter(characters);
		Collisions.checkAllHorizontalCollisionCharacterCharacter(characters);
//		System.out.println( characters.size() );
		
		
		/*------------------------------MOUVEMENTS---------------------------------------*/
//		Pour chaque perso, on le fait suivre son vecteur directeur et on descend son stun et invincibilit�.
		for (Character perso: characters) {
			perso.moveCharacter();
			
			if (perso.isStunned() )
				perso.coolStun(deltaTime);
			
			if (perso.isInvincible())
				perso.coolInvincibility (deltaTime);
			
			if (perso.isInvisible())
				perso.coolInvisibility(deltaTime);
		}
		
		/*------------------------------SUPPRIME LES MORTS ---------------------------------------*/

		if(characters.stream().filter(e -> e instanceof Player).anyMatch(e -> e.isDead()) || roundTime <= 0 ){
			Player tmp;
			tmp = Player.isWinner((Player) player1, (Player) player2);
			tmp.roundWon ++;
			
			if (tmp.roundWon == 3){
				if (tmp.equals(player1))
					System.out.println("Congralutation Player 1 You have won this game! Player 2 you are juste a Loser !!");
				else
					System.out.println("Congralutation Player 2 You have won this game! Player 1 you are juste a Loser !!");
				
				gameEnd = true;
				
			}
			roundTime = roundTimeMax;
			
			player1.regeneration();
			player1.setPosition(2000, 4500);
			
			player2.regeneration();
			player2.setPosition(4000, 4500);
		}
		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.H) ){
//			if ( boolHitbox )
//				boolHitbox = false;
//			else
//				boolHitbox = true;
//		}
//		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.B) ){
//			if ( boolBack )
//				boolBack = false;
//			else
//				boolBack = true;
//		}
//		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.G) ){
//			if ( boolGrid )
//				boolGrid = false;
//			else
//				boolGrid = true;
//		}
	}
	
	
	
	
	public void display(){
		
		map.displayWhiteBackground(camHandler);
		if(boolBack)
			map.displayGraphicBackground(camHandler);
//		map.displayDebugMap(camHandler);

		
		for(Character perso: characters) {
			shapeRenderer.begin(ShapeType.Filled);
			/* Attacks */
			shapeRenderer.setColor(Color.RED);
			
			if (perso.attacks.get(perso.getActivatedAttack()).isActive()) {
				if (perso.getActivatedAttack() >= 0 && perso.getActivatedAttack() <= 11) { /* It's a test because I'm too lazy to create other attacks atm */
					Attacks attack = perso.attacks.get(perso.getActivatedAttack());
					
					if (attack.nextStep(perso.getPosition(), perso.getCommandType())) {
						if (!perso.isStunned()) {
//							System.out.println(perso.stun);
							perso.setStun(attack.getTriggerStun());
						}
//						if(perso == player1){
							TextureRegion frame = attack.getCurrentFrame();
							if(!perso.isInvisible()){
								if (!perso.isFacingRight())
									frame.flip(true, false);
								batch.begin();
								batch.draw(frame, perso.getX() - frame.getRegionWidth()/2, perso.getY());
								batch.end();
								if (!perso.isFacingRight())
									frame.flip(true, false);
//							}
						}
			//			attack.getCurrentPhase().points.forEach(e -> shapeRenderer.circle(e.x + perso.getX(), e.y + perso.getY() + 32, 5));
						
						if (attack.isConditional() && characters.stream()
								.filter(e -> (e != perso) && (attack.getCurrentPhase().checkHitboxCharacterCollision(attack.getCurrentPhase().points, perso.getPosition(), e)))
								.findFirst()
								.isPresent())
							((ConditionalAttack) attack).activateOnHit();
						
						characters	.stream()
									.filter(e -> (e != perso) && attack.getCurrentPhase().checkHitboxCharacterCollision(attack.getCurrentPhase().points, perso.getPosition(), e))
									.forEach(e -> perso.addSpecial(attack.getCurrentPhase().hitCharacter(perso.isFacingRight(), perso, e)));
						
						Collisions.checkAttackMapCollision(map, attack.getCurrentPhase().points, perso.getPosition());
						
						perso.addForce(perso.attacks.get(perso.getActivatedAttack()).move());
					}
				}
				else {
//					System.out.println("Attack #" + perso.getActivatedAttack() + " activated.");
				}
			}
			
			// else du gros if
			else {
				
				if (perso instanceof Player && perso.getGuard()) {
					((Player) perso).drawGuardStance(batch);
				}
				else if (perso.isStunned()) {
					perso.drawCharacterHit(batch);
				}
				else
					perso.displayCharacter(shapeRenderer, batch);
//				else
//					perso.displayStats(shapeRenderer, camHandler, camera, boolHitbox, false );

//				perso.displayCharacter(shapeRenderer, batch);
			}
			
			/* Projectiles */
			shapeRenderer.setColor(Color.RED);
			batch.begin();
			for (ThrowProjectile tothrow : perso.projectiles) {
				for (Projectile projectile : tothrow.projectilesThrown) {
					characters	.stream()
								.filter(e -> (e != perso) && projectile.checkHitboxCharacterCollision(projectile.getPoints(), projectile.position, e))
								.forEach(e -> projectile.hitCharacter(e));
					
					TextureRegion frame = projectile.getFrame();
					if (!projectile.movingRight())
						frame.flip(true, false);
					batch.draw(frame, projectile.position.x - frame.getRegionWidth()/2, projectile.position.y);
					if (!projectile.movingRight())
						frame.flip(true, false);
//					projectile	.getPoints()
//								.stream()
//								.forEach(e -> shapeRenderer.circle(projectile.position.x + e.x, projectile.position.y + e.y, 5));
				}
				
				tothrow.addAll();
				for (Projectile p : tothrow.projectilesThrown) {
					if (Collisions.checkProjectileMapCollision(map, p.getPoints(), p.position))
						p.hit();
				}
				tothrow.projectilesThrown.removeIf(e -> e.ifDestroy());
				tothrow.projectilesThrown.forEach(e -> e.moveProjectile());
			}
			
			if (perso == player1) {
				perso.displayStats(shapeRenderer, camHandler, camera, boolHitbox, true, false );
			}
			
			else if (perso == player2) {
				perso.displayStats(shapeRenderer, camHandler, camera, boolHitbox, false, true );
			}
			
			else
				perso.displayStats(shapeRenderer, camHandler, camera, boolHitbox, false , false);
			
			
			
			font.getData().setScale(5);
			
	
			if(modeCombat)
				displayRoundTime();
			
			batch.end();
			shapeRenderer.end();

		}

		for(BonusOnMap bonus: bonuses){
			bonus.displayBonus(shapeRenderer, batch);
		}
	
		
		
		map.displayGraphicMap(camHandler);
		if( boolGrid){
			map.displayGrid(camHandler);
			map.displayBackgroundGrid(camHandler);
		}

	
	}
	
	public void displayRoundTime(){
		font.draw(batch, String.format("%d", (int)roundTime), camera.position.x, camera.position.y -10 + camHandler.getEffectiveViewPortHeight()/2);
	}
	
	@Override
	public void render() { 
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float mouseX, mouseY;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || gameEnd){
			gameEnd = false;
			modeAventure = false;
			modeCombat = false;
			
			isMapGenerated = false;
			
			try {       
				shapeRenderer.end();
				batch.end();
			}catch (IllegalStateException e){}
			
			
			camera.zoom = 1.8f;
			camera.update();
			shapeRenderer.setProjectionMatrix( camera.combined );
			batch.setProjectionMatrix( camera.combined );
			
			
			characters.clear();
			
		}
		
		if (modeAventure){
			AdventureMode();
		}
		else if(modeCombat){
			CombatMode();
		}
		
		else {
			
			recAventure.set((Gdx.graphics.getWidth() / 2) - 175, (Gdx.graphics.getHeight() / 4) - 110, 90 + widthButton/2, 45 + heightButton/2);
			
			recCombat.set((Gdx.graphics.getWidth() / 2) - 175, (Gdx.graphics.getHeight() / 2) , 87 + widthButton/2 , 45 + heightButton/2);
		
			batch.begin();
			
			batch.draw(texturButtonAv, camera.position.x - (widthButton/2), camera.position.y + heightButton/2, widthButton, heightButton);
			batch.draw(texturButtonCo, camera.position.x - (widthButton/2), camera.position.y - heightButton);
			
			batch.end();
			
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
				mouseX = Gdx.input.getX();
				mouseY = Gdx.input.getY();
				System.out.println("MouseX " + mouseX + " mouseY" + mouseY);
				if (recAventure.contains(mouseX, mouseY))
					modeAventure = true;
				
				else if (recCombat.contains(mouseX, mouseY)){
					System.out.println("ok");
					modeCombat = true;
				}
				
			}
		}
	}
	
}
