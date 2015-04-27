package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.GameScreen;

import com.mygdx.game.components.AIControllerComponent;

import com.mygdx.game.components.PositionComponent;

import com.mygdx.game.components.VisualComponent;
import com.mygdx.game.utility.EntityType;
import com.mygdx.game.utility.Factory;

/**
 * Created by KS on 4/25/2015.
 */
public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        //System.out.println("contact began: " + contact.toString());
        //System.out.println("contact a: " + contact.getFixtureA().getBody().getUserData());
        //System.out.println("contact b: " + contact.getFixtureB().getBody().getUserData());
        //contact.getFixtureA().getUserData()
        EntityType etA = (EntityType) (contact.getFixtureA().getBody().getUserData());
        EntityType etB = (EntityType) (contact.getFixtureB().getBody().getUserData());

        if (etA.type == ('b') && etB.type == 'w')
        {
        	VisualComponent vc = etA.e.getComponent(VisualComponent.class);
        	PositionComponent pc = etA.e.getComponent(PositionComponent.class);
        	float rot = vc.rotation;
        	Factory.createBulletDestroyed(pc.x, pc.y, rot);
        	GameScreen.toBeDeleted.add(etA.e);
        }
        if(etB.type == 'b' && etA.type == 'w')
        {
        	VisualComponent vc = etB.e.getComponent(VisualComponent.class);
        	PositionComponent pc = etB.e.getComponent(PositionComponent.class);
        	float rot = vc.rotation;
        	Factory.createBulletDestroyed(pc.x, pc.y, rot);
        	GameScreen.toBeDeleted.add(etB.e);
        }
        if (etA.type == ('b') && etB.type == 'e')
        {
           etB.e.getComponent(AIControllerComponent.class).health--;
            //GameScreen.toBeDeleted.add(etA.e);
            
            VisualComponent vc = etA.e.getComponent(VisualComponent.class);
        	PositionComponent pc = etA.e.getComponent(PositionComponent.class);
        	float rot = vc.rotation;
        	Factory.createBulletDestroyed(pc.x, pc.y, rot);
        	GameScreen.toBeDeleted.add(etA.e);
        }
        if (etA.type == ('e') && etB.type == 'b')
        {
            etA.e.getComponent(AIControllerComponent.class).health--;
            GameScreen.toBeDeleted.add(etB.e);
            
            VisualComponent vc = etB.e.getComponent(VisualComponent.class);
        	PositionComponent pc = etB.e.getComponent(PositionComponent.class);
        	float rot = vc.rotation;
        	Factory.createBulletDestroyed(pc.x, pc.y, rot);
        	GameScreen.toBeDeleted.add(etB.e);
        }
    }

    @Override
    public void endContact(Contact contact) {
        //System.out.println("contact end: " + contact.toString());

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
