package com.skyteeee.noski.actors;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.skyteeee.noski.logic.Cell;
import com.skyteeee.noski.logic.GameLogic;

public class ShowFieldAction extends TemporalAction {

    GameLogic logic;
    float edgeH;
    float colH;
    float cellH;

    public void setLogic(GameLogic gameLogic, float edgeH, float colH) {
        logic = gameLogic;
        this.edgeH = edgeH;
        this.colH = colH;
        cellH = colH/logic.height;
        System.out.println("Edge: " + edgeH
        + "; Col: " + colH + "; Cell: " + cellH);
    }

    @Override
    protected void begin() {
        super.begin();

        update(0);

    }

    @Override
    protected void update(float percent) {
        int n2 = logic.width / 2;
        for (int x = 0; x < logic.width; x++) {
            float sign = x % 2 == 0 ? 1 : -1;
            int nx = x / 2;

            float newY = sign * Math.max((edgeH + colH * nx / n2) * (1-percent), 0);

            for (int y = 0; y < logic.height; y++) {
                Cell cell = logic.getCell(x, y);
                cell.screenY = newY + y * cellH;

            }
        }
    }
}
