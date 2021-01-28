package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 1000;
		//config.height = 1000;
		config.width = MyGdxGame.WIDTH;
        config.height = MyGdxGame.HIEGHT;
		config.title = "Bridge Boy";
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
