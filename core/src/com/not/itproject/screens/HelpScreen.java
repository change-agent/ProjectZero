package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.objects.SimpleButton;
import com.not.itproject.objects.SimpleRoundButton;
import com.not.itproject.zero.ProjectZero;

public class HelpScreen extends AbstractScreen {
	// declare variables
	SimpleButton btnPrevious;
	SimpleButton btnNext;
	SimpleButton btnClose;
	SimpleRoundButton btnBack;
	HelpState screenStatus;
	
	// determines number of help screens
	private static final int NUM_HELP_PAGES = 3;
	enum HelpState { HELP_ONE, HELP_TWO, HELP_THREE };
	
	// main constructor
	public HelpScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		screenStatus = HelpState.HELP_ONE;
		
		int btnWidth = 100; // assign for main buttons
		btnPrevious = new SimpleButton((int)(gameWidth * 1/4) - btnWidth/2, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnNext = new SimpleButton((int)(gameWidth * 3/4) - btnWidth/2, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnBack = new SimpleRoundButton(20, 20, 15);
	}
	
	// functions to determine the page status and its page number
	private int determineHelpPageNumber(HelpState state) {
		switch (state) {
			case HELP_ONE:		return 1;
			case HELP_TWO:		return 2;
			case HELP_THREE:	return 3;
			default:			return 0;
		}
	}
	
	private HelpState determineHelpPageStatus(int pageNumber) {
		switch (pageNumber) {
			case 1:		return HelpState.HELP_ONE;
			case 2:		return HelpState.HELP_TWO;
			case 3:		return HelpState.HELP_THREE;
		}
		return screenStatus;
	}
	
	// functions to proceed to previous/next help pages
	private void previousHelpPage() {
		int currentPageNumber = determineHelpPageNumber(screenStatus);
		if (currentPageNumber > 1 && currentPageNumber <= NUM_HELP_PAGES) {
			// go to previous page
			screenStatus = determineHelpPageStatus(currentPageNumber - 1);
		}
	}
	
	private void nextHelpPage() {
		int currentPageNumber = determineHelpPageNumber(screenStatus);
		if (currentPageNumber >= 1 && currentPageNumber < NUM_HELP_PAGES) {
			// go to next page
			screenStatus = determineHelpPageStatus(currentPageNumber + 1);
		}
	}

	public void update(float delta) {		
		// update objects
		btnPrevious.update(delta);
		btnNext.update(delta);
		btnBack.update(delta);
		
		// check input from user and perform action
		if (btnBack.isTouched()) {
			game.nextScreen(ProjectZero.mainScreen, this);
			// debug log to console
			ProjectZero.log("Back button is pressed.");
			
		}
		
		// determine screen status
		switch (screenStatus) {
			case HELP_ONE:
				// check input from user and perform action
				if (btnNext.isTouched()) {
					nextHelpPage();
					// debug log to console
					ProjectZero.log("Next page button is pressed.");
					
					
				}
				break;
				
			case HELP_TWO:
				// check input from user and perform action
				if (btnPrevious.isTouched()) {
					previousHelpPage();
					// debug log to console
					ProjectZero.log("Previous page button is pressed.");
					
				} else if (btnNext.isTouched()) {
					nextHelpPage();
					// debug log to console
					ProjectZero.log("Next page button is pressed.");
					
				}
				break;
				
			case HELP_THREE:
				// check input from user and perform action
				if (btnPrevious.isTouched()) {
					previousHelpPage();
					// debug log to console
					ProjectZero.log("Previous page button is pressed.");
					
				}
				break;
		}
	}
	
	@Override
	public void render(float delta) {
//		// clear screen
//		Gdx.graphics.getGL20().glClearColor(0.65f, 1f, 0.85f, 0.85f);
//		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// update objects
		update(delta);

		// determine screen status
		switch (screenStatus) {
			case HELP_ONE:
				// clear screen
				Gdx.graphics.getGL20().glClearColor(0.65f, 0.7f, 0.85f, 0.85f);
				Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
				
				// render first help screen
				batch.begin();
				
				// draw background
				batch.draw(AssetHandler.backgroundUniversal, 0, 0,
						gameWidth, gameHeight);
				
				batch.draw(AssetHandler.helpSessions, 0, 0,
						gameWidth, gameHeight);
				batch.draw(AssetHandler.buttonBack, 
						btnBack.getPosition().x - btnBack.getRadius(), 
						btnBack.getPosition().y - btnBack.getRadius(), 
						btnBack.getRadius() * 2, btnBack.getRadius() * 2);
				batch.draw(AssetHandler.buttonNavigateNext, 
						btnNext.getPosition().x, 
						btnNext.getPosition().y, 
						btnNext.getWidth(), btnNext.getHeight());
				batch.end();
				break;
				
			case HELP_TWO:
				// clear screen
				Gdx.graphics.getGL20().glClearColor(0.65f, 0.8f, 0.85f, 0.85f);
				Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
				
				// render second help screen
				batch.begin();
				
				// draw background
				batch.draw(AssetHandler.backgroundUniversal, 0, 0,
						gameWidth, gameHeight);
				
				batch.draw(AssetHandler.helpTrackSelection, 0, 0,
						gameWidth, gameHeight);
				batch.draw(AssetHandler.buttonBack, 
						btnBack.getPosition().x - btnBack.getRadius(), 
						btnBack.getPosition().y - btnBack.getRadius(), 
						btnBack.getRadius() * 2, btnBack.getRadius() * 2);
				batch.draw(AssetHandler.buttonNavigatePrev, 
						btnPrevious.getPosition().x, 
						btnPrevious.getPosition().y, 
						btnPrevious.getWidth(), btnPrevious.getHeight());
				batch.draw(AssetHandler.buttonNavigateNext, 
						btnNext.getPosition().x, 
						btnNext.getPosition().y, 
						btnNext.getWidth(), btnNext.getHeight());
				batch.end();
				break;
				
			case HELP_THREE:
				// clear screen
				Gdx.graphics.getGL20().glClearColor(0.65f, 0.9f, 0.85f, 0.85f);
				Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
				
				// render second help screen
				batch.begin();
				
				// draw background
				batch.draw(AssetHandler.backgroundUniversal, 0, 0,
						gameWidth, gameHeight);
				
				batch.draw(AssetHandler.helpGameplay, 0, 0,
						gameWidth, gameHeight);
				batch.draw(AssetHandler.buttonBack, 
						btnBack.getPosition().x - btnBack.getRadius(), 
						btnBack.getPosition().y - btnBack.getRadius(), 
						btnBack.getRadius() * 2, btnBack.getRadius() * 2);
				batch.draw(AssetHandler.buttonNavigatePrev, 
						btnPrevious.getPosition().x, 
						btnPrevious.getPosition().y, 
						btnPrevious.getWidth(), btnPrevious.getHeight());
				batch.end();
				break;
		}
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
