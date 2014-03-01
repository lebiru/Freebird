package com.ninjabit.freebird;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen 
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	final int RESPAWN_START = 1000000000;

	private FileHandle handle;
	private int highScore;
	private int currentScore;
	private int rateOfRespawn;

	Texture oceanTexture;
	Sprite oceanSprite;

	TextureAtlas frames = new TextureAtlas("Artwork/Player/freebirdAnimation.txt");
	Animation birdAnimation;
	AnimatedSprite birdAnimatedSprite;

	TextureAtlas fluffFrames = new TextureAtlas("Artwork/Obstacles/fluffAnimation.txt");
	Animation fluffAnimation;
	AnimatedSprite fluffAnimatedSprite;

	TextureAtlas fluff2Frames = new TextureAtlas("Artwork/Obstacles/fluff2Animation.txt");
	Animation fluff2Animation;
	AnimatedSprite fluff2AnimatedSprite;
	
	Texture gameOverTexture;
	
	Texture playButtonTexture;
	Rectangle playButtonRectangle;

	Texture mainMenuButtonTexture;
	Rectangle mainMenuButtonRectangle;
	
	final Freebird game;

	float fluffWidth;
	float fluffHeight; 

	float birdWidth;
	float birdHeight;

	Sound chirp1;
	Sound chirp2;
	Sound chirp3;
	Sound chirp4;
	Sound chirp5;
	Sound die;
	Sound select;

	Rectangle birdRectangle;
	Vector3 touchPos;

	Array<Fluff> fluffs;
	Array<Sound> chripArray;
	long lastDropTime;

	float scrollTimer;
	

	Random ran;

	boolean gameOver = false;

	private void spawnFluff(AnimatedSprite tmpFluffAnimation, boolean isWhite)
	{

		fluffs.add(new Fluff(tmpFluffAnimation, isWhite));
		lastDropTime = TimeUtils.nanoTime();
	}

	public GameScreen(final Freebird game) 
	{		
		this.game = game;
		this.batch = game.batch;
		
		game.font.setColor(Color.BLACK);
		game.font.setScale(1.5f);

		ran = new Random();
		rateOfRespawn = RESPAWN_START;

		oceanTexture = new Texture("Artwork/Background/backgroundAtlas.png");
		oceanTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		oceanSprite = new Sprite(oceanTexture, 0, 0, 800, 480);

		//Check to see if existing "score" file exists.
		if(Gdx.files.internal("data/score").exists())
		{
			handle = Gdx.files.internal("data/score");
			highScore = Integer.parseInt(handle.readString());
		}

		gameOverTexture = new Texture("Artwork/Icons/gameOver.png");
		
		chirp1 = Gdx.audio.newSound(Gdx.files.internal("Sound/chirp1.wav"));
		chirp2 = Gdx.audio.newSound(Gdx.files.internal("Sound/chirp2.wav"));
		chirp3 = Gdx.audio.newSound(Gdx.files.internal("Sound/chirp3.wav"));
		chirp4 = Gdx.audio.newSound(Gdx.files.internal("Sound/chirp4.wav"));
		chirp5 = Gdx.audio.newSound(Gdx.files.internal("Sound/chirp5.wav"));
		
		chripArray = new Array<Sound>();
		
		chripArray.add(chirp1);
		chripArray.add(chirp2);
		chripArray.add(chirp3);
		chripArray.add(chirp4);
		chripArray.add(chirp5);
		
		select = Gdx.audio.newSound(Gdx.files.internal("Sound/select.wav"));
		die = Gdx.audio.newSound(Gdx.files.internal("Sound/die.wav"));
		
		playButtonTexture = game.mainMenuScreen.playButtonTexture;
		playButtonRectangle = game.mainMenuScreen.playButtonRectangle;
		
		mainMenuButtonTexture = new Texture("Artwork/Icons/mainMenu.png");
		mainMenuButtonRectangle = new Rectangle(400, 100, 
				mainMenuButtonTexture.getWidth(), mainMenuButtonTexture.getHeight());
		
		//freebird animation
		birdAnimation = new Animation(0.2f, frames.getRegions());
		birdAnimation.setPlayMode(Animation.LOOP);
		birdAnimatedSprite = new AnimatedSprite(birdAnimation);

		birdWidth = birdAnimatedSprite.getWidth()/2.5f;
		birdHeight = birdAnimatedSprite.getHeight()/1.5f;

		//fluff animation
		fluffAnimation = new Animation(0.1f, fluffFrames.getRegions());
		fluffAnimation.setPlayMode(Animation.LOOP);
		fluffAnimatedSprite = new AnimatedSprite(fluffAnimation);

		//fluff 2 animation
		//fluff animation
		fluff2Animation = new Animation(0.1f, fluff2Frames.getRegions());
		fluff2Animation.setPlayMode(Animation.LOOP);
		fluff2AnimatedSprite = new AnimatedSprite(fluff2Animation);

		fluffWidth = fluffAnimatedSprite.getWidth();
		fluffHeight = fluffAnimatedSprite.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);


		birdRectangle = new Rectangle();
		birdRectangle.x = 20;
		birdRectangle.y = 480 / 2 - birdHeight / 2;
		birdRectangle.width = birdWidth;
		birdRectangle.height = birdHeight;

		touchPos = new Vector3();

		fluffs = new Array<Fluff>();
		spawnFluff(new AnimatedSprite(fluffAnimation), true);


	}



	@Override
	public void render(float delta) 
	{		
		//RENDER UPDATE
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if(!gameOver)
		{

			camera.update();
			batch.setProjectionMatrix(camera.combined);

			batch.begin();
			drawScrollingBackground();
			birdAnimatedSprite.setScale(0.5f);
			birdAnimatedSprite.draw(batch);
			
			checkDifficulty();

			//OUTPUT INFO HERE
			game.font.draw(game.batch, "Score: " + currentScore,  0, 480);
			
			
			for(Fluff f : fluffs)
			{
				f.a.setScale(f.scale);
				f.a.draw(batch);
			}


			batch.end();

			//LOGIC UPDATE
			if(Gdx.input.isTouched())
			{
				touchPos.set(Gdx.input.getX(),  Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				birdRectangle.y = touchPos.y - birdHeight / 2;
			}

			birdWidth = 185 * 0.5f;
			birdHeight = 200 * 0.5f;
			
			birdRectangle.setWidth(birdWidth);
			birdRectangle.setHeight(birdHeight);
			
			
			if(Gdx.input.isKeyPressed(Keys.DOWN))birdRectangle.y -= 200 * Gdx.graphics.getDeltaTime();
			if(Gdx.input.isKeyPressed(Keys.UP))birdRectangle.y += 200 * Gdx.graphics.getDeltaTime();

			if(birdRectangle.y < -50) birdRectangle.y = -50;
			if(birdRectangle.y > 325) birdRectangle.y = 325;

			birdAnimatedSprite.setPosition(birdRectangle.x, birdRectangle.y);

			if(TimeUtils.nanoTime() - lastDropTime > rateOfRespawn) 
			{
				if(ran.nextInt(2) == 0)
				{
					spawnFluff(new AnimatedSprite(fluffAnimation), true);
				}
				else
				{
					spawnFluff(new AnimatedSprite(fluff2Animation), false);
				}
			}

			checkCollisions();
		}
		
		else if(gameOver)
		{
			if(Gdx.input.isTouched())
			{
				touchPos.set(Gdx.input.getX(),  Gdx.input.getY(), 0);
				camera.unproject(touchPos);
			}
			
			camera.update();
			batch.setProjectionMatrix(camera.combined);

			batch.begin();
			drawScrollingBackground();
			
			batch.draw(playButtonTexture, 275 - playButtonTexture.getWidth()/2, 100);
			playButtonRectangle.set(275 - playButtonTexture.getWidth()/2, 100, 
					playButtonTexture.getWidth(), playButtonTexture.getHeight());
			
			batch.draw(mainMenuButtonTexture, 525 - mainMenuButtonTexture.getWidth()/2, 100);
			mainMenuButtonRectangle.set(525 - mainMenuButtonTexture.getWidth()/2, 100, 
					mainMenuButtonTexture.getWidth(), mainMenuButtonTexture.getHeight());
			
			batch.draw(gameOverTexture, 400 - gameOverTexture.getWidth()/2, 240);
			game.font.draw(batch, "Score: " + currentScore, 300, 235);
			game.font.draw(batch, "High Score: " + highScore, 300, 210);
			
			batch.end();
			
			if(playButtonRectangle.contains(touchPos.x, touchPos.y) && Gdx.input.justTouched())
			{
				select.play();
				fluffs.clear();
				gameOver = false;
				currentScore = 0;
				rateOfRespawn = RESPAWN_START;
			}
			
			else if(mainMenuButtonRectangle.contains(touchPos.x, touchPos.y) && Gdx.input.justTouched())
			{
				select.play();
				fluffs.clear();
				gameOver = false;
				currentScore = 0;
				rateOfRespawn = RESPAWN_START;
				game.setScreen(game.mainMenuScreen);
			}
			
		}


	}


	private void checkDifficulty() 
	{
//		if(currentScore == 50)
//		{
//			rateOfRespawn /= 10;
//		}
//		
		
		
	}

	private void drawScrollingBackground() {
		scrollTimer+=Gdx.graphics.getDeltaTime();
		if(scrollTimer>1.0f)
			scrollTimer = 0.0f;

		oceanSprite.setU(scrollTimer);
		oceanSprite.setU2(scrollTimer+1);
		batch.draw(oceanSprite, 0, 0);
		
	}

	private void checkCollisions() 
	{
		Iterator<Fluff> iter = fluffs.iterator();
		while(iter.hasNext())
		{
			Fluff f = iter.next();
			f.a.setPosition(f.fluffRectangle.x, f.fluffRectangle.y);

			//check the Fluff's angle and set the rotation to that
			if(f.angle >= 360)
			{
				f.angle = 0;
			}
			else
			{
				f.angle += 2;
			}
			f.a.setRotation(f.angle);

			f.fluffRectangle.x -= f.speed * Gdx.graphics.getDeltaTime();

			//Points if player dodges a fluff
			if(f.fluffRectangle.x + fluffWidth < 0)
			{
				randomChirp();
				currentScore++;
				iter.remove();
			}


			else if(birdRectangle.overlaps(f.fluffRectangle))
			{

				gameOver = true;
				die.play();
				
				//See if we've beat the high score
				if(currentScore > highScore)
				{
					highScore = currentScore;
				}
				
			}

		}
	}

	private void randomChirp() 
	{
		int r = ran.nextInt(chripArray.size);
		chripArray.get(r).play();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() 
	{

	}

	@Override
	public void resume() {
	}

	@Override
	public void show() 
	{
		

	}

	@Override
	public void hide() 
	{
		fluffs.clear();
	}

	@Override
	public void dispose() 
	{
		batch.dispose();
		oceanTexture.dispose();
		fluffFrames.dispose();
		fluff2Frames.dispose();
		chirp1.dispose();
		chirp2.dispose();
		chirp3.dispose();
		chirp4.dispose();
		chirp5.dispose();
		die.dispose();
		select.dispose();

	}
}











