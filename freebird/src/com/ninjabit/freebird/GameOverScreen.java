package com.ninjabit.freebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameOverScreen implements Screen 
{

	final Freebird game;
	OrthographicCamera camera;

	Skin skin;
	Stage stage;

	public GameOverScreen(final Freebird game) 
	{
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

//		//Gotten from LibGDX UISimple Test
//		stage = new Stage();
//		Gdx.input.setInputProcessor(stage);
//
//		// A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
//		// recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
//		skin = new Skin();
//
//		// Generate a 1x1 white texture and store it in the skin named "white".
//		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
//		pixmap.setColor(Color.WHITE);
//		pixmap.fill();
//		skin.add("white", new Texture(pixmap));
//
//		// Store the default libgdx font under the name "default".
//		skin.add("default", new BitmapFont());
//
//		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
//		TextButtonStyle textButtonStyle = new TextButtonStyle();
//		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
//		//textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
//		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
//		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
//		textButtonStyle.font = skin.getFont("default");
//		skin.add("default", textButtonStyle);
//
//		// Create a table that fills the screen. Everything else will go inside this table.
//		Table table = new Table();
//		table.setFillParent(true);
//		stage.addActor(table);
//
//		// Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
//		final TextButton button = new TextButton("BACK TO MAIN MENU", skin);
//
//		table.add(button).width(200f).height(20f);
//
//
//		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
//		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
//		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
//		// revert the checked state.
//		button.addListener(new ChangeListener() 
//		{
//			public void changed (ChangeEvent event, Actor actor) 
//			{
//				game.setScreen(game.mainMenuScreen);
//			}
//		});
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
//		stage.act();
//		stage.draw();

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, "GAME OVER", 100, 100);
		game.batch.end();

	}

	@Override
	public void resize(int width, int height) 
	{
		//stage.setViewport(800, 480, false);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() 
	{
		skin.dispose();
		stage.dispose();

	}

}
