package com.mygdx.game.systems;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.GameScreen;
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

    public NetworkSystem()
    {
        super(Family.getFor(NetworkComponent.class, PositionComponent.class, MovementComponent.class));
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        MovementComponent move = mm.get(entity);
        NetworkComponent network = nm.get(entity);
        /*network.xPos = pos.x;
        network.yPos = pos.y;
        network.xVel = move.xVel;
        network.yVel = move.yVel;
        */
        HashMap<String, Object> newEntityData = new HashMap<String, Object>();
        newEntityData.put("type", network.type);
        newEntityData.put("playerNum", network.playerNum);
        newEntityData.put("ownerID", network.ownerID);
        newEntityData.put("xPos", pos.x);
        newEntityData.put("yPos", pos.y);
        newEntityData.put("xVel", move.xVel);
        newEntityData.put("yVel", move.yVel);
        
        if (((Integer) newEntityData.get("playerNum")).equals(GameScreen.networkPlayerNum)) {
        	GameScreen.myEntities.remove(newEntityData);
        	GameScreen.myEntities.add(newEntityData);
        }
        else {
        	for (HashMap<String, Object> entity2 : GameScreen.allEntities) {
        		if (entity2.get("playerNum").equals(network.playerNum) && entity2.get("ownerID").equals(network.ownerID)) {
        			pos.x = (Float)entity2.get("xPos");
        			pos.y = (Float)entity2.get("yPos");
        			move.xVel = (Float)entity2.get("xVel");
        			move.xVel = (Float)entity2.get("yVel");
        		}
        	}
        }
        GameScreen.allEntities.remove(newEntityData);
        GameScreen.allEntities.add(newEntityData);
    }
}
