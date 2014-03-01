package com.ninjabit.freebird;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Freebird";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
		cfg.addIcon("Artwork/Text/Title_128.png", Files.FileType.Internal);
		cfg.addIcon("Artwork/Text/Title_32.png", Files.FileType.Internal);
		cfg.addIcon("Artwork/Text/Title_16.png", Files.FileType.Internal);
		
		new LwjglApplication(new Freebird(), cfg);
	}
}
