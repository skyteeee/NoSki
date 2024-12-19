package com.skyteeee.noski.actors;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class ScoreAddAction extends TemporalAction {

    int from;
    int to;
    Label target;

    public void setAll(Label target, int from, int to) {
        this.target = target;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void update(float percent) {
        target.setText("" + Math.round(from + (to - from) * percent));
    }
}
