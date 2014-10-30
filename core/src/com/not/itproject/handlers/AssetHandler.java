package com.not.itproject.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.not.itproject.sessions.GameSession;
import com.not.itproject.zero.ProjectZero;

public class AssetHandler {
	// declare all assets
	public static Texture logoTexture, buttonTexture, buttonStartTexture, buttonTutorialTexture,
							toggleButtonSoundOffTexture, toggleButtonSoundOnTexture,
							buttonLoadGameTexture, buttonNewGameTexture, buttonLoadTexture,
							buttonNextTexture, buttonReadyTexture, buttonStartSessionTexture,
							buttonBackTexture, buttonRefreshTexture, powerUpTexture, obstacleTexture,
							buttonResumeGameTexture, buttonOptionTexture, buttonSaveExitTexture,
							buttonOkayTexture, buttonYesTexture, buttonNoTexture,
							buttonNavigatePrevTexture, buttonNavigateNextTexture;
	public static Texture carRedTexture, carBlueTexture, carGreenTexture, carYellowTexture;
	public static TextureRegion logo, button, buttonStart, buttonTutorial,
							toggleButtonSoundOff, toggleButtonSoundOn,
							buttonLoadGame, buttonNewGame, buttonLoad,
							buttonNext, buttonReady, buttonStartSession,
							buttonBack, buttonRefresh, powerUp, obstacle,
							carRed, carBlue, carGreen, carYellow,
							buttonOption, buttonResumeGame, buttonSaveExit,
							buttonOkay, buttonYes, buttonNo,
							buttonNavigatePrev, buttonNavigateNext;
	
	public static Texture menuTrackTexture, menuVehicleTexture, menuReadyTexture,
							playerRedTexture, playerBlueTexture,
							playerGreenTexture, playerYellowTexture,
							playerNATexture, playerAITexture, playerReadyTexture,
							playerWaitTexture, mapPreviewTexture,
							bannerBlackTexture, bannerWhiteTexture;
	public static TextureRegion menuTrack, menuVehicle, menuReady, mapPreview,
							playerRed, playerBlue,
							playerGreen, playerYellow,
							playerNA, playerAI, playerReady, playerWait,
							bannerBlack, bannerWhite;
	
	public static Texture navigateUpTexture, navigateDownTexture, navigateLeftTexture, navigateRightTexture;
	public static TextureRegion navigateUp, navigateDown, navigateLeft, navigateRight;
	
	public static Texture backgroundUniversalTexture, backgroundErrorTexture, 
							backgroundWinnerTexture, backgroundEndingTexture;
	public static TextureRegion backgroundUniversal, backgroundError, backgroundWinner, backgroundEnding;
	
	public static Texture helpGameplayTexture, helpSessionsTexture, helpTrackSelectionTexture;
	public static TextureRegion helpGameplay, helpSessions, helpTrackSelection;
	
	public static BitmapFont font;
	public static Preferences prefs;
	public static List<Music> playlist = new ArrayList<Music>();;
	public static Music currentSong;
	public static Music music;
	public static Sound crash;
	public static Sound buttonClick;
	public static Sound powerUpCollected;
	public static Sound usePower;
	public static Sound engine;
	
	private static Map<Integer, GameSession> gameSessions;
	public static String[] maps = new String[]{"maps/test.tmx"};
		
	public static void load() {
		/*** load all assets ***/

		// load preferences/settings
		loadSettings();
		
		// load components
		loadNavigation();
		loadBackgrounds();
		loadVehicles();
		loadObjects();
		loadUncategorised();
		loadSounds();
		
		// load fonts
		font = new BitmapFont(Gdx.files.internal("fonts/kenvector.fnt"), true);
		
		// load all saved game sessions
		loadAllGameSessions();
	}
	
	private static void loadBackgrounds() {
		// load backgrounds
		backgroundUniversalTexture = new Texture(Gdx.files.internal("backgrounds/backgroundUniversal.png"));
		backgroundUniversalTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundErrorTexture = new Texture(Gdx.files.internal("backgrounds/backgroundError.png"));
		backgroundErrorTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundWinnerTexture = new Texture(Gdx.files.internal("backgrounds/backgroundWinner.png"));
		backgroundWinnerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundEndingTexture = new Texture(Gdx.files.internal("backgrounds/backgroundEnding.png"));
		backgroundEndingTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		helpGameplayTexture = new Texture(Gdx.files.internal("tutorials/helpGameplay.png"));
		helpGameplayTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		helpSessionsTexture = new Texture(Gdx.files.internal("tutorials/helpSessions.png"));
		helpSessionsTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		helpTrackSelectionTexture = new Texture(Gdx.files.internal("tutorials/helpTrackSelection.png"));
		helpTrackSelectionTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// load regions
		backgroundUniversal = new TextureRegion(backgroundUniversalTexture);
		backgroundUniversal.flip(false, true);
		
		backgroundError = new TextureRegion(backgroundErrorTexture);
		backgroundError.flip(false, true);
		
		backgroundWinner = new TextureRegion(backgroundWinnerTexture);
		backgroundWinner.flip(false, true);
		
		backgroundEnding = new TextureRegion(backgroundEndingTexture);
		backgroundEnding.flip(false, true);
		
		helpGameplay = new TextureRegion(helpGameplayTexture);
		helpGameplay.flip(false, true);
		
		helpSessions = new TextureRegion(helpSessionsTexture);
		helpSessions.flip(false, true);
		
		helpTrackSelection = new TextureRegion(helpTrackSelectionTexture);
		helpTrackSelection.flip(false, true);
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
		buttonNavigatePrevTexture = new Texture(Gdx.files.internal("navigation/buttonNavigatePrev.png"));
		buttonNavigatePrevTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonNavigateNextTexture = new Texture(Gdx.files.internal("navigation/buttonNavigateNext.png"));
		buttonNavigateNextTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
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
		buttonNavigatePrev = new TextureRegion(buttonNavigatePrevTexture);
		buttonNavigatePrev.flip(false, true);
		buttonNavigateNext = new TextureRegion(buttonNavigateNextTexture);
		buttonNavigateNext.flip(false, true);
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
		playerWaitTexture = new Texture(Gdx.files.internal("other/playerWait.png"));
		playerWaitTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bannerBlackTexture = new Texture(Gdx.files.internal("other/banner-black.png"));
		bannerBlackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bannerWhiteTexture = new Texture(Gdx.files.internal("other/banner-white.png"));
		bannerWhiteTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mapPreviewTexture = new Texture(Gdx.files.internal("maps/map-preview.png"));
		mapPreviewTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

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
		playerWait = new TextureRegion(playerWaitTexture);
		playerWait.flip(false, true);
		bannerBlack = new TextureRegion(bannerBlackTexture);
		bannerBlack.flip(false, true);
		bannerWhite = new TextureRegion(bannerWhiteTexture);
		bannerWhite.flip(false, true);
		mapPreview = new TextureRegion(mapPreviewTexture);
		mapPreview.flip(false, true);
	}

	
	public static void loadSettings() {
		/*** load all preferences/settings ***/
		prefs = Gdx.app.getPreferences("ProjectZero");
				
		// initialise settings
		if (!prefs.contains("soundMute")) {
			// default mute = false (sound is on)
			setSoundMute(false);
		}
		if (!prefs.contains("playerID")) {
			// generate new playerID
			prefs.putString("playerID", UUID.randomUUID().toString());
			prefs.flush();
		}
	}

	// --------------------- Sound loading and handling ---------------------- //
	public static void loadSounds() {
		playlist.add(Gdx.audio.newMusic(Gdx.files.internal("sounds/theme.wav")));
		playlist.add(Gdx.audio.newMusic(Gdx.files.internal("sounds/race.wav")));
		playlist.add(Gdx.audio.newMusic(Gdx.files.internal("sounds/Mars.wav")));
		playlist.add(Gdx.audio.newMusic(Gdx.files.internal("sounds/Mercury.wav")));
		playlist.add(Gdx.audio.newMusic(Gdx.files.internal("sounds/map_adv.wav")));
		playlist.add(Gdx.audio.newMusic(Gdx.files.internal("sounds/Venus.wav")));
		playlist.add(Gdx.audio.newMusic(Gdx.files.internal("sounds/map_basic.wav")));
		
		crash = Gdx.audio.newSound(Gdx.files.internal("sounds/crash.wav"));
		powerUpCollected = Gdx.audio.newSound(Gdx.files.internal("sounds/powerUp.wav"));
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonclick.wav"));
		usePower = Gdx.audio.newSound(Gdx.files.internal("sounds/usePower.wav"));
		engine = Gdx.audio.newSound(Gdx.files.internal("sounds/engine.wav"));
		
		currentSong = playlist.get(0);
		if (!getSoundMute()) {
			currentSong.play();
		}
	}
	
	public static void getNextTrack()
	{
		if(getSoundMute() == true) { return; }
		currentSong.stop();
		int index = playlist.indexOf(currentSong);
		int next = MathUtils.clamp(index + 1, 0, playlist.size() - 1);
		currentSong = playlist.get(next);
		if (!getSoundMute()) {
			currentSong.play();
		}
	}

	public static void playMusic()
	{
		if(getSoundMute() == true) { return; }
		if(!currentSong.isPlaying())
		if(!currentSong.isPlaying() && !getSoundMute())
		{
			getNextTrack();
			currentSong.play();
		}
	}

	public static void stopMusic()
	{
		if(currentSong != null)
		{
			currentSong.stop();
		}
	}

	public static void playSound(String sound)
	{
		// sound is not muted
		if(getSoundMute() == false) { 
			if(sound.compareToIgnoreCase("crash") == 0)
			{
				crash.play();
			}
			if(sound.compareToIgnoreCase("powerUpCollected") == 0)
			{
				powerUpCollected.play();
			}
			if(sound.compareToIgnoreCase("buttonClick") == 0)
			{
				buttonClick.play();
			}
			if(sound.compareToIgnoreCase("usePower") == 0)
			{
				usePower.play();
			}
		}
	}

	public static void setSoundMute(boolean value) {
		// set sound mute on/off
		prefs.putBoolean("soundMute", value);
		prefs.flush();
		if (value == true)
		{
			stopMusic();
		}
		else
		{
			playMusic();
		}
	}

	public static boolean getSoundMute() {
		// get sound mute status
		return prefs.getBoolean("soundMute");
	}
	// --------------------- end Sound loading and handling ---------------------- //
	
	public static void loadAllGameSessions() {
		// initialise game sessions
		gameSessions = new HashMap<Integer, GameSession>();
		
		if (prefs.contains("gameSession")) {
			// load session into list
			Json json = new Json();
			json.setTypeName(null);
			json.setUsePrototypes(false);
			json.setIgnoreUnknownFields(true);
			json.setOutputType(OutputType.json);
			
			// store session
			GameSession session = json.fromJson(GameSession.class, prefs.getString("gameSession"));
			gameSessions.put(0, session);
		}
	}
	
	public static Map<Integer, GameSession> getSavedGameSessions() {
		return gameSessions;
	}
	
	public static void saveGameSession() {
		// save session
		Json json = new Json();
		json.setTypeName(null);
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(OutputType.json);
		
		// save to preferences
		ProjectZero.log("Session Data: " + json.toJson(ProjectZero.gameSession));
		prefs.putString("gameSession", json.toJson(ProjectZero.gameSession));
		prefs.flush();
	}
	
	public static void loadGameSession(int index) {
		// load session		
		ProjectZero.gameSession.loadGameSession(gameSessions.get(index));
	}
	
	public static Map<Integer, GameSession> getGameSessions() {
		return gameSessions;
	}

	// debugging playerID
	public static String getPlayerID() {
		return prefs.getString("playerID");
	}
	
	// vibrate device
	public static void vibrate(int time) {
		// vibrate in milliseconds
		Gdx.input.vibrate(time);
//		ProjectZero.log("Vibrate Device");
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
