package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.components.*;

/**
 * Created by McLean on 4/28/2015.
 */
public class EnemySystem extends IteratingSystem {
    private ImmutableArray<Entity> players;
    private ComponentMapper<VisualComponent> vm = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<MovementComponent> cm = ComponentMapper.getFor(MovementComponent.class);
    private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);

    public EnemySystem() {
        super(Family.getFor(EnemyComponent.class));

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = pm.get(entity);
        VisualComponent visual = vm.get(entity);
        MovementComponent collision = cm.get(entity);

	PositionComponent weaponPosition = em.get(entity).weapon.getComponent(PositionComponent.class);
	if (weaponPosition != null)
	{
	     weaponPosition.x = position.x;
	     weaponPosition.y = position.y;
	}

        for (int i = 0; i < players.size(); i++) {
            float px = players.get(i).getComponent(PositionComponent.class).x;
            float py = players.get(i).getComponent(PositionComponent.class).y;
            float dy = py - position.y;
            float dx = px - position.x;
            //System.out.println("Player "+players.get(i).getId() +" : "+Math.abs(px+collision.body.getLinearVelocity().x-position.x)+" - "+Math.abs(dx) );
            if ((dx * dx + dy * dy < 75)) {
                //System.out.println("Player "+players.get(i).getId() +" : "+(Math.abs(px+collision.body.getLinearVelocity().x-position.x)<Math.abs(dx))+" - "+Math.abs(px+collision.body.getLinearVelocity().x-position.x)+" - "+Math.abs(dx));
                //System.out.println(collision.body.getLinearVelocity().x);
                if (collision.body.getLinearVelocity().x > 0) {
                if (!visual.sprite.isFlipX()) visual.sprite.flip(true, false);
            } else {
                if (visual.sprite.isFlipX()) visual.sprite.flip(true, false);
            }
        }
        }
    }

    public void setPlayers(Engine engine){
        players = engine.getEntitiesFor(Family.getFor(PlayerComponent.class));
    }

}
