package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PositionComponent;

/**
 * Created by KS on 3/4/2015.
 */
public class MovementSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);


    public MovementSystem()
    {
        super(Family.getFor(MovementComponent.class, PositionComponent.class));
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = pm.get(entity);
        MovementComponent movement = mm.get(entity);

        position.x = movement.body.getPosition().x - .5f;
        position.y = movement.body.getPosition().y - .5f;
        movement.update();
        //movement.body.applyLinearImpulse(movement.xVel, movement.yVel, movement.body.getPosition().x, movement.body.getPosition().y, true);
        //position.x += movement.xVel * deltaTime;
        //position.y += movement.yVel * deltaTime;
    }
}
