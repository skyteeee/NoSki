package com.skyteeee.noski.actors;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.skyteeee.noski.logic.Cell;

import java.util.List;

public class CellDeathAction extends TemporalAction {

    List<Cell> selection;
    float endX;
    float endY;



    public void init(List<Cell> selection, float endX, float endY) {
        this.selection = selection;
        this.endX = endX;
        this.endY = endY;
        for (Cell c : selection) {
            c.ogScreenX = c.screenX;
            c.ogScreenY = c.screenY;
        }

    }

    @Override
    protected void update(float percent) {

        for (Cell c : selection) {
            c.screenX = c.ogScreenX + (endX - c.ogScreenX) * percent;
            c.screenY = c.ogScreenY + (endY - c.ogScreenY) * percent;

            c.opacity = 1 - (0.8f * percent);

        }


    }

    @Override
    protected void end() {
        super.end();
        update(1);
    }
}
