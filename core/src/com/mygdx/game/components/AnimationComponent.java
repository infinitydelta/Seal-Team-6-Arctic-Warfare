package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by KS on 3/7/2015.
 */
public class AnimationComponent extends Component implements Pool.Poolable {

    public Animation animation;
    public float stateTime = 0;


    public AnimationComponent(Animation animation) {
        this.animation = animation;

    }

    @Override
    public void reset() {

    }
}
