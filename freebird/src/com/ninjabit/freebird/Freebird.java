package com.ninjabit.freebird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Freebird extends Game
{
	SpriteBatch batch;
	BitmapFont font;
	
	MainMenuScreen mainMenuScreen;
	GameScreen gameScreen;
	GameOverScreen gameOverScreen;

	public void create()
	{
		batch = new SpriteBatch();
		//use libgdx's default Arial font.
		font = new BitmapFont();
		
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		gameOverScreen = new GameOverScreen(this);
		
		this.setScreen(mainMenuScreen);

	}
	public void render()
	{
		super.render(); //important
	}

	public void dispose()
	{
		batch.dispose();
		font.dispose();
	}

}

