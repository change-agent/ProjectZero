package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.objects.SimpleButton;
import com.not.itproject.zero.ProjectZero;

public class ErrorScreen extends AbstractScreen {
	// declare variables
	SimpleButton btnOK;
	AbstractScreen nextScreen;
	String errorTitle, errorMessage;
	
	// main constructor
	public ErrorScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		int btnWidth = 120; // assign for main buttons
		btnOK = new SimpleButton((int)(gameWidth * 1/2) - btnWidth/2, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		
		nextScreen = ProjectZero.mainScreen;
		errorTitle = errorMessage = "";
	}
	
	public void displayError(AbstractScreen nextScreen, String errorTitle, String errorMessage) {
		// switch to error screen
		game.setScreen(ProjectZero.errorScreen);
		
		// which screen should be displayed after error message
		this.nextScreen = nextScreen;
		
		// error message to be displayed
		this.errorTitle = errorTitle;
		
		// determine string length
		int stringLength = errorMessage.length();
		if (stringLength > 80) { stringLength = 80; }
		this.errorMessage = errorMessage.substring(0, stringLength);
	}
	
	public void update(float delta) {		
		// update objects
		btnOK.update(delta);
		
		// check input from user and perform action
		if (btnOK.isTouched()) {
			// proceed to next screen
			game.nextScreen(nextScreen, this);			
		}
	}
	
	@Override
	public void render(float delta) {
		// clear screen
		Gdx.graphics.getGL20().glClearColor(1f, 1f, 1f, 1f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// update objects
		update(delta);
		
		// render screen
		batch.begin();
		
		// draw background
		batch.draw(AssetHandler.backgroundUniversal, 0, 0,
				gameWidth, gameHeight);
		batch.draw(AssetHandler.backgroundError, 0, 0,
				gameWidth, gameHeight);
		
		// render message
		AssetHandler.getFont(0.75f).draw(batch, errorTitle, 20, 20);
		AssetHandler.getFont(0.5f).drawWrapped(batch, errorMessage, 30, 55, gameWidth - 60);
		
		// render okay button
		batch.draw(AssetHandler.buttonOkay, 
				btnOK.getPosition().x, btnOK.getPosition().y, 0, 0, 
				btnOK.getWidth(), btnOK.getHeight(), 1, 1, 0);
		batch.end();
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
	}
}
