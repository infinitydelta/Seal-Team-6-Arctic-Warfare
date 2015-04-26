package com.mygdx.game.systems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.GameScreen;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.NetworkComponent;
import com.mygdx.game.components.PositionComponent;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

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
        	synchronized(GameScreen.myEntities) {
        		for (HashMap<String, Object> entity2 : GameScreen.myEntities) {
            		if (entity2.get("playerNum").equals(newEntityData.get("playerNum")) && entity2.get("ownerID").equals(newEntityData.get("ownerID"))) {
            			GameScreen.myEntities.remove(entity2);
            		}
            	}
        		GameScreen.myEntities.add(newEntityData);
        		//Populate and replace myEntities with newEntities
        	}
        }
        else {
        	synchronized(GameScreen.allEntities) {
	        	for (HashMap<String, Object> entity2 : GameScreen.allEntities) {
	        		if (entity2.get("playerNum").equals(network.playerNum) && entity2.get("ownerID").equals(network.ownerID)) {
	        			System.out.println(network.type);
	        			pos.x = (Float)entity2.get("xPos");
	        			pos.y = (Float)entity2.get("yPos");
	        			move.xVel = (Float)entity2.get("xVel");
	        			move.xVel = (Float)entity2.get("yVel");
	        		}
	        	}
	        	//Update each entity with networkComponent with its corresponding allEntities value
        	}
        }
        synchronized(GameScreen.allEntities) {
        	for (HashMap<String, Object> entity2 : GameScreen.allEntities) {
        		if (entity2.get("playerNum").equals(newEntityData.get("playerNum")) && entity2.get("ownerID").equals(newEntityData.get("ownerID"))) {
        			GameScreen.allEntities.remove(entity2);
        		}
        	}
        	GameScreen.allEntities.add(newEntityData);
        }
        if (GameScreen.networkPlayerNum != 0) {
	        //System.out.println("All ents: " + GameScreen.allEntities.toString());
	        //System.out.println("My ents: " + GameScreen.myEntities.toString());
        }
    }
}
