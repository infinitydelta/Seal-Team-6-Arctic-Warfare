package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.GameScreen;
import com.mygdx.game.components.*;
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
        	NetworkComponent nc = etA.e.getComponent(NetworkComponent.class);
        	nc.isDead = true;
        	float rot = vc.rotation;
        	Factory.createBulletDestroyed(pc.x, pc.y, rot);
        	GameScreen.toBeDeleted.add(etA.e);
        }
        if(etB.type == 'b' && etA.type == 'w')
        {
        	VisualComponent vc = etB.e.getComponent(VisualComponent.class);
        	PositionComponent pc = etB.e.getComponent(PositionComponent.class);
        	NetworkComponent nc = etB.e.getComponent(NetworkComponent.class);
        	nc.isDead = true;
        	float rot = vc.rotation;
        	Factory.createBulletDestroyed(pc.x, pc.y, rot);
        	GameScreen.toBeDeleted.add(etB.e);
        }
        if (etA.type == ('b') && etB.type == 'e')
        {
           etB.e.getComponent(EnemyComponent.class).health--;
            if(etB.e.getComponent(EnemyComponent.class).health<=0) {
                //visual.setAnimation(Factory.seal_die_anim);
                Entity ds = Factory.createDeadSeal(etB.e.getComponent(PositionComponent.class).x, etB.e.getComponent(PositionComponent.class).y);
                if(etB.e.getComponent(VisualComponent.class).sprite.isFlipX())
                {
                    ds.getComponent(VisualComponent.class).sprite.setFlip(true, false);
                }
                GameScreen.toBeDeleted.add(etB.e);

            }

            //GameScreen.toBeDeleted.add(etA.e);
            
            VisualComponent vc = etA.e.getComponent(VisualComponent.class);
        	PositionComponent pc = etA.e.getComponent(PositionComponent.class);
        	NetworkComponent nc = etA.e.getComponent(NetworkComponent.class);
        	nc.isDead = true;
        	float rot = vc.rotation;
        	Factory.createBulletDestroyed(pc.x, pc.y, rot);
        	GameScreen.toBeDeleted.add(etA.e);
        }
        if (etA.type == ('e') && etB.type == 'b')
        {
            etA.e.getComponent(EnemyComponent.class).health--;
            if(etA.e.getComponent(EnemyComponent.class).health<=0) {
                Entity ds = Factory.createDeadSeal(etA.e.getComponent(PositionComponent.class).x, etA.e.getComponent(PositionComponent.class).y);
                if(etA.e.getComponent(VisualComponent.class).sprite.isFlipX())
                {
                    ds.getComponent(VisualComponent.class).sprite.setFlip(true, false);
                }
                GameScreen.toBeDeleted.add(etA.e);

            }
            
            VisualComponent vc = etB.e.getComponent(VisualComponent.class);
            PositionComponent pc = etB.e.getComponent(PositionComponent.class);
            NetworkComponent nc = etB.e.getComponent(NetworkComponent.class);
            nc.isDead = true;
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
