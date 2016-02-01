package com.jam.demon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jam.demon.DemonSummoner;


public class DesktopLauncher
{
	public static void main(final String[] arg)
	{
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Demon Summoner";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new DemonSummoner(), config);
	}
}
