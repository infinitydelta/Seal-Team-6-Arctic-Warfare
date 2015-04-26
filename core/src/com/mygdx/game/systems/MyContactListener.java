package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.GameScreen;
import com.mygdx.game.utility.EntityType;

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
            //contact.getFixtureB().getBody().setActive(false);
        	//GameScreen.toBeDeleted.add(etA.e);
        }
        if(etB.type == 'b' && etA.type == 'w')
        {
        	//GameScreen.toBeDeleted.add(etB.e);
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