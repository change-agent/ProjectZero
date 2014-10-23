package com.not.itproject.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.not.itproject.objects.GameVariables;
import com.not.itproject.objects.Player;

public class TestFramework extends Game {
	// declare variables
	public static final String GAME_NAME = "Project Zero";
	public static final int GAME_WIDTH = 320;
	public static int GAME_HEIGHT;
	public static float RATIO;
	BitmapFont font;

	// test variables
	boolean triggerTest;
	int NUM_OF_TESTS = 2;
	int failCounter;
	
	// player variables
	float playerX = GAME_WIDTH / 2, playerY = GAME_HEIGHT * RATIO; 
	float playerWidth = 25, playerHeight = 30;
	float rotation = 0;
	
	Player player;
	SpriteBatch batch;

	@Override
	public void create() {
		// declare variables
		RATIO = (float) GAME_WIDTH / Gdx.graphics.getWidth();
		batch = new SpriteBatch();
		triggerTest = false;
		failCounter = 0;
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		
		// define player
		player = new Player(null, "playerID", GameVariables.PlayerColour.RED, 
				playerX, playerY, playerWidth, playerHeight, rotation, 480, 480);
		
		// perform tests
		Gdx.app.log("Project Zero", "Testing Framework");
	}
	
	public void resetPlayer() {
		// reset variables
		player.getCar().setPosition(new Vector2(playerX, playerY));
		player.getCar().setWidth(playerWidth);
		player.getCar().setHeight(playerHeight);
		player.getCar().setRotation(rotation);
	}

	public void testScenarios(float delta) {
		/*** function for testing scenarios ***/
		
		float playerInitial, playerFinal;
		Gdx.app.log("Test Case", "*** Task #1 - Player Movement (Move Left) ***");
		playerInitial = player.getCar().getPosition().x;
		Gdx.app.log("Test Case", "Expected Result: <" + playerInitial);
		player.getCar().powerOnEngine(-1);
		playerFinal = player.getCar().getPosition().x;
		Gdx.app.log("Test Case", "Actual Result: " + playerFinal);
		// print result
		if (playerInitial > playerFinal) {
			Gdx.app.log("Test Case", "Test Successful \n");
		} else {
			failCounter += 1;
			Gdx.app.log("Test Case", "Test Failed \n");
		}
		
		resetPlayer(); // reset player
		
		Gdx.app.log("Test Case", "*** Task #2 - Player Movement (Move Right) ***");
		playerInitial = player.getCar().getPosition().x;
		Gdx.app.log("Test Case", "Expected Result: >" + player.getCar().getPosition().x);
		player.getCar().powerOnEngine(1);
		playerFinal = player.getCar().getPosition().x;
		Gdx.app.log("Test Case", "Actual Result: " + player.getCar().getPosition().x);
		// print result
		if (playerInitial < playerFinal) {
			Gdx.app.log("Test Case", "Test Successful \n");
		} else {
			failCounter += 1;
			Gdx.app.log("Test Case", "Test Failed \n");
		}
		
		resetPlayer(); // reset player
		
		// output test result
		Gdx.app.log("Results", "*** Testing Results ***");
		Gdx.app.log("Results", "Number of tests: 	" + NUM_OF_TESTS);
		Gdx.app.log("Results", "Number of success: 	" + (NUM_OF_TESTS - failCounter));
		Gdx.app.log("Results", "Number of failure: 	" + (failCounter));		
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		// clear screen
		Gdx.graphics.getGL20().glClearColor(1f, 1f, 1f, 1f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// render batch
		batch.begin();
		font.draw(batch, "Project Zero - Testing Framework", 10, 20);
		batch.end();
		
		// perform test		
		if (triggerTest == false) {
			triggerTest = true;
			testScenarios(Gdx.graphics.getDeltaTime());
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
