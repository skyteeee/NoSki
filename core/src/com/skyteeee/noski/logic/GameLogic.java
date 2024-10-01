package com.skyteeee.noski.logic;

public class GameLogic {
    private Cell[][] field;
    public int width;
    public int height;

    public GameLogic(int fieldWidth, int fieldHeight) {
        field = new Cell[fieldWidth][fieldHeight];
        width = fieldWidth;
        height = fieldHeight;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                field[x][y] = new Cell();
            }
        }
    }

    public Cell getCell(int x, int y) {
        return field[x][y];
    }

}
