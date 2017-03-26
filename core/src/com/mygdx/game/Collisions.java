package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.IA.IAType;

public class Collisions {
	
	static public boolean checkHitboxesCollision(Rectangle hitBox1, Rectangle hitbox2){
		return hitBox1.overlaps(hitbox2);
	}
	
	static public boolean checkVerticalCollision(Rectangle hitBox,Vector2 vectDir, Map map, ShapeRenderer shapeRenderer){
		int nbPoint = 4;
		float spacingPoints;
		double directionY;
		Vector2 verticalVectDir;
		float  parcourY, pasY;
		boolean collision ;
		Vector2 downLeft ;
		Vector2 upLeft ;
	
		float delta;
		
		collision = false;
		downLeft = new Vector2(hitBox.x + 8 , hitBox.y);
		upLeft = new Vector2(hitBox.x + 8, hitBox.y + hitBox.height);
		
//		Vector2 downRight = new Vector2(perso.getHitbox().x + perso.getHitbox().width, perso.getHitbox().y);
//		Vector2 upRight = new Vector2(perso.getHitbox().x + perso.getHitbox().width, perso.getHitbox().y + perso.getHitbox().height);
		Vector2 origin;
		delta = Gdx.graphics.getDeltaTime();
		
		spacingPoints = (hitBox.width-16) / nbPoint;
		
		/*ATTENTION !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
		verticalVectDir = new Vector2(0, vectDir.y).scl(delta);
				
		directionY = Math.signum(verticalVectDir.y);			
//		System.out.println( vectDir.y);			
		origin = (directionY > 0)? upLeft.cpy():  downLeft.cpy();	
		
		pasY = verticalVectDir.y / 10;
		
		for(int i = 0; i <= nbPoint; i++){
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.RED);
			Vector2 tmp = new Vector2( origin.cpy() );
			Vector2 vectmp = new Vector2( 0,0);
//			shapeRenderer.circle(origin.x, origin.y, 10);
			shapeRenderer.end();
			shapeRenderer.begin(ShapeType.Line);

//			shapeRenderer.line(origin.x, origin.y, origin.x,  origin.y - 100);
			shapeRenderer.end();
			for( parcourY = 0 ;( Math.abs(parcourY) < Math.abs(verticalVectDir.y)  && collision == false);  ){
				tmp.add(0, pasY);		
				parcourY += pasY;					
				Vector2 ttmp = map.pointAdressInMap( tmp );		
				
				if(  ( map.isSolidTileIndex ((int)ttmp.x, (int)ttmp.y) )  ) {
					parcourY -= pasY;
					pasY /= 2;
					if (pasY < 0.1) {
						vectmp.add(0, parcourY );
						collision = true;
						
						vectDir.set( vectDir.x,  vectmp.scl(1/delta).y);
					}
				}		
			}				
			
			
			origin.add(  spacingPoints,0 );
		}
		return collision;
	}	

	static public void checkHorizontalCollision( Rectangle hitBox,Vector2 vectDir, Map map, ShapeRenderer shapeRenderer ){
		int nbPoint = 4;
		float spacingPoints;
		double directionX;
		Vector2 horizontalVectDir;
		float  parcourX, pasX;
		boolean collision ;
		Vector2 downLeft ;
		Vector2 downRight ;
	
		float delta;
			collision = false;
			downLeft = new Vector2(hitBox.x, hitBox.y );
			downRight = new Vector2(hitBox.x + hitBox.width, hitBox.y );
			
			Vector2 origin;
			delta = Gdx.graphics.getDeltaTime();
			spacingPoints = hitBox.height / nbPoint;
			horizontalVectDir = new Vector2( vectDir.x, 0).scl(   delta   );
					
			directionX = Math.signum(horizontalVectDir.x);			
	//		System.out.println( vectDir.x);			
			origin = (directionX > 0)? downRight.cpy():  downLeft.cpy();						
			pasX = horizontalVectDir.x / 10;
			
			
			
			
			
			for(int i = 0; i <= nbPoint; i++){
//				shapeRenderer.begin(ShapeType.Filled);
//				shapeRenderer.setColor(Color.RED);
				Vector2 tmp = new Vector2( origin.cpy() );
				Vector2 vectfinal = new Vector2( 0,0);
//				shapeRenderer.circle(origin.x, origin.y, 10);
//				shapeRenderer.end();
//				shapeRenderer.begin(ShapeType.Line);
//
//				shapeRenderer.line(origin.x, origin.y, origin.x - 100, origin.y);
//				shapeRenderer.end();
				
				collision = false;
				for( parcourX = 0 ;( Math.abs(parcourX) < Math.abs(horizontalVectDir.x)  && collision == false);  ){
					tmp.add(pasX, 0);		
					parcourX += pasX;					
					Vector2 ttmp = map.pointAdressInMap( tmp );		
					
					if(map.isSolidTileIndex ((int)ttmp.x, (int)ttmp.y)) {
						parcourX -= pasX;
						pasX /= 2;
						if (pasX < 0.1) {
							vectfinal.add(parcourX, 0);
							collision = true;
							vectDir.set( vectfinal.scl(1/delta).x ,vectDir.y  );
						}
					}		
					
				}				
				origin.add(0,  spacingPoints );
			}
			
			
	}	

	
	
	static public void checkCharacterCharacterCollision(Character c1, Character c2){
		float ecart = 0;
		
		Vector2 v1 = c1.getVectDir().cpy().scl(c1.getWeight()) ;
		Vector2 v2 = c2.getVectDir().cpy().scl(c2.getWeight()) ;
		
		Vector2 vSum = new Vector2(v1.add(v2));
		
		float x1 = c1.getPosition().x;
		float x2 = c2.getPosition().x;
		
	
		Character leftChar;
		Character rightChar;
		
		if(x1 - x2 < 0){
			leftChar = c1;
			rightChar = c2;
		}
		else{
			leftChar = c2;
			rightChar = c1;
		}
		
		Vector2 leftVectDir = leftChar.getVectDir().cpy();
		Vector2 rightVectDir = rightChar.getVectDir().cpy();
		
		if( leftChar.getHitbox().overlaps( rightChar.getHitbox() ) ){
		
			if ( (leftVectDir.x >= 0 && rightVectDir.x <= 0)  ) {
				
				leftChar.setVectDir(vSum.x, leftChar.getVectDir().y);
				rightChar.setVectDir(vSum.x, rightChar.getVectDir().y);
				if(   vSum.x > 0  ){
					leftChar.setX(  rightChar.getX() - rightChar.getHitbox().width );
				}
				else
					rightChar.setX(  leftChar.getX() + leftChar.getHitbox().width );
			}
					
//			ecart = Math.abs(x2+ c1.getHitbox().width ) - Math.abs(x1);
			
			
//			if ( ( x1 < x2   && c1.isFacingRight() && !c2.isFacingRight() )
//				|| ( x2 < x1   && c2.isFacingRight() && !c1.isFacingRight() ) ){
				
//				if(c1.getWeight() > c2.getWeight())
//					c1.setX(c1.getX() + ecart);
		
			
				
		}
		
	}

	

	static public boolean checkPointMapCollision(Map map, Vector2 point, Vector2 position) {
		Vector2 address = point.cpy().add(position);
		if (map.isValidPoint(address)) {
			if (map.isSolidTile(address)) {
//				Destruction Map  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//				map.setTileMapToState(address, 0);
				return true; /* Collides w/ the map */
			}
			return false;
		}
		return true; /* Out of bounds */
	}
	
	static public boolean checkProjectileMapCollision (Map map, ArrayList<Vector2> points, Vector2 position) {
		return points.stream().anyMatch(e -> checkPointMapCollision(map, e, position.cpy()));
	}
	
	static public boolean checkAttackMapCollision (Map map, ArrayList<Vector2> points, Vector2 position) {
		return points.stream().anyMatch(e -> checkPointMapCollision(map, e.cpy().add(0, 32), position.cpy()));
	}
	
	
	
	static public void checkAllHorizontalCollisionCharacterCharacter( ArrayList<Character> characters ){
		int nbPoint = 4;
		float spacingPoints;
		double directionX;
		Vector2 horizontalVectDir;
		float  parcourX, pasX;
		boolean collision ;
		Vector2 downLeft ;
		Vector2 downRight ;
		boolean go = true;
		float delta;
		for(Character curChar: characters){
			go = true;
			
			if (curChar instanceof IA ){
				IA tmp = (IA) curChar;
				if (tmp.getIAType() == IAType.Pinguin)
					go = false;
			}
			if( (!curChar.isInvisible()) && go  ){
				collision = false;
				downLeft = new Vector2(curChar.getHitbox().x, curChar.getHitbox().y + 1);
				downRight = new Vector2(curChar.getHitbox().x + curChar.getHitbox().width, curChar.getHitbox().y + 1);
				
				Vector2 origin;
				delta = Gdx.graphics.getDeltaTime();
				spacingPoints = curChar.getHitbox().height / nbPoint;
				horizontalVectDir = new Vector2( curChar.getVectDir().x, 0).scl(   delta   );
						
				directionX = Math.signum(horizontalVectDir.x);			
		//		System.out.println( vectDir.x);			
				origin = (directionX > 0)? downRight.cpy():  downLeft.cpy();						
				pasX = horizontalVectDir.x / 10;
				
		
				for(int i = 0; i <= nbPoint; i++){
			
					Vector2 tmp = new Vector2( origin.cpy() );
					Vector2 vectfinal = new Vector2( 0,0);
					collision = false;
					for( parcourX = 0 ;( Math.abs(parcourX) < Math.abs(horizontalVectDir.x)  && collision == false);  ){
						tmp.add(pasX, 0);		
						parcourX += pasX;					
						
						if(  characters.stream().filter(e -> (e != curChar) && ( !e.isInvisible() ) ).anyMatch(e -> e.getHitbox().contains(tmp)) ) {
							parcourX -= pasX;
							pasX /= 2;
							if (pasX < 0.1) {
								vectfinal.add(parcourX, 0);
								collision = true;
								curChar.getVectDir().set( vectfinal.scl(1/delta).x ,curChar.getVectDir().y  );
							}
						}		
						
					}				
					origin.add(0,  spacingPoints );
				}
			}
		}
	}
	
	static public void checkAllVerticalCollisionCharacterCharacter( ArrayList<Character> characters ){
		int nbPoint = 4;
		float spacingPoints;
		double directionY;
		Vector2 verticalVectDir;
		float  parcourY, pasY;
		boolean collision ;
		Vector2 downLeft ;
		Vector2 upLeft ;
		boolean go = true;
	
		float delta;
		for(Character curChar: characters){
			
			go = true;
			
			if (curChar instanceof IA ){
				IA tmp = (IA) curChar;
				if (tmp.getIAType() == IAType.Pinguin)
					go = false;
			}
			if( (!curChar.isInvisible()) && go  ){
				collision = false;
				downLeft = new Vector2(curChar.getHitbox().x + 8 , curChar.getHitbox().y);
				upLeft = new Vector2(curChar.getHitbox().x + 8, curChar.getHitbox().y + curChar.getHitbox().height);
				
		//		Vector2 downRight = new Vector2(perso.getHitbox().x + perso.getHitbox().width, perso.getHitbox().y);
		//		Vector2 upRight = new Vector2(perso.getHitbox().x + perso.getHitbox().width, perso.getHitbox().y + perso.getHitbox().height);
				Vector2 origin;
				delta = Gdx.graphics.getDeltaTime();
				
				spacingPoints = (curChar.getHitbox().width-16) / nbPoint;
				
				/*ATTENTION !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
				verticalVectDir = new Vector2(0, curChar.getVectDir().y).scl(delta);
						
				directionY = Math.signum(verticalVectDir.y);			
		//		System.out.println( vectDir.y);			
				origin = (directionY > 0)? upLeft.cpy():  downLeft.cpy();	
				
				pasY = verticalVectDir.y / 10;
				
				for(int i = 0; i <= nbPoint; i++){
			
					Vector2 tmp = new Vector2( origin.cpy() );
					Vector2 vectmp = new Vector2( 0,0);
					
					for( parcourY = 0 ;( Math.abs(parcourY) < Math.abs(verticalVectDir.y)  && collision == false);  ){
						tmp.add(0, pasY);		
						parcourY += pasY;					
						
						if( characters.stream().filter(e -> e != curChar && ( !e.isInvisible() )).anyMatch(e -> e.getHitbox().contains(tmp)) ) {
							parcourY -= pasY;
							pasY /= 2;
							if (pasY < 0.1) {
								vectmp.add(0, parcourY );
								collision = true;
								
								curChar.getVectDir().set( curChar.getVectDir().x,  vectmp.scl(1/delta).y);
							}
						}		
					}				
					
					
					origin.add(  spacingPoints,0 );
				}
			}
		}
	}	

	
}
