package com.not.itproject.handlers;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.not.itproject.zero.ProjectZero;

public class AssetHandler {
	// declare all assets
	public static Texture logoTexture, buttonTexture, buttonStartTexture, buttonTutorialTexture,
							toggleButtonSoundOffTexture, toggleButtonSoundOnTexture,
							buttonLoadGameTexture, buttonNewGameTexture, buttonLoadTexture,
							buttonNextTexture, buttonReadyTexture, buttonStartSessionTexture,
							buttonBackTexture, buttonRefreshTexture, powerUpTexture, obstacleTexture,
							buttonResumeGameTexture, buttonOptionTexture, buttonSaveExitTexture,
							buttonOkayTexture, buttonYesTexture, buttonNoTexture;
	public static Texture carRedTexture, carBlueTexture, carGreenTexture, carYellowTexture;
	public static TextureRegion logo, button, buttonStart, buttonTutorial,
							toggleButtonSoundOff, toggleButtonSoundOn,
							buttonLoadGame, buttonNewGame, buttonLoad,
							buttonNext, buttonReady, buttonStartSession,
							buttonBack, buttonRefresh, powerUp, obstacle,
							carRed, carBlue, carGreen, carYellow,
							buttonOption, buttonResumeGame, buttonSaveExit,
							buttonOkay, buttonYes, buttonNo;
	
	public static Texture menuTrackTexture, menuVehicleTexture, menuReadyTexture,
							playerRedTexture, playerBlueTexture,
							playerGreenTexture, playerYellowTexture,
							playerNATexture, playerAITexture, playerReadyTexture,
							bannerBlackTexture, bannerWhiteTexture;
	public static TextureRegion menuTrack, menuVehicle, menuReady,
							playerRed, playerBlue,
							playerGreen, playerYellow,
							playerNA, playerAI, playerReady,
							bannerBlack, bannerWhite;
	
	public static Texture navigateUpTexture, navigateDownTexture, navigateLeftTexture, navigateRightTexture;
	public static TextureRegion navigateUp, navigateDown, navigateLeft, navigateRight;
	
	public static Texture backgroundUniversalTexture, backgroundErrorTexture;
	public static TextureRegion backgroundUniversal, backgroundError;
	
	public static BitmapFont font;
	public static Preferences prefs;
		
	public static void load() {
		/*** load all assets ***/
		
		// load components
		loadNavigation();
		loadBackgrounds();
		loadVehicles();
		loadObjects();
		loadUncategorised();
		
		// load fonts
		font = new BitmapFont(Gdx.files.internal("fonts/kenvector.fnt"), true);
		
		// load preferences/settings
		loadSettings();
	}
	
	private static void loadBackgrounds() {
		// load backgrounds
		backgroundUniversalTexture = new Texture(Gdx.files.internal("backgrounds/backgroundUniversal.png"));
		backgroundUniversalTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundErrorTexture = new Texture(Gdx.files.internal("backgrounds/backgroundError.png"));
		backgroundErrorTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// load regions
		backgroundUniversal = new TextureRegion(backgroundUniversalTexture);
		backgroundUniversal.flip(false, true);
		backgroundError = new TextureRegion(backgroundErrorTexture);
		backgroundError.flip(false, true);
	}
	
	private static void loadVehicles() {
		// load vehicles
		carRedTexture = new Texture(Gdx.files.internal("vehicles/type1-red.png"));
		carRedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		carBlueTexture = new Texture(Gdx.files.internal("vehicles/type1-blue.png"));
		carBlueTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		carGreenTexture = new Texture(Gdx.files.internal("vehicles/type1-green.png"));
		carGreenTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		carYellowTexture = new Texture(Gdx.files.internal("vehicles/type1-yellow.png"));
		carYellowTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// load regions
		carRed = new TextureRegion(carRedTexture);
		carRed.flip(false, true);
		carBlue = new TextureRegion(carBlueTexture);
		carBlue.flip(false, true);
		carGreen = new TextureRegion(carGreenTexture);
		carGreen.flip(false, true);
		carYellow = new TextureRegion(carYellowTexture);
		carYellow.flip(false, true);
	}
	
	private static void loadNavigation() {
		// load navigational components (buttons)
		buttonTexture = new Texture(Gdx.files.internal("navigation/buttonDefault.png"));
		buttonTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonStartTexture = new Texture(Gdx.files.internal("navigation/buttonStart.png"));
		buttonStartTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonTutorialTexture = new Texture(Gdx.files.internal("navigation/buttonTutorial.png"));
		buttonTutorialTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		toggleButtonSoundOffTexture = new Texture(Gdx.files.internal("navigation/toggleSoundOff.png"));
		toggleButtonSoundOffTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		toggleButtonSoundOnTexture = new Texture(Gdx.files.internal("navigation/toggleSoundOn.png"));
		toggleButtonSoundOnTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonLoadGameTexture = new Texture(Gdx.files.internal("navigation/buttonLoadGame.png"));
		buttonLoadGameTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonNewGameTexture = new Texture(Gdx.files.internal("navigation/buttonNewGame.png"));
		buttonNewGameTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonLoadTexture = new Texture(Gdx.files.internal("navigation/buttonLoad.png"));
		buttonLoadTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonNextTexture = new Texture(Gdx.files.internal("navigation/buttonNext.png"));
		buttonNextTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonReadyTexture = new Texture(Gdx.files.internal("navigation/buttonReady.png"));
		buttonReadyTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonStartSessionTexture = new Texture(Gdx.files.internal("navigation/buttonStartSession.png"));
		buttonStartSessionTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonBackTexture = new Texture(Gdx.files.internal("navigation/buttonBack.png"));
		buttonBackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonRefreshTexture = new Texture(Gdx.files.internal("navigation/buttonRefresh.png"));
		buttonRefreshTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonResumeGameTexture = new Texture(Gdx.files.internal("navigation/buttonResumeGame.png"));
		buttonResumeGameTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonOptionTexture = new Texture(Gdx.files.internal("navigation/buttonOptions.png"));
		buttonOptionTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonSaveExitTexture = new Texture(Gdx.files.internal("navigation/buttonSaveExit.png"));
		buttonSaveExitTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonOkayTexture = new Texture(Gdx.files.internal("navigation/buttonOkay.png"));
		buttonOkayTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonYesTexture = new Texture(Gdx.files.internal("navigation/buttonYes.png"));
		buttonYesTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonNoTexture = new Texture(Gdx.files.internal("navigation/buttonNo.png"));
		buttonNoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		navigateUpTexture = new Texture(Gdx.files.internal("navigation/navigateUp.png"));
		navigateUpTexture .setFilter(TextureFilter.Linear, TextureFilter.Linear);
		navigateDownTexture = new Texture(Gdx.files.internal("navigation/navigateDown.png"));
		navigateDownTexture .setFilter(TextureFilter.Linear, TextureFilter.Linear);
		navigateLeftTexture = new Texture(Gdx.files.internal("navigation/navigateLeft.png"));
		navigateLeftTexture .setFilter(TextureFilter.Linear, TextureFilter.Linear);
		navigateRightTexture = new Texture(Gdx.files.internal("navigation/navigateRight.png"));
		navigateRightTexture .setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		
		// load regions
		button = new TextureRegion(buttonTexture);
		button.flip(false, true);
		buttonStart = new TextureRegion(buttonStartTexture);
		buttonStart.flip(false, true);
		buttonTutorial = new TextureRegion(buttonTutorialTexture);
		buttonTutorial.flip(false, true);
		toggleButtonSoundOff = new TextureRegion(toggleButtonSoundOffTexture);
		toggleButtonSoundOff.flip(false, true);
		toggleButtonSoundOn = new TextureRegion(toggleButtonSoundOnTexture);
		toggleButtonSoundOn.flip(false, true);
		buttonLoadGame = new TextureRegion(buttonLoadGameTexture);
		buttonLoadGame.flip(false, true);
		buttonNewGame = new TextureRegion(buttonNewGameTexture);
		buttonNewGame.flip(false, true);
		buttonLoad = new TextureRegion(buttonLoadTexture);
		buttonLoad.flip(false, true);
		buttonNext = new TextureRegion(buttonNextTexture);
		buttonNext.flip(false, true);
		buttonResumeGame = new TextureRegion(buttonResumeGameTexture);
		buttonResumeGame.flip(false, true);
		buttonOption = new TextureRegion(buttonOptionTexture);
		buttonOption.flip(false, true);
		buttonSaveExit = new TextureRegion(buttonSaveExitTexture);
		buttonSaveExit.flip(false, true);
		buttonReady = new TextureRegion(buttonReadyTexture);
		buttonReady.flip(false, true);
		buttonStartSession = new TextureRegion(buttonStartSessionTexture);
		buttonStartSession.flip(false, true);
		buttonOkay = new TextureRegion(buttonOkayTexture);
		buttonOkay.flip(false, true);
		buttonYes = new TextureRegion(buttonYesTexture);
		buttonYes.flip(false, true);
		buttonNo = new TextureRegion(buttonNoTexture);
		buttonNo.flip(false, true);
		navigateUp = new TextureRegion(navigateUpTexture);
		navigateUp.flip(false, true);
		navigateDown = new TextureRegion(navigateDownTexture);
		navigateDown.flip(false, true);
		navigateLeft = new TextureRegion(navigateLeftTexture);
		navigateLeft.flip(false, true);
		navigateRight = new TextureRegion(navigateRightTexture);
		navigateRight.flip(false, true);
	}
	
	private static void loadObjects() {
		// load objects
		powerUpTexture = new Texture(Gdx.files.internal("objects/power.png"));
		powerUpTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		obstacleTexture = new Texture(Gdx.files.internal("objects/tnt.png"));
		obstacleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// load regions
		powerUp = new TextureRegion(powerUpTexture);
		powerUp.flip(false, true);
		obstacle = new TextureRegion(obstacleTexture);
		obstacle.flip(false, true);
	}
	
	private static void loadUncategorised() {
		// load textures
		logoTexture = new Texture(Gdx.files.internal("project-zero.png"));
		logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		menuTrackTexture = new Texture(Gdx.files.internal("other/menuTrack.png"));
		menuTrackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		menuVehicleTexture = new Texture(Gdx.files.internal("other/menuVehicle.png"));
		menuVehicleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		menuReadyTexture = new Texture(Gdx.files.internal("other/menuReady.png"));
		menuReadyTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerRedTexture = new Texture(Gdx.files.internal("other/playerRed.png"));
		playerRedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerBlueTexture = new Texture(Gdx.files.internal("other/playerBlue.png"));
		playerBlueTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerGreenTexture = new Texture(Gdx.files.internal("other/playerGreen.png"));
		playerGreenTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerYellowTexture = new Texture(Gdx.files.internal("other/playerYellow.png"));
		playerYellowTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerNATexture = new Texture(Gdx.files.internal("other/playerNA.png"));
		playerNATexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerAITexture = new Texture(Gdx.files.internal("other/playerAI.png"));
		playerAITexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerReadyTexture = new Texture(Gdx.files.internal("other/playerReady.png"));
		playerReadyTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bannerBlackTexture = new Texture(Gdx.files.internal("other/banner-black.png"));
		bannerBlackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bannerWhiteTexture = new Texture(Gdx.files.internal("other/banner-white.png"));
		bannerWhiteTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// load regions
		logo = new TextureRegion(logoTexture);
		logo.flip(false, true);
		buttonBack = new TextureRegion(buttonBackTexture);
		buttonBack.flip(false, true);
		buttonRefresh = new TextureRegion(buttonRefreshTexture);
		buttonRefresh.flip(false, true);
		menuTrack = new TextureRegion(menuTrackTexture);
		menuTrack.flip(false, true);
		menuVehicle = new TextureRegion(menuVehicleTexture);
		menuVehicle.flip(false, true);
		menuReady = new TextureRegion(menuReadyTexture);
		menuReady.flip(false, true);
		playerRed = new TextureRegion(playerRedTexture);
		playerRed.flip(false, true);
		playerBlue = new TextureRegion(playerBlueTexture);
		playerBlue.flip(false, true);
		playerGreen = new TextureRegion(playerGreenTexture);
		playerGreen.flip(false, true);
		playerYellow = new TextureRegion(playerYellowTexture);
		playerYellow.flip(false, true);
		playerNA = new TextureRegion(playerNATexture);
		playerNA.flip(false, true);
		playerAI = new TextureRegion(playerAITexture);
		playerAI.flip(false, true);
		playerReady = new TextureRegion(playerReadyTexture);
		playerReady.flip(false, true);
		bannerBlack = new TextureRegion(bannerBlackTexture);
		bannerBlack.flip(false, true);
		bannerWhite = new TextureRegion(bannerWhiteTexture);
		bannerWhite.flip(false, true);
	}
	
	public static void loadSettings() {
		/*** load all preferences/settings ***/
		prefs = Gdx.app.getPreferences("ProjectZero");
				
		// initialise settings
		if (!prefs.contains("soundMute")) {
			// default mute = false
			setSoundMute(false);
		}
		if (!prefs.contains("playerID")) {
			// generate new playerID
			prefs.putString("playerID", UUID.randomUUID().toString());
			prefs.flush();
		}
	}
	
	public static void setSoundMute(boolean value) {
		// set sound mute on/off
		prefs.putBoolean("soundMute", value);
		prefs.flush();
	}
	
	public static boolean getSoundMute() {
		// get sound mute status
		return prefs.getBoolean("soundMute");
	}

	// debugging playerID
	static String debugPlayerID = UUID.randomUUID().toString();
	public static String getPlayerID() {
		// return prefs.getString("playerID");
		return debugPlayerID;
	}
	
	// vibrate device
	public static void vibrate(int time) {
		// vibrate in milliseconds
		Gdx.input.vibrate(time);
		ProjectZero.log("Vibrate Device");
	}
	
	public static BitmapFont getFont(float scale) {
		font.setScale(scale);
		return font;
	}
 	
	public static void release() {
		/*** release all assets ***/
		logoTexture.dispose();
		buttonTexture.dispose(); buttonStartTexture.dispose(); buttonTutorialTexture.dispose();
		toggleButtonSoundOffTexture.dispose(); toggleButtonSoundOnTexture.dispose();
		buttonLoadGameTexture.dispose(); buttonNewGameTexture.dispose(); buttonLoadTexture.dispose(); 
		
		// TODO: RELEASE ALL ASSETS
	}
	
}
