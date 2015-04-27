//IM SO ANGRY
package com.mygdx.game.systems;

import java.util.HashMap;

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
        
        HashMap<String, Object> newEntityData = new HashMap<String, Object>();
        newEntityData.put("type", network.type);
        newEntityData.put("playerNum", network.playerNum);
        newEntityData.put("ownerID", network.ownerID);
        newEntityData.put("xPos", pos.x);
        newEntityData.put("yPos", pos.y);
        newEntityData.put("xVel", move.xVel);
        newEntityData.put("yVel", move.yVel);
        
        boolean myEntityFound = false;
        boolean deleteEntity = false;
        boolean entityExistsInNetwork = false;
        
        if (network.isDead) {
        	deleteEntity = true;
        }
        
        if (GameScreen.toBeDeleted.contains(entity)) {
        	deleteEntity = true;
        }
        
        if (network.playerNum.equals(GameScreen.networkPlayerNum)) {
    		for (HashMap<String, Object> entity2 : GameScreen.myEntities) {
        		if (entity2.get("playerNum").equals(newEntityData.get("playerNum")) && entity2.get("ownerID").equals(newEntityData.get("ownerID"))) {
        			GameScreen.myEntities.remove(entity2);
        			if (!deleteEntity)
        				GameScreen.myEntities.add(newEntityData);
        			myEntityFound = true;
        		}
        	}
    		if (!deleteEntity)
    			GameScreen.myEntities.add(newEntityData);
    		//Populate and replace myEntities with newEntities
        }
        else {
        	for (HashMap<String, Object> entity2 : GameScreen.allEntities) {
        		if (entity2.get("playerNum").equals(network.playerNum) && entity2.get("ownerID").equals(network.ownerID)) {
        			move.xVel = (Float)entity2.get("xVel");
                    move.yVel = (Float)entity2.get("yVel");
        			pos.x = (Float)entity2.get("xPos");
        			pos.y = (Float)entity2.get("yPos");
        			synchronized (GameScreen.world) {
                        move.body.setTransform(pos.x + .5f, pos.y + .5f, 0);
                    }
        		}
        	}
        	//Update each entity with networkComponent with its corresponding allEntities value
        }
    	for (HashMap<String, Object> entity2 : GameScreen.allEntities) {
    		if (entity2.get("playerNum").equals(newEntityData.get("playerNum")) && entity2.get("ownerID").equals(newEntityData.get("ownerID"))) {
    			GameScreen.allEntities.remove(entity2);
    			if (!deleteEntity)
    				GameScreen.allEntities.add(newEntityData);
    			entityExistsInNetwork = true;
    		}
    	}
    	if (!deleteEntity)
    		GameScreen.allEntities.add(newEntityData);
    	if (!entityExistsInNetwork) {
    		deleteEntity = true;
    	}
    	if (deleteEntity) {
    		GameScreen.toBeDeleted.add(entity);
    	}
    	//Unconditionally replaces this entity in allEntities
    	
        /*if (GameScreen.networkPlayerNum == 0) {
	        System.out.println("All ents: " + GameScreen.allEntities.size());
	        System.out.println("My ents: " + GameScreen.myEntities.size());
        }*/
    }
}