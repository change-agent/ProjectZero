package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.not.itproject.handlers.NetworkHandler;

public class GameInputProcessor {
	// declare variables
	GameWorld world;
	Stage stage;
	float gameWidth, gameHeight;

	Touchpad touchpad;
	TouchpadStyle touchpadStyle;
	Skin touchpadSkin;
	Drawable touchBackground;
	Drawable touchKnob;
	
	Button buttonOne;
	Button buttonTwo;
	Button buttonThree;
	ButtonStyle buttonStyle;
	Skin buttonSkin;
	Drawable touchButtonUp;
	Drawable touchButtonDown;

	ButtonStyle buttonMenuStyle;
	Skin buttonMenuSkin;
	Drawable touchButtonMenuUp;
	Drawable touchButtonMenuDown;

	Button buttonMenu;
	
	// main constructor
	public GameInputProcessor(GameWorld world, float gameWidth, float gameHeight) {
		// get world
		this.world = world;
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		
		// create controls
		createTouchpad();
		createButtons();
		
		// create a stage and add controls
		stage = new Stage(new FillViewport(gameWidth, gameHeight));
		stage.addActor(touchpad);
		stage.addActor(buttonOne);
		stage.addActor(buttonTwo);
		stage.addActor(buttonThree);
		stage.addActor(buttonMenu);
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

		// create new TouchPad with the created style
		touchpad = new Touchpad(5, touchpadStyle);
		touchpad.setPosition(8, 8);

	}
	
	public void createButtons() {
		// create a button skin
		buttonSkin = new Skin();
		buttonSkin.add("touchButtonOneUp", new Texture(Gdx.files.internal("controls/touchButton.png")));
		buttonSkin.add("touchButtonTwoUp", new Texture(Gdx.files.internal("controls/touchButton.png")));
		buttonSkin.add("touchButtonThreeUp", new Texture(Gdx.files.internal("controls/touchButton.png")));
		buttonSkin.add("touchButtonOneDown", new Texture(Gdx.files.internal("controls/touchButton.png")));
		buttonSkin.add("touchButtonTwoDown", new Texture(Gdx.files.internal("controls/touchButton.png")));
		buttonSkin.add("touchButtonThreeDown", new Texture(Gdx.files.internal("controls/touchButton.png")));
		buttonSkin.add("buttonMenu", new Texture(Gdx.files.internal("controls/buttonMenu.png")));

		// create button style
		buttonStyle = new ButtonStyle();
		buttonMenuStyle = new ButtonStyle();

		/** create button one **/
		touchButtonUp = buttonSkin.getDrawable("touchButtonOneUp");
		touchButtonDown = buttonSkin.getDrawable("touchButtonOneDown");
		
		// apply the drawables to the button style
		buttonStyle.up = touchButtonUp;
		buttonStyle.down = touchButtonDown;

		// create button
		buttonOne = new Button(buttonStyle);

		/** create button two **/
		touchButtonUp = buttonSkin.getDrawable("touchButtonTwoUp");
		touchButtonDown = buttonSkin.getDrawable("touchButtonTwoDown");
		
		// apply the drawables to the button style
		buttonStyle.up = touchButtonUp;
		buttonStyle.down = touchButtonDown;

		// create button		
		buttonTwo = new Button(buttonStyle);

		/** create button three **/
		touchButtonUp = buttonSkin.getDrawable("touchButtonThreeUp");
		touchButtonDown = buttonSkin.getDrawable("touchButtonThreeDown");
		
		// apply the drawables to the button style
		buttonStyle.up = touchButtonUp;
		buttonStyle.down = touchButtonDown;

		// create button
		buttonThree = new Button(buttonStyle);

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
		
		// determine button locations
		buttonOne.setPosition(220, 10);
		buttonTwo.setPosition(245, 45);
		buttonThree.setPosition(270, 10);
		buttonMenu.setPosition(gameWidth/2 - buttonMenu.getWidth()/2, 10);
	}

	public void update(float delta) {
		// get controls
		if (touchpad.getKnobPercentX() < 0 || touchpad.getKnobPercentX() > 0) {
			float steerAngle = touchpad.getKnobPercentX();
			world.getPlayer().getCar().setSteeringAngle(steerAngle);
		}
		else{
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
		
		// Activate power or menu
		if (buttonTwo.isPressed()) {
			world.getPlayer().flagUsePower();
		}
		if (buttonMenu.isPressed()) {
			// pause game
			world.pauseGame();
		}

		// update stage
		stage.act(delta);
	}

	public void render(float delta) {
		stage.draw();
	}

	public Stage getStage() {
		return stage;
	}

	public void dispose() {

	}
}
