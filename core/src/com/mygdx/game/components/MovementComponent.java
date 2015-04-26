package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by KS on 3/4/2015.
 */
public class MovementComponent extends Component implements Pool.Poolable {

    public Body body;


    public boolean up = false, down = false, left = false, right = false;

    boolean moveX = false, moveY = false;

    public float xVel = 0;
    public float yVel = 0;

    public float acc = 0;

    public MovementComponent (PositionComponent position, World world)
    {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x + .5f, position.y + .5f);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(.5f, .5f);

        fixtureDef.shape = rectangle;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        Fixture fixture = body.createFixture(fixtureDef);
        rectangle.dispose();
    }

    public MovementComponent(CollisionComponent collision)
    {
        body = collision.body;
    }

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
        /*
        if (!moveX)
        {
            body.applyLinearImpulse(xVel, 0, body.getPosition().x, body.getPosition().y, true);
            moveX = true;
        }
        if (!moveY)
        {
            body.applyLinearImpulse(0, yVel, body.getPosition().x, body.getPosition().y, true);
            moveY = true;
        }
        */
        if (body.getLinearVelocity().x != xVel || body.getLinearVelocity().y != yVel)
        {
            //body.setLinearVelocity(xVel, yVel);
        }
        //System.out.println("xvel: " + xVel + ", yvel: " + yVel);
    }

    @Override
    public void reset() {
        xVel = 0;
        yVel = 0;
        acc = 0;
        body = null;
    }
}
