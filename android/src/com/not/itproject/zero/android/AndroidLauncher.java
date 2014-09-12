package com.not.itproject.zero.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.not.itproject.zero.ProjectZero;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		// enable immersive for Android 4.4
		config.useImmersiveMode = true;
		
		initialize(new ProjectZero(), config);
	}
}
