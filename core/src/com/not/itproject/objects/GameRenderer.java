package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.zero.ProjectZero;

public class GameRenderer {
	// declare variables
	GameWorld gameWorld;
	GameInputProcessor gameInputProcessor;
	OrthographicCamera camera;
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	private float gameWidth, gameHeight;
	Box2DDebugRenderer box2Drenderer;
	
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
		camera.setToOrtho(true, gameWidth, gameHeight);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		box2Drenderer = new Box2DDebugRenderer();
	}
	
	public void render(float delta) {
		box2Drenderer.render(gameWorld.worldBox2D, camera.combined);
		
		// get game state
		if (gameWorld.isReady()) { 		
			renderReady(delta); 
			renderControls(delta);	
		} 
		else if (gameWorld.isRunning()) { 	
			renderRunning(delta);
			renderControls(delta);		 
		} 
		else if (gameWorld.isPaused()) { renderPaused(delta); }
		else if (gameWorld.hasEnded()) { renderEnded(delta); }
	}
	
	public void renderEnded(float delta) {
		//change screen to end game screen 
		
	}

	public void renderReady(float delta) {
		
	}
	
	public void renderRunning(float delta) {
		// render running state
		batch.begin();
			batch.draw(AssetHandler.player, 
					gameWorld.getPlayer().getCar().getPosition().x - gameWorld.getPlayer().getCar().getWidth() / 2, 
					gameWorld.getPlayer().getCar().getPosition().y - gameWorld.getPlayer().getCar().getHeight() / 2, 
					gameWorld.getPlayer().getCar().getWidth()/2, gameWorld.getPlayer().getCar().getHeight()/2, 
					gameWorld.getPlayer().getCar().getWidth(), gameWorld.getPlayer().getCar().getHeight(), 
					1.2f, 1.2f, gameWorld.getPlayer().getCar().getRotation());
			
			// Draws other players
			for (Player opponent : gameWorld.opponents) {
				batch.draw(AssetHandler.opponent, 
						opponent.getCar().getPosition().x - opponent.getCar().getWidth() / 2, 
						opponent.getCar().getPosition().y - opponent.getCar().getHeight() / 2, 
						0, 0, 
						opponent.getCar().getWidth(), opponent.getCar().getHeight(), 
						1, 1, opponent.getCar().getRotation());
			}
			
			//Draws static objects (powers and obstacles)
			for (GameObject staticObj : gameWorld.staticObjects) {
				batch.draw(AssetHandler.button, 
						staticObj.getPosition().x - staticObj.getWidth() / 2, 
						staticObj.getPosition().y - staticObj.getHeight() / 2, 
						0, 0, 
						staticObj.getWidth(), staticObj.getHeight(), 
						1, 1, staticObj.getRotation());
			}
		batch.end();
	}

	public void renderPaused(float delta) {
		
	}
	
	public void renderControls(float delta) {
		// update/render controls
		gameInputProcessor.update(delta);
		gameInputProcessor.render(delta);		
	}
	
	public void dispose() {
		
	}
}
