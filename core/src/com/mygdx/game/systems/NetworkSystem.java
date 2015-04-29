//IM SO ANGRY
package com.mygdx.game.systems;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

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
    protected synchronized void processEntity(Entity entity, float deltaTime) {	//Updates myEntities, instances with properties of allEntites, and flags objects not in allEntities for local deletion
        PositionComponent pos = pm.get(entity);
        MovementComponent move = mm.get(entity);
        NetworkComponent network = nm.get(entity);
        
        ConcurrentHashMap<String, Object> newEntityData = new ConcurrentHashMap<String, Object>();
        newEntityData.put("type", network.type);
        newEntityData.put("playerNum", network.playerNum);
        newEntityData.put("ownerID", network.ownerID);
        newEntityData.put("xPos", pos.x);
        newEntityData.put("yPos", pos.y);
        newEntityData.put("xVel", move.xVel);
        newEntityData.put("yVel", move.yVel);
        
        
        if (network.playerNum.equals(GameScreen.networkPlayerNum)) {	//This belong to myEntities
    		for (ConcurrentHashMap<String, Object> myEnt : GameScreen.myEntities) {	
        		if (myEnt.get("playerNum").equals(GameScreen.networkPlayerNum) && myEnt.get("ownerID").equals(newEntityData.get("ownerID"))) {	//Find this entity in myEntities
        			GameScreen.myEntities.remove(myEnt);	//Remove it from myEntities
        			for (ConcurrentHashMap<String, Object> allEnt : GameScreen.allEntities) {	//Find it in allEntities as well to delete it there too
        				if (allEnt.get("playerNum").equals(GameScreen.networkPlayerNum) && allEnt.get("ownerID").equals(newEntityData.get("ownerID")))
        					GameScreen.allEntities.remove(allEnt);	//Remove it from allEntities as well
        			}
        		}
        	}
    		GameScreen.myEntities.add(newEntityData);	//And replace it with a new copy
    		GameScreen.allEntities.add(newEntityData);	//And replace it with a new copy
        }
        else {	//This doesnt belong in myEntities, so we should update our local copies to match the correct attributes
        	for (ConcurrentHashMap<String, Object> allEnt : GameScreen.allEntities) {	//This doesnt belong to me
        		if (allEnt.get("playerNum").equals(network.playerNum) && allEnt.get("ownerID").equals(network.ownerID)) {	//Find this entity in allEntities, and update its attributes
        			move.xVel = (Float)allEnt.get("xVel");
                    move.yVel = (Float)allEnt.get("yVel");
        			pos.x = (Float)allEnt.get("xPos");
        			pos.y = (Float)allEnt.get("yPos");
        			synchronized (GameScreen.world) {
                        move.body.setTransform(pos.x + .5f, pos.y + .5f, 0);
                    }
        		}
        	}
        }
        boolean entityShouldExist = false;
    	for (ConcurrentHashMap<String, Object> allEnt : GameScreen.allEntities) {
    		if (allEnt.get("playerNum").equals(newEntityData.get("playerNum")) && allEnt.get("ownerID").equals(newEntityData.get("ownerID"))) {	//Find this entity in allEntities
    			if (GameScreen.networkPlayerNum == 0) {	//If we are the host, we should update allEntities to match this data
	    			GameScreen.allEntities.remove(allEnt);	//Remove it from allEntities
					GameScreen.allEntities.add(newEntityData);	//Replace it with new data
    			}
    			entityShouldExist = true;
    		}
    	}
    	if (!entityShouldExist) {
    		if (!network.type.equals("player")) {
    			GameScreen.toBeDeleted.add(entity);
    		}
    	}
    }
}