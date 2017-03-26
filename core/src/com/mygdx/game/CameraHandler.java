package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHandler {
	
	private OrthographicCamera camera;
	
	private float effectiveViewportWidth;
	
	private float effectiveViewportHeight;
	
	private int xCamRightEdge;
	private int xCamLeftEdge;
	private int yCamTopEdge;
	private int yCamBottomEdge;
	
	public int getXCamRightEdge(){
		return xCamRightEdge;
	}
	
	public int getXCamLeftEdge(){
		return xCamLeftEdge;
	}
	
	public int getYCamTopEdge(){
		return yCamTopEdge;
	}
	
	public int getYCamBottomEdge(){
		return yCamBottomEdge;
	}
	
	public float getEffectiveViewPortHeight(){
		return effectiveViewportHeight;
	}
	
	public float getEffectiveViewPortWidth(){
		return effectiveViewportWidth;
	}
	
	
	public CameraHandler(OrthographicCamera cameraOrigin) {
		this.camera= cameraOrigin;
		this.xCamLeftEdge = 0;
		this.xCamRightEdge = 0;
		this.yCamTopEdge = 0;
		this.yCamBottomEdge = 0;
		this.effectiveViewportHeight = 0;
		this.effectiveViewportWidth = 0;
	}
	
	public void inputCamera(){
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-100, 0, 0);
	    }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	camera.translate(100, 0, 0);
	    }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -100, 0);
	    }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
    		camera.translate(0, 100, 0);
	    }
        if (Gdx.input.isKeyPressed(Input.Keys.T)) {
            camera.zoom += 0.2;
	    }
        if (Gdx.input.isKeyPressed(Input.Keys.Y)) {
    		camera.zoom -= 0.2;
		}
		
	}
	
	public void cameraGestion( Map map, Character currentPlayer, ShapeRenderer shapeRenderer, SpriteBatch batch){
		float pixelWidthMap = map.getWidthTile() * map.getWidthMap();
		float pixelHeightMap = map.getHeightTile() * map.getHeightMap();

		camera.zoom = MathUtils.clamp(camera.zoom, 1.3f, Math.min( pixelWidthMap/ camera.viewportWidth ,  pixelHeightMap/ camera.viewportHeight)  );
		this.effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		this.effectiveViewportHeight = camera.viewportHeight * camera.zoom;
		
		float diviseur = 10;
		
		if(currentPlayer.getX() > (camera.position.x + effectiveViewportWidth/diviseur)){
			camera.position.x = currentPlayer.getX() - (effectiveViewportWidth/diviseur) ;
		}
		else if(currentPlayer.getX() < (camera.position.x - effectiveViewportWidth/diviseur)){
			camera.position.x = currentPlayer.getX() + (effectiveViewportWidth/diviseur) ;

		}
		
		camera.position.x = MathUtils.clamp(  camera.position.x , effectiveViewportWidth / 2f, (pixelWidthMap) - ( effectiveViewportWidth / 2f) );
		camera.position.y = MathUtils.clamp( currentPlayer.getY() + currentPlayer.getHitbox().height, effectiveViewportHeight / 2f, pixelHeightMap - ( effectiveViewportHeight / 2f));		 
		 
		this.xCamRightEdge =  (int) ( ( (camera.position.x) + (effectiveViewportWidth/2)  ) / map.getWidthTile());
		this.xCamLeftEdge =(int) ( ( (camera.position.x) - (effectiveViewportWidth/2)  ) / map.getWidthTile());
		this.yCamBottomEdge=(int)  ( ( (camera.position.y) - (effectiveViewportHeight /2)    ) / map.getHeightTile());
		this.yCamTopEdge=(int)  ( ( (camera.position.y) + (effectiveViewportHeight /2)    ) / map.getHeightTile());

		camera.update();
		shapeRenderer.setProjectionMatrix( camera.combined );
		batch.setProjectionMatrix( camera.combined );
	}
	
	public void cameraGestionCombatMode( Map map, Character player1, Character player2 , ShapeRenderer shapeRenderer, SpriteBatch batch){
		float pixelWidthMap = map.getWidthTile() * map.getWidthMap();
		float pixelHeightMap = map.getHeightTile() * map.getHeightMap();

		Vector2 millieu = new Vector2( Math.abs(player1.getX() + player2.getX()) /2 ,Math.abs(  ( player1.getY()+(player1.getHitbox().height/2) + player2.getY() +(player2.getHitbox().height/2)   )    /2    ) ) ;
		float distancex_cam_millieu = Math.abs( (camera.position.x) -(millieu.x)   );
		float distancey_cam_millieu = Math.abs( (camera.position.y) -(millieu.y)   );
		
		float distanceXPlayer = Math.abs(player1.getX() - player2.getX()) ;
		float distanceYPlayer = Math.abs(player1.getY() - player2.getY()) ;
		
		float x1=player1.getPosition().x;
		float y1 = player1.getPosition().y;
		
		float x2=player2.getPosition().x;
		float y2 = player2.getPosition().y;
		
		float distance= (float) Math.sqrt(Math.pow( (x2 - x1), 2) + Math.pow( (y2 - y1), 2));
		
		
		camera.zoom -= 0.03;

		camera.zoom = MathUtils.clamp(camera.zoom, Math.min(Math.min( pixelWidthMap/ camera.viewportWidth ,  pixelHeightMap/ camera.viewportHeight), Math.max( Math.max ((distanceXPlayer +800)/ camera.viewportWidth, (distanceYPlayer +500)/ camera.viewportHeight), 2f)),  4.7f  );
//		System.out.println("zoom de : " + camera.zoom);

		this.effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		this.effectiveViewportHeight = camera.viewportHeight * camera.zoom;
		
		

		float vitesseTx= distancex_cam_millieu/2;
		float vitesseTy= distancey_cam_millieu/2;
		
//		camera.position.set(millieu, 0);
		if( camera.position.x < millieu.x ){
			camera.translate(vitesseTx ,0 );
		}
		
		else if( camera.position.x > millieu.x ){
			camera.translate(-vitesseTx, 0);
		}
		
		if( camera.position.y < millieu.y ){
			camera.translate(0, vitesseTy);
		}
		
		else if( camera.position.y > millieu.y ){
			camera.translate(0, -vitesseTy);
		}
//		System.out.println("distance de : " +(int) distanceXPlayer  );
		
		camera.position.x = MathUtils.clamp(  camera.position.x , effectiveViewportWidth / 2f, (pixelWidthMap) - ( effectiveViewportWidth / 2f) );
		camera.position.y = MathUtils.clamp( camera.position.y , effectiveViewportHeight / 2f, pixelHeightMap - ( effectiveViewportHeight / 2f));		 
		 
		this.xCamRightEdge =  (int) ( ( (camera.position.x) + (effectiveViewportWidth/2)  ) / map.getWidthTile());
		this.xCamLeftEdge =(int) ( ( (camera.position.x) - (effectiveViewportWidth/2)  ) / map.getWidthTile());
		this.yCamBottomEdge=(int)  ( ( (camera.position.y) - (effectiveViewportHeight /2)    ) / map.getHeightTile());
		this.yCamTopEdge=(int)  ( ( (camera.position.y) + (effectiveViewportHeight /2)    ) / map.getHeightTile());

		camera.update();
		shapeRenderer.setProjectionMatrix( camera.combined );
		batch.setProjectionMatrix( camera.combined );
	}
	
		
	
}
