package com.not.itproject.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetHandler {
	// declare all assets
	public static Texture logoTexture, buttonTexture, buttonStartTexture, buttonTutorialTexture,
							toggleButtonSoundOffTexture, toggleButtonSoundOnTexture,
							buttonLoadGameTexture, buttonNewGameTexture, buttonLoadTexture,
							buttonNextTexture, buttonReadyTexture, buttonStartSessionTexture;
	public static TextureRegion logo, button, buttonStart, buttonTutorial,
							toggleButtonSoundOff, toggleButtonSoundOn,
							buttonLoadGame, buttonNewGame, buttonLoad,
							buttonNext, buttonReady, buttonStartSession;
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
 	
	public static void release() {
		/*** release all assets ***/
		logoTexture.dispose();
		buttonTexture.dispose(); buttonStartTexture.dispose(); buttonTutorialTexture.dispose();
		toggleButtonSoundOffTexture.dispose(); toggleButtonSoundOnTexture.dispose();
		buttonLoadGameTexture.dispose(); buttonNewGameTexture.dispose(); buttonLoadTexture.dispose();
	}
	
}
