package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.networks.NetworkMessage;
import com.not.itproject.zero.ProjectZero;

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
	TiledMapRenderer tiledMapRenderer;
	
	// renderer state variables
	private float resumeCountDown;
	private float VIEWPORT_FACTOR = 4.0f;
	private boolean isViewportScaled;
	
	// paused menu variables
	SimpleButton btnResumeGame;
	SimpleButton btnSaveExit;
//	SimpleButton btnOption; // implement as a stretch when game has more options
	ToggleButton btnSoundToggle;
	
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
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(gameWorld.getTiledMap());
		
		// initialise ready state variables
		resumeCountDown = 0f;
		isViewportScaled = false;
		
		// set viewport - no scaling
		scaleViewport(false);
		
		// initialise paused menu variables
		int btnWidth = 120; // assign for main buttons
		btnResumeGame = new SimpleButton((int)(gameWidth * 1/2) - btnWidth/2, 
				(int)(gameHeight/2 -35), btnWidth, 30);
		btnSaveExit = new SimpleButton((int)(gameWidth * 1/2) - btnWidth/2, 
				(int)(gameHeight/2), btnWidth, 30);
		btnWidth = 100; // reassign for sound toggle
		btnSoundToggle = new ToggleButton((int)(gameWidth / 2) - btnWidth/2, 
				(int)(gameHeight * 4/5), btnWidth, 20, AssetHandler.getSoundMute());
	}
	
	public void update(float delta) {
		// get game state
		
		// determine viewport
		if (!isViewportScaled && (gameWorld.isReady() || gameWorld.isRunning())) {
			// scale for gameplay
			isViewportScaled = true;
			scaleViewport(true);
		} 
		else if (isViewportScaled && (gameWorld.isPaused())) {
			// do not scale
			isViewportScaled = false;
			scaleViewport(false);			
		}
		
		if (gameWorld.isReady()) { updateReady(delta); } 
		else if (gameWorld.isRunning()) { updateRunning(delta); } 
		else if (gameWorld.isPaused()) { updatePaused(delta); }
//		else if (gameWorld.hasEnded()) { updateEnded(delta); }
	}
	
	private void updateReady(float delta) {
		// update ready state
		camera.position.set(new Vector3(gameWorld.getPlayer().getCar().getPosition(), 0));
		camera.update();
        tiledMapRenderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
		resumeCountDown += delta;
		
		// change state after countdown
		if (resumeCountDown >= 6f) {
			// reset countdown
			resumeCountDown = 0;
			gameWorld.startGame();
		}
	}
	
	private void updateRunning(float delta) {
		// update running state
		camera.position.set(clampCameraPosition(gameWorld.getPlayer().getCar().getPosition()), 0);
		camera.update();
        tiledMapRenderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
	}
	
	private void updatePaused(float delta) {
		// update paused menu objects
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		btnResumeGame.update(delta);
		btnSoundToggle.update(delta);
		
		// check input from user and perform action
		if (btnResumeGame.isTouched()) {
			// resume game
			gameWorld.resumeGame();
			
			// send network message
			if (NetworkHandler.isHost()) {
				// as host - send information about game state
				NetworkHandler.getNetworkServer().sendGameStateInformation(NetworkMessage.GameState.RESUME);
			} else if (NetworkHandler.isClient()) {
				// as client - send information about game state
				NetworkHandler.getNetworkClient().sendGameStateInformation(NetworkMessage.GameState.RESUME);
			} 
		}	 
		else if (btnSaveExit.isTouched()) {
			// save game
			gameWorld.saveGame();
			
			// exit game
			gameWorld.endGame();
			
			// send network message
			if (NetworkHandler.isHost()) {
				// as host - send information about game state
				NetworkHandler.getNetworkServer().sendGameStateInformation(NetworkMessage.GameState.EXIT);
			} else if (NetworkHandler.isClient()) {
				// as client - send information about game state
				NetworkHandler.getNetworkClient().sendGameStateInformation(NetworkMessage.GameState.EXIT);
			} 
		} 
		else if (btnSoundToggle.isTouched()) {
			// toggle sound mute
			AssetHandler.setSoundMute(btnSoundToggle.isToggleOn());
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
        tiledMapRenderer.render();
		batch.begin();

		// draw players
		renderPlayers(delta);
		
		// draw obstacles
		renderObstacles(delta);
			
		// draw countdown
		renderCountDown(resumeCountDown);
		batch.end();
	}
	
	private void renderCountDown(float countDown) {
		// render banner
		batch.draw(AssetHandler.bannerWhite, 
				gameWorld.getPlayer().getCar().getPosition().x - 50, 
				gameWorld.getPlayer().getCar().getPosition().y - 18, 
				0, 0, 100, 13, 1, 1, 0);
		
		// render countdown		
		if (countDown >= 1f && countDown < 2f) {
			AssetHandler.getFont(0.2f).draw(batch, "READY", 
					gameWorld.getPlayer().getCar().getPosition().x - 13, 
					gameWorld.getPlayer().getCar().getPosition().y - 13);
		}
		else if (countDown >= 2f && countDown < 3f) {
			AssetHandler.getFont(0.25f).draw(batch, "3", 
					gameWorld.getPlayer().getCar().getPosition().x - 2,
					gameWorld.getPlayer().getCar().getPosition().y - 14);
		}
		else if (countDown >= 3f && countDown < 4f) {
			AssetHandler.getFont(0.25f).draw(batch, "2", 
					gameWorld.getPlayer().getCar().getPosition().x - 2,
					gameWorld.getPlayer().getCar().getPosition().y - 14);
		}
		else if (countDown >= 4f && countDown < 5f) {
			AssetHandler.getFont(0.25f).draw(batch, "1", 
					gameWorld.getPlayer().getCar().getPosition().x - 1,
					gameWorld.getPlayer().getCar().getPosition().y - 14);
		}
		else if (countDown >= 5f && countDown < 6f) {
			AssetHandler.getFont(0.3f).draw(batch, "GO!", 
					gameWorld.getPlayer().getCar().getPosition().x - 8,
					gameWorld.getPlayer().getCar().getPosition().y - 15);
		}
	}
	
	public void renderRunning(float delta) {
		// render running state
		tiledMapRenderer.render();
		batch.begin();

		// draw players
		renderPlayers(delta);

		// draw obstacles
		renderObstacles(delta);
		batch.end();
	}

	public void renderPaused(float delta) {
		// render paused controls/buttons
		batch.begin();
		batch.draw(AssetHandler.buttonResumeGame, 
				btnResumeGame.getPosition().x, btnResumeGame.getPosition().y, 
				btnResumeGame.getWidth(), btnResumeGame.getHeight());
		batch.draw(AssetHandler.buttonSaveExit, 
				btnSaveExit.getPosition().x, btnSaveExit.getPosition().y, 
				btnSaveExit.getWidth(), btnSaveExit.getHeight());
		
		// display sound mute button
		if (AssetHandler.getSoundMute() == true) {
			// sound mute = on
			batch.draw(AssetHandler.toggleButtonSoundOn, 
					btnSoundToggle.getPosition().x, btnSoundToggle.getPosition().y, 
					btnSoundToggle.getWidth(), btnSoundToggle.getHeight());
		} else {
			// sound mute = off
			batch.draw(AssetHandler.toggleButtonSoundOff, 
					btnSoundToggle.getPosition().x, btnSoundToggle.getPosition().y, 
					btnSoundToggle.getWidth(), btnSoundToggle.getHeight());
		}
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
	
	private void renderPlayers(float delta) {
		// Draws players
		TextureRegion playerImage = null;
		for (Player player : gameWorld.players) {
			// determine player colour
			switch (player.getPlayerColour()) {
				case RED:
					playerImage = AssetHandler.carRed;
					break;
				case BLUE:
					playerImage = AssetHandler.carBlue;
					break;
				case GREEN:
					playerImage = AssetHandler.carGreen;
					break;
				case YELLOW:
					playerImage = AssetHandler.carYellow;
					break;
			}
			
			// render player
			batch.draw(playerImage, 
					player.getCar().getPosition().x - player.getCar().getWidth() / 2, 
					player.getCar().getPosition().y - player.getCar().getHeight() / 2, 
					player.getCar().getWidth()/2, player.getCar().getHeight()/2, 
					player.getCar().getWidth(), player.getCar().getHeight(), 
					1.5f, 1.5f, player.getCar().getRotation());
		}
	}
	
	private void renderObstacles(float delta) {
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
							1.0f, 1.0f, staticObj.getRotation());
				}
			}
			else{
				batch.draw(AssetHandler.obstacle, 
						staticObj.getPosition().x - staticObj.getWidth() / 2, 
						staticObj.getPosition().y - staticObj.getHeight() / 2, 
						0, 0, 
						staticObj.getWidth(), staticObj.getHeight(), 
						1.0f, 1.0f, staticObj.getRotation());
			}
		}
	}
	
	public void scaleViewport(boolean scale) {
		// determine whether to scale viewport depending on ingame or not
		if (scale == true) {
			// in game - requires scaling
			camera.setToOrtho(true, gameWidth / VIEWPORT_FACTOR, gameHeight / VIEWPORT_FACTOR);
			camera.update();
		} else if (scale == false) {
			// restore to default
			camera.setToOrtho(true, gameWidth, gameHeight);
			camera.update();
		}
	}
	
	public void dispose() {
		
	}
	
	private Vector2 clampCameraPosition(Vector2 cameraPosition) 
	{
		Vector2 newCameraPosition = new Vector2();
		newCameraPosition.set(cameraPosition);
		if( newCameraPosition.x - camera.viewportWidth / 2 <= 0 ) {
			newCameraPosition.x = camera.viewportWidth / 2;
		}
		else if(newCameraPosition.x + camera.viewportWidth / 2 >= gameWorld.getMapWidth()) {
			newCameraPosition.x = gameWorld.getMapWidth() - camera.viewportWidth / 2;
		}
		if( newCameraPosition.y - camera.viewportHeight / 2 <= 0 ) {
			newCameraPosition.y = camera.viewportHeight / 2;
		}
		else if(newCameraPosition.y + camera.viewportHeight / 2 >= gameWorld.getMapHeight()) {
			newCameraPosition.y = gameWorld.getMapHeight() - camera.viewportHeight / 2;
		}
		return newCameraPosition;
	}
}
