package com.not.itproject.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetHandler {
	// declare all assets
	public static Texture logoTexture, buttonTexture;
	public static TextureRegion logo, button;	
	public static Preferences prefs;
	
	public static void load() {
		/*** load all assets ***/
		
		// load textures
		logoTexture = new Texture(Gdx.files.internal("project-zero.png"));
		logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonTexture = new Texture(Gdx.files.internal("buttonDefault.png"));
		buttonTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// load regions
		logo = new TextureRegion(logoTexture);
		logo.flip(false, true);
		button = new TextureRegion(buttonTexture);
		button.flip(false, true);
		
		// load preferences/settings
		prefs = Gdx.app.getPreferences("ProjectZero");
	}
	
	public static void release() {
		/*** release all assets ***/
		logoTexture.dispose();
	}
	
}
