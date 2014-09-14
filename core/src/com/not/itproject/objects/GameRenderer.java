package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.zero.ProjectZero;

public class GameRenderer {
	// declare variables
	GameWorld world;
	OrthographicCamera camera;
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	private float gameWidth, gameHeight;
	
	// main constructor
	public GameRenderer(GameWorld world) {
		// get world
		this.world = world;
		
		// calculate ratio
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		gameWidth = ProjectZero.GAME_WIDTH;
		gameHeight = screenHeight / (screenWidth / gameWidth);
		
		// initialise variables
		camera = new OrthographicCamera();
		camera.setToOrtho(true, gameWidth, gameHeight);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
	}
	
	public void render(float delta) {
		// get game state
		if (world.isReady()) { renderReady(delta); } 
		else if (world.isRunning()) { renderRunning(delta); } 
		else if (world.isPaused()) { renderPaused(delta); }
		else if (world.hasEnded()) { renderEnded(delta); }
	}
	
	public void renderEnded(float delta) {
		//change screen to end game screen 
		
	}

	public void renderReady(float delta) {
		
	}
	
	public void renderRunning(float delta) {
		// render running state
		batch.begin();
		batch.draw(AssetHandler.button, 
				world.getPlayer().getPosition().x - world.getPlayer().getWidth() / 2, 
				world.getPlayer().getPosition().y - world.getPlayer().getHeight() / 2, 
				0, 0, 
				world.getPlayer().getWidth(), world.getPlayer().getHeight(), 
				1, 1, world.getPlayer().getRotation());
		
		// Draws other players
		for (Player opponent : world.opponents) {
			batch.draw(AssetHandler.button, 
					opponent.getPosition().x - opponent.getWidth() / 2, 
					opponent.getPosition().y - opponent.getHeight() / 2, 
					0, 0, 
					opponent.getWidth(), opponent.getHeight(), 
					1, 1, opponent.getRotation());
		}
		
		//Draws static objects (powers and obstacles)
		for (GameObject staticObj : world.staticObjects) {
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
	
	public void dispose() {
		
	}
}
