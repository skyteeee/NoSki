package com.skyteeee.noski.actors;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;

public class ScoreParticleAction extends TemporalAction {

    ParticleEffectPool.PooledEffect effect;
    Vector2 from;
    Vector2 to;
    Array<ParticleEffectPool.PooledEffect> active;

    public void setAll(ParticleEffectPool.PooledEffect effect, Array<ParticleEffectPool.PooledEffect> active, Vector2 from, Vector2 to) {
        this.effect = effect;
        this.from = from;
        this.to = to;
        this.active = active;
    }

    @Override
    protected void begin() {
        super.begin();
        effect.setPosition(from.x, from.y);
        effect.start();
        active.add(effect);
    }

    @Override
    protected void update(float percent) {
        effect.setPosition(from.x + (to.x - from.x) * percent, from.y + (to.y - from.y) * percent);
    }

    @Override
    protected void end() {
        super.end();
    }
}
