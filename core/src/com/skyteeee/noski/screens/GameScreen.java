package com.skyteeee.noski.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.skyteeee.noski.NoSkiGame;
import com.skyteeee.noski.actors.CellDeathAction;
import com.skyteeee.noski.actors.FieldActor;
import com.skyteeee.noski.logic.Cell;
import com.skyteeee.noski.logic.GameLogic;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    final NoSkiGame game;
    public final static int virtWidth = 1600;
    public final static int virtHeight = 900;
    OrthographicCamera camera;
    ExtendViewport viewport;
    NinePatch button;
    NinePatch buttonDown;

    GameLogic gameLogic;

    Table wordTable;
    Table sidePanel;
    Label scoreLabel;

    FieldActor fieldActor;


    Stage stage;

    public GameScreen(final NoSkiGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, virtWidth, virtHeight);
        viewport = new ExtendViewport(virtWidth,virtHeight, camera);
        stage = new Stage(viewport, game.batch);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);



        gameLogic = new GameLogic(10,10);
        fieldActor = new FieldActor(game, gameLogic, this::wordMatched);
        fieldActor.setup();
        gameLogic.newLevel();

        wordTable = new Table();
        wordTable.align(Align.left);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = game.mainFont;
        titleStyle.fontColor = NoSkiGame.colorTextRegular;

        for (String word : gameLogic.wordBank) {
            Label title = new Label(word, titleStyle);
            wordTable.add(title).align(Align.left);
            wordTable.row();
        }


        Label.LabelStyle scoreStyle = new Label.LabelStyle();
        scoreStyle.font = game.titleFont;
        scoreStyle.fontColor = NoSkiGame.colorTextRegular;
        Label.LabelStyle scoreTitleStyle = new Label.LabelStyle();
        scoreTitleStyle.font = game.titleFont;
        scoreTitleStyle.fontColor = Color.WHITE;
        Label scoreTitle = new Label("Score:", scoreTitleStyle);

        scoreLabel = new Label("0", scoreStyle);
        sidePanel = new Table();
        sidePanel.add(scoreTitle).expandX().right().top().padRight(15);
        sidePanel.add(scoreLabel).right().top().padRight(50);
        sidePanel.row();
        sidePanel.add(wordTable).left().expand();

        //table.debug();
        table.add(fieldActor).height(fieldActor.initSizeY).width(fieldActor.initSizeX).pad(50);
        table.add(sidePanel).fill().expand().top().padTop(50).padBottom(50);
        table.row();
        table.top().left();

    }

    private void wordMatched(int wordIdx, List<Cell> selection) {

        for (Cell c : selection) {
            Cell newCell = c.copy();
            newCell.status = Cell.CellStatus.NORMAL;
            fieldActor.particleCells.add(newCell);
        }



        Actor actor = wordTable.getChildren().get(wordIdx);
        if (actor instanceof Label) {
            Label label = (Label) actor;
            label.setColor(Color.CHARTREUSE);

            CellDeathAction deathAction = Actions.action(CellDeathAction.class);
            Vector2 labelStageC = label.localToStageCoordinates(new Vector2(label.getX(), label.getY()));
            Vector2 labelFieldC = fieldActor.stageToLocalCoordinates(labelStageC);
            Vector2 fieldStageC = fieldActor.localToStageCoordinates(new Vector2(fieldActor.getX(), fieldActor.getY()));
            Vector2 labelFieldD = label.localToActorCoordinates(fieldActor, new Vector2(0, label.getHeight()/2 - fieldActor.cellSizeY/2));

            System.out.println("Label Stage C: x:" + labelStageC.x + ", y: " + labelStageC.y);
            System.out.println("Field Stage C: x:" + fieldStageC.x + ", y: " + fieldStageC.y + " LX: " + fieldActor.getX() + " LY: " + fieldActor.getY() + " OX: " + fieldActor.getOriginX() + " OY: " + fieldActor.getOriginY());
            System.out.println("Label Field C: x:" + labelFieldC.x + ", y: " + labelFieldC.y);
            System.out.println("Label Field D: x:" + labelFieldD.x + ", y: " + labelFieldD.y);



            deathAction.init(fieldActor.particleCells, labelFieldD.x, labelFieldD.y);
            deathAction.setDuration(0.4f);
            deathAction.setInterpolation(Interpolation.pow3In);
            fieldActor.addAction(sequence(deathAction, run(() -> fieldActor.particleCells.clear())));
        }



        scoreLabel.setText(gameLogic.score);

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
