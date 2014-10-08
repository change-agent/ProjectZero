package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.zero.ProjectZero;
import com.sun.corba.se.spi.ior.MakeImmutable;

public class GameRenderer {
	// declare world variables
	GameWorld gameWorld;
	GameInputProcessor gameInputProcessor;
	
	// Camera rendering
	OrthographicCamera camera;
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	private float gameWidth, gameHeight;
	
	// Map rendering
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	
	// ready state variables
	private float resumeCountDown;
	
	// paused menu variables
	SimpleButton btnResumeGame;
	SimpleButton btnOption;
	SimpleButton btnSaveExit;
	
	// main constructor
	public GameRenderer(GameWorld gameWorld, GameInputProcessor gameInputProcessor) {
		// get world
		this.gameWorld = gameWorld;
		
		// get controls
		this.gameInputProcessor = gameInputProcessor;
		
		// calculate ratio
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		gameWidth = ProjectZero.GAME_WIDTH;
		gameHeight = screenHeight / (screenWidth / gameWidth);
		
		// initialize variables
		camera = new OrthographicCamera();
		camera.setToOrtho(true, gameWidth / 4, gameHeight / 4);
		camera.update();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		// Load the map and set up the obstacles
		tiledMap = new TmxMapLoader().load("maps/test.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		
		for(MapObject object : tiledMap.getLayers().get("Obstacles").getObjects()) {
			if(object instanceof RectangleMapObject) {
				float x = ((RectangleMapObject) object).getRectangle().x;
				float y = ((RectangleMapObject) object).getRectangle().y;
				float width = ((RectangleMapObject) object).getRectangle().width;
				float height = ((RectangleMapObject) object).getRectangle().height;
				gameWorld.staticObjects.add(new Obstacle(gameWorld.worldBox2D, x, y, width * 10, height * 10, 0));
			}
			else if(object instanceof EllipseMapObject){
				float x = ((EllipseMapObject) object).getEllipse().x;
				float y = ((EllipseMapObject) object).getEllipse().y;
				float width = ((EllipseMapObject) object).getEllipse().width;
				float height = ((EllipseMapObject) object).getEllipse().height;
				gameWorld.staticObjects.add(new PowerUpContainer(gameWorld.worldBox2D, x, y, width * 10, height * 10, 0));
			}
		}
		
		// initialise ready state variables
		resumeCountDown = 0f;
		
		// initialise paused menu variables
		int btnWidth = 120; // assign for main buttons
		btnResumeGame = new SimpleButton((int)(gameWidth * 1/2) - btnWidth/2, 
				(int)(gameHeight/2 -35), btnWidth, 30);
		btnSaveExit = new SimpleButton((int)(gameWidth * 1/2) - btnWidth/2, 
				(int)(gameHeight/2), btnWidth, 30);
		btnOption = new SimpleButton((int)(gameWidth * 1/2) - btnWidth/2,
				(int)(gameHeight/2 +35), btnWidth, 30);
	}
	
	public void update(float delta) {
		// get game state
		camera.position.set(new Vector3(gameWorld.getPlayer().getCar().getPosition(), 0));
		camera.update();
        tiledMapRenderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
		
		if (gameWorld.isReady()) { updateReady(delta); } 
//		else if (gameWorld.isRunning()) { updateRunning(delta); } 
		else if (gameWorld.isPaused()) { updatePaused(delta); }
//		else if (gameWorld.hasEnded()) { updateEnded(delta); }
	}
	
	private void updateReady(float delta) {
		// update ready state
		resumeCountDown += delta;
		
		// change state after countdown
		if (resumeCountDown >= 6f) {
			gameWorld.startGame();
		}
	}
	
	private void updatePaused(float delta) {
		// update paused menu objects
		btnResumeGame.update(delta);
		
		// check input from user and perform action
		if (btnResumeGame.isTouched()) {
			// resume game
			gameWorld.resumeGame();
		}	
	}
 	
	public void render(float delta) {
		// get game state
		if (gameWorld.isReady()) { 		
			renderReady(delta); 
			renderControls(delta);	
		} 
		else if (gameWorld.isRunning()) { 	
			//box2Drenderer.render(gameWorld.worldBox2D, camera.combined);
			renderRunning(delta);
			renderControls(delta);		 
		} 
		else if (gameWorld.isPaused()) { renderPaused(delta); }
		else if (gameWorld.hasEnded()) { renderEnded(delta); }
	}

	public void renderReady(float delta) {
		// render ready state
		batch.begin();
		renderCountDown(resumeCountDown);
		batch.end();
	}
	
	private void renderCountDown(float countDown) {
		// render countdown		
		if (countDown >= 1f && countDown < 2f) {
			AssetHandler.getFont(1f).draw(batch, "READY", 20, 20);
		}
		else if (countDown >= 2f && countDown < 3f) {
			AssetHandler.getFont(1f).draw(batch, "3", 20, 20);
		}
		else if (countDown >= 3f && countDown < 4f) {
			AssetHandler.getFont(1f).draw(batch, "2", 20, 20);
		}
		else if (countDown >= 4f && countDown < 5f) {
			AssetHandler.getFont(1f).draw(batch, "1", 20, 20);
		}
		else if (countDown >= 5f && countDown < 6f) {
			AssetHandler.getFont(1f).draw(batch, "GO", 20, 20);
		}
	}
	
	public void renderRunning(float delta) {
		// render running state
        tiledMapRenderer.render();
		batch.begin();
			
			// Draws players
			for (Player players : gameWorld.players) {
				batch.draw(AssetHandler.opponent, 
						players.getCar().getPosition().x - players.getCar().getWidth() / 2, 
						players.getCar().getPosition().y - players.getCar().getHeight() / 2, 
						players.getCar().getWidth()/2, players.getCar().getHeight()/2, 
						players.getCar().getWidth(), players.getCar().getHeight(), 
						1.5f, 1.5f, players.getCar().getRotation());
			}
			
			//Draws static objects (powers and obstacles)
			for (GameObject staticObj : gameWorld.staticObjects) {
				if(staticObj.getObjType().value() == GameObject.ObjType.POWER_UP_CONTAINER.value())
				{
					// Only draw a power up if it is not in its cool down period
					PowerUpContainer powerUpContainer = (PowerUpContainer) staticObj;
					if(!powerUpContainer.isCoolingDown()) 
					{
						batch.draw(AssetHandler.powerUp, 
								staticObj.getPosition().x - staticObj.getWidth() / 2, 
								staticObj.getPosition().y - staticObj.getHeight() / 2, 
								0, 0, 
								staticObj.getWidth(), staticObj.getHeight(), 
								1.5f, 1.5f, staticObj.getRotation());
					}
				}
				else{
					batch.draw(AssetHandler.obstacle, 
							staticObj.getPosition().x - staticObj.getWidth() / 2, 
							staticObj.getPosition().y - staticObj.getHeight() / 2, 
							0, 0, 
							staticObj.getWidth(), staticObj.getHeight(), 
							1.5f, 1.5f, staticObj.getRotation());
				}
			}
		batch.end();
	}

	public void renderPaused(float delta) {
		// render paused controls/buttons
		batch.begin();
		batch.draw(AssetHandler.btnResumeGame, 
				btnResumeGame.getPosition().x, btnResumeGame.getPosition().y, 
				btnResumeGame.getWidth(), btnResumeGame.getHeight());
		batch.draw(AssetHandler.btnOption, 
				btnOption.getPosition().x, btnOption.getPosition().y, 
				btnOption.getWidth(), btnOption.getHeight());
		batch.draw(AssetHandler.btnSaveExit, 
				btnSaveExit.getPosition().x, btnSaveExit.getPosition().y, 
				btnSaveExit.getWidth(), btnSaveExit.getHeight());
		batch.end();
	}
	
	public void renderEnded(float delta) {
		//change screen to end game screen 
		
	}
	
	public void renderControls(float delta) {
		// update/render controls
		gameInputProcessor.update(delta);
		gameInputProcessor.render(delta);	
	}
	
	public void dispose() {
		
	}
}
