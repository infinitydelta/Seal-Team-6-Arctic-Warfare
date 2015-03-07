package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by KS on 3/4/2015.
 */
public class MovementComponent extends Component implements Pool.Poolable {

    public boolean up = false, down = false, left = false, right = false;

    public float xVel = 0;
    public float yVel = 0;

    public float acc = 0;

    public MovementComponent(float xVel, float yVel, float acc)
    {
        this.xVel = xVel;
        this.yVel = yVel;
        this.acc = acc;
    }


    @Override
    public void reset() {
        xVel = 0;
        yVel = 0;
        acc = 0;
    }
}
