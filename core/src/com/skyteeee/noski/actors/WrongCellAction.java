package com.skyteeee.noski.actors;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.skyteeee.noski.logic.Cell;

public class WrongCellAction extends TemporalAction {

    Cell cell;

    private static final int FLASHTIMES = 3;

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    protected void update(float percent) {
        float f = (float) Math.abs(Math.cos(FLASHTIMES * Math.PI * percent));
        cell.color.g = f;
        cell.color.b = f;
    }

    @Override
    protected void end() {
        super.end();
        cell.color.b = 1;
        cell.color.g = 1;
    }
}
