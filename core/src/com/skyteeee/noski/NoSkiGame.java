package com.skyteeee.noski;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.skyteeee.noski.screens.MainMenu;

public class NoSkiGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		font.getData().setScale(5);
		font.setColor(0,0,0,1);
		img = new Texture("badlogic.jpg");
		setScreen(new MainMenu(this));
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		img.dispose();
	}
}
