package com.not.itproject.handlers;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetHandler {
	// declare all assets
	public static Texture logoTexture, buttonTexture, buttonStartTexture, buttonTutorialTexture,
							toggleButtonSoundOffTexture, toggleButtonSoundOnTexture,
							buttonLoadGameTexture, buttonNewGameTexture, buttonLoadTexture,
							buttonNextTexture, buttonReadyTexture, buttonStartSessionTexture,
							buttonBackTexture, buttonRefreshTexture;
	public static Texture playerTexture, opponentTexture;
	public static TextureRegion logo, button, buttonStart, buttonTutorial,
							toggleButtonSoundOff, toggleButtonSoundOn,
							buttonLoadGame, buttonNewGame, buttonLoad,
							buttonNext, buttonReady, buttonStartSession,
							buttonBack, buttonRefresh;
	public static TextureRegion player, opponent;
	
	public static Texture menuTrackTexture, menuVehicleTexture, menuReadyTexture,
							playerRedTexture, playerBlueTexture,
							playerGreenTexture, playerYellowTexture,
							playerNATexture, playerAITexture;
	public static TextureRegion menuTrack, menuVehicle, menuReady,
							playerRed, playerBlue,
							playerGreen, playerYellow,
							playerNA, playerAI;
	
	public static BitmapFont font;
	public static Preferences prefs;
	
	public static void load() {
		/*** load all assets ***/
		
		// load textures
		logoTexture = new Texture(Gdx.files.internal("project-zero.png"));
		logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonTexture = new Texture(Gdx.files.internal("buttonDefault.png"));
		buttonTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonStartTexture = new Texture(Gdx.files.internal("buttonStart.png"));
		buttonStartTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonTutorialTexture = new Texture(Gdx.files.internal("buttonTutorial.png"));
		buttonTutorialTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		toggleButtonSoundOffTexture = new Texture(Gdx.files.internal("toggleSoundOff.png"));
		toggleButtonSoundOffTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		toggleButtonSoundOnTexture = new Texture(Gdx.files.internal("toggleSoundOn.png"));
		toggleButtonSoundOnTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonLoadGameTexture = new Texture(Gdx.files.internal("buttonLoadGame.png"));
		buttonLoadGameTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonNewGameTexture = new Texture(Gdx.files.internal("buttonNewGame.png"));
		buttonNewGameTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonLoadTexture = new Texture(Gdx.files.internal("buttonLoad.png"));
		buttonLoadTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonNextTexture = new Texture(Gdx.files.internal("buttonNext.png"));
		buttonNextTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonReadyTexture = new Texture(Gdx.files.internal("buttonReady.png"));
		buttonReadyTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonStartSessionTexture = new Texture(Gdx.files.internal("buttonStartSession.png"));
		buttonStartSessionTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerTexture = new Texture(Gdx.files.internal("vehicles/type1-red.png"));
		playerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		opponentTexture = new Texture(Gdx.files.internal("vehicles/type1-blue.png"));
		opponentTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonBackTexture = new Texture(Gdx.files.internal("buttonBack.png"));
		buttonBackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonRefreshTexture = new Texture(Gdx.files.internal("buttonRefresh.png"));
		buttonRefreshTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		menuTrackTexture = new Texture(Gdx.files.internal("menuTrack.png"));
		menuTrackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		menuVehicleTexture = new Texture(Gdx.files.internal("menuVehicle.png"));
		menuVehicleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		menuReadyTexture = new Texture(Gdx.files.internal("menuReady.png"));
		menuReadyTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerRedTexture = new Texture(Gdx.files.internal("players/playerRed.png"));
		playerRedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerBlueTexture = new Texture(Gdx.files.internal("players/playerBlue.png"));
		playerBlueTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerGreenTexture = new Texture(Gdx.files.internal("players/playerGreen.png"));
		playerGreenTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerYellowTexture = new Texture(Gdx.files.internal("players/playerYellow.png"));
		playerYellowTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerNATexture = new Texture(Gdx.files.internal("players/playerNA.png"));
		playerNATexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerAITexture = new Texture(Gdx.files.internal("players/playerAI.png"));
		playerAITexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// load regions
		logo = new TextureRegion(logoTexture);
		logo.flip(false, true);
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
		buttonReady = new TextureRegion(buttonReadyTexture);
		buttonReady.flip(false, true);
		buttonStartSession = new TextureRegion(buttonStartSessionTexture);
		buttonStartSession.flip(false, true);
		player = new TextureRegion(playerTexture);
		player.flip(false, true);
		opponent = new TextureRegion(opponentTexture);
		opponent.flip(false, true);
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
		
		// load fonts
		font = new BitmapFont(Gdx.files.internal("fonts/kenvector.fnt"), true);
		
		// load preferences/settings
		loadSettings();
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
	}
	
}
