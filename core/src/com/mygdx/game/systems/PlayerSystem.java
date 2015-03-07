package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;

/**
 * Created by KS on 3/6/2015.
 */
public class PlayerSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);

    public PlayerSystem()
    {
        super(Family.getFor(MovementComponent.class, PlayerComponent.class));
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent player = pm.get(entity);
        player.update();
    }
}
