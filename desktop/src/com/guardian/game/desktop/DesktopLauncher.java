package com.guardian.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.guardian.game.GuardianGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1080/2;
		config.height = 1922/2;
//		config.width = 1538/4;
//		config.height = 2560/4;
		config.resizable = true;
		new LwjglApplication(new GuardianGame(), config);
	}
}
