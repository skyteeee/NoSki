package com.skyteeee.noski.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.skyteeee.noski.NoSkiGame;
import com.skyteeee.noski.logic.Cell;
import com.skyteeee.noski.logic.GameLogic;
import com.skyteeee.noski.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class FieldActor extends Actor {
    public interface WordMatchCallback {
        void run(int wordIdx, List<Cell> selection);
    }

    NinePatch cellPatch;
    NinePatch selectedCellPatch;
    NinePatch deadCellPatch;
    public float initSizeX = 1200f;
    public float initSizeY = 800f;
    public GameLogic field;
    NoSkiGame game;

    public float cellSizeX;
    public float cellSizeY;

    WordMatchCallback matchCallback;



    ArrayList<Cell> selection = new ArrayList<>();
    public List<Cell> particleCells = new ArrayList<>();
    //Set<Cell> selection = new LinkedHashSet<>();

    public FieldActor(NoSkiGame game, GameLogic field, WordMatchCallback callback) {
        cellPatch = game.uiAtlas.createPatch("NoSkiCell");
        selectedCellPatch = game.uiAtlas.createPatch("NoSkiCellSelected");
        deadCellPatch = game.uiAtlas.createPatch("NoSkiCellDead");
        this.field = field;
        this.game = game;
        cellSizeX = initSizeX/field.width;
        cellSizeY = initSizeY/field.height;
        float side = Math.min(cellSizeX, cellSizeY);
        cellSizeX = cellSizeY = side;
        matchCallback = callback;
    }

    private Cell getCell(float x, float y) {
        int fieldX = (int)(x / cellSizeX);
        int fieldY = (int)(y / cellSizeY);
        if (fieldX >= field.width || fieldX < 0
        || fieldY >= field.height || fieldY < 0) {
            return null;
        }
        return field.getCell(fieldX, fieldY);
    }

    public void addShowAction(ShowFieldAction showAction, Runnable callback) {
        float colH = cellSizeY * field.height;
        showAction.setLogic(field, (GameScreen.virtHeight + colH)/2, colH);
        showAction.setDuration(2f);
        showAction.setInterpolation(Interpolation.pow2Out);
        addAction(sequence(showAction, run(callback)));
    }

    public void setup() {

        for (int y = 0; y < field.height; y++) {
            for (int x = 0; x < field.width; x++) {
                Cell cell = field.getCell(x, y);
                cell.screenX = cellSizeX * cell.x;
                cell.screenY = cellSizeY * cell.y;
            }
        }

        ShowFieldAction showAction = Actions.action(ShowFieldAction.class);
        addShowAction(showAction, () -> {});

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                selection.clear();
                Cell cell = getCell(x, y);
                if (cell != null && cell.status != Cell.CellStatus.DEAD) {
                    cell.status = Cell.CellStatus.SELECTED;
                    selection.add(cell);
                    System.out.println("Touchdown coordinates: (" + x + ", " + y + ")");
                    System.out.println("Field coordinates: (" + cell.x + ", " + cell.y + ") = " + cell.value);
                    return true;
                }
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                for (Cell cell : selection) {
                    cell.status = Cell.CellStatus.NORMAL;
                }

                int wordIdx = field.checkSelection(selection);
                if (wordIdx >= 0) {
                    boolean wrongCellFound = false;
                    String word = field.getWord(wordIdx);
                    for (Cell c : selection) {
                        String w = c.parentWord;
                        if (w != null && !w.equals(word)) {
                            wrongCellFound = true;
                            wrongCellAnimation(c);
                        }
                    }

                    if (!wrongCellFound) {
                        field.onMatch(wordIdx);
                        for (Cell cell : selection) cell.status = Cell.CellStatus.DEAD;
                        matchCallback.run(wordIdx, selection);
                    }
                }

                System.out.println("Selection: " + selection);

                super.touchUp(event, x, y, pointer, button);

            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Cell cell = getCell(x, y);
                if (cell != null && cell.status != Cell.CellStatus.DEAD) {
                    int i = selection.indexOf(cell);
                    if (i == -1) {
                        Cell prev = selection.get(selection.size()-1);
                        if (cell.distance(prev) == 1) {
                            selection.add(cell);
                            cell.status = Cell.CellStatus.SELECTED;
                        }
                    } else {
                        if (i != selection.size()-1) {
                            for (int j = selection.size()-1; j > i; j--) {
                                selection.get(j).status = Cell.CellStatus.NORMAL;
                                selection.remove(j);
                            }
                        }
                    }
                }
                super.touchDragged(event, x, y, pointer);
            }
        });
    }

    private void wrongCellAnimation(Cell c) {
        Cell nc = c.copy();
        particleCells.add(nc);
        WrongCellAction action = Actions.action(WrongCellAction.class);
        action.setDuration(1);
        action.setCell(nc);
        addAction(sequence(action, run(() -> {
            particleCells.remove(nc);
        })));
    }

    void drawCellBg(Batch batch, Cell cell, float myX, float myY) {
        NinePatch patch;
        switch (cell.status) {
            case NORMAL:
                patch = cellPatch;
                break;
            case SELECTED:
                patch = selectedCellPatch;
                break;
            case DEAD:
                patch = deadCellPatch;
                break;
            default:
                patch = cellPatch;
                break;
        }
        patch.setColor(cell.color);
        patch.draw(batch, myX + cell.screenX, myY + cell.screenY, cellSizeX, cellSizeY);
        patch.setColor(Color.WHITE);
    }

    void drawCellTxt(Batch batch, Cell cell, float myX, float myY, float delta) {
        String text = cell.value;
        if (text != null) {
            game.mainFont.draw(batch, text,
                    myX + cell.screenX,
                    myY + cell.screenY + delta,
                    cellSizeX, Align.center, false);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float myX = getX();
        float myY = getY();
        for (int y = 0; y < field.height; y++) {
            for (int x = 0; x < field.width; x++) {
                Cell cell = field.getCell(x, y);
                drawCellBg(batch, cell, myX, myY);
            }
        }
        float fontAscent = game.mainFont.getAscent();
        float delta = (cellSizeY/2 + fontAscent) + 1;
        for (int y = 0; y < field.height; y++) {
            for (int x = 0; x < field.width; x++) {
                Cell cell = field.getCell(x, y);
                drawCellTxt(batch, cell, myX, myY, delta);
            }
        }

        for (Cell c : particleCells) {
            drawCellBg(batch, c, myX, myY);
            drawCellTxt(batch, c, myX, myY, delta);

        }

    }
}
