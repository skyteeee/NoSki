package com.skyteeee.noski;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.skyteeee.noski.screens.MainMenu;

public class NoSkiGame extends Game {
	public SpriteBatch batch;
	public BitmapFont mainFont;
	public BitmapFont titleFont;
	public BitmapFont smallFont;
	public TextureAtlas uiAtlas;
	public static Color colorBG = new Color(0x6a9a6eff);
	public static Color colorTextOnButton = new Color(0x274e13ff);
	public static Color colorTextRegular = new Color(0xffe383ff);

	@Override
	public void create () {
		batch = new SpriteBatch();
		loadFonts();
		loadTextures();
		setScreen(new MainMenu(this));
	}

	private void loadTextures() {
		uiAtlas = new TextureAtlas(Gdx.files.internal("ui/ui.atlas"));

	}

	private void loadFonts() {

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32;
		mainFont = generator.generateFont(parameter);
		mainFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		mainFont.setColor(colorTextOnButton);

		parameter.size = 24;
		smallFont = generator.generateFont(parameter);
		smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		smallFont.setColor(colorTextOnButton);

		generator.dispose();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/semibold.ttf"));
		parameter.size = 48;
		titleFont = generator.generateFont(parameter);
		titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		titleFont.setColor(colorTextOnButton);
		generator.dispose();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		mainFont.dispose();
		titleFont.dispose();
		smallFont.dispose();
	}
}
