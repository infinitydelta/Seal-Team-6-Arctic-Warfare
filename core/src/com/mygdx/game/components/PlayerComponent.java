package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.MainGame;

/**
 * Created by KS on 3/5/2015.
 */
public class PlayerComponent extends Component implements Pool.Poolable {

    //internal stuff
    Entity player;
    MovementComponent movement;
    VisualComponent visual;

    public boolean up = false, down = false, left = false, right = false;
    private boolean moveX = false, moveY = false;
    private float root2 = (float) Math.sqrt(2);


    //gameplay values
    float speed = 5;
    float diagSpeed = speed/root2;


    public PlayerComponent(Entity player)
    {
        this.player = player;
        movement = player.getComponent(MovementComponent.class); //need movement instantiated
        visual = player.getComponent(VisualComponent.class);
    }

    public void update()
    {
        movement.xVel = 0;
        movement.yVel = 0;
        moveX = false;
        moveY = false;

        if (left)
        {
            if (!visual.sprite.isFlipX()) visual.sprite.flip(true, false);

            movement.xVel = -speed;
            moveX = true;
        }
        if (right)
        {
            if (visual.sprite.isFlipX()) visual.sprite.flip(true, false);
            movement.xVel = speed;
            moveX = true;
        }
        if (up)
        {
            if (moveX) {
                movement.yVel = diagSpeed;
                movement.xVel /= root2;

            }
            else movement.yVel = speed;
            moveY = true;
        }
        if (down)
        {
            if (moveX) {
                movement.yVel = -diagSpeed;
                movement.xVel /= root2;

            }
            else movement.yVel = -speed;
            moveY = true;
        }

        if (!moveY && !moveX) visual.setAnimation(MainGame.idleAnmation);
        else visual.setAnimation(MainGame.runAnimation);


    }


    @Override
    public void reset() {

    }
}
