package com.not.itproject.zero.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.not.itproject.zero.ProjectZero;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		// set configurations
		config.width = 960;
		config.height = 540;
		
		new LwjglApplication(new ProjectZero(), config);
	}
}
