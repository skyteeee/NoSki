package com.skyteeee.noski.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.skyteeee.noski.NoSkiGame;

public class MainMenu implements Screen {
    final NoSkiGame game;
    OrthographicCamera camera;
    ExtendViewport viewport;
    NinePatch button;
    NinePatch buttonDown;

    Stage stage;

    public MainMenu(final NoSkiGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 900);
        viewport = new ExtendViewport(1600,900, camera);
        stage = new Stage(viewport, game.batch);

        button = game.uiAtlas.createPatch("NoSkiButton");
        buttonDown = game.uiAtlas.createPatch("NoSkiButtonDown");

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = new NinePatchDrawable(buttonDown);
        buttonStyle.up = new NinePatchDrawable(button);
        buttonStyle.font = game.titleFont;
        buttonStyle.fontColor = NoSkiGame.colorTextOnButton;

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = game.titleFont;
        titleStyle.fontColor = NoSkiGame.colorTextRegular;
        Label title = new Label("Welcome to NoSki!", titleStyle);

        TextButton startButton = new TextButton("start", buttonStyle);
        table.add(title).padBottom(100).padTop(100);
        table.row();
        table.add(startButton).width(400).height(100);
        table.align(Align.top);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start Clicked.");
                game.setScreen(new GameScreen(game));
            }
        });


    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(NoSkiGame.colorBG);
        stage.act(delta);
        stage.draw();
//        game.batch.begin();
//        game.titleFont.draw(game.batch,"Hello world!", 400, 700, 800f, Align.center, false);
//        button.draw(game.batch, 600, 500, 400, 100);
//        game.titleFont.draw(game.batch, "start", 600, 570, 400, Align.center, false);
//        game.batch.end();

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
        stage.dispose();
    }
}
