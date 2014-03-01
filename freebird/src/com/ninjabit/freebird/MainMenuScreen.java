package com.ninjabit.freebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen 
{

	final Freebird game;
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture titleTexture;

	Sound select;

	Music featherMusic;

	Texture playButtonTexture;
	Rectangle playButtonRectangle;

	Texture creditsButtonTexture;
	Rectangle creditsButtonRectangle;

	Texture mainMenuButtonTexture;
	Rectangle mainMenuButtonRectangle;

	Texture oceanTexture;
	Sprite oceanSprite;
	float scrollTimer;
	Vector3 touchPos;
	boolean showingCredits;

	public MainMenuScreen(final Freebird game)
	{
		this.game = game;
		Texture.setEnforcePotImages(false);
		this.batch = game.batch;

		touchPos = new Vector3();
		showingCredits = false;

		select = Gdx.audio.newSound(Gdx.files.internal("Sound/select.wav"));

		oceanTexture = new Texture("Artwork/Background/backgroundAtlas.png");
		oceanTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		oceanSprite = new Sprite(oceanTexture, 0, 0, 800, 480);

		playButtonTexture = new Texture("Artwork/Icons/playButton.png");
		playButtonRectangle = new Rectangle(400, 240, playButtonTexture.getWidth(), playButtonTexture.getHeight());

		mainMenuButtonTexture = new Texture("Artwork/Icons/mainMenu.png");
		mainMenuButtonRectangle = new Rectangle(400, 100, 
				mainMenuButtonTexture.getWidth(), mainMenuButtonTexture.getHeight());

		creditsButtonTexture = new Texture("Artwork/Icons/Credits.png");
		creditsButtonRectangle = new Rectangle(400, 240, creditsButtonTexture.getWidth(), creditsButtonTexture.getHeight());

		titleTexture =  new Texture(Gdx.files.internal("Artwork/Text/Title.png"));

		// load the drop sound effect and the rain background "music"
		featherMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/Feathers.ogg"));

		// start the playback of the background music immediately
		featherMusic.setLooping(true);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0,0,0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isTouched())
		{
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		if(!showingCredits)
		{

			batch.begin();

			scrollTimer+=Gdx.graphics.getDeltaTime();
			if(scrollTimer>1.0f)
				scrollTimer = 0.0f;

			oceanSprite.setU(scrollTimer);
			oceanSprite.setU2(scrollTimer+1);

			camera.update();
			batch.setProjectionMatrix(camera.combined);
			batch.draw(oceanSprite, 0, 0);
			batch.draw(titleTexture, 400 - titleTexture.getWidth()/2, 370 - titleTexture.getHeight()/2);

			batch.draw(playButtonTexture, 400 - playButtonTexture.getWidth()/2, 150);
			playButtonRectangle.set(400 - playButtonTexture.getWidth()/2, 150, 
					playButtonTexture.getWidth(), playButtonTexture.getHeight());

			batch.draw(creditsButtonTexture, 400 - creditsButtonTexture.getWidth()/2, 60);
			creditsButtonRectangle.set(400 - creditsButtonTexture.getWidth()/2, 60, 
					creditsButtonTexture.getWidth(), creditsButtonTexture.getHeight());

			batch.end();
		}

		else
		{
			batch.begin();

			scrollTimer+=Gdx.graphics.getDeltaTime();
			if(scrollTimer>1.0f)
				scrollTimer = 0.0f;

			oceanSprite.setU(scrollTimer);
			oceanSprite.setU2(scrollTimer+1);

			camera.update();
			batch.setProjectionMatrix(camera.combined);
			batch.draw(oceanSprite, 0, 0);
			batch.draw(titleTexture, 400 - titleTexture.getWidth()/2, 370 - titleTexture.getHeight()/2);

			game.font.draw(batch, "Programmer: Bilal @ninjabit6", 200, 250);
			game.font.draw(batch, "Art/Sound: Ted soundcloud.com/ttvgm", 200, 200);

			batch.draw(mainMenuButtonTexture, 400 - mainMenuButtonTexture.getWidth()/2, 75);
			mainMenuButtonRectangle.set(400 - mainMenuButtonTexture.getWidth()/2, 75, 
					mainMenuButtonTexture.getWidth(), mainMenuButtonTexture.getHeight());


			batch.end();
		}

		if(playButtonRectangle.contains(touchPos.x, touchPos.y) && Gdx.input.justTouched() && !showingCredits)
		{
			select.play();
			game.setScreen(game.gameScreen);
		}

		else if(creditsButtonRectangle.contains(touchPos.x, touchPos.y) && Gdx.input.justTouched() && !showingCredits)
		{
			select.play();
			showingCredits = true;
		}

		else if(mainMenuButtonRectangle.contains(touchPos.x, touchPos.y) && Gdx.input.justTouched() && showingCredits)
		{
			select.play();
			showingCredits = false;
		}



	}

	@Override
	public void resize(int width, int height) 
	{


	}

	@Override
	public void show() {
		featherMusic.play();

	}

	@Override
	public void hide() {


	}

	@Override
	public void pause() {


	}

	@Override
	public void resume() {


	}

	@Override
	public void dispose() 
	{

	}

}
