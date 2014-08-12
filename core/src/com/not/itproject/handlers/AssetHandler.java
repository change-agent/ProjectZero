package com.not.itproject.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetHandler {
	// declare all assets
	public static Texture logoTexture;
	public static TextureRegion logo;	
	
	public static void load() {
		/*** load all assets ***/
		
		// load textures
		logoTexture = new Texture(Gdx.files.internal("project-zero.png"));
		logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// load regions
		logo = new TextureRegion(logoTexture);
		logo.flip(false, true);
	}
	
	public static void release() {
		/*** release all assets ***/
		logoTexture.dispose();
	}
	
}
