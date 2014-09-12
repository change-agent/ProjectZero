package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;


public class GameInputProcessor {
	// declare variables
	GameWorld world;
	Stage stage;
	
	Touchpad touchpad;
	TouchpadStyle touchpadStyle;
	Skin touchpadSkin;
	Drawable touchBackground;
	Drawable touchKnob;
	Vector2 position;
	
	// main constructor
	public GameInputProcessor(GameWorld world) {
		// get world
		this.world = world;
		
		// create a touchpad skin    
		touchpadSkin = new Skin();
		
        // set background image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        
        // set knob image
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        
        // create TouchPad Style
        touchpadStyle = new TouchpadStyle();
        
        // create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        
        // apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        
        // create new TouchPad with the created style
        touchpad = new Touchpad(5, touchpadStyle);
        touchpad.setPosition(30, 30);
        touchpad.setSize(150, 150);
        
        //Create a Stage and add TouchPad
//        stage = new Stage(new StretchViewport(gameWidth, gameHeight), batch);
        stage = new Stage();
//        stage.addActor(touchpad);
        
        position = new Vector2(0, 0);
	}
	
	public void update(float delta) {
        //Move blockSprite with TouchPad
//        blockSprite.setX(blockSprite.getX() + touchpad.getKnobPercentX()*blockSpeed);
//        blockSprite.setY(blockSprite.getY() + touchpad.getKnobPercentY()*blockSpeed);
		
//		if (touchpad.getKnobPercentX() > 0) {
//			world.getPlayer().moveLeft(100 * touchpad.getKnobPercentX(), delta);
//		} else if (touchpad.getKnobPercentX() < 0) {
//			world.getPlayer().moveRight(100 * touchpad.getKnobPercentX(), delta);			
//		}
//		
		
		
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
