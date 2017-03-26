package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class Map {
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	
	private OrthographicCamera camera;

	
	protected int[][] tile;
	
	protected int[][] backgroundTab;
	
	protected final int widthTile = 128;
	protected final int heightTile = 128;
	
	private TextureRegion[][] tileSet;
	private ArrayList<Texture> background;
	
	private int widthBackground;
	private int heightBackground;
	
	private int widthMap;
	private final int heightMap = 80;
	
	private boolean generationNotFinished ;
	
	
	private int generatedSegment;
	
	private int xSegmentStart;
	private int widthSegment;	
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
		
		generationNotFinished = true;

		if(! isValidWidthMap(widthMap))
			throw new IllegalArgumentException("Largeur de map trop petite");
		this.widthMap = widthMap;
		
//		if(! isValidHeightMap(heightMap))
//			throw new IllegalArgumentException("Hauteur de map trop petite");
//		this.heightMap = heightMap;
		
//		this.widthSegment =10;
		this.widthSegment =(int) (cameraOrigin.viewportWidth / widthTile);
		
		
		tile = new int[this.widthMap][this.heightMap];
		widthBackground = (this.widthMap/16) + 1; 
		heightBackground = (this.widthMap/8) + 1;
		
		backgroundTab = new int[ widthBackground ][ heightBackground];
		
//		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/cols, sheet.getHeight()/rows);
		
		tileSet = TextureRegion.split(new Texture( Gdx.files.internal("Tiles/TilesetGrass2.png")), 128, 128);
		
		background = new ArrayList<>();
		
		background.add( new Texture(Gdx.files.internal("Background/Background_StarryNightI.png")) );
		background.add( new Texture(Gdx.files.internal("Background/Background_StarryNightII.png")) );
		background.add( new Texture(Gdx.files.internal("Background/Background_StarryNightIII.png")) );
		
		heightDifferenceMax = getHighDifferenceMax(weightPlayer, nbJumpMax);
//		System.out.println("On a en height max :" + heightDifferenceMax);
		this.xSegmentStart = 1;
		this.altitudeSegment = 10;
		this.widthSegment = 10;
	}
	
	

	
	
	public void displayDebugMap(CameraHandler cameraHandler){
		
		int x1 = cameraHandler.getXCamLeftEdge();
		int x2 = cameraHandler.getXCamRightEdge();
		
		int y1 = cameraHandler.getYCamBottomEdge();
		int y2 = cameraHandler.getYCamTopEdge();
		
		shapeRenderer.begin(ShapeType.Filled);
	
		for (int i =x1 ; i < x2 + 1; i++) {
			if (i < widthMap) {
				for (int j = y1; j < y2 + 1; j++) {
					if (j < heightMap) {
						if (tile[i][j] <= 0){
							shapeRenderer.setColor(1, 1, 1, 1);
						}
						else{
							shapeRenderer.setColor(.35f, .2f, 0, 1);
						}
						shapeRenderer.rect(i * widthTile, j * heightTile, widthTile, heightTile);
					}
				}
			}
		}
		
		shapeRenderer.end();
	}
	
	public void displayConsoleMap(CameraHandler cameraHandler) {
		
		int x1 = cameraHandler.getXCamLeftEdge();
		int x2 = cameraHandler.getXCamRightEdge();
		
		int y1 = cameraHandler.getYCamBottomEdge();
		int y2 = cameraHandler.getYCamTopEdge();
    	
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
	
	public void displayWhiteBackground(CameraHandler cameraHandler) {
		
		int x1 = cameraHandler.getXCamLeftEdge();
		int x2 = cameraHandler.getXCamRightEdge();
		
		int y1 = cameraHandler.getYCamBottomEdge();
		int y2 = cameraHandler.getYCamTopEdge();
    	

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
	
	public void displayGraphicMap(CameraHandler cameraHandler) {
		
		int x1 = cameraHandler.getXCamLeftEdge();
		
		int x2 = cameraHandler.getXCamRightEdge();
		
		int y1 = cameraHandler.getYCamBottomEdge();
		int y2 = cameraHandler.getYCamTopEdge();    	
    	
//		System.out.println("xstart: " + x1 + " xEnd: " + x2);
		
//		System.out.println("ystart: " + y1 + " yEnd: " + y2);

    	batch.begin();
		for (int i =x1 ; i <= x2; i++) {
			if (i < widthMap) {
				for (int j = y1; j <= y2; j++) {
					if (j < heightMap) {
						
//						if(i %16 == 0 && j % 8 == 0 ){
//							batch.draw(background, i * widthTile, j*heightTile, 2048, 1024) ;
//						}

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
	
//	public void displayGraphicBackground(CameraHandler cameraHandler) {
//		
//		int x1 = cameraHandler.getXCamLeftEdge();
//		x1 -= (x1 % 16);
//
//		int x2 = cameraHandler.getXCamRightEdge();
//		
//		int y1 = cameraHandler.getYCamBottomEdge();
//		y1 -= (y1 % 8);
//
//		int y2 = cameraHandler.getYCamTopEdge();    	
//    	
////		System.out.println("xstart: " + x1 + " xEnd: " + x2);
////		System.out.println("ystart: " + y1 + " yEnd: " + y2);
//
//    	batch.begin();
//		for (int i =x1 ; i <= x2; i+= 16) {
//			if (i < widthMap) {
//				for (int j = y1; j <= y2; j+=8) {
//					if (j < heightMap) {
//							batch.draw(background.get(/* ((int) (Math.pow(i%j, 3)) )*/ (i+j) % 3     ) , i * widthTile, j*heightTile, 2048, 1024)  ;
//					}
//				}
//			}
//		}
//		batch.end();
//	}


	public void displayGraphicBackground(CameraHandler cameraHandler) {
		
		int x1 = cameraHandler.getXCamLeftEdge() / 16;
//		x1 -= (x1 % 16);
	
		int x2 = cameraHandler.getXCamRightEdge() / 16;
		
		int y1 = cameraHandler.getYCamBottomEdge() / 8;
//		y1 -= (y1 % 8);
	
		int y2 = cameraHandler.getYCamTopEdge() / 8;    	
		
	//	System.out.println("xstart: " + x1 + " xEnd: " + x2);
	//	System.out.println("ystart: " + y1 + " yEnd: " + y2);
	
		batch.begin();
		for (int i =x1 ; i <= x2; i++) {
			if (i < widthMap) {
				for (int j = y1; j <= y2; j++) {
					if (j < heightMap) {
							batch.draw(background.get(		backgroundTab[i][j]		) , i * 2048, j* 1024, 2048, 1024)  ;
					}
//					System.out.println(backgroundTab[i][j]);
					
				}
			}
		}
		batch.end();
	}

	public void displayGrid(CameraHandler cameraHandler) {
		
		int x1 = cameraHandler.getXCamLeftEdge();
		int x2 = cameraHandler.getXCamRightEdge();
		
		int y1 = cameraHandler.getYCamBottomEdge();
		int y2 = cameraHandler.getYCamTopEdge();
		
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

	public void displayBackgroundGrid(CameraHandler cameraHandler) {
		
		int x1 = cameraHandler.getXCamLeftEdge();
		x1 -= (x1 % 16);

		int x2 = cameraHandler.getXCamRightEdge();
		
		int y1 = cameraHandler.getYCamBottomEdge();
		y1 -= (y1 % 8);

		int y2 = cameraHandler.getYCamTopEdge();    	
    	
//		System.out.println("xstart: " + x1 + " xEnd: " + x2);
//		System.out.println("ystart: " + y1 + " yEnd: " + y2);

    	shapeRenderer.begin(ShapeType.Line);
    	shapeRenderer.setColor(Color.RED);
		for (int i =x1 ; i <= x2; i+= 16) {
			if (i < widthMap) {
				for (int j = y1; j <= y2; j+=8) {
					if (j < heightMap) {
						
						if(i %16 == 0 && j % 8 == 0 ){
							
							shapeRenderer.rect(i * widthTile, j * heightTile, widthTile * 16, heightTile * 8);
						}

					}
				}
			}
		}
		shapeRenderer.end();
	}
	
	public void generateBackground(Random randGen){
		for(int i = 0; i < widthBackground ; i++){
			for (int j = 0; j < heightBackground; j++){
				
				backgroundTab[i][j] = randGen.nextInt(3);
				System.out.println(randGen.nextInt(3));
			}
		}
	}
	
	public void generateBorders(){
		
		for(int i = 0; i < widthMap ; i++){
			for (int j = 0; j < heightMap; j++){
				if ( i == 0 || i == widthMap - 1 || j == 0 || j == heightMap - 1 )
					tile[i][j] = ((i+j)%3 == 0)? BOTTOM_C2: BOTTOM_C1;
				else
					tile[i][j] = 0;
			}
		}
		
	}
	
	public void closeSegment(int xClose, int upY, int downY){
		
		tile[xClose][upY] = FRONT_R;
		tile[xClose][ upY - 1] = TOP_R;
		
		for (int j = upY - 2; j >= downY; j-- ){
			tile[xClose][j] = (j%3 ==0)? BOTTOM_R1:BOTTOM_R2;;
		}
		
			
			
		
				
	}
	public void generateArena1(){
		int arenaAltitude = 5;
		
		int part1 = 18;
		int altitudePart2= 10;
		int part2 = 40;
		
		generateBorders();
		
		fillSegment(1, part1, arenaAltitude );
		
		fillSegment( part1 + 1 , part2, altitudePart2 );
		closeSegment(part2,altitudePart2, arenaAltitude );
		
		fillSegment( part2 + 1 , widthMap-2, arenaAltitude );
	
	}
	
	public void generateArena2(){
		int arenaAltitude = 20;
		
		int part1 = 18;
		int altitudePart2= 10;
		int part2 = 40;
		
		generateBorders();
		
		fillSegment(1, part1, arenaAltitude );
		closeSegment(part1,arenaAltitude, altitudePart2);

		fillSegment( part1 + 1 , part2, altitudePart2 );
		
		
		fillSegment( part2 + 1 , widthMap-2, arenaAltitude );
	
	}
	public void generateArena0(){
		int arenaAltitude = 30;
		
		
		
		generateBorders();
		
		fillSegment(1, widthMap-2, arenaAltitude );
	
	}
	
	public void generateMap(Random randGen) {

		generateBorders();
		
		fillRandomMap(1, randGen);
		
		
		xSegmentStart = 1; //Force l'appel � update_Map
		generatedSegment ++;
			
	}
	
	public void updateMap(Random randGen){
			xSegmentStart += (widthSegment);			
			fillRandomMap(xSegmentStart, randGen);
			generatedSegment ++;
			System.out.println("On a g�n�r�: " + generatedSegment + " segment");

	}
	
	public void generationRestart(){
		this.xSegmentStart = 1;
		this.altitudeSegment = 10;
		this.widthSegment = 10;
		this.generationNotFinished = true;
	}
	
	public boolean isGenerationNotFinished(){
		return generationNotFinished;
	}

	public void fillRandomMap(int xStart, Random randGen) {
		int nextAltitude;
		int nextWidth;
		int xEnd;
		int localStart;
		
		nextWidth =  randGen.nextInt (50) + 20;
		this.widthSegment = nextWidth;
		
		/*Si on arrive � la fin du niveau*/
		if( xSegmentStart + widthSegment >= widthMap){
			System.out.println("width = " + widthSegment + " est trop grand ");

			widthSegment = (widthMap-1) - xSegmentStart;
			System.out.println("on ajuste �  width = " + widthSegment);
			generationNotFinished = false;
		}
		do{
			nextAltitude = randGen.nextInt (heightMap - 8)  + 5;
		}while ( nextAltitude - this.altitudeSegment > heightDifferenceMax  );
		
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
		
		System.out.println( this.altitudeSegment = nextAltitude);
		
		fillSegment( localStart , xEnd, this.altitudeSegment);
		
//		for (int i = localStart ; i <= xEnd; i++) {
//			for (int j = 1; j <= this.altitudeSegment; j++) {
//				
//				/*DERNIER*/
//				if( j == this.altitudeSegment ){
//					if (tile[i - 1][j] == 0 && i == localStart){
//						tile[i][j] = FRONT_L;
//					}
//				
//					else
//						tile[i][j] = FRONT_C;
//				}
//				
//				
//				/*AVANT DERNIER*/
//				else if(j == this.altitudeSegment - 1){
//					if (tile[i - 1][j] == 0 && i == localStart){
//						tile[i][j] = TOP_L;
//					}
//					else
//						tile[i][j] = TOP_C;
//				}
//				
//				
//				/*LE RESTE*/
//				else{
//					
//					/*On a du solide � gauche*/
//					if (tile[i - 1][j] >0 )
//						tile[i][j] = ((i+j)%2 ==0 )?BOTTOM_C2:BOTTOM_C1;
//					
//					/*On a du traversable � gauche*/
//					else
//						tile[i][j] = (j%3 ==0)?BOTTOM_L2:BOTTOM_L1;
//					
//				}
//			}
//		}
		
	}
	
	public void fillSegment(int xStart, int xEnd, int altitude){
		for (int i = xStart ; i <= xEnd; i++) {
			for (int j = 1; j <= altitude; j++) {
				
				/*DERNIER*/
				if( j == altitude ){
					if (tile[i - 1][j] == 0 && i == xStart){
						tile[i][j] = FRONT_L;
					}
				
					else
						tile[i][j] = FRONT_C;
				}
				
				
				/*AVANT DERNIER*/
				else if(j == altitude - 1){
					if (tile[i - 1][j] == 0 && i == xStart){
						tile[i][j] = TOP_L;
					}
					else
						tile[i][j] = TOP_C;
				}
				
				
				/*LE RESTE*/
				else{
					
					/*On a du solide � gauche*/
					if (tile[i - 1][j] >0 )
						tile[i][j] = ((i+j)%2 ==0 )?BOTTOM_C2:BOTTOM_C1;
					
					/*On a du traversable � gauche*/
					else
						tile[i][j] = (j%3 ==0)?BOTTOM_L2:BOTTOM_L1;
					
				}
			}
		}
	}

	public void smoothMap(int xStart, int xEnd) {
		
		int surroundingWallCount = 0;
		for (int x = xStart ; x < xEnd ; x++) {
			for (int y = 1; y < heightMap - 1; y++) {

				surroundingWallCount = getSurroundigWallCount(x, y);

				if (surroundingWallCount < 4) {
					tile[x][y] = 0;

				} else if (surroundingWallCount > 4)
					tile[x][y] = 1;
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
	
	public int getHighDifferenceMax(float weightPlayer, int nbJumpMax){

		return (int) (( (12 / weightPlayer) - 1 ) * nbJumpMax);
	}
	
	public int getXSegmentStart(){
		return xSegmentStart;
	}
	
	public int getWidthSegment(){
		return widthSegment;
	}
	
	public int getAltitudeSegment(){
		return altitudeSegment;
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


