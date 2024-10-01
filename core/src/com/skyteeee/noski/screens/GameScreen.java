package com.skyteeee.noski.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
import com.skyteeee.noski.actors.FieldActor;
import com.skyteeee.noski.logic.GameLogic;

public class GameScreen implements Screen {
    final NoSkiGame game;
    OrthographicCamera camera;
    ExtendViewport viewport;
    NinePatch button;
    NinePatch buttonDown;

    GameLogic gameLogic;



    Stage stage;

    public GameScreen(final NoSkiGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 900);
        viewport = new ExtendViewport(1600,900, camera);
        stage = new Stage(viewport, game.batch);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = game.titleFont;
        titleStyle.fontColor = NoSkiGame.colorTextRegular;
        Label title = new Label("Game in Progress", titleStyle);

        gameLogic = new GameLogic(10,10);
        FieldActor fieldActor = new FieldActor(game, gameLogic);

        table.add(fieldActor).height(fieldActor.initSizeY).width(fieldActor.initSizeX).pad(50);
        table.add(title).padBottom(100).padTop(100);
        table.row();
        table.align(Align.topLeft);

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
