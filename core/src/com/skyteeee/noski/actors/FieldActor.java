package com.skyteeee.noski.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.skyteeee.noski.NoSkiGame;
import com.skyteeee.noski.logic.GameLogic;
import com.skyteeee.noski.screens.MainMenu;

public class FieldActor extends Actor {

    NinePatch cellPatch;
    public float initSizeX = 800f;
    public float initSizeY = 800f;
    GameLogic field;
    NoSkiGame game;

    float cellSizeX;
    float cellSizeY;

    public FieldActor(NoSkiGame game, GameLogic field) {
        cellPatch = game.uiAtlas.createPatch("NoSkiCell");
        this.field = field;
        this.game = game;
        cellSizeX = initSizeX/field.width;
        cellSizeY = initSizeY/field.height;
        float side = Math.min(cellSizeX, cellSizeY);
        cellSizeX = cellSizeY = side;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float myX = getX();
        float myY = getY();
        for (int y = 0; y < field.height; y++) {
            for (int x = 0; x < field.width; x++) {
                cellPatch.draw(batch, myX + x * cellSizeX, myY + y * cellSizeY, cellSizeX, cellSizeY);
            }
        }
        float fontAscent = game.mainFont.getAscent();
        float fontDescent = game.mainFont.getDescent();
        float fontHeight = game.mainFont.getLineHeight();
        float delta = (cellSizeY/2 + fontAscent) + 1;
        for (int y = 0; y < field.height; y++) {
            for (int x = 0; x < field.width; x++) {
                game.mainFont.draw(batch, field.getCell(x,y).value,
                        myX + x * cellSizeX,
                        myY + y * cellSizeY + delta,
                        cellSizeX, Align.center, false);
            }
        }
    }
}
