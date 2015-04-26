package com.mygdx.game.systems;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

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
        if (contact.getFixtureB().getBody().getUserData().equals('b'))
        {
            //contact.getFixtureB().getBody().setActive(false);
        }
    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("contact end: " + contact.toString());

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
