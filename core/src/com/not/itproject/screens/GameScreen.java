package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.objects.GameInputProcessor;
import com.not.itproject.objects.GameRenderer;
import com.not.itproject.objects.GameWorld;
import com.not.itproject.zero.ProjectZero;

public class GameScreen extends AbstractScreen {
	// declare variables
	GameWorld gameWorld;
	GameRenderer gameRenderer;
	GameInputProcessor gameInputProcessor;
	boolean isInitialised;
	
	// main constructor
	public GameScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		gameWorld = new GameWorld(game);
		gameInputProcessor = new GameInputProcessor(gameWorld, gameWidth, gameHeight);
		gameRenderer = new GameRenderer(gameWorld, gameInputProcessor);
         
        Gdx.input.setInputProcessor(gameInputProcessor.getStage());
        isInitialised = false;
	}

	public void update(float delta) {
		// initialise world
		if (!isInitialised) {
			gameWorld.initialiseGameWorld();
			isInitialised = true;
		} else {
			// send updates to network
			sendNetworkUpdates();
		}
		
		// update world
		gameWorld.update(delta);	
		gameRenderer.update(delta);
	}
	
	public void updatePlayer(String playerID, Vector2 position, Vector2 velocity, float rotation) {
		// update player with new values
		gameWorld.getPlayerByID(playerID).getCar().applyMovement(position, velocity, rotation);
	}
	
	public void sendNetworkUpdates() {
		// send updates to network
		if (NetworkHandler.isHost()) {
			// as host - send information about car
			NetworkHandler.getNetworkServer().sendGameCarInformation(
					gameWorld.getPlayer().getCar().getPosition(), 
					gameWorld.getPlayer().getCar().getVelocity(), 
					gameWorld.getPlayer().getCar().getRotation());
		} else if (NetworkHandler.isClient()) {
			// as client - send information about car
			NetworkHandler.getNetworkClient().sendGameCarInformation(
					gameWorld.getPlayer().getCar().getPosition(), 
					gameWorld.getPlayer().getCar().getVelocity(), 
					gameWorld.getPlayer().getCar().getRotation());
			
		} 
	}
	
	@Override
	public void render(float delta) {
		// clear screen
		Gdx.graphics.getGL20().glClearColor(1f, 0.85f, 0.45f, 0.85f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// render game
		update(delta);
		gameRenderer.render(delta); 
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		game.dispose();
		gameWorld.dispose();
		gameRenderer.dispose();
		gameInputProcessor.dispose();
	}
}
