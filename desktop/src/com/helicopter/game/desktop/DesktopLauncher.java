package com.helicopter.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.helicopter.game.HelicopterGame;
import com.helicopter.game.HelicopterGameScene;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 1000;
		config.width = 1980;
		new LwjglApplication(new HelicopterGame(), config);
	}
}
