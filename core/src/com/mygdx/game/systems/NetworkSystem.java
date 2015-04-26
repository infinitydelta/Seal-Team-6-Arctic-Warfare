package com.mygdx.game.systems;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.NetworkComponent;
import com.mygdx.game.components.PositionComponent;

/**
 * Created by KS on 4/25/2015.
 */
public class NetworkSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);
    private ComponentMapper<NetworkComponent> nm = ComponentMapper.getFor(NetworkComponent.class);
    public static HashSet<HashMap<String, Object>> myEntities;
    public static HashSet<HashMap<String, Object>> allEntities;

    public NetworkSystem()
    {
        super(Family.getFor(NetworkComponent.class, PositionComponent.class, MovementComponent.class));
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        MovementComponent move = mm.get(entity);
        NetworkComponent network = nm.get(entity);
        network.xPos = pos.x;
        network.yPos = pos.y;
        network.xVel = move.xVel;
        network.yVel = move.yVel;
        
        HashMap<String, Object> newEntityData = new HashMap<String, Object>();
        newEntityData.put("type", network.type);
        newEntityData.put("owner", network.owner);
        newEntityData.put("ownerID", network.ownerID);
        newEntityData.put("xPos", network.xPos);
        newEntityData.put("yPos", network.yPos);
        newEntityData.put("xVel", network.xVel);
        newEntityData.put("yVel", network.yVel);
    }
}
