package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by KS on 3/4/2015.
 */
public class PositionComponent extends Component implements Pool.Poolable{

    public float x = 0f;
    public float y = 0f;
    public float radians = 0;

    public PositionComponent(float x, float y)
    {

        this.x = x;
        this.y = y;
        this.radians = 0;
    }
    public PositionComponent(float x, float y, float radians)
    {
        this.x = x;
        this.y = y;
        this.radians = radians;
    }

    @Override
    public void reset() {
        x = 0f;
        y = 0f;
        radians = 0;
    }
}
