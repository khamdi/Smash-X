package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Collisions {
	
	
	static public void checkVerticalCollision(ArrayList<Character> characters, Map map){
		int nbPoint = 4;
		float spacingPoints;
		double directionY;
		Vector2 verticalVectDir;
		float  parcourY, pasY;
		boolean collision ;
		Vector2 downLeft ;
		Vector2 upLeft ;
	
		float delta;
		for(Character perso: characters){
			collision = false;
			downLeft = new Vector2(perso.getHitbox().x, perso.getHitbox().y);
			upLeft = new Vector2(perso.getHitbox().x, perso.getHitbox().y + perso.getHitbox().height);
			
	//		Vector2 downRight = new Vector2(perso.getHitbox().x + perso.getHitbox().width, perso.getHitbox().y);
	//		Vector2 upRight = new Vector2(perso.getHitbox().x + perso.getHitbox().width, perso.getHitbox().y + perso.getHitbox().height);
			Vector2 origin;
			delta = Gdx.graphics.getDeltaTime();
			spacingPoints = perso.getHitbox().width / nbPoint;
			verticalVectDir = new Vector2(0, perso.getVectDir().y).scl(delta);
					
			directionY = Math.signum(verticalVectDir.y);			
	//		System.out.println( perso.getVectDir().y);			
			origin = (directionY > 0)? upLeft.cpy():  downLeft.cpy();						
			pasY = verticalVectDir.y / 10000;
			
			for(int i = 0; i <= nbPoint; i++){
		
				Vector2 tmp = new Vector2( origin.cpy() );
				Vector2 vectmp = new Vector2( 0,0);
				collision = false;
				for( parcourY = 0 ;( Math.abs(parcourY) < Math.abs(verticalVectDir.y)  && collision == false);  ){
					tmp.add(0, pasY);		
					parcourY += pasY;					
					Vector2 ttmp = map.pointAdressInMap( tmp );		
					
					if( map.isSolidTileIndex ((int)ttmp.x, (int)ttmp.y)) {
						parcourY -= pasY;
						pasY /= 2;
						if (pasY < 0.1) {
							vectmp.add(0, parcourY );
							collision = true;
							perso.getVectDir().set( perso.getVectDir().x,  vectmp.scl(1/delta).y);
							if(directionY < 0 && perso.isOnAir()){
								perso.setStun(0.1f);
								perso.resetJump();
								perso.setOnFloor();
							}
						}
					}		
				}				
				origin.add(  spacingPoints,0 );
			}
		}
	}	

	static public void checkHorizontalCollision( ArrayList<Character> characters, Map map ){
		int nbPoint = 4;
		float spacingPoints;
		double directionX;
		Vector2 horizontalVectDir;
		float  parcourX, pasX;
		boolean collision ;
		Vector2 downLeft ;
		Vector2 downRight ;
	
		float delta;
		for(Character perso: characters){
			collision = false;
			downLeft = new Vector2(perso.getHitbox().x, perso.getHitbox().y + 1);
			downRight = new Vector2(perso.getHitbox().x + perso.getHitbox().width, perso.getHitbox().y + 1);
			
			Vector2 origin;
			delta = Gdx.graphics.getDeltaTime();
			spacingPoints = perso.getHitbox().width / nbPoint;
			horizontalVectDir = new Vector2( perso.getVectDir().x, 0).scl(   delta   );
					
			directionX = Math.signum(horizontalVectDir.x);			
	//		System.out.println( perso.getVectDir().x);			
			origin = (directionX > 0)? downRight.cpy():  downLeft.cpy();						
			pasX = horizontalVectDir.x / 10000;
			
	
			for(int i = 0; i <= nbPoint; i++){
		
				Vector2 tmp = new Vector2( origin.cpy() );
				Vector2 vectfinal = new Vector2( 0,0);
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
							perso.getVectDir().set( vectfinal.scl(1/delta).x ,perso.getVectDir().y  );
						}
					}		
					
				}				
				origin.add(0,  spacingPoints );
			}
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
				map.setTileMapToState(address, 0);
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
	
	

	
}
