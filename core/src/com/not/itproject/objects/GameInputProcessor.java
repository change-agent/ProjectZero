package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.networks.NetworkMessage;
import com.not.itproject.objects.GameWorld.GameState;
import com.not.itproject.zero.ProjectZero;

public class GameInputProcessor {
	// declare variables
	GameWorld world;
	Stage stage;
	float gameWidth, gameHeight;

	Touchpad touchpad;
	TouchpadStyle touchpadStyle;
	Skin touchpadSkin;
	Drawable touchBackground, touchKnob;
	
	Button buttonOne, buttonTwo, buttonThree;
	Drawable touchButtonUp, touchButtonDown;
	ButtonStyle buttonMenuStyle;
	Drawable touchButtonMenuUp, touchButtonMenuDown;
	Skin buttonMenuSkin;
	Button buttonMenu;
	
	Label lapsCounter;
	LabelStyle labelStyle;
	Image hudBg, enginePowerBack, enginePowerFront;
	
	// main constructor
	public GameInputProcessor(GameWorld world, float gameWidth, float gameHeight) {
		// get world
		this.world = world;
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		
		// create controls
		createTouchpad();
		createButtons();
		createHub();
		
		// create a stage and add controls
		stage = new Stage(new FillViewport(gameWidth, gameHeight));
		stage.addActor(touchpad);
		stage.addActor(buttonOne);
		stage.addActor(buttonTwo);
		stage.addActor(buttonThree);
		stage.addActor(buttonMenu);
		
		// add hub variables
		stage.addActor(hudBg);
		stage.addActor(enginePowerBack);
		stage.addActor(enginePowerFront);
		stage.addActor(lapsCounter);
	}

	public void createTouchpad() {
		// create a touchpad skin
		touchpadSkin = new Skin();
		touchpadSkin
				.add("touchBackground",
						new Texture(Gdx.files
								.internal("controls/touchBackground.png")));
		touchpadSkin.add("touchKnob",
				new Texture(Gdx.files.internal("controls/touchKnob.png")));
		
		// create drawables
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");

		// create touchPad Style
		touchpadStyle = new TouchpadStyle();

		// apply the Drawables to the TouchPad Style
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;
		touchpadStyle.knob.setMinWidth(35);
		touchpadStyle.knob.setMinHeight(35);
		
		// create new TouchPad with the created style
		touchpad = new Touchpad(5, touchpadStyle);
		touchpad.setPosition(8, 8);
	}
	
	public void createButtons() {
		// create a button skin
		Skin buttonSkin = new Skin();
		buttonSkin.add("touchButtonOneUp", new Texture(Gdx.files.internal("controls/buttonAccelerate.png")));
		buttonSkin.add("touchButtonOneDown", new Texture(Gdx.files.internal("controls/buttonAccelerate.png")));
		buttonSkin.add("touchButtonTwoUp", new Texture(Gdx.files.internal("controls/buttonDecelerate.png")));
		buttonSkin.add("touchButtonTwoDown", new Texture(Gdx.files.internal("controls/buttonDecelerate.png")));
		buttonSkin.add("touchButtonThreeUp", new Texture(Gdx.files.internal("controls/buttonSpecial.png")));
		buttonSkin.add("touchButtonThreeDown", new Texture(Gdx.files.internal("controls/buttonSpecial.png")));
		buttonSkin.add("buttonMenu", new Texture(Gdx.files.internal("controls/buttonMenu.png")));

		// create button style
		ButtonStyle buttonOneStyle = new ButtonStyle();
		ButtonStyle buttonTwoStyle = new ButtonStyle();
		ButtonStyle buttonThreeStyle = new ButtonStyle();
		buttonMenuStyle = new ButtonStyle();

		/** create button one **/
		touchButtonUp = buttonSkin.getDrawable("touchButtonOneUp");
		touchButtonDown = buttonSkin.getDrawable("touchButtonOneDown");
		
		// apply the drawables to the button style
		buttonOneStyle.up = touchButtonUp;
		buttonOneStyle.down = touchButtonDown;

		// create button
		buttonOne = new Button(buttonOneStyle);

		/** create button two **/
		touchButtonUp = buttonSkin.getDrawable("touchButtonTwoUp");
		touchButtonDown = buttonSkin.getDrawable("touchButtonTwoDown");
		
		// apply the drawables to the button style
		buttonTwoStyle.up = touchButtonUp;
		buttonTwoStyle.down = touchButtonDown;

		// create button		
		buttonTwo = new Button(buttonTwoStyle);

		/** create button three **/
		touchButtonUp = buttonSkin.getDrawable("touchButtonThreeUp");
		touchButtonDown = buttonSkin.getDrawable("touchButtonThreeDown");
		
		// apply the drawables to the button style
		buttonThreeStyle.up = touchButtonUp;
		buttonThreeStyle.down = touchButtonDown;

		// create button
		buttonThree = new Button(buttonThreeStyle);

		/** create button menu **/
		touchButtonUp = buttonSkin.getDrawable("buttonMenu");
		touchButtonDown = buttonSkin.getDrawable("buttonMenu");
		
		// apply the drawables to the button style
		buttonMenuStyle.up = touchButtonUp;
		buttonMenuStyle.down = touchButtonDown;

		// create button
		buttonMenu = new Button(buttonMenuStyle);
		
		// determine button size
		buttonMenu.setSize(70, 25);
		buttonOne.setSize(35, 35);
		buttonTwo.setSize(35, 35);
		buttonThree.setSize(35, 35);
		
		// determine button locations
		buttonOne.setPosition(220, 10);
		buttonTwo.setPosition(245, 45);
		buttonThree.setPosition(270, 10);
		buttonMenu.setPosition(gameWidth/2 - buttonMenu.getWidth()/2, 10);
	}
	
	private void createHub() {
		// define label variables
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/kenvector.fnt"), false);
		font.setScale(0.5f);
		labelStyle = new LabelStyle(font, Color.BLACK);
		lapsCounter = new Label("Laps", labelStyle);
		
		// create hub background
		hudBg = new Image(AssetHandler.bannerWhite);
		hudBg.setPosition(0, gameHeight - 28);
		hudBg.setSize(125, 28);
		enginePowerBack = new Image(AssetHandler.bannerWhite);
		enginePowerBack.setSize(125, 28);
		enginePowerBack.setPosition(gameWidth - 125, gameHeight - 28);
		enginePowerFront = new Image(AssetHandler.bannerBlack);
		enginePowerFront.setSize(119, 22);
		enginePowerFront.setPosition(gameWidth - 122, gameHeight - 25);
		
		// determine text & position
		updateHub();
		lapsCounter.setPosition(10, gameHeight - 20);
	}
	
	private void updateHub() {
		if (ProjectZero.gameScreen != null && 
				ProjectZero.gameScreen.getGameWorld().getGameState() == GameState.RUNNING) {
			// get lap details
			String lapDetails = "Laps: " + 
					ProjectZero.gameScreen.getGameWorld().getPlayer().getLapNum() + " / " +
					GameVariables.GAME_LAPS;
			lapsCounter.setText(lapDetails);
			hudBg.setVisible(true);
		} else {
			lapsCounter.setText("");
			hudBg.setVisible(false);
		}
		
		// engine power usage
		float maxEnginePower = 119;
		if (world.getPlayerByID(AssetHandler.getPlayerID()) != null) {
			float enginePowerRatio = Math.abs(world.getPlayerByID(
					AssetHandler.getPlayerID()).getCar().getEnginePower()) 
					/ GameVariables.MAX_HORSEPOWER;
			enginePowerFront.setSize(maxEnginePower * enginePowerRatio, 22);
		}
	}

	public void update(float delta) {
		// only update controls if game is running
		if (world.isRunning()) {
			
			// get controls
			if (touchpad.getKnobPercentX() < 0 || touchpad.getKnobPercentX() > 0) {
				float steerAngle = touchpad.getKnobPercentX();
				world.getPlayer().getCar().setSteeringAngle(steerAngle);
			}
			else {
				world.getPlayer().getCar().zeroSteeringAngle();
			}
			
	
			// Power on engine if pressed else power off engine
			if (buttonTwo.isPressed()) {
				world.getPlayer().getCar().powerOnEngine(1);
			}
			else if (buttonOne.isPressed()) {
				world.getPlayer().getCar().powerOnEngine(-1);
			}
			else{
				world.getPlayer().getCar().powerOffEngine();
			}
			
			// Activate power or menu
			if (buttonThree.isPressed()) {
				world.getPlayer().flagUsePower();
			}
			if (buttonMenu.isPressed()) {
				// pause game
				world.pauseGame();
			}
			
			/*// Use this to test with desktop
			if (Gdx.input.isKeyPressed(Keys.A)) {
				float steerAngle = -1.0f;
				world.getPlayer().getCar().setSteeringAngle(steerAngle);
			}
			else if (Gdx.input.isKeyPressed(Keys.D)) {
				float steerAngle = 1.0f;
				world.getPlayer().getCar().setSteeringAngle(steerAngle);
			}
			else{
				world.getPlayer().getCar().zeroSteeringAngle();
			}*/
			
			// Power on engine if pressed else power off engine
			if (Gdx.input.isKeyPressed(Keys.W)) {
				world.getPlayer().getCar().powerOnEngine(-1);
			}
			else if (Gdx.input.isKeyPressed(Keys.S)) {
				world.getPlayer().getCar().powerOnEngine(1);
			}
			else{
				world.getPlayer().getCar().powerOffEngine();
			}
			
			// debug only
			if (GameVariables.DEBUG) {
				if (Gdx.input.isKeyPressed(Keys.NUM_0)) {
					// increase game lap
					world.getPlayerByID(AssetHandler.getPlayerID()).getCar().getOwner().incrementLap();
				}
			}
			
			// Activate power or menu
			if (buttonTwo.isPressed()) {
				world.getPlayer().flagUsePower();
			}
			if (buttonMenu.isPressed()) {				
				// pause game
				world.pauseGame();
				
				// send network message
				if (NetworkHandler.isHost()) {
					// as host - send information about game state
					NetworkHandler.getNetworkServer().sendGameStateInformation(NetworkMessage.GameState.PAUSE);
				} else if (NetworkHandler.isClient()) {
					// as client - send information about game state
					NetworkHandler.getNetworkClient().sendGameStateInformation(NetworkMessage.GameState.PAUSE);
				} 
			}
		}
		
		// update stage
		updateHub();
		stage.act(delta);
	}

	public void render(float delta) {
		// draw controls and hub
		stage.draw();
	}

	public Stage getStage() {
		return stage;
	}

	public void dispose() {

	}
}
