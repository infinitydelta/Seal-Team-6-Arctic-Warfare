package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.AIControllerComponent;
import com.mygdx.game.dungeon.DungeonGenerator;

/**
 * Created by McLean on 4/23/2015.
 */
public class AISystem extends IteratingSystem {

    private ComponentMapper<AIControllerComponent> aim = ComponentMapper.getFor(AIControllerComponent.class);

    public AISystem(Family family) {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AIControllerComponent ai = aim.get(entity);
        //GenPath(ai);
    }

    void GenPath(AIControllerComponent ai){
        int dim = DungeonGenerator.mapSize/2;
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                ai.h[i][j] = (Math.abs(i-ai.xIndex)+Math.abs(j-ai.xIndex));
            }
        }

    }
}
