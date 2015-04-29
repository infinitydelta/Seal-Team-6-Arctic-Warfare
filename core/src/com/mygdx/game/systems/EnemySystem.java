package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.PositionComponent;

public class EnemySystem extends IteratingSystem
{
	private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);
	public EnemySystem() 
	{
		super(Family.getFor(EnemyComponent.class));
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent weaponPosition = em.get(entity).weapon.getComponent(PositionComponent.class);
		PositionComponent position = entity.getComponent(PositionComponent.class);
		if (weaponPosition != null)
	    {
	        weaponPosition.x = position.x;
	        weaponPosition.y = position.y;
	    }
		
	}

}
