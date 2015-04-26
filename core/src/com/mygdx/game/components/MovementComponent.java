package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by KS on 3/4/2015.
 */
public class MovementComponent extends Component implements Pool.Poolable {

    public Body body;


    //public boolean up = false, down = false, left = false, right = false;

    boolean moveX = false, moveY = false;

    public float xVel = 0;
    public float yVel = 0;

    public float acc = 0;


    //used for player right now
    public MovementComponent(CollisionComponent collision, World world, float xVel, float yVel, float acc)
    {
        this.xVel = xVel;
        this.yVel = yVel;
        this.acc = acc;
        body = collision.body;
        body.applyLinearImpulse(xVel, yVel, 0, 0, true);
        //body.setLinearVelocity(xVel, yVel);

    }

    public void update()
    {

    }

    @Override
    public void reset() {
        xVel = 0;
        yVel = 0;
        acc = 0;
        body = null;
    }
}
