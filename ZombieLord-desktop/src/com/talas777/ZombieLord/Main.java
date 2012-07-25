package com.talas777.ZombieLord;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ZombieLord";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 320;
		cfg.fullscreen = false;
		
		new LwjglApplication(new ZombieLord(), cfg);
	}
}
