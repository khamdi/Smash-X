package com.mygdx.game;

import java.util.Date;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

public class Map {
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	
	private OrthographicCamera camera;

	
	protected int[][] tile;
	protected final int widthTile = 128;
	protected final int heightTile = 128;
	
	private TextureRegion[][] tileSet;
	
	private int widthMap;
	private final int heightMap = 80;
	
	private Random randGen;
	private boolean randomSeed = false;
	
	private int xSegmentStart;
	
	private int generatedSegment;
	
	private int widthSegment;	// 
	private int altitudeSegment;
	
	private int heightDifferenceMax;
	
	protected static final int BOTTOM_L2 = 1;
	protected static final int BOTTOM_C2 = 2;
	protected static final int BOTTOM_R2= 3;
	
	protected static final int BOTTOM_L1 = 4;
	protected static final int BOTTOM_C1 = 5;
	protected static final int BOTTOM_R1 = 6;
	
	protected static final int TOP_L= 7;
	protected static final int TOP_C = 8;
	protected static final int TOP_R = 9;
	
	protected static final int FRONT_L= (-1);
	protected static final int FRONT_C = (-2);
	protected static final int FRONT_R = (-3);
	
	public Map(ShapeRenderer shapeOrigin, SpriteBatch batchOrigin, OrthographicCamera cameraOrigin, int widthMap, float weightPlayer, int nbJumpMax) {
		shapeRenderer = shapeOrigin;
		camera = cameraOrigin;
		batch = batchOrigin;
		
		randGen = new java.util.Random(randomSeed ? (new Date()).getTime() : 1);

		if(! isValidWidthMap(widthMap))
			throw new IllegalArgumentException("Largeur de map trop petite");
		this.widthMap = widthMap;
		
//		if(! isValidHeightMap(heightMap))
//			throw new IllegalArgumentException("Hauteur de map trop petite");
//		this.heightMap = heightMap;
		
//		this.widthSegment =10;
		this.widthSegment =(int) (cameraOrigin.viewportWidth / widthTile);
		
		
		tile = new int[this.widthMap][this.heightMap];
		
//		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/cols, sheet.getHeight()/rows);
		
		tileSet = TextureRegion.split(new Texture( Gdx.files.internal("TilesetSnow2.png")), 128, 128);
		
		heightDifferenceMax = getHighDifferenceMax(weightPlayer, nbJumpMax);
		System.out.println("On a en height max :" + heightDifferenceMax);
	}
	
	public int getHighDifferenceMax(float weightPlayer, int nbJumpMax){

		return (int) ( (12 / weightPlayer) - 1 ) * nbJumpMax;
	}
	

	public void displayConsoleMap(OrthographicCamera camera) {
		float actualViewportWidth = camera.viewportWidth * (camera.zoom);
		float actualViewportHeight = camera.viewportHeight * (camera.zoom);
		
    	int x1 = (int) ( ( (camera.position.x) - (actualViewportWidth /2)  ) / widthTile);
    	int x2 = (int) ( ( (camera.position.x) + (actualViewportWidth /2)  ) / widthTile);
    	
    	int y1 = (int) ( ( (camera.position.y) - (actualViewportHeight /2)    ) / heightTile);
    	int y2 = (int) ( ( (camera.position.y) + (actualViewportHeight /2)    ) / heightTile);
    	
		System.out.println(x1 + "  " + x2);
		
		for (int i =x1 ; i < x2 + 1; i++) {
			if (i < widthMap) {
				for (int j = y1; j < y2 + 1; j++) {
					if (j < heightMap) {
						System.out.print((tile[i][j] == 1) ? "1" : "0");
					}
				}
				System.out.println("");
			}
		}
		
	}

	
	public void displayWhiteBackground(OrthographicCamera camera) {
		float actualViewportWidth = camera.viewportWidth * (camera.zoom);
		float actualViewportHeight = camera.viewportHeight * (camera.zoom);
		
    	int x1 = (int) ( ( (camera.position.x) - (actualViewportWidth /2)  ) / widthTile);
    	int x2 = (int) ( ( (camera.position.x) + (actualViewportWidth /2)  ) / widthTile);
    	
    	int y1 = (int) ( ( (camera.position.y) - (actualViewportHeight /2)    ) / heightTile);
    	int y2 = (int) ( ( (camera.position.y) + (actualViewportHeight /2)    ) / heightTile);
    	

    	shapeRenderer.begin(ShapeType.Filled);
    	shapeRenderer.setColor(1, 1, 1, 1);

    	for (int i =x1 ; i <= x2; i++) {
			if (i < widthMap) {
				for (int j = y1; j <= y2; j++) {
					if (j < heightMap) {
						shapeRenderer.rect(i * widthTile, j * heightTile, widthTile, heightTile);

					}
				}
			}
    	}
		shapeRenderer.end();

		
	}
	
	
	public void displayGraphicMap(OrthographicCamera camera) {
		float actualViewportWidth = camera.viewportWidth * (camera.zoom);
		float actualViewportHeight = camera.viewportHeight * (camera.zoom);
		
    	int x1 = (int) ( ( (camera.position.x) - (actualViewportWidth /2)  ) / widthTile);
    	int x2 = (int) ( ( (camera.position.x) + (actualViewportWidth /2)  ) / widthTile);
    	
    	int y1 = (int) ( ( (camera.position.y) - (actualViewportHeight /2)    ) / heightTile);
    	int y2 = (int) ( ( (camera.position.y) + (actualViewportHeight /2)    ) / heightTile);
    	
//		shapeRenderer.begin(ShapeType.Filled);
//
//		for (int i =x1 ; i < x2 + 1; i++) {
//			if (i < widthMap) {
//				for (int j = y1; j < y2 + 1; j++) {
//					if (j < heightMap) {
//						if (tile[i][j] == 0){
//							shapeRenderer.setColor(1, 1, 1, 1);
//						}
//						else{
//							shapeRenderer.setColor(.35f, .2f, 0, 1);
//						}
//						shapeRenderer.rect(i * widthTile, j * heightTile, widthTile, heightTile);
//					}
//				}
//			}
//		}
//			
//		shapeRenderer.end();

    	
    	batch.begin();
		for (int i =x1 ; i <= x2; i++) {
			if (i < widthMap) {
				for (int j = y1; j <= y2; j++) {
					if (j < heightMap) {
						
						switch(tile[i][j]){
								
							case FRONT_L:
								batch.draw(tileSet[0][0], i * widthTile, j * heightTile);
								break;
								
							case FRONT_C:
								batch.draw(tileSet[0][1], i * widthTile, j * heightTile);
								break;
								
							case FRONT_R:
								batch.draw(tileSet[0][2], i * widthTile, j * heightTile);
								break;
							
							case TOP_L:
								batch.draw(tileSet[1][0], i * widthTile, j * heightTile);
								break;
								
							case TOP_C:
								batch.draw(tileSet[1][1], i * widthTile, j * heightTile);
								break;
									
							case TOP_R:
								batch.draw(tileSet[1][2], i * widthTile, j * heightTile);
								break;
								
								
							case BOTTOM_L1:
								batch.draw(tileSet[2][0], i * widthTile, j * heightTile);
								break;
								
							case BOTTOM_C1:
								batch.draw(tileSet[2][1], i * widthTile, j * heightTile);
								break;
								
							case BOTTOM_R1:
								batch.draw(tileSet[2][2], i * widthTile, j * heightTile);
								break;
								
							
							case BOTTOM_L2:
								batch.draw(tileSet[3][0], i * widthTile, j * heightTile);
								break;
								
							case BOTTOM_C2:
								batch.draw(tileSet[3][1], i * widthTile, j * heightTile);
								break;
							case BOTTOM_R2:
								batch.draw(tileSet[3][2], i * widthTile, j * heightTile);
								break;
								
							

							
						}
						
					}
				}
			}
		}
			
    	
		batch.end();
		
		
		
	}
	
	public void displayGrid(OrthographicCamera camera) {
		float actualViewportWidth = camera.viewportWidth * (camera.zoom);
		float actualViewportHeight = camera.viewportHeight * (camera.zoom);
		
    	int x1 = (int) ( ( (camera.position.x) - (actualViewportWidth /2)  ) / widthTile);
    	int x2 = (int) ( ( (camera.position.x) + (actualViewportWidth /2)  ) / widthTile);
    	
    	int y1 = (int) ( ( (camera.position.y) - (actualViewportHeight /2)    ) / heightTile);
    	int y2 = (int) ( ( (camera.position.y) + (actualViewportHeight /2)    ) / heightTile);
    	
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0, 0, 0, 1);
		for (int i =x1 ; i < x2 + 1; i++) {
			if (i < widthMap) {
				for (int j = y1; j < y2 + 1; j++) {
					if (j < heightMap) {
						
						shapeRenderer.rect(i * widthTile, j * heightTile, widthTile, heightTile);
					}
				}
			}
		}
			
		shapeRenderer.end();
		
	}

	public void generateMap() {

		for(int i = 0; i < widthMap ; i++){
			for (int j = 0; j < heightMap; j++){
				if ( i == 0 || i == widthMap - 1 || j == 0 || j == heightMap - 1 )
					tile[i][j] = ((i+j)%3 == 0)? BOTTOM_C2: BOTTOM_C1;
				else
					tile[i][j] = 0;
			}
		}
		
		fillRandomMap(1);
		
		
		xSegmentStart = 1; //Force l'appel à update_Map
		generatedSegment ++;
			
	}

	public void fillRandomMap(int xStart) {
		int nextAltitude;
		int nextWidth;
		int xEnd;
		int localStart;
		
		nextWidth =  randGen.nextInt (50) + 20;
		this.widthSegment = nextWidth;
		
		/*Si on arrive à la fin du niveau*/
		if( xSegmentStart + widthSegment >= widthMap)
			widthSegment = (widthMap-1) - xSegmentStart;
		
		do{
			nextAltitude = randGen.nextInt (heightMap - 8)  + 5;
		}while ( (nextAltitude > altitudeSegment && nextAltitude - this.altitudeSegment > heightDifferenceMax)  );
		
		xEnd = xStart + (this.widthSegment-1);
		localStart = xStart ;
		
		
		/*On doit conclure la derniere colonne du segment precedent si le prochain segment est plus bas*/
		if( nextAltitude < this.altitudeSegment){
			tile[xStart][this.altitudeSegment] = FRONT_R;
			tile[xStart][this.altitudeSegment - 1] = TOP_R;
			for(int j = altitudeSegment -2; j >= nextAltitude ; j--){
				tile[xStart][j]  = (j%3 ==0)? BOTTOM_R1:BOTTOM_R2;
			}
			for(int j = nextAltitude - 1; j >= 1 ; j--){
				tile[xStart][j] = (j%3 ==0)? BOTTOM_C1:BOTTOM_C2;
			}
			localStart++;
		}
		
		this.altitudeSegment = nextAltitude;
		
		
		for (int i = localStart ; i <= xEnd; i++) {
			for (int j = 1; j <= this.altitudeSegment; j++) {
				
				/*DERNIER*/
				if( j == this.altitudeSegment ){
					if (tile[i - 1][j] == 0 && i == localStart){
						tile[i][j] = FRONT_L;
					}
				
					else
						tile[i][j] = FRONT_C;
				}
				
				
				/*AVANT DERNIER*/
				else if(j == this.altitudeSegment - 1){
					if (tile[i - 1][j] == 0 && i == localStart){
						tile[i][j] = TOP_L;
					}
					else
						tile[i][j] = TOP_C;
				}
				
				
				
				/*LE RESTE*/
				else{
					
					/*On a du solide à gauche*/
					if (tile[i - 1][j] >0 )
						tile[i][j] = ((i+j)%2 ==0 )?BOTTOM_C2:BOTTOM_C1;
					
					/*On a du traversable à gauche*/
					else
						tile[i][j] = (j%3 ==0)?BOTTOM_L2:BOTTOM_L1;
					
				}
			}
		}
		
	}
	
	public void updateMap(int xPerso){
		float actualViewportWidth = camera.viewportWidth * (camera.zoom);
		float xCamRightEdge = ( (camera.position.x) + (actualViewportWidth /2)  ) / widthTile;
		
		if( xCamRightEdge > xSegmentStart) {
			xSegmentStart += (widthSegment);
			
			fillRandomMap(xSegmentStart);
		
			
			generatedSegment ++;
			System.out.println(generatedSegment);
		}
		
	}
	

	public void smoothMap(int xStart, int xEnd) {
//		int surroundingWallCount = 0;
//		for (int x = 1; x < widthMap - 1; x++) {
//			for (int y = 1; y < heightMap - 1; y++) {
//
//				surroundingWallCount = getSurroundigWallCount(x, y);
//
//				if (surroundingWallCount < 4) {
//					tile[x][y] = 0;
//
//				} else if (surroundingWallCount > 4)é
//					tile[x][y] = 1;
//
//				// else
//				// tile[x][y] = 1;
//			}
//		}
		
		int surroundingWallCount = 0;
		for (int x = xStart ; x < xEnd ; x++) {
			for (int y = 1; y < heightMap - 1; y++) {

				surroundingWallCount = getSurroundigWallCount(x, y);

				if (surroundingWallCount < 4) {
					tile[x][y] = 0;

				} else if (surroundingWallCount > 4)
					tile[x][y] = 1;

				// else
				// tile[x][y] = 1;
			}
		}
	}

	public int getSurroundigWallCount(int xBlock, int yBlock) {
		int count = 0;

		for (int xNeighbour = xBlock - 1; xNeighbour <= xBlock + 1; xNeighbour++) {
			for (int yNeighbour = yBlock - 1; yNeighbour <= yBlock + 1; yNeighbour++) {

				if (xNeighbour >= 0 && xNeighbour <= widthMap - 1 && yNeighbour >= 0 && yNeighbour <= heightMap - 1)

					if (xNeighbour != xBlock || yNeighbour != yBlock)
						count += tile[xNeighbour][yNeighbour];

			}
		}
		return count;
	}
	
	public boolean isSolidTileIndex (int indexX, int indexY){
		return tile[indexX][indexY] > 0;
	}
	
	public boolean isSolidTileIndex (Vector2 point){
		return tile[(int)(point.x)][(int)(point.y)] > 0 ;
	}
	
	public boolean isSolidTile (Vector2 point){
		return tile[(int)(point.x/widthTile)][(int)(point.y/heightTile)] > 0;
	}

	public int getWidthMap(){
		return widthMap;
	}
	public int getHeightMap(){
		return heightMap;
	}
	
	public float getWidthTile(){
		return widthTile;
	}
	public float getHeightTile(){
		return heightTile;
	}
	
	public Vector2 pointAdressInMap(Vector2 point){
		Vector2 adress = new Vector2( (int)point.x/widthTile, (int) point.y/heightTile );
		return adress;
	}

	public boolean isValidWidthMap( int widthMap){
		return (widthMap * widthTile) > (camera.viewportWidth * camera.zoom) ;		
	}
	
	public boolean isValidHeightMap( int heightMap){
		return (heightMap * heightTile) > (camera.viewportHeight * camera.zoom) ;	
	}
	
	public boolean isValidPoint (Vector2 point) {
		Vector2 tmp = pointAdressInMap(point);
		return (tmp.x >= 0 && tmp.y >= 0 && tmp.x < widthMap && tmp.y < heightMap);
	}

	public boolean isValidPoint (int x, int y) {
		return ((x >= 0) && (y >= 0) && x < widthMap && y < heightMap);
	}
	
	public boolean isMutablePoint (Vector2 point) {
		Vector2 tmp = pointAdressInMap (point);
		return (tmp.x > 0 && tmp.y > 0 && tmp.x < widthMap - 1 && tmp.y < heightMap);
	}
	
	public void setTileMapToState (Vector2 point, int state) {
		if (isMutablePoint(point)) {
			tile[(int) point.x/widthTile][(int) point.y/heightTile] = state;
		}
	}
	
	public void setTileMapToState (int x, int y, int state) {
		if (isValidPoint(x, y))
			tile[x][y] = state;
	}
	
}


