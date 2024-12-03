package com.skyteeee.noski.actors;

public class HideFieldAction extends ShowFieldAction {

    @Override
    protected void update(float percent) {
        super.update(1-percent);
    }
}
