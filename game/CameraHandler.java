package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class CameraHandler {

public void inputCamera(OrthographicCamera camera){
		
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
		 
		 
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            camera.rotate( - 1.5f, 0, 0, 1);
	    }
        if (Gdx.input.isKeyPressed(Input.Keys.N)) {
        	camera.rotate(  1.5f, 0, 0, 1);
		}
		 
		 
		
	}
	
	public void cameraGestion(OrthographicCamera camera, Map map, ArrayList<Character> characters, ShapeRenderer shapeRenderer, SpriteBatch batch){
		float pixelWidthMap = map.getWidthTile() * map.getWidthMap();
		float pixelHeightMap = map.getHeightTile() * map.getHeightMap();

		camera.zoom = MathUtils.clamp(camera.zoom, 1.3f, Math.min( pixelWidthMap/ camera.viewportWidth ,  pixelHeightMap/ camera.viewportHeight)  );
		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
		
		camera.position.x = MathUtils.clamp(  characters.get(1).getX() + (250) , effectiveViewportWidth / 2f, (pixelWidthMap) - ( effectiveViewportWidth / 2f) );
		camera.position.y = MathUtils.clamp( characters.get(1).getY() , effectiveViewportHeight / 2f, pixelHeightMap - ( effectiveViewportHeight / 2f));		 
		 
		camera.update();
		shapeRenderer.setProjectionMatrix( camera.combined );
		batch.setProjectionMatrix( camera.combined );
	}
		
	
}
