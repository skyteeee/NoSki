package com.skyteeee.noski.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.skyteeee.noski.NoSkiGame;

public class MainMenu implements Screen {
    final NoSkiGame game;
    OrthographicCamera camera;
    ExtendViewport viewport;
    NinePatch button;

    public MainMenu(final NoSkiGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 900);
        viewport = new ExtendViewport(1600,900, camera);

        button = game.uiAtlas.createPatch("NoSkiButton");

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(NoSkiGame.colorBG);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.titleFont.draw(game.batch,"Hello world!", 400, 700, 800f, Align.center, false);
        button.draw(game.batch, 600, 500, 400, 100);
        game.titleFont.draw(game.batch, "start", 600, 570, 400, Align.center, false);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
