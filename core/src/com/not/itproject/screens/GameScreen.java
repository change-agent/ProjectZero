package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.not.itproject.objects.GameInputProcessor;
import com.not.itproject.objects.GameRenderer;
import com.not.itproject.objects.GameWorld;
import com.not.itproject.zero.ProjectZero;

public class GameScreen extends AbstractScreen {
	// declare variables
	GameWorld gameWorld;
	GameRenderer gameRenderer;
	GameInputProcessor gameInputProcessor;
	
	// main constructor
	public GameScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		gameWorld = new GameWorld(game);
		gameInputProcessor = new GameInputProcessor(gameWorld, gameWidth, gameHeight);
		gameRenderer = new GameRenderer(gameWorld, gameInputProcessor);
         
        Gdx.input.setInputProcessor(gameInputProcessor.getStage());
	}

	public void update(float delta) {
		// update world
		gameWorld.update(delta);	
		gameRenderer.update(delta);
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
